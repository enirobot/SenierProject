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
    private int counts;
    private String date;
    private double totalWeight;
    private int totalCount;
    private List<MainNewsItem> newsItems;

    public MainNewsList(){}

    public MainNewsList(String word, int counts, String date, double totalWeight, int totalCount ,List<MainNewsItem> mainNewsItems) {
        this.word = word;
        this.counts = counts;
        this.date = date;
        this.totalWeight = totalWeight;
        this.totalCount=totalCount;
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

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<MainNewsItem> getNewsItems() {
        return newsItems;
    }

    public void setNewsItems(List<MainNewsItem> newsItems) {
        this.newsItems = newsItems;
    }
}
