package com.team.news.mongoDB;

import org.springframework.data.annotation.Id;

/**
 * DB에 저장되어 있는 한개의 요소를 나타내는 모델
 */
public class News {

    @Id
    private String id;		// DB document id

    public String title;	// 제목
    public String company;	// 언론사
    public String date;		// 날짜
    public String category;	// 카테고리
    public String url;		// 기사 주소
    public String content;	// 내용

    public News(String title, String company, String date,
                String category, String url, String content) {

        this.title = title;
        this.company = company;
        this.date = date;
        this.category = category;
        this.url = url;
        this.content = content;
    }

    @Override
    public String toString() {
        return "News [id=" + id + ", category=" + category  + ", date=" + date + ", url=" + url + "]";
    }

}
