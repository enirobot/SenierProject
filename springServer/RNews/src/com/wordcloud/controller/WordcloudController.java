package com.wordcloud.controller;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
/**
 * Handles requests for the application home page.
 */
@Controller


public class WordcloudController {
	@RequestMapping("wordcloud")
	public String Insert(Model model) throws Exception { 
		String page = "WC_sample";
		System.out.println("wordcloud controller access");

		RConnection connection = null;
		String names[];
		String counts[];
		REXP rexpNames;
		REXP rexpCounts;
		
		//R�� ���� �м��� ����� word cloud ���Ŀ� �´� �迭�� ����
		
		List<WCform> list = new ArrayList<WCform>();
		
		//�ӽ÷� �������� �������
		
		connection = new RConnection();
		connection.eval("library(\"rJava\")");
		connection.eval("library(\"KoNLP\")");
		connection.eval("useNIADic()");
		connection.eval("con <- mongolite::mongo(collection = \"news\", db = \"project\", url = \"mongodb://localhost\", verbose = TRUE)");
		connection.eval("df <- con$find(query = '{}')");
		connection.eval("data <- sapply( unique(df[\"articleContent\"]), extractNoun, USE.NAMES = F )");
		connection.eval("data_unlist <- unlist(data)");
		connection.eval("data_unlist <- gsub(\"[^ㄱ-ㅣ가-힣]+\", \"\", data_unlist)");
		connection.eval("data_Filter <- Filter( function(x) {nchar(x) >= 2}, data_unlist)");
		connection.eval("wordcount <- table(data_Filter)");
		connection.eval("wordcount_top <-head(sort(wordcount, decreasing = T), 100)");
		
		rexpNames = connection.eval("names(wordcount_top)");
		rexpCounts = connection.eval("wordcount_top");
		
		names = rexpNames.asStrings();
		counts = rexpCounts.asStrings();  
		
		for(int i = 0; i < names.length; i++) {
			WCform temp = new WCform(names[i], counts[i]);
			list.add(temp);
		}
		
		model.addAttribute("wordcloud",list);
		
		return page;
	}

	
}
