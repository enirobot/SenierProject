package com.team.news.Form;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * 클라이언트에서 워드클라우드 다이어그램을 그릴 때 필요한 정보 보낼 때 사용되는 모델
 */
public class WCForm {
    @Id
    private String word;
    private double totalWeight;
    private List<String> idList;

    public WCForm() {}

    public WCForm(String word, double totalWeight, List<String> idList)
    {
        this.word = word;
        this.totalWeight = totalWeight;
        this.idList = idList;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
}
