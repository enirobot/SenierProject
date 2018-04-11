package com.mongo.board;

import java.net.URI;

import org.springframework.data.annotation.Id;


public class News {
	
	@Id
	private String id;
	
	public String keyword;
	public String articleDate;
	public String articleCategory;
	public String articleContent;
	public String url;
	
	public News()
	{
		this.articleContent = null;
		this.articleCategory = null;
		this.articleDate = null;
		this.keyword = null;
		this.url = null;
	}

	public String getId()
	{
		return id;
	}
	public void setUrl(String url)
	{
		this.url = url.replace("?", "=.q_m");
	}
	public String getUrl()
	{
		String temp;
		temp = url.replace("=.q_m", "?");
		return temp;
	}

	//document ����� ���� ������
	@Override
	public String toString() {
		return "News [category=" + articleCategory  + ", date=" + articleDate + ", keyword=" + keyword + ", url=" + getUrl() + "]";
	}
	
}
