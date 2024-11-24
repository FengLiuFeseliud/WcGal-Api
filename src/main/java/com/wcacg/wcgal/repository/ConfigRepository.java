package com.wcacg.wcgal.repository;

import com.wcacg.wcgal.entity.Config;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends CrudRepository<Config, Long>, PagingAndSortingRepository<Config, Long>,
        QuerydslPredicateExecutor<Config> {
}
