package com.team.news.Controller;

import com.team.news.Form.MainNewsList;
import com.team.news.Form.WCForm;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import com.team.news.Form.LineForm;
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;

@Controller
public class LineController {

    private NewsRepository newsRepository;
    private MainNewsListRepository mainNewsListRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public LineController(NewsRepository newsRepository, MainNewsListRepository mainNewsListRepository, MongoTemplate mongoTemplate) {
        this.newsRepository = newsRepository;
        this.mainNewsListRepository = mainNewsListRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/line")
    public String main(Model model) {
//        System.out.println(33);
        return "line";
    }

    @ResponseBody
    @PostMapping("/lineChart")
    public List<LineForm> line() {

       // List<LineForm> list = new ArrayList<>();
        List<LineForm> list2 = new ArrayList<>();
        List<MainNewsList> mainNewsListHist = new ArrayList<>();
       // List<WCForm> list = new ArrayList<>();

        int count=0;

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -2);     // 2시간 전
        String beforeTime = date.format(cal.getTime());
//        System.out.println(beforeTime);

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();    // 현재 시간
        String fromTime = dateFormat.format(now.minusHours(3));         // 3시간 전
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
    //단어별 3시간 단위의 종합적 신뢰도를 합하여 큰 것 순으로 위에서부터 가져옴

        for(WCForm item : WCFormList) {
           mainNewsListHist.addAll( mainNewsListRepository.findMainNewsListsByDateBetweenAndWord(fromTime, toTime, item.getWord()));
  //         System.out.println(item.getWord());//검찰 출석 조사
            count++;
            if(count==3)
                break;
        }//3개의 단어를 각각에 대한 Hist 정보를 저장

        for (MainNewsList item : mainNewsListHist) {
            list2.add(new LineForm(item.getWord(),item.getCounts(),item.getDate()));
//            System.out.println(item.getWord());
        } //Line그래프를 위한 저장

//        for (LineForm item : list2){
//           System.out.println(item.getWord() + ' ' + item.getCounts() + ' ' + item.getDate());
//           System.out.println(7);
//        }//확인 기능

        return list2;     // 상위 10개
    }
}
