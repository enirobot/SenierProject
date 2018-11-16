package com.team.news.Form;

import com.mongodb.annotations.ThreadSafe;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 * 정제되지 않은 뉴스 데이터를 나타내는 모델
 */
@ThreadSafe
public class News {

    @Id
    private String id;		// DB document id

    private String title;	// 제목
    private String company;	// 언론사
    private String date;    // 날짜 (yyyy/MM/dd HH:mm)
    private String crawling_date; // 크롤링 시간
    private String category;// 카테고리
    private String url;		// 기사 주소
    private String content;	// 내용
    private int like_count; // 좋아요 수 총합
    private int[] reaction_list; // 좋아요, 훈훈해요, 슬퍼요, 화나요, 후속기사 원해요 순
    private int comment_count;
    private int recommend_count;
    private double weight;

    public News(String title, String company, String date,
                String category, String url, String content) {

        this.title = title;
        this.company = company;
        this.date = date;
        this.category = category;
        this.url = url;
        this.content = content;
    }

    public News(String date) {
        this.crawling_date = date;
        reaction_list = new int[5];
        this.title = null;
        this.company = null;
        this.date = null;
        this.category = null;
        this.url = null;
        this.content = null;
    }

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

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUrl(String url) {
        this.url = url.replace("?", "=.q_m");
    }

    public String getUrl() {
        String temp;
        temp = url.replace("=.q_m", "?");
        return temp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getRecommend_count() {
        return recommend_count;
    }

    public void setRecommend_count(int recommend_count) {
        this.recommend_count = recommend_count;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setReaction_list(int index,int num) { this.reaction_list[index] = num; }

    public int getReaction_list(int index) { return reaction_list[index];}

    public boolean IsInHour(int hour) throws ParseException
    {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm"); //set Time format

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.add(Calendar.HOUR, -hour);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(dateFormat.parse(this.date));

        if(cal1.before(cal2))
            return true;
        else
            return false;
    }

    public boolean IsOutHour(int hour) throws ParseException
    {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm"); //set Time format

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.add(Calendar.HOUR, -hour);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(dateFormat.parse(this.date));

        if(cal1.after(cal2))
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "News [category=" + category  + ", date=" + date + ", title=" + title + ", url=" + getUrl() + "]";
    }

}
