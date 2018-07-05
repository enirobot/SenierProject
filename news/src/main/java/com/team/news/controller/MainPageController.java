package com.team.news.controller;

import com.team.news.mongoDB.News;
import com.team.news.mongoDB.NewsRepository;
import com.team.news.wordCloud.WCForm;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


@Controller
public class MainPageController {

    @Autowired
    private NewsRepository repository;

    @ResponseBody
    @PostMapping("/WordCloud")
    public List<WCForm> wc() {
        List<News> news = repository.findAll();
//        List<News> news = repository.findByDateGreaterThanEqual("2018/07/03 20:00");
        List<WCForm> list = new ArrayList<>();
        HashMap<String, Integer> wordList = new HashMap<>();
        String temp = null;

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
