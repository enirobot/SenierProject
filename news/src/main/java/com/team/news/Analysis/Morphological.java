package com.team.news.Analysis;

import com.mongodb.Block;
import com.mongodb.client.DistinctIterable;
import com.team.news.Form.*;
import com.team.news.Repository.SankeyRepository;
import com.team.news.Repository.MainNewsListRepository;
import com.team.news.Repository.NewsRepository;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Morphological {

    private Logger logger = LoggerFactory.getLogger(Morphological.class);


    public void analysis(NewsRepository newsRepository, MainNewsListRepository mainNewsListRepository,
                         String MorphologicalTime) {

        List<MainNewsList> list = new ArrayList<>();
        HashMap<String, WCNode> wordList = new HashMap<>();
        String keyword = null;

        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String currentTime = dateFormat.format(today);

        List<News> news = newsRepository.findByCrawlingDateGreaterThanEqual(MorphologicalTime);

        logger.info("news 개수 : " + news.size() + "개");

        // 형태소 분석
        for (News item : news) {
            for (LNode node : Analyzer.parseJava(item.getTitle())) {
                if (node.morpheme().getFeatureHead().equalsIgnoreCase("NNG")) {

                    keyword = node.morpheme().getSurface();
                    if (keyword.length() < 2)
                        continue;

                    if (!wordList.containsKey(keyword)) {
                        WCNode wcNode = new WCNode(1,
                                item.getWeight(),
                                item.getEmtion_weight(),1);
                        wcNode.add(new MainNewsItem(
                                item.getTitle(),
                                item.getCompany(),
                                item.getDate(),
                                item.getUrl()));
                        wordList.put(keyword, wcNode);
                    }

                    else {

                        WCNode wcTemp = wordList.get(keyword);  // 키워드에 해당되는 값 가져옴
                        wcTemp.sumCounts(1);    // 카운트 1씩 증가
                        wcTemp.sumTotalWeight(item.getWeight());    // 가중치
                        wcTemp.sumTotalEmotionWeight(item.getEmtion_weight());
                        wcTemp.sumTotalComment(item.getComment_count());
                        wcTemp.add( new MainNewsItem(
                                        item.getTitle(),
                                        item.getCompany(),
                                        item.getDate(),
                                        item.getUrl()));
                        wordList.put(keyword, wcTemp);  // 키워드에 해당되는 값 갱신
                    }
                }
            }
        }

        for (String key : wordList.keySet()) {
            WCNode item = wordList.get(key);

            if (item.getTotalWeight() <= 0 || item.getCounts() <= 1)
                continue;

            list.add(new MainNewsList(key,
                    item.getCounts(),
                    currentTime,
                    item.getTotalWeight(),
                    item.getTotalEmtionWeight(),
                    item.getTotalCommentCount(),
                    item.getMainNewsItems()));
        }

        mainNewsListRepository.saveAll(list);   // mongoDB에 저장 (mainNewsList)

        logger.info("형태소분석 : " + list.size() + "개");
    }

    public void sankey_major_analysis(NewsRepository newsrepository, SankeyRepository sankeyRepository, MongoTemplate mongoTemplate)
    {
        int sankeySize = 0;
        ArrayList<SankeyForm> sankeyForms;
        List<SankeyForm> revSankeyForms;
        SankeyFormAndDate sankeyFormList = new SankeyFormAndDate();
        sankeyFormList.setGroup("major");

        ArrayList<String> company = new ArrayList<String>();
        ArrayList<String> category = new ArrayList<String>();

        category.add("정치");
        category.add("경제");
        category.add("사회");
        category.add("생활/문화");
        category.add("세계");
        category.add("IT/과학");
        category.add("연예");
        category.add("스포츠");

        DistinctIterable<String> distinct = mongoTemplate.getCollection("news").distinct("company", String.class);

        distinct.forEach(new Block<String>() {
            @Override
            public void apply(final String result) {
                    company.add(result);

            }
        });

        sankeyForms = getSankeyFormList(company, category, newsrepository);
        sankeySize = sankeyForms.size();

        revSankeyForms = sankeyForms.subList(sankeySize / 2, sankeySize);


        sankeyForms = new ArrayList<>( sankeyForms.subList(0, sankeySize / 2 ) );

        for (SankeyForm item : revSankeyForms) {
            SankeyForm temp = new SankeyForm();

            temp.setSource( item.getDestination() );
            temp.setDestination( item.getSource() );
            temp.setValue( item.getValue() );

            sankeyForms.add( temp );
        }

        sankeyFormList.setSankeyitems( sankeyForms );

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        String currentTime = date.format(cal.getTime());
        sankeyFormList.setDate(currentTime);

        sankeyRepository.save(sankeyFormList);
    }
    private ArrayList<SankeyForm> getSankeyFormList( List<String> company, List<String> category, NewsRepository repository) {
        String companyName;
        String destinationName;
        int value;

        ArrayList<SankeyForm> sankeyFormArrayList = new ArrayList<>();

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.HOUR, -2 );    // 2시간 이내
        String beforeTime = date.format(cal.getTime());


        for(int i=0;i<company.size();i++){
            companyName = company.get(i);

            for(int j=0;j<category.size();j++)
            {
                destinationName = category.get(j);
                value = repository.countByCategoryLikeAndCompanyAndDateGreaterThanEqual(destinationName,
                        companyName,
                        beforeTime);

                SankeyForm tmp = new SankeyForm();
                tmp.setSource(companyName);
                tmp.setDestination(destinationName);
                tmp.setValue(value);

                if(value != 0) {
                    sankeyFormArrayList.add(tmp);
                }
            }
        }

        return sankeyFormArrayList;
    }

}
