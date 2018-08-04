package com.team.news.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * html 테스트할 때 사용되는 컨트롤러
 */
@Controller
public class HomeController {

    @GetMapping("/home")
    public String index(Model model) {

        return "home";
    }
}
