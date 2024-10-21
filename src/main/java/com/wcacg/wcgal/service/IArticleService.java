package com.wcacg.wcgal.service;

import com.wcacg.wcgal.entity.Article;
import com.wcacg.wcgal.entity.dto.PageDto;
import com.wcacg.wcgal.entity.dto.article.ArticleAddDto;
import com.wcacg.wcgal.entity.dto.article.ArticleDto;
import org.springframework.data.domain.Page;

public interface IArticleService {
    ArticleDto addArticle(ArticleAddDto article, long userId);
    ArticleDto updateArticle(ArticleAddDto articleDto);
    Long deleteArticle(Long id);
    Article getArticle(Long id);
    Page<Article> getArticles(PageDto pageDto);
    Long getCount();
}