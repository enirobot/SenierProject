package com.team.news.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.news.mongoDB.News;
import com.team.news.mongoDB.NewsRepository;
import com.team.news.wordCloud.WCForm;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainPageController {

    @Autowired
    private NewsRepository repository;

    @GetMapping("/main")
    public String main(Model model) {
//        List<News> news = repository.findByArticleDateGreaterThanEqual("2018/07/03 20:59");
        List list = new ArrayList<>();



        for (int i = 0; i < 10; i++) {
            Map map = new HashMap();
            map.put("word", String.valueOf(i));
            map.put("size", String.valueOf(i));
            list.add(map);
        }

        JSONArray jsonArray = new JSONArray(list);

        model.addAttribute( "json", jsonArray );

//        model.addAttribute("wordcloud", wcForms);

        return "main";
    }
}
