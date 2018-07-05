package com.team.news.mongoDB;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NewsRepository extends MongoRepository<News, String> {

    List<News> findAll();   // 모든 내용
    List<News> findByDateGreaterThanEqual(String date); // date 날짜 이후의 내용
}
