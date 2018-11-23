package com.team.news.Form;

import java.util.ArrayList;
import java.util.List;

/**
 * 형태소 분석 + 단어 출현 횟수 계산 중간 단계에 사용
 */

public class WCNode {
    private int counts;
    private double totalWeight;
    private int totalEmotionWeight;
    private List<MainNewsItem> mainNewsItems;

    public WCNode(int counts, double totalWeight, int totalEmotionWeight) {
        this.counts = counts;
        this.totalWeight = totalWeight;
        this.totalEmotionWeight = totalEmotionWeight;
        this.mainNewsItems = new ArrayList<MainNewsItem>();
    }

    public WCNode(int counts, MainNewsItem mainNewsItem) {
        this.counts = counts;
        this.mainNewsItems = new ArrayList<MainNewsItem>();
        this.mainNewsItems.add(mainNewsItem);
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void sumCounts(int counts) { this.counts = this.counts + counts; }

    public void sumTotalWeight(double totalWeight) {
        this.totalWeight = this.totalWeight + totalWeight;
    }

    public void sumTotalEmotionWeight(int totalEmtionWeight) {this.totalEmotionWeight = this.totalEmotionWeight + totalEmtionWeight; }

    public List<MainNewsItem> getMainNewsItems() {
        return mainNewsItems;
    }

    public void setMainNewsItems(List<MainNewsItem> mainNewsItems) {
        this.mainNewsItems = mainNewsItems;
    }

    public void add(MainNewsItem mainNewsItem) {
        mainNewsItems.add(mainNewsItem);
    }

    public int getTotalEmtionWeight() {
        return totalEmotionWeight;
    }

    public void setTotalEmtionWeight(int totalEmtionWeight) {
        this.totalEmotionWeight = totalEmtionWeight;
    }
}
