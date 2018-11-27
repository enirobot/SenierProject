package com.team.news.Form;

import java.util.ArrayList;
import java.util.List;

public class PieFormAndDate {

    private String date;
    private List<PieForm> pieitems = new ArrayList<PieForm>();
    private String group;

    public void addPieitems(PieForm pieForm) {
        this.pieitems.add(pieForm);
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<PieForm> getPieitems() {
        return pieitems;
    }

    public void setPieitems(List<PieForm> pieitems) {
        this.pieitems = pieitems;
    }

    public String getDate() {return date;}

    public String toString() {return "SankeyFormAndDate : date="+date+", group="+group;}
}