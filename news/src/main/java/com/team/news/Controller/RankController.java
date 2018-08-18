package com.team.news.Controller;

import com.mongodb.Block;
import com.mongodb.client.DistinctIterable;
import com.team.news.Form.MainNewsList;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import com.team.news.Form.RankForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.String;

@Controller
public class RankController {

    private NewsRepository newsRepository;
    private MainNewsListRepository mainNewsListRepository;
    private NewsRepository repository;
    private MongoTemplate mongoTemplate;

    @Autowired
    public RankController(NewsRepository newsRepository, MainNewsListRepository mainNewsListRepository,
                          NewsRepository repository, MongoTemplate mongoTemplate) {
        this.newsRepository = newsRepository;
        this.mainNewsListRepository = mainNewsListRepository;
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/ranking")
    public String index(Model model) {

        return "ranking";
    }

    @ResponseBody
    @PostMapping("/weeklyList")
    public List<RankForm> weeklyRank() {

        List<RankForm> weeklyList = new ArrayList<>();

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -(Calendar.DAY_OF_WEEK) + 1); // 현재 날짜가 포함된 주의 시작날
        cal1.set(Calendar.HOUR_OF_DAY, 0);      // 시간을 0으로 초기화
        cal1.clear(Calendar.MINUTE);            // 분을 0으로 초기화
        String beforeTime = date.format(cal1.getTime());
        System.out.println(beforeTime);

        // List<MainNewsList> mainNewsLists = mainNewsListRepository.findMainNewsListByDateGreaterThanEqual(beforeTime);

        DistinctIterable<String> distinct = mongoTemplate.getCollection("news").distinct("word", String.class);
        ArrayList<String> word = new ArrayList<String>();
        distinct.forEach(new Block<String>() {
            @Override
            public void apply(final String result) {
                word.add(result);
            }
        });

        int count1 = 0;
        for (int i = 0; i < word.size(); i++) {
            List<MainNewsList> mainNewsLists = mainNewsListRepository.findMainNewsListByWordAndDateGreaterThanEqual(word.get(i), beforeTime);
            for (int j = 0; j < mainNewsLists.size(); j++) {
                count1 += Integer.parseInt(mainNewsLists.get(j).getCounts());
            }
            weeklyList.add(new RankForm(word.get(i), count1));
        }

        return weeklyList.subList(0, 10);     // 상위 10개
    }

    @ResponseBody
    @PostMapping("/monthlyList")
    public List<RankForm> monthlyRank() {

        List<RankForm> monthlyList = new ArrayList<>();

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.DATE, 1);             // 현재 날짜가 포함된 월의 시작날
        cal2.set(Calendar.HOUR_OF_DAY, 0);      // 시간을 0으로 초기화
        cal2.clear(Calendar.MINUTE);            // 분을 0으로 초기화
        String beforeTime = date.format(cal2.getTime());
        System.out.println(beforeTime);

//        List<MainNewsList> mainNewsLists = mainNewsListRepository.findMainNewsListByDateGreaterThanEqual(beforeTime);

        DistinctIterable<String> distinct = mongoTemplate.getCollection("news").distinct("word", String.class);
        ArrayList<String> word = new ArrayList<String>();
        distinct.forEach(new Block<String>() {
            @Override
            public void apply(final String result) {
                word.add(result);
            }
        });

        int count2 = 0;
        for (int i = 0; i < word.size(); i++) {
            List<MainNewsList> mainNewsLists = mainNewsListRepository.findMainNewsListByWordAndDateGreaterThanEqual(word.get(i), beforeTime);
            for (int j = 0; j < mainNewsLists.size(); j++) {
                count2 += Integer.parseInt(mainNewsLists.get(j).getCounts());
            }
            monthlyList.add(new RankForm(word.get(i), count2));
        }


        return monthlyList.subList(0, 10);     // 상위 10개
    }
}
