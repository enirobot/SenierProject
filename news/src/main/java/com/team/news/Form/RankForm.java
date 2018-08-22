package com.team.news.Form;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class RankForm {

    @Id
    private String id;

    private String word;
    private long totalCounts;
    private List<RankNode> nodeList;

    public RankForm(String word, int totalCounts)
    {
        this.word = word;
        this.totalCounts = totalCounts;
        this.nodeList = new ArrayList<>();
    }

    public RankForm() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getTotalCounts() {
        return totalCounts;
    }

    public void setTotalCounts(long totalCounts) {
        this.totalCounts = totalCounts;
    }

    public List<RankNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<RankNode> nodeList) {
        this.nodeList = nodeList;
    }

    public void add(RankNode rankNode) {
        nodeList.add(rankNode);
    }
}
