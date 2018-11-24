package com.team.news.Controller;

import com.team.news.Form.GameDataForm;
import com.team.news.Form.MainNewsList;
import com.team.news.Form.WCForm;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import com.team.news.WebCrawler.CrawlingNaver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;

@Controller
public class GameController {

    private final MongoTemplate mongoTemplate;
    private WebDriver driver;
    private ChromeOptions options;

    @Autowired
    public GameController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;

    }


    @GetMapping("/game")
    public String main(Model model) throws Exception {
        String os = System.getProperty("os.name");

        if (os.equals("Windows 10")) {
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        }

        if (os.equals("Mac OS X")) {
            System.setProperty("webdriver.chrome.driver", "chromedriver");
        }

        options = new ChromeOptions();
        options.addArguments("headless");

        driver = new ChromeDriver(options);

        return "game";
    }

    @ResponseBody
    @PostMapping("/getKeyword")
    public List keyword()
    {
        List<GameDataForm> list = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();    // 현재 시간

        String fromTime = dateFormat.format(now.minusDays(6));         // 3시간 전
        String toTime = dateFormat.format(now.minusHours(0));           // 0시간 전

        Aggregation agg = newAggregation(
                match(Criteria.where("date").gte(fromTime).lte(toTime)
                        .and("totalWeight").gt(0)
                        .and("counts").gt(1)),
                group("word")
                        .sum("totalWeight").as("totalWeight")
                        .push("_id").as("idList"),
                sort(DESC, "totalWeight"),
                limit(30)
        );

        AggregationResults<WCForm> result = mongoTemplate.aggregate(agg, "mainNewsList", WCForm.class);
        List<WCForm> WCFormList = result.getMappedResults();

        for (WCForm item : WCFormList) {
            //System.out.println(item.getWord() + " " + item.getTotalWeight() + " " + item.getIdList().size());
            //list.add(new WCForm(item.getWord(), item.getTotalWeight(), item.getIdList()));
            driver.get("https://www.google.co.kr/");

            list.add(new GameDataForm(item.getWord(),item.getTotalWeight(),"url" ));
        }

        return list;
    }




}
