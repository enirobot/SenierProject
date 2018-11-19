package com.team.news.Form;

public class BubbleForm {
    public String source;
    public String destination;
    public int value;

    public BubbleForm() {}

    public int getValue() {
        return value;
    }

    @Override
    public String toString()
    {
        return "bubbleofsource : " + source + ". bubbleofdestination : " + destination + ", value : " + value;
    }
}