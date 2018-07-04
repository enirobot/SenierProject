package com.team.news.MongoDB;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NewsRepository extends MongoRepository<News, String> {

    public List<News> findAll();

}
