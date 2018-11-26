package com.team.news.Controller;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.DistinctIterable;
import com.team.news.Form.SankeyForm;
import com.team.news.Form.SankeyFormAndDate;
import com.team.news.Repository.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class GraphController {


    private final GraphRepository graphRepository;

    @Autowired
    public GraphController(GraphRepository graphRepository) {
        this.graphRepository = graphRepository;
    }

    @ResponseBody
    @PostMapping("/sankey_major_post")
    public List<SankeyForm> sankey() {
        SankeyFormAndDate sankeyFormAndDate;
        sankeyFormAndDate = graphRepository.findTopByGroupOrderByDateDesc("major");


        return sankeyFormAndDate.getSankeyitems();
    }

    @GetMapping("/sankey")
    public String main(Model model) {

        return "sankey";
    }
}
