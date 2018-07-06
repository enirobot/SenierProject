package com.team.news.controller;

import com.team.news.MongoDB.News;
import com.team.news.MongoDB.NewsRepository;
import com.team.news.wordCloud.WCForm;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 메인 페이지
 */

@Controller
public class MainPageController {

    @Autowired
    private NewsRepository repository;

    @ResponseBody
    @PostMapping("/WordCloud")
    public List<WCForm> wc() {
        List<WCForm> list = new ArrayList<>();
        HashMap<String, Integer> wordList = new HashMap<>();
        String temp = null;

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
<<<<<<< HEAD
<<<<<<< HEAD
        cal.add( Calendar.HOUR_OF_DAY, -10 );    // 1시간 이내
=======
        cal.add( Calendar.HOUR_OF_DAY, -1 );    // 1시간 이내
>>>>>>> parent of 9229125... 내용없음
        List<News> news = repository.findByDateGreaterThanEqual( date.format( cal.getTime() ) );
=======
        cal.add( Calendar.HOUR_OF_DAY, -1 );
        Date before = cal.getTime();
        System.out.println( before );
        System.out.println( date.format( before ) );
//        List<News> news = repository.findByDateGreaterThanEqual( date.format( before ) );
        List<News> news = repository.findByDateGreaterThanEqual("2018/07/04 20:00");


        if (news.size() <= 0)
            return null;
>>>>>>> 6bed78301d80dc6da2bd5a9a027a3910905debc8

        // 형태소 분석
        for (News item : news) {
            for (LNode node : Analyzer.parseJava(item.title)) {
                if (node.morpheme().getFeatureHead().equalsIgnoreCase("NNG")) {
                    temp = node.morpheme().getSurface();

                    if (!wordList.containsKey(temp)) {
                        wordList.put(temp, 1);

                    } else {
                        wordList.put(temp, wordList.get(temp) + 1);
                    }
                }
            }
        }

        wordList.entrySet().stream()
                .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .forEach(k -> list.add(new WCForm(k.getKey(), String.valueOf(k.getValue()))));

        return list;
    }

    @GetMapping("/main")
    public String main(Model model) {

        return "main";
    }
}
