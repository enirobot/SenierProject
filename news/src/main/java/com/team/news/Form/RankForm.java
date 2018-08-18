package com.team.news.Form;

import java.util.List;

public class RankForm {
    private String word;
    private int counts;

    public RankForm(String word, int counts)
    {
        this.word = word;
        this.counts = counts;
    }

    public RankForm() {}

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
}
