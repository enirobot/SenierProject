package com.team.news.WebCrawler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.team.news.Form.News;

import com.team.news.Repository.NewsRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class Crawling_naver{

    private String url;
    private Document doc;
    private Elements ele;// = doc.select("div.article");
    private Date today = new Date();


    NewsRepository newsRepository;

    News news;


    public Crawling_naver(NewsRepository newsRepository) {

        this.newsRepository = newsRepository;

    }

    public void start() {
        int total_cnt = 0;

        try {
            total_cnt += run(100, "정치");
            total_cnt += run(101, "경제");
            total_cnt += run(102, "사회");
            total_cnt += run(103, "생활/문화");
            total_cnt += run(104, "세계");
            total_cnt += run(105, "IT/과학");
            total_cnt += run_entertainment();
            total_cnt += run_sports("kbaseball", "스포츠-야구");
            total_cnt += run_sports("wbaseball", "스포츠-해외야구");
            total_cnt += run_sports("kfootball", "스포츠-축구");
            total_cnt += run_sports("wfootball", "스포츠-해외축구");
            total_cnt += run_sports("basketball", "스포츠-농구");
            total_cnt += run_sports("esports", "스포츠-e스포츠");
        }
        catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("\r\n총 기사 개수 : "+total_cnt);

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();    // 7시간 이내
        System.out.println(date.format(cal.getTime()));
    }


    public int run(int num, String category) {
        int cnt = 0;
        String tmp_hour;
//        System.out.println("--------"+category+"--------");
        for(int i=1;i<30;i++) {
//            System.out.println("page : "+i);

            try {

                url = "http://news.naver.com/main/list.nhn?mode=LSD&mid=sec&listType=title&sid1="+num+"&page="+i;
                doc = Jsoup.connect(url).get();
                ele = doc.select(".type02 li");

                //System.out.println(ele);

                for(Element e : ele){

                    news = new News();
                    Element tmp = e.selectFirst("a");
                    Elements span =  e.select("span");

                    news.setTitle(tmp.text()); // title

                    if(!isExist(news.getTitle())) {
                        news.setCategory(category); // category
                        news.setUrl(tmp.attr("href")); // url
                        news.setCompany(span.get(0).text()); //company
                        tmp_hour = span.get(span.size()-1).text(); //before hour

                        //content
                        try {
                            doc = Jsoup.connect(news.getUrl()).get();
                            ele = doc.select("#articleBodyContents");
                            news.setContent(ele.text());
                            //System.out.println(ele.text());
                        }
                        catch(Exception e2) {
//                            System.out.println("content error!");
                        }
                        finally {
                            if(!tmp_hour.contains("분전")) {
//                                System.out.println("기사 개수 : "+cnt);
                                return cnt;
                            }
                            else {
                                news.setDate(tmp_hour);
                            }

                            newsRepository.save(news);
                            cnt++;
                        }
                    }
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
//        System.out.println("기사 개수 : "+cnt);
        return cnt;
    }

    public int run_entertainment()
    {
        String category = "연예";
        int cnt = 0;
        String tmp_hour;
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String attach_url = "https://entertain.naver.com";
//        System.out.println("--------"+category+"--------");
        for(int i=1;i<30;++i) {

//            System.out.println("page : "+i);

            try {
                this.url = "https://entertain.naver.com/now?sid=106&date="+date.format(today)+"&page="+i;
                Document doc = Jsoup.connect(this.url).header("Accept-Encoding", "gzip, deflate")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(0)
                        .get();
                ele = doc.select(".news_lst li .tit_area");

                for(Element e : ele){
                    news = new News();

                    Element tmp = e.selectFirst("a");
                    Elements span =  e.select("span");

                    news.setTitle(tmp.text()); // title
                    if(!isExist(news.getTitle())) {

                        news.setCategory(category); // category
                        news.setUrl(attach_url.concat(tmp.attr("href"))); // url
                        int index = span.text().indexOf(" ");

                        news.setCompany(span.text().substring(0, index)); //company
                        tmp_hour = span.select("em").text(); //before hour
                        //System.out.println(tmp_hour);
                        try {
                            doc = Jsoup.connect(news.getUrl()).get();
                            ele = doc.select(".article_Body");
                            news.setContent(ele.text());
                        }
                        catch(Exception e2) {
//                            System.out.println("content error!");
                        }
                        finally {
                            if(!tmp_hour.contains("분전")) {
//                                System.out.println("기사 개수 : "+cnt);
                                return cnt;
                            }
                            else {
                                news.setDate(tmp_hour);
                            }

                            newsRepository.save(news);
                            cnt++;
                        }
                    }
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
//        System.out.println("기사 개수 : "+cnt);
        return cnt;

    }


    public int run_sports(String group, String category) throws java.text.ParseException {
        int cnt = 0;
//        System.out.println("--------"+category+"--------");
        for(int i=1;i<2;i++) {

//            System.out.println("page : "+i);

            try {

                url = "https://sports.news.naver.com/"+group+"/news/index.nhn?isphoto=N&page="+i;
                doc = Jsoup.connect(url).get();
                ele = doc.select("script");
                Elements eles = ele.attr("type", "text/javascript");
                String attach_url = "https://sports.news.naver.com/kbaseball/news/read.nhn?";

                for(Element e : eles)
                    if (e.data().contains("NewsListPage")) {
                        String tmp = e.data();
                        String split[];
                        split = tmp.split("\\r?\\n");
                        String jsonStr = split[7].substring(17);
                        try {
                            JSONParser jsonParser = new JSONParser();
                            JSONObject jsonObj = (JSONObject) jsonParser.parse(jsonStr);
                            JSONArray listarray = (JSONArray) jsonObj.get("list");
                            for (int j = 0; j < listarray.size(); j++) {
                                News news = new News();
                                JSONObject tempObj = (JSONObject) listarray.get(j);

                                news.setCategory(category);
                                news.setCompany(tempObj.get("officeName").toString());
                                news.setTitle(tempObj.get("title").toString());
                                news.setDate_2(tempObj.get("datetime").toString().replace(".", "/"));
                                news.setUrl(attach_url + "oid=" + tempObj.get("oid") + "&aid=" + tempObj.get("aid"));

                                if (!isExist(news.getTitle())) {

                                    try {
                                        doc = Jsoup.connect(news.getUrl()).get();
                                        ele = doc.select(".news_end");
                                        news.setContent(ele.text());
                                    } catch (Exception e2) {
//                                        System.out.println("content error!");
                                    } finally {
                                        //시간체크
                                        if (!news.IsInOnehour()) {
//                                            System.out.println("기사 개수 : " + cnt);
                                            return cnt;
                                        }

                                        newsRepository.save(news);
                                        cnt++;
                                    }
                                }
                            }

                        } catch (ParseException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
//        System.out.println("기사 개수 : " + cnt);
        return cnt;
    }

    public boolean isExist(String title){

        int count = 0;
        count = newsRepository.countByTitleLike(title);

        if(count == 0) {
            return false;
        }
        else
            return true;
    }

}
