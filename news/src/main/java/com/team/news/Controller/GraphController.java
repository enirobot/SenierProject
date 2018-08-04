package com.team.news.Controller;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.DistinctIterable;
import com.team.news.Form.SankeyForm;
import com.team.news.Repository.NewsRepository;
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


    private final NewsRepository repository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public GraphController(NewsRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @ResponseBody
    @PostMapping("/sankey_post")
    public List<SankeyForm> sankey() {

        System.out.println("sankey");
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

        addList(list, company, category);

        return list;
    }


    @ResponseBody
    @PostMapping("/sankey_sports_post")
    public List<SankeyForm> sankey_sports() {

        System.out.println("sankey_sports");
        List<SankeyForm> list = new ArrayList<>();

        DistinctIterable<String> distinct = mongoTemplate.getCollection("news").distinct("company", String.class);
        ArrayList<String> company = new ArrayList<String>();
        distinct.forEach(new Block<String>() {
            @Override
            public void apply(final String result) {
                company.add(result);
            }
        });


        BasicDBObject querry = new BasicDBObject();
        querry.put("category",java.util.regex.Pattern.compile("스포츠"));
        distinct = mongoTemplate.getCollection("news").distinct("category",querry, String.class);
        ArrayList<String> category = new ArrayList<String>();
        distinct.forEach(new Block<String>() {
            @Override
            public void apply(final String result) {
                category.add(result);
            }
        });

        addList(list, company, category);

        return list;
    }


    private void addList(List<SankeyForm> list, ArrayList<String> company, ArrayList<String> category) {
        for(int i=0;i<company.size();i++){
            for(int j=0;j<category.size();j++)
            {
                SankeyForm tmp = new SankeyForm();
                tmp.source = company.get(i);
                tmp.destination = category.get(j);
                tmp.value = repository.countByCategoryAndCompany(category.get(j), company.get(i));

                if(tmp.value != 0)
                    list.add(tmp);
            }
        }
    }


    @GetMapping("/sankey")
    public String main(Model model) {

        return "sankey";
    }
}
