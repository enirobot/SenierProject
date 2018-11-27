package com.team.news.Controller;

import com.team.news.Form.*;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;

@Controller
public class BubbleController {

    private MainNewsListRepository mainNewsListRepository;
    private MongoTemplate mongoTemplate;

    @Autowired
    public BubbleController( MainNewsListRepository mainNewsListRepository, MongoTemplate mongoTemplate) {
        this.mainNewsListRepository = mainNewsListRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @ResponseBody
    @PostMapping("/bubble_post")
    public List<BubbleForm> bubble() {
        List<BubbleForm> list = new ArrayList<>();
        int count = 0;

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();    // 현재 시간db
        String fromTime = dateFormat.format(now.minusHours(12));         // 2시간 전
        String toTime = dateFormat.format(now.minusHours(0));           // 0시간 전

        List<MainNewsList> mainNewsLists = mainNewsListRepository. findMainNewsListByDateGreaterThanEqual(fromTime);
        //리스트에 값을 받아옴
        System.out.println("check"+mainNewsLists);


        for (MainNewsList item : mainNewsLists) {
            if(item.getCounts()<5||item.getTotalWeight()>3||item.getTotalWeight()<0.1)//카운트 20이하거름
                continue;

            list.add(new BubbleForm(item.getWord(),item.getCommentcounts(), item.getCounts(), item.getReactioninfo(),item.getTotalWeight()));
            count++;
            System.out.println("check"+item.getWord()+item.getCounts());
            if(count == 25 )
                break;
        }//값 10개까지만넣음


        for (BubbleForm bubbleForm : list) {
            System.out.println(bubbleForm.getWord()+" "+bubbleForm.getCommentcounts()+" "+bubbleForm.getReactioninfo()+" "+bubbleForm.getTotalweight()+" "+bubbleForm.getCounts());
        }

        return list;     // 10개넣어줌

    }

    @GetMapping("/bubble")
    public String main(Model model) {

        return "bubble";
    }



}
