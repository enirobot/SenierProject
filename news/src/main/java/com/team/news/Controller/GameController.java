package com.team.news.Controller;

import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class GameController {
    private final NewsRepository newsRepository;
    private final MongoTemplate mongoTemplate;

    private final MainNewsListRepository mainNewsListRepository;

    @Autowired
    public GameController(NewsRepository newsRepository, MainNewsListRepository mainNewsListRepository
            , MongoTemplate mongoTemplate) {
        this.newsRepository = newsRepository;
        this.mongoTemplate = mongoTemplate;
        this.mainNewsListRepository = mainNewsListRepository;
    }


    @GetMapping("/game")
    public String main(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String str = "야호";

        request.setAttribute("yaho",str);

        return "game";
    }



}
