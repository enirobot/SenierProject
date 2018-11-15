package com.team.news.WebCrawler;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CrawlingNaver {

    static final int MAX_PAGE = 30;
    private String url;
    private Document doc;
    private Elements ele;// = doc.select("div.article");
    private LocalDateTime today = LocalDateTime.now();
    private WebDriver driver;
    private ChromeOptions options;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    private NewsRepository newsRepository;

    private News news;

    private Logger logger = LoggerFactory.getLogger(CrawlingNaver.class);


    public CrawlingNaver(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }


    // ChromeDriver 설정 메소드
    public void SelenumSetup() {
        String os = System.getProperty("os.name");

        if (os.equals("Windows 10")) {
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        }

        if (os.equals("Mac OS X")) {
            System.setProperty("webdriver.chrome.driver", "chromedriver");
        }

        options = new ChromeOptions();
        options.addArguments("headless");
    }


    public void start() {
        int total_cnt = 0;

        SelenumSetup();
        driver = new ChromeDriver(options);

        String[] category_arr = {"정치","경제","사회","생활/문화","세계","IT/과학"};
        String[] sports_arr = {"kbaseball","wbaseball","kfootball","wfootball","basketball","esports"};
        String[] sports_categroy_arr = {"스포츠-야구","스포츠-해외야구","스포츠-축구","스포츠-해외축구","스포츠-농구","스포츠-e스포츠"};


        total_cnt += run_entertainment();

        for(int i=0;i<category_arr.length;i++)
            total_cnt += run(i+100,category_arr[i]);

//        try {
//           total_cnt += run_entertainment();
//
//            for(int i=0;i<sports_arr.length;i++)
//                total_cnt += run_sports(sports_arr[i],sports_categroy_arr[i]);
//
//            for(int i=0;i<category_arr.length;i++)
//                total_cnt += run(i+100,category_arr[i]);
//        }
//        catch (java.text.ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        logger.info("\r\n총 기사 개수 : " + total_cnt);

        driver.quit();
    }


    public int run(int num, String category) {
        int cnt = 0;
        String tmp_hour;

        logger.info("--------" + category + "--------");

        for(int i=1;i<MAX_PAGE;i++) {
            try {

                url = "http://news.naver.com/main/list.nhn?mode=LSD&mid=sec&listType=title&sid1="+num+"&page="+i;
                doc = Jsoup.connect(url).get();
                ele = doc.select(".type02 li");

                if(ele.isEmpty()) break;

                Elements last_element = ele.last().select("span");
                tmp_hour = last_element.get(last_element.size()-1).text();

                if(tmp_hour.contains("시간전")) {
                    for (Element e : ele) {


                        news = new News();
                        Element tmp = e.selectFirst("a");
                        Elements span = e.select("span");

                        news.setTitle(tmp.text()); // title
                        news.setUrl(tmp.attr("href")); // url

                        if (!isExist(news.getTitle(), news.getUrl())) {
                            news.setCategory(category); // category
                            news.setCompany(span.get(0).text()); //company
                            tmp_hour = span.get(span.size() - 1).text(); //before hour

                            if (tmp_hour.contains("3시간전")) {
                                logger.info("\"기사 개수 : \"" + cnt);
                                return cnt;
                            }
                            if (tmp_hour.contains("시간전")) {
                                try {
                                    doc = Jsoup.connect(news.getUrl()).get();
                                    ele = doc.select("#articleBodyContents");
                                    news.setContent(ele.text());
                                    Elements ele2 = doc.select("div .sponsor span");
                                    news.setDate(ele2.select(".t11").first().text().replaceAll("-", "/"));

                                    countReaction(news, 0);
                                    newsRepository.save(news);
                                    cnt++;
                                } catch (Exception e2) {
                                    logger.debug("news_error : " + e2);
                                }
                            }

                        }
                    }
                }
                //ghostDriver.quit();
            } catch (IOException e1) {
                logger.info("error : " + e1);
                e1.printStackTrace();

            }

        }
        return cnt;
    }

    public int run_entertainment()
    {
        String category = "연예";
        int cnt = 0;
        String tmp_hour;
        String attach_url = "https://entertain.naver.com";
        DateTimeFormatter urlDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        logger.info("--------entertainment--------");

        for(int i=1;i<MAX_PAGE;++i) {
            try {
                this.url = "https://entertain.naver.com/now?sid=106&date="+urlDateFormat.format(today)+"&page="+i;
                Document doc = Jsoup.connect(this.url).get();
                ele = doc.select(".news_lst li .tit_area");

                if(ele.isEmpty()) break;

                Elements last_element = ele.last().select("span");
                tmp_hour = last_element.select("em").text();

                if(tmp_hour.contains("시간전")) {
                    for (Element e : ele) {
                        news = new News();

                        Element tmp = e.selectFirst("a");
                        Elements span = e.select("span");

                        news.setTitle(tmp.text()); // title
                        news.setUrl(attach_url.concat(tmp.attr("href"))); // url
                        int index = span.text().indexOf(" ");

                        if (!isExist(news.getTitle(), news.getUrl())) {
                            news.setCategory(category); // category
                            news.setCompany(span.text().substring(0, index)); //company
                            tmp_hour = span.select("em").text(); //before hour

                            if (tmp_hour.contains("3시간전")) {
                                logger.info("\"기사 개수 : \"" + cnt);
                                return cnt;
                            }

                            if (tmp_hour.contains("시간전")) {
                                try {
                                    driver.get(news.getUrl());
                                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                                    JavascriptExecutor executor = (JavascriptExecutor) driver;

                                    String time_s = driver.findElement(By.xpath("//span[@class = 'author']/em")).getText();
                                    LocalDateTime old_date = LocalDateTime.parse(time_s, DateTimeFormatter.ofPattern("yyyy.MM.dd a h:mm"));
//                                Date old_date = new SimpleDateFormat("yyyy.MM.dd a h:mm").parse(time_s);
//                                news.setDate(dateFormat.format(old_date));
                                    news.setDate(dateFormat.format(old_date));

                                    news.setContent(driver.findElement(By.id("articeBody")).getText());
                                    countReaction(news, 1);
                                    newsRepository.save(news);
                                    cnt++;

                                } catch (Exception e2) {
                                    logger.debug("entertainment_error : " + e2);
                                }
                            }

                        }
                    }
                }
            } catch (IOException e1) {
                logger.info("error : " + e1);
                e1.printStackTrace();
            }
        }
        logger.info("\"기사 개수 : \"" + cnt);
        return cnt;

    }


    public int run_sports(String group, String category) throws java.text.ParseException {
        int cnt = 0;

        logger.info("--------"+category+"--------");

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

                                if(news.IsOutHour(3)) {
                                    logger.info("\"기사 개수 : \"" + cnt);
                                    return cnt;
                                }

                                if (!isExist(news.getTitle(),news.getUrl())) {
                                    if(news.IsInHour(3) && news.IsOutHour(1)) {
                                        try {
                                            driver.get(news.getUrl());
                                            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                                            JavascriptExecutor executor = (JavascriptExecutor)driver;

//                                            System.out.println(news.getTitle());
//                                            System.out.println(news.getDate());

                                            news.setContent(driver.findElement(By.id("newsEndContents")).getText());
                                            countReaction(news,0);
                                            newsRepository.save(news);
                                            cnt++;
                                        } catch (Exception e2) {
                                            logger.debug(category + "sports_error : " + e2);
                                        }

                                    }

                                }
                            }

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        logger.info("\"기사 개수 : \"" + cnt);
        return cnt;
    }

    public boolean isExist(String title, String url){

        int count = 0;
        count += newsRepository.countByTitle(title);
        count += newsRepository.countByUrl(url);

        if(count == 0) {
            return false;
        }
        else
            return true;
    }

    public void countReaction(News news, int flag)
    {
        //flag : 1, entertainment
        //flag : 0, sprots, run

        driver.get(news.getUrl());
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        JavascriptExecutor executor = (JavascriptExecutor)driver;

        String like_str;
        String comment_str;
        String recommend_str;

        int like_count = 0;
        int comment_count = 0;
        int recommend_count = 0;


        if(flag == 0) {
            like_str = driver.findElement(By.xpath(
                    "//span[contains(@class,'u_likeit_text _count')]")).getText();
            comment_str = driver.findElement(By.xpath(
                    "//span[@class='lo_txt']")).getText();
        }
        else {
            like_str = driver.findElement(By.xpath(
                    "//a[@class='u_likeit_button _face off']/span[contains(@class,'text')]")).getText();
            comment_str = driver.findElement(By.xpath(
                    "//span[@class='u_cbox_count']")).getText();
        }

//        comment_str = driver.findElement(By.xpath(
//                //"//span[@class='u_cbox_count'] | //em[@class='simplecmt_num']")).getText();
//                "//span[@class='lo_txt']")).getText();

        recommend_str = driver.findElement(By.cssSelector("em.u_cnt._count")).getText();

        like_str = like_str.replace(",","");
        comment_str = comment_str.replace(",","");
        recommend_str = recommend_str.replace(",","");

        if(like_str.equals("공감") || like_str.equals(""))
            news.setLike_count(0);
        else {
            like_count = Integer.parseInt(like_str);
            news.setLike_count(like_count);
        }

        if(comment_str.equals("댓글") || comment_str.equals(""))
            news.setComment_count(0);
        else {
            comment_count = Integer.parseInt(comment_str);
            news.setComment_count(Integer.parseInt(comment_str));
        }

        if(recommend_str.equals(""))
            news.setRecommend_count(0);
        else {
            recommend_count = Integer.parseInt(recommend_str);
            news.setRecommend_count(Integer.parseInt(recommend_str));
        }

        LocalDateTime newsTime = LocalDateTime.parse(news.getDate(), dateFormat);

        Duration duration = Duration.between(newsTime, today);  //두 날짜 차이

        double weight = (like_count + recommend_count + comment_count) / Math.sqrt(duration.getSeconds());
        news.setWeight(weight);


        String tmp;
        WebElement element;
        for(int i=1;i<=5;i++){
            tmp  =  driver.findElement(By.xpath(
                    "//div[contains(@class,'end_btn')]/div[@class='_reactionModule u_likeit']/ul/li["+i+"]")).getText();
            int index = tmp.indexOf('\n');
            tmp = tmp.substring(index+1);
            tmp = tmp.replace(",","");
            news.setReaction_list(i-1,Integer.parseInt(tmp));

        }

    }
}
