package com.team.news.Analysis;

import com.mongodb.Block;
import com.mongodb.client.DistinctIterable;
import com.team.news.Form.*;
import com.team.news.Repository.GraphRepository;
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
                                item.getEmtion_weight(),1);//1
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
                        wcTemp.sumTotalComment(item.getComment_count());//////////////////
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
                    item.getTotalCommentCount(),/////
                    item.getMainNewsItems()));
        }

        mainNewsListRepository.saveAll(list);   // mongoDB에 저장 (mainNewsList)

        logger.info("형태소분석 : " + list.size() + "개");
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

        List<SankeyForm> list = new ArrayList<>();

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

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar cal = Calendar.getInstance();
        String currentTime = date.format(cal.getTime());

        sankeyFormList.setDate(currentTime);

        graphRepository.save(sankeyFormList);
    }

    public void sankey_sports_analysis(NewsRepository newsrepository, GraphRepository graphRepository, MongoTemplate mongoTemplate)
    {
        SankeyFormAndDate sankeyFormList = new SankeyFormAndDate();
        sankeyFormList.setGroup("sports");

        DistinctIterable<String> distinct = mongoTemplate.getCollection("news").distinct("company", String.class);
        ArrayList<String> company = new ArrayList<String>();
        distinct.forEach(new Block<String>() {
            @Override
            public void apply(final String result) {
                company.add(result);
            }
        });

        ArrayList<String> category = new ArrayList<String>();
        category.add("야구");
        category.add("해외야구");
        category.add("축구");
        category.add("해외축구");
        category.add("농구");
        category.add("e스포츠");

        addList(sankeyFormList, company, category, newsrepository);

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar cal = Calendar.getInstance();
        String currentTime = date.format(cal.getTime());

        sankeyFormList.setDate(currentTime);

        graphRepository.save(sankeyFormList);
    }

    private void addList( SankeyFormAndDate sankeyFormAndDate, ArrayList<String> company,
                          ArrayList<String> category, NewsRepository repository) {

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.HOUR, -2 );    // 2시간 이내
        String beforeTime = date.format(cal.getTime());

        for(int i=0;i<company.size();i++){
            for(int j=0;j<category.size();j++)
            {
                SankeyForm tmp = new SankeyForm();
                tmp.source = company.get(i);
                tmp.destination = category.get(j);
                tmp.value = repository.countByCategoryLikeAndCompanyAndDateGreaterThanEqual(category.get(j), company.get(i),beforeTime);

                if(tmp.value != 0) {
                    sankeyFormAndDate.addSankeyitems(tmp);
                }
            }
        }
    }

}
