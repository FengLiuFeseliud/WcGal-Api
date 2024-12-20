package com.wcacg.wcgal.repository;

import com.wcacg.wcgal.entity.MainComment;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<MainComment, Long>, PagingAndSortingRepository<MainComment, Long>,
        QuerydslPredicateExecutor<MainComment> {
}
