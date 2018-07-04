package com.team.news.mongoDB;

import org.springframework.data.annotation.Id;

public class News {

    @Id
    private String id;				// DB document id

    public String articleTitle;		// 제목
    public String articleCategory;	// 카테고리
    public String articleDate;		// 날짜
    public String articleContent;	// 내용
    public String articleCompany;	// 언론사
    public String keyword;			// 키워드
    public String url;				// 기사 주소

    public News(String articleTitle, String articleCategory, String articleDate,
                String articleContent, String articleCompany, String keyword, String url) {

        this.articleTitle = articleTitle;
        this.articleCategory = articleCategory;
        this.articleDate = articleDate;
        this.articleContent = articleContent;
        this.articleCompany = articleCompany;
        this.keyword = keyword;
        this.url = url;
    }

    @Override
    public String toString() {
        return "News [id=" + id + ", category=" + articleCategory  + ", date=" + articleDate + ", keyword=" + keyword + ", url=" + url + "]";
    }

}
