package com.wcacg.wcgal.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.wcacg.wcgal.entity.*;
import com.wcacg.wcgal.entity.dto.ArticleTagDto;
import com.wcacg.wcgal.entity.dto.PageDto;
import com.wcacg.wcgal.entity.dto.SearchDto;
import com.wcacg.wcgal.entity.dto.TagsDto;
import com.wcacg.wcgal.entity.dto.article.ArticleAddDto;
import com.wcacg.wcgal.entity.dto.article.ArticleDto;
import com.wcacg.wcgal.entity.dto.article.ArticleInfoDto;
import com.wcacg.wcgal.entity.dto.favorite.FavoriteItemDto;
import com.wcacg.wcgal.entity.dto.user.UserInfoDto;
import com.wcacg.wcgal.exception.ClientError;
import com.wcacg.wcgal.repository.ArticleRepository;
import com.wcacg.wcgal.repository.ArticleTagsRepository;
import com.wcacg.wcgal.repository.FavoriteItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class ArticleService implements IArticleService, IArticleTagService {
    private final ArticleRepository articleRepository;
    private final ArticleTagsRepository articleTagsRepository;
    private final FavoriteItemRepository favoriteItemRepository;

    public ArticleService(ArticleRepository articleRepository, ArticleTagsRepository articleTagsRepository,
        FavoriteItemRepository favoriteItemRepository) {
        this.articleRepository = articleRepository;
        this.articleTagsRepository = articleTagsRepository;
        this.favoriteItemRepository = favoriteItemRepository;
    }

    private ArticleDto articleToArticleDto(Article article){
        ArticleDto articleDto = new ArticleDto();
        BeanUtils.copyProperties(article, articleDto);
        return articleDto;
    }

    @Override
    public ArticleDto addArticle(ArticleAddDto articleDto, long userId) {
        this.countTags(new TagsDto(Arrays.asList(articleDto.getTags())));
        Article article = new Article();
        BeanUtils.copyProperties(articleDto, article);
        article.setArticleId(null);
        article.setArticleAuthor(new User(userId));
        article.setComments(0L);
        article.setLikes(0L);
        article.setViews(0L);
        article.setFavorites(0L);
        article.setTags(String.join(",", articleDto.getTags()));
        return this.articleToArticleDto(articleRepository.save(article));
    }

    @Override
    public ArticleDto updateArticle(ArticleAddDto articleDto) {
        Article article = articleRepository.findById(articleDto.getArticleId()).orElse(null);
        if (article == null){
            throw new ClientError.NotFindException("文章id " + articleDto.getArticleId() + " 不存在... qwq");
        }

        this.addArticleTag(new TagsDto(Arrays.stream(
                articleDto.getTags() != null? articleDto.getTags(): new String[]{}).toList()));
        article.setArticleTitle(articleDto.getArticleTitle());
        article.setArticleContent(articleDto.getArticleContent());
        article.setCover(articleDto.getCover());
        article.setTags(String.join(",", articleDto.getTags()));
        article.setUpdateTime(null);
        return this.articleToArticleDto(articleRepository.save(article));
    }

    @Override
    public Long deleteArticle(Long articleId) {
        this.articleRepository.deleteById(articleId);
        return articleId;
    }

    public ArticleDto toArticleDto(Article article, Stream<ArticleTags> tags) {
        ArticleDto articleDto = new ArticleDto();
        BeanUtils.copyProperties(article, articleDto);

        UserInfoDto userDto = new UserInfoDto();
        BeanUtils.copyProperties(article.getArticleAuthor(), userDto);
        articleDto.setArticleAuthor(userDto);

        List<ArticleTagDto> articleTagsDtoList = new ArrayList<>();
        tags.forEach(articleTag -> {
            ArticleTagDto articleTagsDto = new ArticleTagDto();
            BeanUtils.copyProperties(articleTag, articleTagsDto);
            articleTagsDtoList.add(articleTagsDto);
        });
        articleDto.setTagsData(articleTagsDtoList.toArray(new ArticleTagDto[0]));
        return articleDto;
    }

    public ArticleDto findArticleTagsToArticleDto(Article article){
        return this.toArticleDto(article, StreamSupport.stream(
                this.getArticleTagsByTagNames(article.getTags().split(",")).spliterator(), false));
    }

    public List<ArticleInfoDto> findArticleTagsToArticleDtoList(Page<Article> articlePage) {
        List<ArticleInfoDto> articleDtoList = new ArrayList<>();
        List<String> allTagNames = new ArrayList<>();

        articlePage.forEach(article -> allTagNames.addAll(List.of(article.getTags().split(","))));
        List<ArticleTags> allTags = (List<ArticleTags>) this.getArticleTagsByTagNames(allTagNames.toArray(new String[0]));
        articlePage.forEach(article -> {
            ArticleInfoDto articleInfoDto = new ArticleInfoDto();
            List<String> tagNames = List.of(article.getTags().split(","));
            BeanUtils.copyProperties(toArticleDto(article,
                    allTags.stream().filter(tag -> tagNames.contains(tag.getTagName()))), articleInfoDto);
            articleDtoList.add(articleInfoDto);
        });
        return articleDtoList;
    }

    public List<FavoriteItemDto> findArticleTagsToFavoriteItemDtoList(Page<FavoriteItem> favoriteItemPage) {
        List<FavoriteItemDto> favoriteItemList = new ArrayList<>();
        List<String> allTagNames = new ArrayList<>();

        favoriteItemPage.forEach(favoriteItem -> allTagNames.addAll(List.of(favoriteItem.getArticle().getTags().split(","))));
        List<ArticleTags> allTags = (List<ArticleTags>) this.getArticleTagsByTagNames(allTagNames.toArray(new String[0]));
        favoriteItemPage.forEach(favoriteItem -> {
            ArticleInfoDto articleInfoDto = new ArticleInfoDto();
            List<String> tagNames = List.of(favoriteItem.getArticle().getTags().split(","));
            BeanUtils.copyProperties(toArticleDto(favoriteItem.getArticle(),
                    allTags.stream().filter(tag -> tagNames.contains(tag.getTagName()))), articleInfoDto);
            FavoriteItemDto favoriteItemDto = new FavoriteItemDto();
            BeanUtils.copyProperties(favoriteItem, favoriteItemDto);
            favoriteItemDto.setArticle(articleInfoDto);
            favoriteItemList.add(favoriteItemDto);
        });
        return favoriteItemList;
    }

    @Override
    public ArticleDto getArticle(Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId).orElse(null);
        if (article == null){
            throw new ClientError.NotFindException("文章id " + articleId + " 不存在... qwq");
        }
        article.setViews(article.getViews() + 1);
        QFavoriteItem qFavoriteItem = QFavoriteItem.favoriteItem;
        if (userId == 0){
            return this.findArticleTagsToArticleDto(this.articleRepository.save(article));
        }

        ArticleDto articleDto = this.findArticleTagsToArticleDto(this.articleRepository.save(article));
        Iterable<FavoriteItem> favoriteItems = this.favoriteItemRepository.findAll(qFavoriteItem.article.articleId.eq(articleId)
                .and(qFavoriteItem.favoriteUserId.eq(userId)));

        List<FavoriteItemDto> favoriteItemDtoList = new ArrayList<>();
        favoriteItems.forEach(favoriteItem -> {
            FavoriteItemDto favoriteItemDto = new FavoriteItemDto();
            favoriteItem.setArticle(null);
            BeanUtils.copyProperties(favoriteItem, favoriteItemDto);
            favoriteItemDtoList.add(favoriteItemDto);
        });

        articleDto.setFavoriteItems(favoriteItemDtoList);
        return articleDto;
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
        for (String keyword : keywords) {
            key = keyword;
            if (key.startsWith("#")) {
                if (be == null) {
                    be = qArticle.tags.contains(key.replace("#", ""));
                    continue;
                }
                be = be.and(qArticle.tags.contains(key.replace("#", "")));
                continue;
            }

            if (key.startsWith("@")) {
                if (be == null) {
                    be = qArticle.articleAuthor.userName.eq(key.replace("@", ""));
                    continue;
                }
                be = be.and(qArticle.articleAuthor.userName.eq(key.replace("@", "")));
                continue;
            }

            if (be == null) {
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

}
