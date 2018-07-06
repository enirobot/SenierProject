package com.team.news.Form;

public class SankeyForm {
    public String source;
    public String destination;
    public int value;

    @Override
    public String toString()
    {
        return "source : " + source + ". destination : " + destination + ", value : " + value + "\r\n";
    }
}
