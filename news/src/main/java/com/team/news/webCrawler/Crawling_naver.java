package com.team.news.webCrawler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.team.news.Form.News;

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


    BasicDBObject document;
    DBCollection collection;
    News news;


    public Crawling_naver() {

        String MongoDB_IP = " 45.119.145.73";
        int MongoDB_PORT = 25000;
        String DB_NAME = "project";

        //Connect to MongoDB
        @SuppressWarnings("resource")
        MongoClient  mongoClient = new MongoClient(new ServerAddress(MongoDB_IP, MongoDB_PORT));
        @SuppressWarnings("deprecation")
        DB db = mongoClient.getDB(DB_NAME);
        collection = db.getCollection("news");


    }


    public int run(int num, String category) {
        int cnt = 0;
        String tmp_hour;
        System.out.println("--------"+category+"--------");
        for(int i=1;i<30;i++) {
            System.out.println("page : "+i);

            try {

                url = "http://news.naver.com/main/list.nhn?mode=LSD&mid=sec&listType=title&sid1="+num+"&page="+i;
                doc = Jsoup.connect(url).get();
                ele = doc.select(".type02 li");

                //System.out.println(ele);

                for(Element e : ele){

                    news = new News();
                    Element tmp = e.selectFirst("a");
                    Elements span =  e.select("span");

                    news.setUrl(tmp.attr("href")); // url
                    if(!isExist("url",news.getUrl())) {

                        news.setCategory(category); // category
                        news.setTitle(tmp.text()); // title
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
                            System.out.println("content error!");
                        }
                        finally {
                            if(!tmp_hour.contains("분전")) {
                                System.out.println("기사 개수 : "+cnt);
                                return cnt;
                            }
                            else {
                                news.setDate(tmp_hour);
                            }

                            insertDB(news);
                            cnt++;
                        }
                    }
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        System.out.println("기사 개수 : "+cnt);
        return cnt;
    }

    public int run_entertainment()
    {
        String category = "연예";
        int cnt = 0;
        String tmp_hour;
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String attach_url = "https://entertain.naver.com";
        System.out.println("--------"+category+"--------");
        for(int i=1;i<30;++i) {

            System.out.println("page : "+i);

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

                    news.setUrl(attach_url.concat(tmp.attr("href"))); // url

                    if(!isExist("url",news.getUrl())) {

                        news.setCategory(category); // category
                        news.setTitle(tmp.text()); // title

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
                            System.out.println("content error!");
                        }
                        finally {
                            if(!tmp_hour.contains("분전")) {
                                System.out.println("기사 개수 : "+cnt);
                                return cnt;
                            }
                            else {
                                news.setDate(tmp_hour);
                            }

                            insertDB(news);
                            cnt++;
                        }
                    }
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        System.out.println("기사 개수 : "+cnt);
        return cnt;

    }


    public int run_sports(String group, String category) throws java.text.ParseException {
        int cnt = 0;
        System.out.println("--------"+category+"--------");
        for(int i=1;i<2;i++) {

            System.out.println("page : "+i);

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
                                news.setDate(tempObj.get("datetime").toString().replace(".", "/"));
                                news.setUrl(attach_url + "oid=" + tempObj.get("oid") + "&aid=" + tempObj.get("aid"));

                                if (!isExist("url", news.getUrl())) {

                                    try {
                                        doc = Jsoup.connect(news.getUrl()).get();
                                        ele = doc.select(".news_end");
                                        news.setContent(ele.text());
                                    } catch (Exception e2) {
                                        System.out.println("content error!");
                                    } finally {
                                        //시간체크
                                        if (!news.IsInOnehour()) {
                                            System.out.println("기사 개수 : " + cnt);
                                            return cnt;
                                        }

                                        insertDB(news);
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
        System.out.println("기사 개수 : "+cnt);
        return cnt;
    }

    public boolean isExist(String where, String is){
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put(where, is);
        DBCursor cursor = collection.find(whereQuery);

        if(cursor.count() == 0)
            return false;
        else
            return true;
    }

    public void insertDB(News news)
    {
        document = new BasicDBObject();

        document.put("title", news.getTitle());
        document.put("company", news.getCompany());
        document.put("date", news.getDate());
        document.put("category", news.getCategory());
        document.put("url", news.getUrl());
        document.put("content", news.getContent());

        collection.insert(document);
    }
}
