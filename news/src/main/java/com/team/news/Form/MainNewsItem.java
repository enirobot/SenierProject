package com.team.news.Form;

public class MainNewsItem {

    private String title;	// 제목
    private String company;	// 언론사
    private String date;		// 날짜
    private String url;		// 기사 주소

    public MainNewsItem(String title, String company, String date, String url){
        this.title = title;
        this.company = company;
        this.date = date;
        this.url = url;
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

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
