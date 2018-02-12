package com.mongo.board;

import org.springframework.data.annotation.Id;


//collection의 기본 형태
//Nosql이라 따로 스키마가 없어서 collection 형태를 바꾸고 싶으면 그냥 여기서 바꾸면 됨.
public class News {
	
	//document의 id에 해당하는 부분
	//@id를 적게 되면 따로 id를 입력하지 않아도 자동으로 id가 할당됨. id는 sql과 달리 복잡한 형태.
	//이 부분이 없으면 db 접속이 불가능하다는 것 같은데 정확히는 모르겠습니다.
	@Id
	private String id;
	
	private String title;
	private String company;
	private String site;
	
	public News(String title, String company, String site)
	{
		this.title = title;
		this.company = company;
		this.site = site;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	//document 출력을 위한 재정의
	@Override
	public String toString() {
		return "News [id=" + id + ", title=" + title + ", company="+company+", site="+site+"]";
	}
	
}
