package com.team.news.Form;

import java.util.List;

/**
 * 클라이언트에 정보 보낼 때 사용되는 모델
 */
public class WCForm {
    private String word;
    private String counts;
    private List<String> idList;

    public WCForm(String word, String counts, List<String> idList)
    {
        this.word = word;
        this.counts = counts;
        this.idList = idList;
    }

    public WCForm() {}

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getCounts() {
        return counts;
    }

    public void setCount(String counts) {
        this.counts = counts;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
}
