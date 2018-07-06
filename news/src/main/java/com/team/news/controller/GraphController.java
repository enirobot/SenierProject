package com.team.news.controller;

import com.mongodb.Block;
import com.mongodb.client.DistinctIterable;
import com.team.news.Form.SankeyForm;
import com.team.news.MongoDB.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class GraphController {


    @Autowired
    private NewsRepository repository;
    @Autowired
    MongoTemplate mongoTemplate;

    @ResponseBody
    @PostMapping("/sankey_post")
    public List<SankeyForm> sankey() {

        System.out.println("efeeefe");
        List<SankeyForm> list = new ArrayList<>();

        DistinctIterable<String> distinct = mongoTemplate.getCollection("news").distinct("company", String.class);
        ArrayList<String> company = new ArrayList<String>();
        distinct.forEach(new Block<String>() {
            @Override
            public void apply(final String result) {
                company.add(result);
            }
        });

        distinct = mongoTemplate.getCollection("news").distinct("category", String.class);
        ArrayList<String> category = new ArrayList<String>();
        distinct.forEach(new Block<String>() {
            @Override
            public void apply(final String result) {
                category.add(result);
            }
        });

        for(int i=0;i<company.size();i++){
            for(int j=0;j<category.size();j++)
            {
                SankeyForm tmp = new SankeyForm();
                tmp.source = company.get(i);
                tmp.destination = category.get(j);
                tmp.value = repository.countByCategoryAndCompany(company.get(i),category.get(j));

                System.out.println(tmp.toString());
                list.add(tmp);
            }
        }

        return list;
    }

    @GetMapping("/sankey")
    public String main(Model model) {

        return "sankey";
    }
}
