package com.wcacg.wcgal.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.wcacg.wcgal.entity.*;
import com.wcacg.wcgal.entity.dto.PageDto;
import com.wcacg.wcgal.entity.dto.comment.CommentAddDto;
import com.wcacg.wcgal.entity.dto.comment.CommentDelDto;
import com.wcacg.wcgal.entity.dto.comment.CommentDto;
import com.wcacg.wcgal.entity.dto.comment.CommentUpdateDto;
import com.wcacg.wcgal.entity.dto.user.UserInfoDto;
import com.wcacg.wcgal.entity.dto.user.UserTokenDto;
import com.wcacg.wcgal.exception.ClientError;
import com.wcacg.wcgal.repository.ArticleRepository;
import com.wcacg.wcgal.repository.CommentRepository;
import com.wcacg.wcgal.repository.SubCommentRepository;
import com.wcacg.wcgal.service.type.ResourceType;
import com.wcacg.wcgal.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;
    private final ArticleRepository articleRepository;

    public CommentService(CommentRepository commentRepository, SubCommentRepository subCommentRepository,
                          ArticleRepository articleRepository){
        this.commentRepository = commentRepository;
        this.subCommentRepository = subCommentRepository;
        this.articleRepository = articleRepository;
    }

    public Page<? extends Comment> getComments(String resourceId, PageDto pageDto){
        if (ResourceType.getType(resourceId) == ResourceType.COMMENT){
            BooleanExpression be = QComment.comment.resourceId.eq(resourceId);
            if (pageDto.getDesc() == null){
                return this.subCommentRepository.findAll(be, PageRequest.of(pageDto.getPage(), pageDto.getLimit()));
            }
            return this.subCommentRepository.findAll(be, PageRequest.of(pageDto.getPage(), pageDto.getLimit(),
                    Sort.by(pageDto.getDesc() ? Sort.Direction.DESC: Sort.Direction.ASC, "updateTime")));
        }

        BooleanExpression be = QMainComment.mainComment.resourceId.eq(resourceId);
        if (pageDto.getDesc() == null){
            return this.commentRepository.findAll(be, PageRequest.of(pageDto.getPage(), pageDto.getLimit()));
        }
        return this.commentRepository.findAll(be, PageRequest.of(pageDto.getPage(), pageDto.getLimit(),
                Sort.by(pageDto.getDesc() ? Sort.Direction.DESC: Sort.Direction.ASC, "updateTime")));
    }

    public CommentDto commentToCommentDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        BeanUtils.copyProperties(comment, commentDto);
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(comment.getCommentAuthor(), userInfoDto);
        commentDto.setCommentAuthor(userInfoDto);
        return commentDto;
    }

    public List<CommentDto> findSubCommentToCommentDto(Page<? extends Comment> page){
        List<CommentDto> comments = new ArrayList<>();
        page.forEach(comment -> {
            CommentDto commentDto = this.commentToCommentDto(comment);
            if (!(comment instanceof MainComment mainComment)){
                comments.add(commentDto);
                return;
            }

            int count = mainComment.getSubComment().size();
            mainComment.getSubComment().subList(0, Math.min(count, 3)).forEach(subComment -> {
                commentDto.addSubComments(this.commentToCommentDto(subComment));
            });
            commentDto.setSubCommentCount(count);
            comments.add(commentDto);
        });
        return comments;
    }

    public CommentDto addArticleComment(CommentAddDto commentAddDto, long userId){
        Article article = this.articleRepository.findById(ResourceType.getId(commentAddDto.getResourceId())).orElse(null);
        if (article == null){
            throw new ClientError.NotFindException("文章资源id " + commentAddDto.getResourceId() + " 不存在... qwq");
        }

        if (commentAddDto.getCommentId() > 0){
            return this.addArticleSubComment(commentAddDto, article, userId);
        }

        MainComment comment = new MainComment();
        comment.setCommentAuthor(new User(userId));
        comment.setResourceId(commentAddDto.getResourceId());
        comment.setContent(commentAddDto.getContent());
        comment.setLikes(0L);
        comment = this.commentRepository.save(comment);

        article.setComments(article.getComments() + 1);
        this.articleRepository.save(article);
        return this.commentToCommentDto(comment);
    }

    public CommentDto addArticleSubComment(CommentAddDto commentAddDto, Article article, long userId) {
        MainComment comment = this.commentRepository.findById(commentAddDto.getCommentId()).orElse(null);
        if (comment == null || !comment.getResourceId().equals(commentAddDto.getResourceId())){
            throw new ClientError.NotFindException("评论id " + commentAddDto.getCommentId() + " 不存在... qwq");
        }

        Comment subComment = new Comment();
        subComment.setCommentAuthor(new User(userId));
        subComment.setResourceId(ResourceType.COMMENT.getName()+ "_" + comment.getCommentId());
        subComment.setContent(commentAddDto.getContent());
        subComment.setLikes(0L);
        comment.getSubComment().add(subComment);

        this.subCommentRepository.save(subComment);
        comment = this.commentRepository.save(comment);

        article.setComments(article.getComments() + 1);
        this.articleRepository.save(article);
        return this.commentToCommentDto(comment);
    }

    private boolean canSetComment(Comment comment, HttpServletRequest request){
        UserTokenDto userTokenDto = TokenUtils.decodedToken(request);
        return !userTokenDto.isAdmin() && comment.getCommentAuthor().getUserId() != userTokenDto.getUserId();
    }

    private void delMainComment(CommentDelDto commentDelDto, HttpServletRequest request, Consumer<Long> consumer){
        MainComment comment = this.commentRepository.findById(commentDelDto.getCommentId()).orElse(null);
        if (comment == null){
            throw new ClientError.NotFindException("评论id " + commentDelDto.getCommentId() + " 不存在... qwq");
        }

        if (!comment.getResourceId().equals(commentDelDto.getResourceId())){
            throw new ClientError.NotFindException("资源id " + commentDelDto.getResourceId() + " 不存在... qwq");
        }

        if (this.canSetComment(comment, request)){
            throw new ClientError.NotPermissionsException("你只能删除自己的评论...");
        }

        long count = comment.getSubComment().size() + 1;
        this.subCommentRepository.deleteAll(comment.getSubComment());
        this.commentRepository.delete(comment);
        consumer.accept(count);
    }

    public void delArticleComment(CommentDelDto commentDelDto, HttpServletRequest request){
        Article article = this.articleRepository.findById(ResourceType.getId(commentDelDto.getResourceId())).orElse(null);
        if (article == null){
            throw new ClientError.NotFindException("文章资源id " + commentDelDto.getResourceId() + " 不存在... qwq");
        }

        this.delMainComment(commentDelDto, request, count -> article.setComments(article.getComments() - count));
        this.articleRepository.save(article);
    }

    public void delSubComment(CommentDelDto commentDelDto, HttpServletRequest request) {
        Comment comment = this.subCommentRepository.findById(commentDelDto.getCommentId()).orElse(null);
        if (comment == null){
            throw new ClientError.NotFindException("评论id " + commentDelDto.getCommentId() + " 不存在... qwq");
        }

        if (this.canSetComment(comment, request)){
            throw new ClientError.NotPermissionsException("你只能删除自己的评论...");
        }

        this.subCommentRepository.delete(comment);
    }

    public CommentDto updateComment(CommentUpdateDto commentUpdateDto, HttpServletRequest request) {
        Comment comment = this.commentRepository.findById(commentUpdateDto.getCommentId()).orElse(null);
        if (comment == null){
            comment = this.subCommentRepository.findById(commentUpdateDto.getCommentId()).orElse(null);
        }

        if (comment == null){
            throw new ClientError.NotFindException("评论id " + commentUpdateDto.getCommentId() + " 不存在... qwq");
        }

        if (this.canSetComment(comment, request)){
            throw new ClientError.NotPermissionsException("你只能编辑自己的评论...");
        }

        comment.setContent(commentUpdateDto.getContent());
        comment.setUpdateTime(null);
        if (comment instanceof MainComment){
            return this.commentToCommentDto(this.commentRepository.save((MainComment) comment));
        }
        return this.commentToCommentDto(this.subCommentRepository.save(comment));
    }
}
