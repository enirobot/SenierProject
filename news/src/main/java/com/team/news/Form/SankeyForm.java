package com.team.news.Form;

public class SankeyForm {
    private String source;
    private String destination;
    private int value;

    public SankeyForm() {}

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString()
    {
        return "source : " + source + ". destination : " + destination + ", value : " + value;
    }
}