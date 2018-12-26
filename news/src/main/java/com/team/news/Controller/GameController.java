package com.team.news.Controller;

import com.team.news.Form.*;


import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import org.bson.types.ObjectId;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;

@Controller
public class GameController {

    private final MongoTemplate mongoTemplate;
    private final MainNewsListRepository mainNewsListRepository;
    private final NewsRepository newsRepository;
    private String fromTime;
    private String toTime;
    private HashMap<String, List<String>> newsMap;

    @Autowired
    public GameController(MongoTemplate mongoTemplate, MainNewsListRepository mainNewsListRepository, NewsRepository newsRepository) {
        this.mongoTemplate = mongoTemplate;
        this.mainNewsListRepository=mainNewsListRepository;
        this.newsRepository=newsRepository;
    }

    @GetMapping("/game")
    public String main(Model model) throws Exception {

        newsMap = new HashMap<String, List<String>>();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();    // 현재 시간

        fromTime = dateFormat.format(now.minusDays(216));         // 3시간 전
        toTime = dateFormat.format(now.minusHours(0));

        return "game";
    }

    @ResponseBody
    @PostMapping("/getKeyword")
    public List keyword()
    {
        int cnt = 0;
        List<GameDataForm> list = new ArrayList<>();
        Aggregation agg = newAggregation(
                match(Criteria.where("date").gte(fromTime).lte(toTime)
                        .and("totalWeight").gt(0)
                        .and("counts").gt(1)),
                group("word")
                        .sum("totalWeight").as("totalWeight")
                        .push("_id").as("idList"),
                sort(DESC, "totalWeight"),
                limit(30)
        );

        AggregationResults<WCForm> result = mongoTemplate.aggregate(agg, "mainNewsList", WCForm.class);
        List<WCForm> WCFormList = result.getMappedResults();

        for (WCForm item : WCFormList) {
//            System.out.println(item.getWord() + " " + item.getTotalWeight());
            newsMap.put(item.getWord(),item.getIdList());
            list.add(new GameDataForm(item.getWord(),item.getTotalWeight(),"url" ));
        }

        return list;
    }

    @ResponseBody
    @PostMapping("/getNewsList")
    public  List<MainNewsItem> newslist(@RequestBody String word)
    {
        word = word.replace("\"","");

        List<MainNewsItem> idList = new ArrayList<>();
        List<MainNewsList> mainNewsList;

        mainNewsList = mainNewsListRepository.findByIdIn( newsMap.get(word) );



        for (MainNewsList item : mainNewsList) {
            for (MainNewsItem tem : item.getNewsItems()) {
                if(!idList.contains(tem.getTitle()))
                    idList.add(tem);
                if (idList.size() > 10)
                    break;
            }
            if (idList.size() > 10)
                break;
        }

        return idList;
    }




}
