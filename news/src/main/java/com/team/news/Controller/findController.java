package com.team.news.Controller;

import com.team.news.Form.*;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 *
 */

@Controller
public class findController {

    private final NewsRepository newsRepository;
    private final MainNewsListRepository mainNewsListRepository;

    @Autowired
    public findController(NewsRepository newsRepository, MainNewsListRepository mainNewsListRepository) {
        this.newsRepository = newsRepository;
        this.mainNewsListRepository = mainNewsListRepository;
    }


    @ResponseBody
	@PostMapping("/findKeyword")
    public List<WCSearchForm> findKeyword(@RequestBody String data) {

//        System.out.println(data);

        List<WCSearchForm> list = new ArrayList<>();
        HashMap<String, WCSearchNode> wordList = new HashMap<>();
        String temp;

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.HOUR_OF_DAY, -3 );    // 24시간 이내
        String beforeTime = date.format(cal.getTime());

        List<News> news = newsRepository.findNewsByTitleLikeAndDateGreaterThanEqual( data.replaceAll("\"", ""), beforeTime );


        // 형태소 분석
        for (News item : news) {
            for (LNode node : Analyzer.parseJava(item.getTitle())) {
                if (node.morpheme().getFeatureHead().equalsIgnoreCase("NNG")) {
                    temp = node.morpheme().getSurface();

                    if (!wordList.containsKey(temp)) {
                        wordList.put(temp, new WCSearchNode(1));
                        wordList.get(temp).add(item.getId());
                    }

                    else {
                        WCSearchNode wcTemp = wordList.get(temp);
                        wcTemp.setCounts(wcTemp.getCounts() + 1);
                        wcTemp.add(item.getId());
                        wordList.put(temp, wcTemp);
                    }
                }
            }
        }

        WCSearchFormComparator wcFormComparator = new WCSearchFormComparator();

        // 오름차순 정렬
        wordList.entrySet().stream()
                .sorted((k1, k2) ->
                        wcFormComparator.compare(k1.getValue(), k2.getValue()))
                .forEach(k ->
                        list.add(new WCSearchForm(k.getKey(),
                                String.valueOf(k.getValue().getCounts()),
                                k.getValue().getIdList())));

//        System.out.println(list.size() + "개");

        if (list.size() > 10) {
            return list.subList(0, 10);
        } else {
            return list;
        }
    }

    // 정렬할 때 사용할 comparator 정의
    class WCSearchFormComparator implements Comparator<WCSearchNode> {

        @Override
        public int compare(WCSearchNode o1, WCSearchNode o2) {

            if (o1.getCounts() > o2.getCounts()) {
                return -1;
            } else if (o1.getCounts() < o2.getCounts()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
