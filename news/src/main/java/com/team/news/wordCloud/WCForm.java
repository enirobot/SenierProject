package com.team.news.wordCloud;

public class WCForm {
    private String word;
    private String size;

    public WCForm(String word, String size)
    {
        this.word = word;
        this.size = size;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
