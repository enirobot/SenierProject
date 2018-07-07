package com.team.news.Form;

public class MainNewsList {
    private String title;
    private String url;

    public MainNewsList(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public MainNewsList() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
