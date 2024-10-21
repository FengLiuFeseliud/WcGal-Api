package com.wcacg.wcgal.repository;

import com.wcacg.wcgal.entity.Article;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ArticleRepository extends CrudRepository<Article, Long>, PagingAndSortingRepository<Article, Long>,
        QuerydslPredicateExecutor<Article> {
}
