package com.team.news.WebCrawler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class Crawling_naver{

    static final int MAX_PAGE = 30;
    private String url;
    private Document doc;
    private Elements ele;// = doc.select("div.article");
    private Date today = new Date();
    private WebDriver driver;
    private ChromeOptions options;
    private DateFormat dateFormat;
    NewsRepository newsRepository;

    News news;


    public Crawling_naver(NewsRepository newsRepository) {

        this.newsRepository = newsRepository;

    }

    public void SelenumSetup()
    {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        options = new ChromeOptions();
        options.addArguments("headless");
    }


    public void start() {
        int total_cnt = 0;

        SelenumSetup();
        driver = new ChromeDriver(options);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        String[] category_arr = {"정치","경제","사회","생활/문화","세계","IT/과학"};
        String[] sports_arr = {"kbaseball","wbaseball","kfootball","wfootball","basketball","esports"};
        String[] sports_categroy_arr = {"스포츠-야구","스포츠-해외야구","스포츠-축구","스포츠-해외축구","스포츠-농구","스포츠-e스포츠"};


        try {
            total_cnt += run_entertainment();

            for(int i=0;i<sports_arr.length;i++)
                total_cnt += run_sports(sports_arr[i],sports_categroy_arr[i]);

            for(int i=0;i<category_arr.length;i++)
                total_cnt += run(i+100,category_arr[i]);
        }
        catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("\r\n총 기사 개수 : "+total_cnt);

        Calendar cal = Calendar.getInstance();    // 7시간 이내
        System.out.println(dateFormat.format(cal.getTime()));
        driver.quit();
    }


    public int run(int num, String category) {
        int cnt = 0;
        String tmp_hour;
        //System.out.println("--------"+category+"--------");

        for(int i=1;i<MAX_PAGE;i++) {
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
                    news.setUrl(tmp.attr("href")); // url

                    if(!isExist(news.getTitle(),news.getUrl())) {
                        news.setCategory(category); // category
                        news.setCompany(span.get(0).text()); //company
                        tmp_hour = span.get(span.size()-1).text(); //before hour

                        if(tmp_hour.contains("시간전")) {
                            if(tmp_hour.contains("3시간전")) {
                                //System.out.println("3시간 넘음");
                                return cnt;
                            }
                            try {
                                doc = Jsoup.connect(news.getUrl()).get();
                                ele = doc.select("#articleBodyContents");
                                news.setContent(ele.text());
                                Elements ele2 = doc.select("div .sponsor span");
                                news.setDate(ele2.select(".t11").first().text().replaceAll("-","/"));

//                                System.out.println(news.getTitle());
//                                System.out.println(news.getDate());

                                countReaction(news);
                                newsRepository.save(news);
                                cnt++;
                            } catch (Exception e2) {
                                //System.out.println("content error : "+e2);
                                //System.out.println(news.getUrl());
                            }


                        }

                    }
                }
                //ghostDriver.quit();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                System.out.println("error : "+e1);
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
        //System.out.println("--------entertainment--------");
        for(int i=1;i<MAX_PAGE;++i) {
            try {
                this.url = "https://entertain.naver.com/now?sid=106&date="+date.format(today)+"&page="+i;
                Document doc = Jsoup.connect(this.url).get();
                ele = doc.select(".news_lst li .tit_area");

                for(Element e : ele){
                    news = new News();

                    Element tmp = e.selectFirst("a");
                    Elements span =  e.select("span");

                    news.setTitle(tmp.text()); // title
                    news.setUrl(attach_url.concat(tmp.attr("href"))); // url
                    int index = span.text().indexOf(" ");

                    if(!isExist(news.getTitle(),news.getUrl())) {
                        news.setCategory(category); // category
                        news.setCompany(span.text().substring(0, index)); //company
                        tmp_hour = span.select("em").text(); //before hour

                        if(tmp_hour.contains("시간전")) {
                            if(tmp_hour.contains("3시간전")) {
                               //System.out.println("3시간 넘음");
                                return cnt;
                            }
                            try {
                                driver.get(news.getUrl());
                                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                                JavascriptExecutor executor = (JavascriptExecutor)driver;

                                String time_s = driver.findElement(By.xpath("//span[@class = 'author']/em")).getText();
                                Date old_date = new SimpleDateFormat("yyyy.MM.dd a h:mm").parse(time_s);
                                news.setDate(dateFormat.format(old_date));

//                                System.out.println(news.getDate());
//                                System.out.println(news.getTitle());

                                news.setContent(driver.findElement(By.id("articeBody")).getText());
                                countReaction(news);
                                newsRepository.save(news);
                                cnt++;

                            } catch (Exception e2) {
                                System.out.println("error2 : "+e2);
                            }
                        }

                    }
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                System.out.println("error : "+e1);
                e1.printStackTrace();
            }
        }
//        System.out.println("기사 개수 : "+cnt);
        return cnt;

    }


    public int run_sports(String group, String category) throws java.text.ParseException {
        int cnt = 0;
        //System.out.println("--------"+category+"--------");
        for(int i=1;i<MAX_PAGE;i++) {

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
                                news = new News();
                                JSONObject tempObj = (JSONObject) listarray.get(j);

                                news.setCategory(category);
                                news.setCompany(tempObj.get("officeName").toString());
                                news.setTitle(tempObj.get("title").toString());
                                news.setDate(tempObj.get("datetime").toString().replace(".","/"));
                                news.setUrl(attach_url + "oid=" + tempObj.get("oid") + "&aid=" + tempObj.get("aid"));

                                if (!isExist(news.getTitle(),news.getUrl())) {
                                    if(news.IsInHour(3) && news.IsOutHour(1)) {
                                        try {
                                            driver.get(news.getUrl());
                                            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                                            JavascriptExecutor executor = (JavascriptExecutor)driver;

//                                            System.out.println(news.getTitle());
//                                            System.out.println(news.getDate());

                                            news.setContent(driver.findElement(By.id("newsEndContents")).getText());
                                            countReaction(news);
                                            newsRepository.save(news);
                                            cnt++;
                                        } catch (Exception e2) {
                                            System.out.println("error3 : "+e2);
                                        }

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

    public boolean isExist(String title, String url){

        int count = 0;
        count += newsRepository.countByTitleLike(title);
        count += newsRepository.countByUrlLike(url);

        if(count == 0) {
            return false;
        }
        else
            return true;
    }
    public void countReaction(News news)
    {
        driver.get(news.getUrl());
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        JavascriptExecutor executor = (JavascriptExecutor)driver;

        String like_count = driver.findElement(By.xpath(
                "//a[@class='u_likeit_button _face off']/span[contains(@class,'text')]")).getText();
        String comment_count = driver.findElement(By.xpath(
                "//span[@class='u_cbox_count'] | //em[@class='simplecmt_num']")).getText();
        String recommend_count = driver.findElement(By.cssSelector("em.u_cnt._count")).getText();

        like_count = like_count.replace(",","");
        comment_count = comment_count.replace(",","");
        recommend_count = recommend_count.replace(",","");

        if(like_count.equals("공감") || like_count.equals(""))
            news.setLike_count(0);
        else
            news.setLike_count(Integer.parseInt(like_count));

        if(recommend_count.equals(""))
            news.setRecommend_count(0);
        else
            news.setRecommend_count(Integer.parseInt(recommend_count));

        if(comment_count.equals(""))
            news.setComment_count(0);
        else
            news.setComment_count(Integer.parseInt(comment_count));

//         System.out.println("like : " +news.getLike_count());
//         System.out.println("comment : " +news.getComment_count());
//         System.out.println("recommand : " +news.getRecommend_count());
    }

}
