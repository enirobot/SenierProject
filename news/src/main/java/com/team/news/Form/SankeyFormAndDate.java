package com.team.news.Form;

import java.util.ArrayList;
import java.util.List;

public class SankeyFormAndDate {

    private String date;
    private List<SankeyForm> sankeyitems = new ArrayList<SankeyForm>();
    private String group;

    public void addSankeyitems(SankeyForm sankeyForm) {
        this.sankeyitems.add(sankeyForm);
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SankeyForm> getSankeyitems() {
        return sankeyitems;
    }
}