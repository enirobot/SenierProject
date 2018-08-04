package com.team.news.WordCloud;

import java.util.List;

/**
 * 형태소 분석 + 단어 출현 횟수 계산 중간 단계에 사용
 */

public class WCNode {
    private int counts;
    private List<String> idList;

    public WCNode(int counts, List<String> idList) {
        this.counts = counts;
        this.idList = idList;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public void add(String id) {
        idList.add(id);
    }
}
