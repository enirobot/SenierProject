package com.team.news.Controller;

import com.team.news.Form.MainNewsList;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import com.team.news.Form.LineForm;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class LineController {

    private NewsRepository newsRepository;
    private MainNewsListRepository mainNewsListRepository;

    @Autowired
    public LineController(NewsRepository newsRepository, MainNewsListRepository mainNewsListRepository) {
        this.newsRepository = newsRepository;
        this.mainNewsListRepository = mainNewsListRepository;
    }

    @GetMapping("/line")
    public String main(Model model) {
        System.out.println(33);
        return "line";
    }

    @ResponseBody
    @PostMapping("/lineChart")
    public List<LineForm> line() {

        List<LineForm> list = new ArrayList<>();
        List<LineForm> list2 = new ArrayList<>();
        List<MainNewsList> mainNewsListHist = new ArrayList<>();
        int count=0;

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -2);     // 2시간 전
        String beforeTime = date.format(cal.getTime());
        System.out.println(beforeTime);

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();    // 현재 시간
        String fromTime = dateFormat.format(now.minusHours(3));         // 3시간 전
        String toTime = dateFormat.format(now.minusHours(0));           // 0시간 전

        List<MainNewsList> mainNewsLists = mainNewsListRepository.findByDateBetweenAndTotalWeightGreaterThanOrderByTotalWeightDescCountsDesc(fromTime, toTime, 0);
        
        for (MainNewsList item : mainNewsLists) {
                list.add(new LineForm(item.getWord(), item.getCounts(), item.getDate()));
                count++;
                System.out.println(item.getWord());
                if(count == 3 )
                    break;
        }

        //
        for(LineForm item : list) {
           mainNewsListHist.addAll( mainNewsListRepository.findMainNewsListsByDateBetweenAndWord(fromTime, toTime, item.getWord()));
           System.out.println(item.getWord());//검찰 출석 조사
        }

        for (MainNewsList item : mainNewsListHist) {
            list2.add(new LineForm(item.getWord(),item.getCounts(),item.getDate()));
            System.out.println(item.getWord());
        }

        //for (LineForm item : list2){
           // System.out.println(item.getWord() + ' ' + item.getCounts() + ' ' + item.getDate());
           // System.out.println(7);
        //}//확인 기능

        return list2;     // 상위 10개
    }
}
