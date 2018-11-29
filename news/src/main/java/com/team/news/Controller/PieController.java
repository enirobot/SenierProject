package com.team.news.Controller;

import com.team.news.Form.*;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Controller
public class PieController {

    private MainNewsListRepository mainNewsListRepository;
    private NewsRepository newsRepository;
    private MongoTemplate mongoTemplate;

    @Autowired
    public PieController(MainNewsListRepository mainNewsListRepository, MongoTemplate mongoTemplate, NewsRepository Repository) {
        this.mainNewsListRepository = mainNewsListRepository;
        this.mongoTemplate = mongoTemplate;
        this.newsRepository=Repository;
    }

    @ResponseBody
    @PostMapping("/pie_post")
    public ArrayList<PieForm> Pie() {
        String categoryName;
        int value;
        ArrayList<String> category = new ArrayList<String>();
        category.add("정치");
        category.add("경제");
        category.add("사회");
        category.add("생활/문화");
        category.add("세계");
        category.add("IT/과학");
        category.add("연예");
        category.add("스포츠");

        ArrayList<PieForm> pieFormArrayList = new ArrayList<>();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();    // 현재 시간db
        String fromTime = dateFormat.format(now.minusHours(12));         // 2시간 전


            for(int j=0;j<category.size();j++)
            {
                categoryName = category.get(j);
//                System.out.println(category.get(j)+"이다");///////////////////////
                value = newsRepository.countByCategoryLikeAndDateGreaterThanEqual(categoryName, fromTime);

                PieForm tmp = new PieForm();
                tmp.setCategory(categoryName);
                tmp.setValue(value);
//                System.out.println(tmp.value+"이다");///////////////////////
               if(value != 0) {
                    pieFormArrayList.add(tmp);
               }
            }

        return pieFormArrayList;
    }

    @GetMapping("/pie")
    public String main(Model model) {

        return "pie";
    }


}
