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


    @ResponseBody
    @PostMapping("/sankey_minor_post")
    public List<SankeyForm> sankey_minor() {

        List<SankeyForm> sort_list, list;

        SankeyFormAndDate sankeyFormAndDate;
        sankeyFormAndDate = graphRepository.findTopByGroupOrderByDateDesc("minor");

        list = sankeyFormAndDate.getSankeyitems();
        sort_list = sankeyFormAndDate.getSankeyitems();

        if(list.size() > 10)
        {
            sort_list.sort(Comparator.comparing(SankeyForm::getValue));
            return sort_list.subList(list.size()-10,list.size());
        }

        return sankeyFormAndDate.getSankeyitems();
    }

    @ResponseBody
    @PostMapping("/sankey_sports_post")
    public List<SankeyForm> sankey_sports() {

        SankeyFormAndDate sankeyFormAndDate;
        sankeyFormAndDate = graphRepository.findTopByGroupOrderByDateDesc("sports");

        return sankeyFormAndDate.getSankeyitems();
    }

    @GetMapping("/sankey")
    public String main(Model model) {

        return "sankey";
    }
}
