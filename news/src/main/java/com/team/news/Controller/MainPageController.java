package com.team.news.Controller;

import com.team.news.Form.*;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * 메인 페이지
 */

@Controller
public class MainPageController {

    Logger logger = LoggerFactory.getLogger(MainPageController.class);


    private final NewsRepository newsRepository;
    private final MongoTemplate mongoTemplate;

    private final MainNewsListRepository mainNewsListRepository;

    @Autowired
    public MainPageController(NewsRepository newsRepository, MainNewsListRepository mainNewsListRepository
            , MongoTemplate mongoTemplate) {
        this.newsRepository = newsRepository;
        this.mongoTemplate = mongoTemplate;
        this.mainNewsListRepository = mainNewsListRepository;
    }

    @GetMapping("/main")
    public String main(Model model) throws Exception {
        return "main";
    }

    @GetMapping("/index")
    public String index(Model model) {

        return "index";
    }

    @GetMapping("/mainNewsList")
    public String mainNewsList(Model model) {

        return "mainNewsList";
    }

    @GetMapping("/mainSearchNewsList")
    public String mainSearchNewsList(Model model) {

        return "mainSearchNewsList";
    }

    @ResponseBody
    @PostMapping("/analysisSearchNewsList")
    public List<News> analysisSearchNewsList(@RequestBody List<String> dataList) {

        List<News> newsList = new ArrayList<>();

        for (String item : dataList)
            newsList.add(newsRepository.findNewsById( item.replaceAll("\"", "") ) );

        return newsList;
    }


    /**
     * main wordcloude에 보여줄 때 사용되는 데이터를 보내줌
     * @return
     */
    @ResponseBody
    @PostMapping("/WordCloud")
    public List<WCForm> wc() {

        List<WCForm> list = new ArrayList<>();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();    // 현재 시간

        String fromTime = dateFormat.format(now.minusHours(10));         // 3시간 전
        String toTime = dateFormat.format(now.minusHours(0));           // 0시간 전

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

        logger.info("WCFormList size:" + WCFormList.size());

        for (WCForm item : WCFormList) {
            System.out.println(item.getWord() + " " + item.getTotalWeight() + " " + item.getIdList().size());
            list.add(new WCForm(item.getWord(), item.getTotalWeight(), item.getIdList()));
        }

        logger.info("wordcloud 개수 : " + WCFormList.size() + "개");

        return list;     // 상위 30개
    }


    /**
     * main wordcloude에서 단어를 눌렀을 때 리스트 페이지 보여주는데 필요한 데이터들을 보내줌
     * @param data
     * @return
     */
    @ResponseBody
    @PostMapping("/newsList")
    public List<MainNewsItem> NewsList(@RequestBody String data) {

        List<MainNewsItem> idList = new ArrayList<>();
        List<MainNewsList> mainNewsList;

        // url 형식으로 들어오는 id의 list를 잘라내어 그것을 가지고 mainNewsItem들을 가져옴
        mainNewsList = mainNewsListRepository.findByIdIn(
                Arrays.asList(data.replaceAll("\"", "").split(",")));

        for (MainNewsList item : mainNewsList)
            for (MainNewsItem tem: item.getNewsItems())
                idList.add(tem);

        return idList;
    }

}