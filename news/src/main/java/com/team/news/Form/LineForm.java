package com.team.news.Form;

/**
 * 클라이언트에서 Line Chart를 그릴 때 사용될 모델
 */

public class LineForm {
    public String word;
    public String counts;
    public String date;

    public LineForm(String word, String counts, String date)
    {
        this.word = word;
        this.counts = counts;
        this.date = date;
    }

    public LineForm() {}

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
