package com.team.news.Controller;

import com.mongodb.Block;
import com.mongodb.client.DistinctIterable;
import com.team.news.Form.MainNewsList;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import com.team.news.Form.RankForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.String;

@Controller
public class RankController {

    private NewsRepository newsRepository;
    private MainNewsListRepository mainNewsListRepository;
    private MongoTemplate mongoTemplate;
    private DistinctIterable<String> distinct;

    @Autowired
    public RankController(NewsRepository newsRepository, MainNewsListRepository mainNewsListRepository,
                          MongoTemplate mongoTemplate) {
        this.newsRepository = newsRepository;
        this.mainNewsListRepository = mainNewsListRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/ranking")
    public String index(Model model) {

        return "ranking";
    }

    @ResponseBody
    @PostMapping("/weeklyList")
    public List<RankForm> weeklyRank() {

        List<RankForm> weeklyList = new ArrayList<>();

//        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
//        Calendar cal1 = Calendar.getInstance();
//        cal1.add(Calendar.DATE, -(Calendar.DAY_OF_WEEK) + 1); // 현재 날짜가 포함된 주의 시작날
//        cal1.set(Calendar.HOUR_OF_DAY, 0);      // 시간을 0으로 초기화
//        cal1.clear(Calendar.MINUTE);            // 분을 0으로 초기화
//        String beforeTime = date.format(cal1.getTime());
//        System.out.println(beforeTime);

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.MINUTE, -30 );    // 30분 이내
        String beforeTime = date.format(cal.getTime());

        distinct = mongoTemplate.getCollection("mainNewsList").distinct("word", String.class);

        List<MainNewsList> mainNewsLists;

        int  weeklyListCount = 0;
        for (String item : distinct) {
            System.out.println(item);
            mainNewsLists = mainNewsListRepository.findMainNewsListByWordAndDateGreaterThanEqual(item, beforeTime);

            for (MainNewsList mitem : mainNewsLists) {
                weeklyListCount += Integer.parseInt( mitem.getCounts() );
            }

            if (weeklyListCount != 0) {
                weeklyList.add( new RankForm(item, weeklyListCount) );
            }

            weeklyListCount = 0;
        }

//        Collections.sort(weeklyList, new MainNewsListComparator());

        return null;
    }

    @ResponseBody
    @PostMapping("/monthlyList")
    public List<RankForm> monthlyRank() {

        List<RankForm> monthlyList = new ArrayList<>();

//        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
//        Calendar cal2 = Calendar.getInstance();
//        cal2.set(Calendar.DATE, 1);             // 현재 날짜가 포함된 월의 시작날
//        cal2.set(Calendar.HOUR_OF_DAY, 0);      // 시간을 0으로 초기화
//        cal2.clear(Calendar.MINUTE);            // 분을 0으로 초기화
//        String beforeTime = date.format(cal2.getTime());
//        System.out.println(beforeTime);

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.MINUTE, -30 );    // 30분 이내
        String beforeTime = date.format(cal.getTime());

        distinct = mongoTemplate.getCollection("mainNewsList").distinct("word", String.class);

        List<MainNewsList> mainNewsLists;

        int  monthlyListCount = 0;
        for (String item : distinct) {
            System.out.println(item);
            mainNewsLists = mainNewsListRepository.findMainNewsListByWordAndDateGreaterThanEqual(item, beforeTime);

            for (MainNewsList mitem : mainNewsLists) {
                monthlyListCount += Integer.parseInt( mitem.getCounts() );
            }

            if (monthlyListCount != 0) {
                monthlyList.add( new RankForm(item, monthlyListCount) );
            }

            monthlyListCount = 0;
        }

//        Collections.sort(monthlyList, new MainNewsListComparator());


//        return monthlyList.subList(0, monthlyList.size());     // 상위 10개
        return null;
    }




//
//    // 정렬할 때 사용할 comparator 정의
//    class MainNewsListComparator implements Comparator<RankForm> {
//
//        @Override
//        public int compare(RankForm o1, RankForm o2) {
//
//            if (o1.getCounts() > o2.getCounts()) {
//                return -1;
//            } else if (o1.getCounts() < o2.getCounts()) {
//                return 1;
//            } else {
//                return 0;
//            }
//        }
//    }
}


