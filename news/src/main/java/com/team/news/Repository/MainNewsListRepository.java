package com.team.news.Repository;

import com.team.news.Form.MainNewsList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface MainNewsListRepository extends MongoRepository<MainNewsList, String> {

    @Nullable
    List<MainNewsList> findAllBy();

    @Nullable
    List<MainNewsList> findMainNewsListByDateGreaterThanEqual(@Nullable String date);   // date 날짜 이후의 내용

    List<MainNewsList> findMainNewsListByWordAndDateGreaterThanEqual(String word, String date);

    int countMainNewsListByDateGreaterThanEqualAndWord(String time, String word);
}
