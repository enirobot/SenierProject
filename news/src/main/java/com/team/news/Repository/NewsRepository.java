package com.team.news.Repository;

import com.team.news.Form.News;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

public interface NewsRepository extends MongoRepository<News, String> {

    @Nullable
    List<News> findAll();   // 모든 내용

    @Nullable
    List<News> findByDateGreaterThanEqual(@Nullable String date); // date 날짜 이후의 내용

    @Nullable
    List<News> findAllById(List<String> list);

    int countByCategoryAndCompany(String category, String company);
}