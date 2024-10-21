package com.wcacg.wcgal.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.wcacg.wcgal.entity.*;
import com.wcacg.wcgal.entity.dto.PageDto;
import com.wcacg.wcgal.entity.dto.comment.CommentAddDto;
import com.wcacg.wcgal.entity.dto.comment.CommentDelDto;
import com.wcacg.wcgal.entity.dto.comment.CommentDto;
import com.wcacg.wcgal.entity.dto.comment.CommentUpdateDto;
import com.wcacg.wcgal.entity.dto.user.UserInfoDto;
import com.wcacg.wcgal.repository.ArticleRepository;
import com.wcacg.wcgal.repository.CommentRepository;
import com.wcacg.wcgal.repository.SubCommentRepository;
import com.wcacg.wcgal.service.type.ResourceType;
import com.wcacg.wcgal.service.type.ServiceErrorType;
import com.wcacg.wcgal.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        String[] resourceIds =  resourceId.split("_");
        if (resourceIds.length != 2){
            return null;
        }

        long id = Long.parseLong(resourceIds[1]);
        if (resourceIds[0].equals(ResourceType.ARTICLE.getName())){
            if (this.articleRepository.findById(id).isEmpty()){
                return null;
            }
        }

        if (resourceIds[0].equals(ResourceType.COMMENT.getName())){
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

    public Comment addArticleComment(CommentAddDto commentAddDto, long userId){
        Article article = this.articleRepository.findById(ResourceType.getId(commentAddDto.getResourceId())).orElse(null);
        if (article == null){
            return null;
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
        return comment;
    }

    public Comment addArticleSubComment(CommentAddDto commentAddDto, Article article, long userId) {
        MainComment comment = this.commentRepository.findById(commentAddDto.getCommentId()).orElse(null);
        if (comment == null || !comment.getResourceId().equals(commentAddDto.getResourceId())){
            return null;
        }

        Comment subComment = new Comment();
        subComment.setCommentAuthor(new User(userId));
        subComment.setResourceId(ResourceType.COMMENT.getName()+ "_" + comment.getCommentId());
        subComment.setContent(commentAddDto.getContent());
        subComment.setLikes(0L);
        comment.getSubComment().add(subComment);

        this.subCommentRepository.save(subComment);
        this.commentRepository.save(comment);

        article.setComments(article.getComments() + 1);
        this.articleRepository.save(article);
        return subComment;
    }

    private ServiceErrorType canSetComment(Comment comment, HttpServletRequest request){
        Map<String, String> tokenData = TokenUtils.decodedToken(request);
        if (!tokenData.get("admin").equals("true")
                && comment.getCommentAuthor().getUserId() != Long.parseLong(tokenData.get("user_id"))){
            return ServiceErrorType.NOT_PERMISSIONS;
        }
        return ServiceErrorType.OK;
    }

    private ServiceErrorType delMainComment(CommentDelDto commentDelDto, HttpServletRequest request, Consumer<Long> consumer){
        MainComment comment = this.commentRepository.findById(commentDelDto.getCommentId()).orElse(null);
        if (comment == null){
            return ServiceErrorType.NOT_FIND;
        }

        if (!comment.getResourceId().equals(commentDelDto.getResourceId())){
            return ServiceErrorType.NOT_FIND;
        }

        if (this.canSetComment(comment, request) != ServiceErrorType.OK){
            return ServiceErrorType.NOT_PERMISSIONS;
        }

        long count = comment.getSubComment().size() + 1;
        this.subCommentRepository.deleteAll(comment.getSubComment());
        this.commentRepository.delete(comment);
        consumer.accept(count);
        return ServiceErrorType.OK;
    }

    public ServiceErrorType delArticleComment(CommentDelDto commentDelDto, HttpServletRequest request){
        Article article = this.articleRepository.findById(ResourceType.getId(commentDelDto.getResourceId())).orElse(null);
        if (article == null){
            return ServiceErrorType.NOT_FIND;
        }

        ServiceErrorType type = this.delMainComment(commentDelDto, request, count ->
                article.setComments(article.getComments() - count));
        if (type!= ServiceErrorType.OK){
            return type;
        }

        this.articleRepository.save(article);
        return type;
    }

    public ServiceErrorType delSubComment(CommentDelDto commentDelDto, HttpServletRequest request) {
        Comment comment = this.subCommentRepository.findById(commentDelDto.getCommentId()).orElse(null);
        if (comment == null){
            return ServiceErrorType.NOT_FIND;
        }

        if (this.canSetComment(comment, request) != ServiceErrorType.OK){
            return ServiceErrorType.NOT_PERMISSIONS;
        }

        this.subCommentRepository.delete(comment);
        return ServiceErrorType.OK;
    }

    public ServiceErrorType updateComment(CommentUpdateDto commentUpdateDto, HttpServletRequest request) {
        Comment comment = this.commentRepository.findById(commentUpdateDto.getCommentId()).orElse(null);
        if (comment == null){
            comment = this.subCommentRepository.findById(commentUpdateDto.getCommentId()).orElse(null);
        }

        if (comment == null){
            return ServiceErrorType.NOT_FIND;
        }

        if (this.canSetComment(comment, request) != ServiceErrorType.OK){
            return ServiceErrorType.NOT_PERMISSIONS;
        }

        comment.setContent(commentUpdateDto.getContent());
        comment.setUpdateTime(null);
        if (comment instanceof MainComment){
            this.commentRepository.save((MainComment) comment);
        } else  {
            this.subCommentRepository.save(comment);
        }
        return ServiceErrorType.OK;
    }
}
