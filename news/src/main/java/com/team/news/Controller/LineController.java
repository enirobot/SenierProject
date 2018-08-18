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

        return "line";
    }

    @ResponseBody
    @PostMapping("/lineChart")
    public List<LineForm> line(@RequestBody String keyword) {

        List<LineForm> list = new ArrayList<>();

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);     // 검색일로부터 일주일 전의 날짜를 저장
        String beforeTime = date.format(cal.getTime());
        System.out.println(beforeTime);
        System.out.println(keyword);

//        int mainNewsLists = mainNewsListRepository.countMainNewsListByDateGreaterThanEqualAndWord(beforeTime, keyword);
//        List<LineForm> mainNewsLists = mainNewsListRepository.findMainNewsListByDateGreaterThanEqual(beforeTime);
//        for (MainNewsList item : mainNewsLists) {
//            if(keyword.equals("\"" + item.getWord() + "\""))    // keyword를 큰 따옴표가 붙은 형태로 받아옴
//                list.add(new LineForm(item.getWord(), item.getCounts(), item.getDate()));
//        }

        for (LineForm item : list.subList(0, 7)){
            System.out.println(item.getWord() + ' ' + item.getCounts() + ' ' + item.getDate());
        }
        return list.subList(0, 7);     // 상위 10개
    }
}
