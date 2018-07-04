package com.team.news.mongoDB;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NewsRepository extends MongoRepository<News, String> {

    List<News> findAll();
    List<News> findByArticleDateGreaterThanEqual(String ArticleDate);
}
