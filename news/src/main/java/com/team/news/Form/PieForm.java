package com.team.news.Form;

/**
 * 클라이언트에서 Line Chart를 그릴 때 사용될 모델
 */

public class PieForm {

    public String category;//카테고리
    public int value;//카테고리별기사개수
    public String date;
    public PieForm(){}

    public PieForm(String category, int value)
    {
        this.category = category;
        this.value = value;
    }


    public String getCategory() {
        return category;
    }

    public int getValue(){return value;}

    public void setValue(int value){this.value=value;}//

    public void setCategory(String category) { this.category= category; }//


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
