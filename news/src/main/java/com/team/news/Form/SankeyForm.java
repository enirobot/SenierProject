package com.team.news.Form;

/**
 * 클라이언트에서 Sankey 다이어그램을 그릴 때 사용될 데이터를 DB에 저장할 모델
 */

public class SankeyForm {
    public String source;
    public String destination;
    public int value;

    public SankeyForm() {}

    @Override
    public String toString()
    {
        return "source : " + source + ". destination : " + destination + ", value : " + value + "\r\n";
    }
}
