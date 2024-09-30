package com.wcacg.wcgal.repository;

import com.wcacg.wcgal.entity.ArticleTags;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleTagsRepository extends CrudRepository<ArticleTags, Long>, PagingAndSortingRepository<ArticleTags, Long>, QuerydslPredicateExecutor<ArticleTags> {

    ArticleTags findByTagName(String name);

    @Query("SELECT new com.wcacg.wcgal.entity.ArticleTags(tags.tagId, tags.tagName, tags.tagCount) FROM ArticleTags tags")
    List<ArticleTags> selectSmall();
}
