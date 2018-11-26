package com.team.news.Controller;

import com.team.news.Form.*;
import com.team.news.Repository.GraphRepository;
import com.team.news.Repository.GraphRepository2;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class BubbleController {

    private final GraphRepository2 graphRepository2;
    private MainNewsListRepository mainNewsListRepository;

    @Autowired
    public BubbleController(GraphRepository2 graphRepository, MainNewsListRepository mainNewsListRepository) {
        this.mainNewsListRepository = mainNewsListRepository;
        this.graphRepository2 = graphRepository;
    }

    @PostMapping("/bubble_post")
    public List<BubbleForm> bubble() {
        List<BubbleForm> list = new ArrayList<>();
        int count = 0;

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();    // 현재 시간
        String fromTime = dateFormat.format(now.minusHours(-1));         // 1시간 전
        String toTime = dateFormat.format(now.minusHours(0));           // 0시간 전

        List<MainNewsList> mainNewsLists = mainNewsListRepository.findByDateBetweenOrderByTotalWeightDescCountsDesc(fromTime, toTime);
        //리스트에 값을 받아옴

        for (MainNewsList item : mainNewsLists) {
            list.add(new BubbleForm(item.getWord(), item.getCounts(), item.getCommentcounts(),item.getReactioninfo(),item.getTotalWeight()));
            count++;
            System.out.println(item.getWord());
            if(count == 10 )
                break;
        }//값 10개까지만넣음


        return list;     // 10개넣어줌

    }

    @GetMapping("/bubble")
    public String main(Model model) {

        return "bubble";
    }



}
