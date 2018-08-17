package com.team.news.Form;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class MainNewsList {

    @Id
    private String id;

    private String word;
    private String counts;
    private String date;
    private List<MainNewsItem> newsItems;

    public MainNewsList(){}

    public MainNewsList(String word, String counts, String date, List<MainNewsItem> mainNewsItems) {
        this.word = word;
        this.counts = counts;
        this.date = date;
        this.newsItems = mainNewsItems;
    }

    public void add(MainNewsItem mainNewsItem) {
        newsItems.add(mainNewsItem);
    }

    public String getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<MainNewsItem> getNewsItems() {
        return newsItems;
    }

    public void setNewsItems(List<MainNewsItem> newsItems) {
        this.newsItems = newsItems;
    }
}
