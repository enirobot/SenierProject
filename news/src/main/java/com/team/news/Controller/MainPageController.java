package com.team.news.Controller;

import com.team.news.Analysis.Morphological;
import com.team.news.Form.*;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 메인 페이지
 */

@Controller
public class MainPageController {


    private final NewsRepository newsRepository;
    private final MainNewsListRepository mainNewsListRepository;

    @Autowired
    public MainPageController(NewsRepository newsRepository, MainNewsListRepository mainNewsListRepository) {
        this.newsRepository = newsRepository;
        this.mainNewsListRepository = mainNewsListRepository;
    }

    @GetMapping("/main")
    public String main(Model model) {

        return "main";
    }

    @GetMapping("/mainNewsList")
    public String mainNewsList(Model model) {

        return "mainNewsList";
    }

    @ResponseBody
    @PostMapping("/WordCloud")
    public List<WCForm> wc() {


        List<WCForm> list = new ArrayList<>();

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.MINUTE, -30 );    // 1시간 이내
        String beforeTime = date.format(cal.getTime());

        List<MainNewsList> mainNewsLists = mainNewsListRepository.findMainNewsListByDateGreaterThanEqual( beforeTime );

        for (MainNewsList item : mainNewsLists) {
            list.add( new WCForm(item.getWord(), item.getCounts(), item.getId()) );
        }


        return list.subList(0, 30);     // 상위 30개
    }



    @ResponseBody
    @PostMapping("/newsList")
    public String NewsList(@RequestBody String data) {

        System.out.println( data );
//        System.out.println( repository.findAllById(itemList) );
//
//        newsList = repository.findAllById(itemList);
//
//        for(News item : newsList) {
//            mainNewsList.add(new MainNewsList(item.getTitle(), item.getUrl()));
//        }

        return "mainNewsList";
    }

}