package com.mongo.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crawling.controller.Crawling;
import com.mongo.board.MongoDAO;
import com.mongo.board.News;

/**
 * Handles requests for the application home page.
 */
@Controller


public class insertController {
	@RequestMapping("insert")
	public String Insert(HttpServletRequest request) throws Exception { 
		String page = "home";

		request.setCharacterEncoding("UTF-8");
		String keyword = request.getParameter("keyword");
		
		Crawling parser = new Crawling(keyword);
 		parser.run();

		return page;
	}

	
}
