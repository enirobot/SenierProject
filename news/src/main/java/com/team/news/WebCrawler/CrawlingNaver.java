package com.team.news.WebCrawler;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.team.news.Form.News;

import com.team.news.Repository.NewsRepository;
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

    static final int MAX_PAGE = 20;
    private String url;
    private Document doc;
    private Elements ele;// = doc.select("div.article");
    private LocalDateTime today = LocalDateTime.now();
    private WebDriver driver;
    private WebDriver driver2;
    private ChromeOptions options;
    private String crawling_date;
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

        crawling_date = dateFormat.format(today).toString();

        SelenumSetup();
        driver = new ChromeDriver(options);
        driver2 = new ChromeDriver(options);

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

        logger.info("\"총 기사 개수 : \"" + total_cnt);
        driver.quit();
        driver2.quit();
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
                        try {
                            news = new News(crawling_date);
                            Element tmp = e.selectFirst("a");
                            Elements span = e.select("span");

                            news.setTitle(tmp.text()); // title
                            news.setUrl(tmp.attr("href")); // url
                            news.setCategory(category); // category
                            news.setCompany(span.get(0).text()); //company
                            tmp_hour = span.get(span.size() - 1).text(); //before hour

                            if (tmp_hour.contains("3시간전")||tmp_hour.contains("4시간전")) {
                                logger.info("\"기사 개수 : \"" + cnt);
                                return cnt;
                            }

                            if (!isExist(news.getTitle(), news.getUrl())) {
                                if (tmp_hour.contains("시간전")) {
                                    driver2.get(news.getUrl());
                                    driver2.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

                                    String contents = driver2.findElement(By.xpath("//div[@id='articleBodyContents']")).getText();
                                    String date = driver2.findElement(By.xpath("//span[@class='t11']")).getText();
                                    date = date.replace("-", "/");

                                    news.setContent(contents);
                                    news.setDate(date);
                                    countReaction(driver2, news, 0);
                                    newsRepository.save(news);
                                    cnt++;
                                }

                            }
                            else
                            {
                                logger.info("\"기사 개수 : \"" + cnt);
                                return cnt;
                            }
                        }
                        catch (Exception e1) {
                            logger.info("url : " + news.getUrl());
                            logger.info("news_error : " + e1);
                            e1.printStackTrace();
                        }
                    }
                }
            } catch (IOException e1) {

            }

        }
        logger.info("\"기사 개수 : \"" + cnt);
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
                        news = new News(crawling_date);

                        Element tmp = e.selectFirst("a");
                        Elements span = e.select("span");

                        news.setTitle(tmp.text()); // title
                        news.setUrl(attach_url.concat(tmp.attr("href"))); // url
                        news.setCategory(category); // category

                        tmp_hour = span.select("em").text(); //before hour
                        int index = span.text().indexOf(" ");
                        news.setCompany(span.text().substring(0, index)); //company

                        if (tmp_hour.contains("3시간전") || tmp_hour.contains("4시간전")) {
                            logger.info("\"기사 개수 : \"" + cnt);
                            return cnt;
                        }

                        if (!isExist(news.getTitle(), news.getUrl())) {
                            if (tmp_hour.contains("시간전")) {
                                try {
                                    driver2.get(news.getUrl());
                                    driver2.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

                                    String time_s = driver2.findElement(By.xpath("//span[@class = 'author']/em")).getText();
                                    LocalDateTime old_date = LocalDateTime.parse(time_s, DateTimeFormatter.ofPattern("yyyy.MM.dd a h:mm"));
                                    news.setDate(dateFormat.format(old_date));

                                    news.setContent(driver2.findElement(By.id("articeBody")).getText());
                                    countReaction(driver2, news, 1);
                                    newsRepository.save(news);
                                    cnt++;

                                } catch (Exception e2) {
                                    logger.info("url : " + news.getUrl());
                                    logger.debug("entertainment_error : " + e2);
                                }
                            }
                        }
                        else
                        {
                            logger.info("\"기사 개수 : \"" + cnt);
                            return cnt;
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
        WebElement tmp_e = null;
        logger.info("--------"+category+"--------");

        for(int i=1;i<MAX_PAGE;i++) {

            url = "https://sports.news.naver.com/"+group+"/news/index.nhn?isphoto=N&page="+i;
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            JavascriptExecutor executor = (JavascriptExecutor) driver;

            By mySelector = By.xpath("//div[@class='content_area']/div[@class='news_list']/ul/li");
            List<WebElement> ele_list = driver.findElements(mySelector);

            tmp_e = driver.findElement(mySelector);


            for(int j=1;j<=ele_list.size();j++) {
                try {
                    news = new News(crawling_date);

                    String title = null;
                    String company = null;
                    String url = null;
                    String date = null;

                    WebElement ele = driver.findElement(By.xpath("//div[@class='content_area']/div[@class='news_list']/ul/li[" + j + "]"));
                    title = ele.findElement(By.cssSelector("a.title")).getText();
                    company = ele.findElement(By.cssSelector("span.press")).getText();
                    url = ele.findElement(By.cssSelector("a.title")).getAttribute("href");
                    date = ele.findElement(By.cssSelector("span.time")).getText();
                    date = date.replace(".", "/");

                    news.setTitle(title);
                    news.setCompany(company);
                    news.setDate(date);
                    news.setUrl(url);
                    news.setCategory(category);

                    if (news.IsInHour(3)) {
                        if (!news.IsInHour(1)) {
                            if(!isExist(news.getTitle(), news.getUrl())){
                                driver2.get(news.getUrl());
                                driver2.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

                                String contents = driver2.findElement(By.xpath("//div[@id='newsEndContents']")).getText();
                                news.setContent(contents);
                                countReaction(driver2, news, 1);
                                newsRepository.save(news);
                                cnt++;
                            }
                            else
                            {
                                logger.info("\"기사 개수 : \"" + cnt);
                                return cnt;
                            }

                        }
                    } else {
                        logger.info("\"기사 개수 : \"" + cnt);
                        return cnt;
                    }
                } catch(Exception e3) {
                    logger.info("url : " + news.getUrl());
                    logger.info("sports_error : " + e3);
                    e3.printStackTrace();
                }
            }

        }
        logger.info("\"기사 개수 : \"" + cnt);
        return cnt;
    }

    public boolean isExist(String title, String url){

        url = url.replace("?", "=.q_m");

        int count1 = 0;
        int count2 = 0;
        count1 = newsRepository.countByTitle(title);
        count2 = newsRepository.countByUrl(url);

        if(count1 > 0 && count2 > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public void countReaction(WebDriver driver, News news, int flag)
    {
        //flag : 1, entertainment
        //flag : 0, sprots, run

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

        int emotion_weight = news.getReaction_list(0) + news.getReaction_list(1)
                - news.getReaction_list(2) - news.getReaction_list(3);

    }
}
