package com.wordcloud.controller;

public class WCform {
	private String word;
	private String size;
	
	public WCform(String word, String size)
	{
		this.setWord(word);
		this.setSize(size);
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
