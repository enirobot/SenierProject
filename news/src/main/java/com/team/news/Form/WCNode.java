package com.team.news.Form;

import java.util.ArrayList;
import java.util.List;

/**
 * 형태소 분석 + 단어 출현 횟수 계산 중간 단계에 사용
 */

public class WCNode {
    private int counts;
    private double totalWeight;
    private int totalEmtionWeight;
    private int totalComment;
    private List<MainNewsItem> mainNewsItems;

    public WCNode(int counts, double totalWeight,int totalComment) {
        this.counts = counts;
        this.totalWeight = totalWeight;
        this.totalComment=totalComment;
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

    public void sumComment(int totalComment) {
        this.totalComment = this.totalComment +totalComment;
    }

    public void sumTotalEmotionWeight(int totalEmtionWeight) {this.totalEmtionWeight = this.totalEmtionWeight + totalEmtionWeight; }

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
        return totalEmtionWeight;
    }

    public void setTotalEmtionWeight(int totalEmtionWeight) {
        this.totalEmtionWeight = totalEmtionWeight;
    }

    public int getTotalCommentCount() {
        return totalComment;
    }
}
