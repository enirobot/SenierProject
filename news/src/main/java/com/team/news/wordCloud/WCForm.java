package com.team.news.wordCloud;

import java.util.List;

/**
 * 클라이언트에 정보 보낼 때 사용되는 모델
 */
public class WCForm {
    private String word;
    private String counts;
    private List<String> urlList;

    public WCForm(String word, String counts, List<String> urlList)
    {
        this.word = word;
        this.counts = counts;
        this.urlList = urlList;
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

    public void setCount(String counts) {
        this.counts = counts;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }
}
