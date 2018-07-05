package com.team.news.controller;

import com.team.news.mongoDB.News;
import com.team.news.mongoDB.NewsRepository;
import com.team.news.wordCloud.WCForm;
import com.team.news.wordCloud.WCNode;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 메인 페이지
 */

@Controller
public class MainPageController {

    private final NewsRepository repository;

    @Autowired
    public MainPageController(NewsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/main")
    public String main(Model model) {

        return "main";
    }

    @ResponseBody
    @PostMapping("/WordCloud")
    public List<WCForm> wc() {
        List<WCForm> list = new ArrayList<>();
        HashMap<String, WCNode> wordList = new HashMap<>();
        String temp = null;

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.HOUR_OF_DAY, -7 );    // 7시간 이내
        List<News> news = repository.findByDateGreaterThanEqual( date.format( cal.getTime() ) );

        // 형태소 분석
        for (News item : news) {
            for (LNode node : Analyzer.parseJava(item.title)) {
                if (node.morpheme().getFeatureHead().equalsIgnoreCase("NNG")) {
                    temp = node.morpheme().getSurface();

                    if (!wordList.containsKey(temp)) {
                        wordList.put(temp, new WCNode(1, new ArrayList<>()));
                        wordList.get(temp).add(item.url);

                    } else {
                        WCNode wcTemp = wordList.get(temp);
                        wcTemp.setCounts( wcTemp.getCounts() + 1 );
                        wcTemp.add( item.url );
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
                        list.add(new WCForm(k.getKey(),
                                String.valueOf(k.getValue().getCounts()),
                                k.getValue().getUrlList())));

        return list.subList(0, 30);     // 상위 30개
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
