package com.wcacg.wcgal.service;

import com.wcacg.wcgal.entity.Article;
import com.wcacg.wcgal.entity.dto.ArticleDto;
import com.wcacg.wcgal.entity.dto.PageDto;
import org.springframework.data.domain.Page;

public interface IArticleService {
    Article addArticle(ArticleDto article);
    Article updateArticle(ArticleDto article);
    Long deleteArticle(Long id);
    Article getArticle(Long id);
    Page<Article> getArticles(PageDto pageDto);
    Long getCount();
}