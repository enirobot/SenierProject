package com.team.news.Form;

import java.util.List;

/**
 * 클라이언트에서 워드클라우드 다이어그램을 그릴 때 필요한 정보 보낼 때 사용되는 모델
 */
public class WCForm {
    private String word;
    private int counts;
    private String id;

    public WCForm(String word, int counts, String id)
    {
        this.word = word;
        this.counts = counts;
        this.id = id;
    }

    public WCForm() {}

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCounts() {
        return counts;
    }

    public void setCount(int counts) {
        this.counts = counts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
