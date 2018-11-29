package com.team.news.Form;

/**
 * 클라이언트에서 Line Chart를 그릴 때 사용될 모델
 */

public class BubbleForm {
    public String word;//키워드(이름)
    public int counts;//기사수(크기)
    public int totalcommentCount;//댓글수(세로)
    public int totalEmotionWeight;//감정가중치(색깔)
    public double totalWeight;//총가중치
    public String date;
    public BubbleForm(){}

    public BubbleForm(String word, int counts, int totalcommentCount,int totalEmotionWeight, double totalWeight)
    {
        this.word = word;
        this.counts = counts;
        this.totalcommentCount=totalcommentCount;
        this.totalEmotionWeight=totalEmotionWeight;
        this.totalWeight=totalWeight;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public  int getCommentcounts() {
        return totalcommentCount;
    }

    public  int getReactioninfo() {
        return totalEmotionWeight;
    }

    public double getTotalweight() { return totalWeight; }

    public void setCommentcounts(int totalcommentCount) {
        this.totalcommentCount = totalcommentCount;
    }

    public int getCounts() {
        return counts;
    }

    public void setCount(int counts) {
        this.counts = counts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
