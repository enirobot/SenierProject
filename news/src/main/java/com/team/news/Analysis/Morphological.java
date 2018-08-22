package com.team.news.Analysis;

import com.mongodb.Block;
import com.mongodb.client.DistinctIterable;
import com.team.news.Form.*;
import com.team.news.Repository.GraphRepository;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import com.team.news.Repository.RankRepository;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.springframework.data.mongodb.core.MongoTemplate;

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

    public void sankey_major_analysis(NewsRepository newsrepository, GraphRepository graphRepository)
    {
        SankeyFormAndDate sankeyFormList = new SankeyFormAndDate();
        sankeyFormList.setGroup("major");

        ArrayList<String> company = new ArrayList<String>();
        company.add("중앙일보");
        company.add("국민일보");
        company.add("한겨례");
        company.add("KBS 뉴스");
        company.add("MBC 뉴스");
        company.add("JTBC");
        company.add("YTN");
        company.add("연합뉴스TV");


        ArrayList<String> category = new ArrayList<String>();
        category.add("정치");
        category.add("경제");
        category.add("사회");
        category.add("생활/문화");
        category.add("세계");
        category.add("IT/과학");
        category.add("연예");

        addList(sankeyFormList, company, category, newsrepository);

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        String currentTime = date.format(cal.getTime());

        sankeyFormList.setDate(currentTime);

        graphRepository.save(sankeyFormList);
    }

    public void sankey_minor_analysis(NewsRepository newsrepository, GraphRepository graphRepository, MongoTemplate mongoTemplate)
    {
        SankeyFormAndDate sankeyFormList = new SankeyFormAndDate();
        sankeyFormList.setGroup("minor");


        DistinctIterable<String> distinct = mongoTemplate.getCollection("news").distinct("company", String.class);
        ArrayList<String> company_else = new ArrayList<String>();
        distinct.forEach(new Block<String>() {
            @Override
            public void apply(final String result) {
                if(result != "중앙일보" || result != " 국민일보" || result != "한겨례" || result != "KBS 뉴스" || result != "MBC 뉴스" || result != "JTBC"
                || result != "YTN" || result != "연합뉴스TV") {
                    company_else.add(result);
                }
            }
        });

        ArrayList<String> category = new ArrayList<String>();
        category.add("정치");
        category.add("경제");
        category.add("사회");
        category.add("생활/문화");
        category.add("세계");
        category.add("IT/과학");
        category.add("연예");

        addList(sankeyFormList, company_else, category, newsrepository);

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        String currentTime = date.format(cal.getTime());

        sankeyFormList.setDate(currentTime);

        graphRepository.save(sankeyFormList);
    }

    private void addList( SankeyFormAndDate sankeyFormAndDate, ArrayList<String> company, ArrayList<String> category, NewsRepository repository) {

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.MINUTE, -30 );    // 1시간 이내
        String beforeTime = date.format(cal.getTime());

        for(int i=0;i<company.size();i++){
            for(int j=0;j<category.size();j++)
            {
                SankeyForm tmp = new SankeyForm();
                tmp.source = company.get(i);
                tmp.destination = category.get(j);
                tmp.value = repository.countByCategoryAndCompanyAndDateGreaterThanEqual(category.get(j), company.get(i),beforeTime);

                if(tmp.value != 0) {
                    sankeyFormAndDate.addSankeyitems(tmp);
                }
            }
        }
    }


    public void Rank_analysis(MainNewsListRepository mainNewsListRepository, RankRepository rankRepository) {

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.MINUTE, -30 );    // 1시간 이내
        String beforeTime = date.format(cal.getTime());

        List<MainNewsList> mainNewsLists = mainNewsListRepository.findMainNewsListByDateGreaterThanEqual( beforeTime );

        for (MainNewsList item : mainNewsLists) {
            RankForm rankForm = rankRepository.findRankFormByWord(item.getWord());

            if (rankForm != null) {
                rankForm.setTotalCounts( rankForm.getTotalCounts() + Integer.parseInt(item.getCounts()) );
                rankForm.add( new RankNode( item.getDate(), Integer.parseInt(item.getCounts()) ) );
                rankRepository.save( rankForm );
            }

            else {
                RankForm temp = new RankForm(item.getWord(), Integer.parseInt(item.getCounts()));
                temp.add( new RankNode( item.getDate(), Integer.parseInt(item.getCounts()) ) );
                rankRepository.save( temp );
            }
        }
    }

}
