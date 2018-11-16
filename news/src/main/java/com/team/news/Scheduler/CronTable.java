package com.team.news.Scheduler;

import com.team.news.Analysis.Morphological;
import com.team.news.Repository.GraphRepository;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import com.team.news.WebCrawler.CrawlingNaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


/**
 * 스케줄러가 할 일 정의
 */
@Component
public class CronTable {

    private final NewsRepository newsRepository;
    private final MainNewsListRepository mainNewsListRepository;
    private final GraphRepository graphRepository;
    private final MongoTemplate mongoTemplate;

    private Logger logger = LoggerFactory.getLogger(CronTable.class);


    @Autowired
    public CronTable(NewsRepository newsRepository, MainNewsListRepository mainNewsListRepository
            , GraphRepository graphRepository, MongoTemplate mongoTemplate) {
        this.newsRepository = newsRepository;
        this.mainNewsListRepository = mainNewsListRepository;
        this.graphRepository = graphRepository;
        this.mongoTemplate = mongoTemplate;
    }

//    // 매일 21시 30분 0초에 실행
//    @Scheduled(cron = "0 30 21 * * *")
//    public void dayJob() {
//
//    }
//
//    // 매월 1일 0시 0분 0초에 실행
//    @Scheduled(cron = "0 0 0 1 * *")
//    public void monthJob() {
//
//    }
//
    //서버 시작하고 10초후에 실행 후 30분마다 실행
    @Scheduled(initialDelay = 10000, fixedDelay = 1800000)
    public void Job() {

        System.out.println("--CronTable start--");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();    // 현재 시간

        String fromTime = dateFormat.format(now.minusHours(3));         // 3시간 전
        String toTime = dateFormat.format(now.minusHours(0));           // 0시간 전
//        String MorphologicalTime = dateFormat.format(now.minusMinutes(40));
        String MorphologicalTime = dateFormat.format(now.minusMinutes(600));

        logger.info("web crawling start");
        CrawlingNaver crawlingNaver = new CrawlingNaver(newsRepository);
        crawlingNaver.start();
        logger.info("web crawling end");

        logger.info("web morphological start");
        Morphological morphological = new Morphological();
        morphological.analysis(newsRepository, mainNewsListRepository, MorphologicalTime);
        logger.info("web morphological end");

        logger.info("web sankey_major_analysis start");
        morphological.sankey_major_analysis(newsRepository,graphRepository);
        logger.info("web sankey_major_analysis end");
        logger.info("web sankey_minor_analysis start");
        morphological.sankey_minor_analysis(newsRepository,graphRepository,mongoTemplate);
        logger.info("web sankey_minor_analysis end");
        logger.info("web sankey_sports_analysis start");
        morphological.sankey_sports_analysis(newsRepository,graphRepository,mongoTemplate);
        logger.info("web sankey_sports_analysis end");

        System.out.println("--CronTable end--");

    }
}
