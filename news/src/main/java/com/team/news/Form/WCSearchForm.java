package com.team.news.Form;

import java.util.List;

public class WCSearchForm {
    private String word;
    private String counts;
    private List<String> idList;

    public WCSearchForm(String word, String counts, List<String> idList) {
        this.word = word;
        this.counts = counts;
        this.idList = idList;
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

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
}
