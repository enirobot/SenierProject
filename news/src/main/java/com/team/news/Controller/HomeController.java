package com.team.news.Controller;
import org.slf4j.Logger;

import com.team.news.Repository.NewsRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * html 테스트할 때 사용되는 컨트롤러
 */
@RestController
public class HomeController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private NewsRepository repository;



    @GetMapping("/home")
    public String index(Model model) {
        return "home";
    }
}
