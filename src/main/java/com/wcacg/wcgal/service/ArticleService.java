package com.wcacg.wcgal.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.wcacg.wcgal.entity.Article;
import com.wcacg.wcgal.entity.Comment;
import com.wcacg.wcgal.entity.QArticle;
import com.wcacg.wcgal.entity.User;
import com.wcacg.wcgal.entity.dto.*;
import com.wcacg.wcgal.entity.dto.user.UserInfoDto;
import com.wcacg.wcgal.repository.ArticleRepository;
import com.wcacg.wcgal.repository.ArticleTagsRepository;
import com.wcacg.wcgal.repository.CommentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ArticleService implements IArticleService, IArticleTagService {
    private final ArticleRepository articleRepository;
    private final ArticleTagsRepository articleTagsRepository;
    private final CommentRepository commentRepository;

    public ArticleService(ArticleRepository articleRepository, ArticleTagsRepository articleTagsRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.articleTagsRepository = articleTagsRepository;
        this.commentRepository = commentRepository;
    }

    private List<String> tagsToTagsList(ArticleDto articleDto){
        return Arrays.stream(articleDto.getTags() != null? articleDto.getTags().split(","): new String[]{}).toList();
    }

    @Override
    public Article addArticle(ArticleAddDto articleDto, long userId) {
        this.countTags(new TagsDto(Arrays.asList(articleDto.getTags())));
        Article articleEntity = new Article();
        BeanUtils.copyProperties(articleDto, articleEntity);
        articleEntity.setArticleAuthor(new User(userId));
        articleEntity.setComments(0L);
        articleEntity.setLikes(0L);
        articleEntity.setViews(0L);
        articleEntity.setFavorites(0L);
        articleEntity.setTags(String.join(",", articleDto.getTags()));
        return articleRepository.save(articleEntity);
    }

    @Override
    public Article updateArticle(ArticleDto articleDto) {
        if (articleDto.getArticleId() == null){
            return null;
        }

        Article article = articleRepository.findById(articleDto.getArticleId()).orElse(null);
        if (article == null){
            return null;
        }

        this.addArticleTag(new TagsDto(this.tagsToTagsList(articleDto)));
        Article articleEntity = new Article();
        BeanUtils.copyProperties(articleDto, articleEntity);
        articleEntity.setArticleId(article.getArticleId());
        return articleRepository.save(articleEntity);
    }

    @Override
    public Long deleteArticle(Long articleId) {
        this.articleRepository.deleteById(articleId);
        return articleId;
    }

    public ArticleDto findArticleTagsToArticleDto(Article article) {
        String[] tags = article.getTags().split(",");

        ArticleDto articleDto = new ArticleDto();
        BeanUtils.copyProperties(article, articleDto);
        articleDto.setTags(tags);

        UserInfoDto userDto = new UserInfoDto();
        BeanUtils.copyProperties(article.getArticleAuthor(), userDto);
        articleDto.setArticleAuthor(userDto);

        if (tags.length == 0){
            articleDto.setTagsData(new ArticleTagDto[]{});
            return articleDto;
        }

        List<ArticleTagDto> articleTagsDtoList = new ArrayList<>();
        this.getArticleTagsByTagNames(tags).forEach(articleTag -> {
            ArticleTagDto articleTagsDto = new ArticleTagDto();
            BeanUtils.copyProperties(articleTag, articleTagsDto);
            articleTagsDtoList.add(articleTagsDto);
        });
        articleDto.setTagsData(articleTagsDtoList.toArray(new ArticleTagDto[0]));
        return articleDto;
    }

    public List<ArticleInfoDto> findArticleTagsToArticleDtoList(Page<Article> articlePage) {
        List<ArticleInfoDto> articleDtoList = new ArrayList<>();
        articlePage.forEach(article -> {
            ArticleInfoDto articleInfoDto = new ArticleInfoDto();
            BeanUtils.copyProperties(findArticleTagsToArticleDto(article), articleInfoDto);
            articleDtoList.add(articleInfoDto);
        });
        return articleDtoList;
    }

    @Override
    public Article getArticle(Long articleId) {
        return articleRepository.findById(articleId).orElse(null);
    }

    @Override
    public Page<Article> getArticles(PageDto pageDto) {
        if (pageDto.getDesc() == null){
            return articleRepository.findAll(PageRequest.of(pageDto.getPage(), pageDto.getLimit()));
        }

        return articleRepository.findAll(PageRequest.of(pageDto.getPage(), pageDto.getLimit(),
                Sort.by(pageDto.getDesc() ? Sort.Direction.DESC: Sort.Direction.ASC, "articleId")));
    }

    public Page<Article> searchArticles(SearchDto searchDto) {
        QArticle qArticle = QArticle.article;
        String[] keywords = searchDto.getKeyword().split(" ");

        String key;
        BooleanExpression be = null;
        for (int index = 0; index < keywords.length; index++){
            key = keywords[index];
            if (key.startsWith("#")){
                if (be == null){
                    be = qArticle.tags.contains(key.replace("#", ""));
                    continue;
                }
                be = be.and(qArticle.tags.contains(key.replace("#", "")));
                continue;
            }

            if(key.startsWith("@")){
                if (be == null){
                    be = qArticle.articleAuthor.userName.eq(key.replace("@", ""));
                    continue;
                }
                be = be.and(qArticle.articleAuthor.userName.eq(key.replace("@", "")));
                continue;
            }

            if (be == null){
                be = qArticle.articleTitle.contains(key);
            } else {
                be = be.and(qArticle.articleTitle.contains(key));
            }
        }

        if (be == null){
            return this.getArticles(searchDto);
        }

        if (searchDto.getDesc() == null){
            return articleRepository.findAll(be, PageRequest.of(searchDto.getPage(), searchDto.getLimit()));
        }
        return articleRepository.findAll(be, PageRequest.of(searchDto.getPage(), searchDto.getLimit(),
                Sort.by(searchDto.getDesc() ? Sort.Direction.DESC: Sort.Direction.ASC, "articleId")));
    }


    @Override
    public Long getCount(){
        return articleRepository.count();
    }

    @Override
    public ArticleTagsRepository getArticleTagsRepository() {
        return this.articleTagsRepository;
    }

    public Comment comment(Article article, CommentDto commentDto, long userId) {
        Comment comment = new Comment();
        comment.setComment(commentDto.getComment());
        comment.setCommentAuthor(new User(userId));
        article.addComment(comment);
        comment = commentRepository.save(comment);
        articleRepository.save(article);
        return comment;
    }
}
