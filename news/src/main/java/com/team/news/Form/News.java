package com.team.news.Form;

import org.springframework.data.annotation.Id;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 * 정제되지 않은 뉴스 데이터를 나타내는 모델
 */
public class News {

    @Id
    private String id;		// DB document id

    private String title;	// 제목
    private String company;	// 언론사
    private String date;		// 날짜
    private String category;	// 카테고리
    private String url;		// 기사 주소
    private String content;	// 내용

    public News(String title, String company, String date,
                String category, String url, String content) {

        this.title = title;
        this.company = company;
        this.date = date;
        this.category = category;
        this.url = url;
        this.content = content;
    }

    public News() {}

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String s)
    {
        s = s.replace("분전", "");

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm"); //set Time format

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
//        System.out.println(s);
        cal.add(Calendar.MINUTE, -Integer.parseInt(s));
        this.date = dateFormat.format(cal.getTime());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUrl(String url)
    {
        this.url = url.replace("?", "=.q_m");
    }

    public String getUrl()
    {
        String temp;
        temp = url.replaceAll("=.q_m", "?");
        return temp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean IsInOnehour() throws ParseException
    {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm"); //set Time format

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.add(Calendar.HOUR, -1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(dateFormat.parse(this.date));

        if(cal1.before(cal2))
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "News [category=" + category  + ", date=" + date + ", title=" + title + ", url=" + getUrl() + "]";
    }
}
