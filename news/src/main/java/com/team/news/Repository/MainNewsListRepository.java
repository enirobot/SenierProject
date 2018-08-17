package com.team.news.Repository;

import com.team.news.Form.MainNewsList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface MainNewsListRepository extends MongoRepository<MainNewsList, String> {

    @Nullable
    List<MainNewsList> findAllBy();

    @Nullable
    MainNewsList findMainNewsListById(String id);

    @Nullable
    List<MainNewsList> findMainNewsListByDateGreaterThanEqual(@Nullable String date);   // date 날짜 이후의 내용


}
