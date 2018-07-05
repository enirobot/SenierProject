package com.team.news.wordCloud;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WCFormRepository extends MongoRepository<WCForm, String> {

    List<WCForm> findAll();   // 모든 내용
    List<WCForm> findByDateGreaterThanEqual(String date); // date 날짜 이후의 내용
}
