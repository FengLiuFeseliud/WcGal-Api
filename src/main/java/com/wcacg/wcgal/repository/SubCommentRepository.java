package com.wcacg.wcgal.repository;

import com.wcacg.wcgal.entity.Comment;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCommentRepository extends CrudRepository<Comment, Long>, PagingAndSortingRepository<Comment, Long>,
        QuerydslPredicateExecutor<Comment> {
}
