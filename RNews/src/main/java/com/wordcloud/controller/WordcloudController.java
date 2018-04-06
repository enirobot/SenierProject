package com.wordcloud.controller;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles requests for the application home page.
 */
@Controller


public class WordcloudController {
	@RequestMapping("wordcloud")
	public String Insert(Model model) throws Exception { 
		String page = "WC_sample";
		System.out.println("wordcloud controller access");

		
		//R�� ���� �м��� ����� word cloud ���Ŀ� �´� �迭�� ����
		
		List<WCform> list = new ArrayList<WCform>();
		
		//�ӽ÷� �������� �������
		
		Random rand = new Random();
		for(int i=0;i<100;i++) {
			String rand_num1 = Integer.toString(rand.nextInt(30));
			String rand_num2 = Integer.toString(rand.nextInt(20));
			WCform temp = new WCform(rand_num1, rand_num2);
			list.add(temp);
		}
		

		
		model.addAttribute("wordcloud",list);
		
		return page;
	}

	
}
