package com.team.news.Form;

import java.util.ArrayList;
import java.util.List;

public class WCSearchNode {
    private int counts;
    private List<String> idList;

    public WCSearchNode(int counts) {
        this.counts = counts;
        this.idList = new ArrayList<String>();
    }

    public WCSearchNode(int counts, String id) {
        this.counts = counts;
        this.idList = new ArrayList<String>();
        this.idList.add(id);
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public void add(String id) {
        this.idList.add(id);
    }
}
