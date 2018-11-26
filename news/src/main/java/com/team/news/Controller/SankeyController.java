package com.team.news.Controller;

import com.team.news.Form.SankeyForm;
import com.team.news.Form.SankeyFormAndDate;
import com.team.news.Repository.SankeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class SankeyController {


    private final SankeyRepository sankeyRepository;

    @Autowired
    public SankeyController(SankeyRepository sankeyRepository) {
        this.sankeyRepository = sankeyRepository;
    }

    @ResponseBody
    @PostMapping("/sankey_major_post")
    public List<SankeyForm> sankey() {
        SankeyFormAndDate sankeyFormAndDate;
        sankeyFormAndDate = sankeyRepository.findTopByGroupOrderByDateDesc("major");


        return sankeyFormAndDate.getSankeyitems();
    }

    @GetMapping("/sankey")
    public String main(Model model) {

        return "sankey";
    }
}
