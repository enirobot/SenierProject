package com.spring.RNews;

public class WCform {
	private String word;
	private String count;
	
	public WCform(String word, String count)
	{
		this.word = word;
		this.count = count;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

}