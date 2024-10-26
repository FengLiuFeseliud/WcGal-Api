package com.wcacg.wcgal.repository;

import com.wcacg.wcgal.entity.Favorite;
import com.wcacg.wcgal.entity.User;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FavoriteRepository extends CrudRepository<Favorite, Long>, PagingAndSortingRepository<Favorite, Long>,
        QuerydslPredicateExecutor<Favorite> {

    List<Favorite> findAllByCreateUser(User user);
}
