package com.team.news.wordCloud;

import java.util.List;

/**
 * 형태소 분석 + 단어 출현 횟수 계산 중간 단계에 사용
 */

public class WCNode {
    private int counts;
    private List<String> urlList;

    public WCNode(int counts, List<String> urlList) {
        this.counts = counts;
        this.urlList = urlList;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public void add(String id) {
        urlList.add(id);
    }
}
