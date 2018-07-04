package com.team.news.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String index(Model model) {
        model.addAttribute("name", "SpringBlog from Millky");

        return "home";
    }
}
