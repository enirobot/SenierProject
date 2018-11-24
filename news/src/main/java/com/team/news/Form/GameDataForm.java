package com.team.news.Form;

public class GameDataForm {
    private String word;
    private double totalWeight;
    private String imageUrl;

    public GameDataForm(String word, double totalWeight, String imageUrl) {
        this.word = word;
        this.totalWeight = totalWeight;
        this.imageUrl = imageUrl;
    }

    public GameDataForm(){}

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }
}
