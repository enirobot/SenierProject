package com.team.news.Form;

import java.util.ArrayList;
import java.util.List;

public class BubbleFormAndDate {

    private String date;
    private List<BubbleForm> bubbleitems = new ArrayList<BubbleForm>();
    private String group;

    public void addBubbleitems(BubbleForm bubbleForm) {
        this.bubbleitems.add(bubbleForm);
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<BubbleForm> getBubbleitems() {
        return bubbleitems;
    }

    public String getDate() {return date;}

    public String toString() {return "BubbleFormAndDate : dateofday="+date+", groupofbubble="+group;}
}