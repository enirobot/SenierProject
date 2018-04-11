package com.crawling.controller;

import com.mongo.board.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class CSVnews{
	public String articleContent;
	public String articleCategory;
	public String articleDate;
	public String keyword;
	public void resetData(){
		this.articleCategory = null;
		this.articleContent = null;
		this.articleDate = null;
		this.keyword = null;
	}
}
public class Crawling{
	public String field = "키워드, 날짜, URL, 분류, 내용 \r\n";
	public String csvFileName = "D:/2018.csv";
	public String dateTag = "BUILD";
	public String category = "KEYWORDS";
	public String keyword = "NEWS-KEYWORDS";
	public String input_keyword;
	
	//private CSVnews news = new CSVnews();
	public String url;
	private Document doc;
	private Elements ele;// = doc.select("div.article");
	private BufferedWriter writer;
	

	MongoDAO mongoDAO = new MongoDAO();
	
	public Crawling(String input_keyword) throws Exception{
		this.input_keyword = input_keyword;
		//writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFileName), "MS949"));
		//writer.write(field);
	}
	public void run(){//54500
		for(int i = 1 ; i< 30 ; i++){
			try{
				
				News news = new News();
				
				setURL(i, news);			
				setKeyword(news);
				System.out.println(i);
				
				if(news.keyword != null){
					
					setArticleCategory2(news);
					setArticleDate(news);
					setArticleContent(news);
				
					//writer.write(news.keyword+" , "+news.articleDate+" , "+url+" , "+news.articleCategory +" , "+news.articleContent +"\r\n");
					
					if(!mongoDAO.exist("url",news.url))
						mongoDAO.insert(news);
				
				}
			}catch(Exception e){

			}
		}
//		try {
//			//writer.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	public void setURL(int i, News news) throws Exception{
		url = "http://www.imaeil.com/sub_news/sub_news_view.php?news_id="+Integer.toString(i)+"&yy=2018";
		news.setUrl(url);
		doc = Jsoup.connect(url).get();
		
	}
	public void setKeyword(News news){
		ele = doc.select("META");
		for(Element e : ele)
			if(e.attr("NAME").equals(keyword))
				news.keyword = e.attr("CONTENT");
		if(news.keyword.contains(input_keyword))
			news.keyword = input_keyword;
		else
			news.keyword = null;
				
	}
	public void setArticleCategory2(News news){
		ele = doc.select("meta");
		for(Element e : ele)
			{
				if(e.attr("content").equals("스포츠"))
					news.articleCategory = "스포츠";
				else if(e.attr("content").equals("경제"))
					news.articleCategory = "경제";
				else if(e.attr("content").equals("문화"))
					news.articleCategory = "문화";
				else if(e.attr("content").equals("정치"))
					news.articleCategory = "정치";
			}
				
	}
	
	public void setArticleDate(News news){
		ele = doc.select("META");
		for(Element e : ele)
			if(e.attr("NAME").equals(dateTag))
				news.articleDate = e.attr("CONTENT");
	}
	public void setArticleContent(News news){
		ele = doc.select("div#_article");
		news.articleContent = ele.text();
		news.articleContent = news.articleContent.replaceAll(",", "");
	}
}