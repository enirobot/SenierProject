package com.team.news.Controller;

import com.team.news.Form.News;
import com.team.news.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * html 테스트할 때 사용되는 컨트롤러
 */
@Controller
public class HomeController {

    @Autowired
    private NewsRepository repository;

    @GetMapping("/home")
    public String index(Model model) {

        List<News> temp = repository.findNewsByTitleIsLike("포토");


        return "home";
    }
}
