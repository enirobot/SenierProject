package com.team.news.Repository;

import com.team.news.Form.MainNewsList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface MainNewsListRepository extends MongoRepository<MainNewsList, String> {

    @Nullable
    List<MainNewsList> findAllBy();

    @Nullable
    List<MainNewsList> findMainNewsListByDateGreaterThanEqual(@Nullable String date);   // date 날짜 이후의 내용

    // Date에서 from과 to 사이의 값을 totalWeight 내림차순, counts 내림차순 한 값을 가져옴
    @Nullable
    List<MainNewsList> findByDateBetweenOrderByTotalWeightDescCountsDesc(@Nullable String from, String to);

    // Date에서 from과 to 사이의 값을 counts 내림차순, totalWeight 내림차순 한 값을 가져옴
    @Nullable
    List<MainNewsList> findByDateBetweenAndTotalWeightGreaterThanOrderByCountsDescTotalWeightDesc(@Nullable String from, String to, int weight);;

    @Nullable
    List<MainNewsList> findMainNewsListByWordAndDateGreaterThanEqual(String word, String date);

    @Nullable
    MainNewsList findMainNewsListById(String id);


    int countMainNewsListByDateGreaterThanEqualAndWord(String time, String word);
}