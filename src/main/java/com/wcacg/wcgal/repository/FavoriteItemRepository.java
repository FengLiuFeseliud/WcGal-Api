package com.wcacg.wcgal.repository;

import com.wcacg.wcgal.entity.FavoriteItem;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FavoriteItemRepository  extends CrudRepository<FavoriteItem, Long>, PagingAndSortingRepository<FavoriteItem, Long>,
        QuerydslPredicateExecutor<FavoriteItem> {


    // favorite_id
    void deleteAllByFavoriteIdIn(List<Long> favoriteIds);
}
