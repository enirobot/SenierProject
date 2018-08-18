package com.team.news.Analysis;

import com.team.news.Form.*;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

public class Morphological {

    public void analysis(NewsRepository newsRepository, MainNewsListRepository mainNewsListRepository, String beforeTime) {

        List<MainNewsList> list = new ArrayList<>();
        HashMap<String, WCNode> wordList = new HashMap<>();
        String temp = null;

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        String currentTime = date.format(cal.getTime());

        List<News> news = newsRepository.findByDateGreaterThanEqual( beforeTime );

        // 형태소 분석
        for (News item : news) {
            for (LNode node : Analyzer.parseJava(item.getTitle())) {
                if (node.morpheme().getFeatureHead().equalsIgnoreCase("NNG")) {
                    temp = node.morpheme().getSurface();

                    if (!wordList.containsKey(temp)) {
                        wordList.put(temp, new WCNode(1));
                        wordList.get(temp).add( new MainNewsItem(
                                                    item.getTitle(),
                                                    item.getCompany(),
                                                    item.getDate(),
                                                    item.getUrl()));
                    }

                    else {
                        WCNode wcTemp = wordList.get(temp);
                        wcTemp.setCounts(wcTemp.getCounts() + 1);
                        wcTemp.add( new MainNewsItem(
                                        item.getTitle(),
                                        item.getCompany(),
                                        item.getDate(),
                                        item.getUrl()));
                        wordList.put(temp, wcTemp);
                    }
                }
            }
        }

        WCFormComparator wcFormComparator = new WCFormComparator();

        // 오름차순 정렬
        wordList.entrySet().stream()
                .sorted((k1, k2) ->
                        wcFormComparator.compare(k1.getValue(), k2.getValue()))
                .forEach(k ->
                        list.add(new MainNewsList(k.getKey(),
                                                String.valueOf(k.getValue().getCounts()),
                                                currentTime,
                                                k.getValue().getMainNewsItems())));


        //mainNewsListRepository.deleteAll();
        mainNewsListRepository.saveAll(list);   // mongoDB에 저장 (mainNewsList)
        System.out.println(list.size() + "개");
    }

    // 정렬할 때 사용할 comparator 정의
    class WCFormComparator implements Comparator<WCNode> {

        @Override
        public int compare(WCNode o1, WCNode o2) {

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
