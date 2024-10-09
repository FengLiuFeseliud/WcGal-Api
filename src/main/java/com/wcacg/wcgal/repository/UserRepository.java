package com.wcacg.wcgal.repository;

import com.wcacg.wcgal.entity.Article;
import com.wcacg.wcgal.entity.User;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long>,
        QuerydslPredicateExecutor<User> {

    User findByUserName(String username);
    User findByEmail(String email);
}
