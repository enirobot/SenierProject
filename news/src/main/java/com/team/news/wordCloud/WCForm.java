package com.team.news.wordCloud;

/**
 * 클라이언트에 정보 보낼 때 사용되는 모델
 */
public class WCForm {
    private String word;
    private String counts;

    public WCForm(String word, String counts)
    {
        this.word = word;
        this.counts = counts;
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
}
