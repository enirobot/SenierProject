package com.team.news.Repository;

import com.team.news.Form.MainNewsList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.Nullable;
import sun.applet.Main;

import java.util.List;

public interface MainNewsListRepository extends MongoRepository<MainNewsList, String> {

    @Nullable
    List<MainNewsList> findAllBy();

    @Nullable
    List<MainNewsList> findMainNewsListByDateGreaterThanEqual(@Nullable String date);   // date 날짜 이후의 내용

    @Nullable
    List<MainNewsList> findMainNewsListsByWordGreaterThanEqualOrderByCountsDesc(@Nullable String date);


    // Date에서 from과 to 사이의 값을 totalWeight 내림차순, counts 내림차순 한 값을 가져옴
    @Nullable
    List<MainNewsList> findByDateBetweenOrderByTotalWeightDescCountsDesc(@Nullable String from, String to);

    // Date에서 from과 to 사이, totalWeight가 0이 아닌 값을 counts 내림차순, totalWeight 내림차순 하여 가져옴
    @Nullable
    List<MainNewsList> findByDateBetweenAndTotalWeightGreaterThanOrderByCountsDescTotalWeightDesc(@Nullable String from, String to, int weight);

    // Date에서 from과 to 사이, totalWeight가 0이 아닌 값을 totalWeight 내림차순, counts 내림차순 하여 가져옴
    @Nullable
    List<MainNewsList> findByDateBetweenAndTotalWeightGreaterThanAndCountsGreaterThanOrderByTotalWeightDescCountsDesc(@Nullable String from, String to, int weight, int counts);

    @Nullable
    List<MainNewsList> findMainNewsListByWordAndDateGreaterThanEqual(String word, String date);

    @Nullable
    List<MainNewsList> findByIdIn(List<String> idList);


    int countMainNewsListByDateGreaterThanEqualAndWord(String time, String word);

    @Nullable
    List<MainNewsList> findMainNewsListsByDateBetweenAndWord(String fromTime, String toTime, String word);
    @Nullable
    List<MainNewsList> findByDateBetweenAndTotalWeightGreaterThanOrderByTotalWeightDescCountsDesc(String fromTime, String toTime, int i);
}