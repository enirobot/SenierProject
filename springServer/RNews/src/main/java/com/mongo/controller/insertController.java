package com.mongo.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
		//home 뒤에 .jsp가 붙지않는 이유는 servlet-context.xml에서 생략할 수 있도록 했기 때문
		
		request.setCharacterEncoding("UTF-8");
		String title = request.getParameter("title");
		String company = request.getParameter("company");
		String site = request.getParameter("site");
		
		//제대로 동작하는지 보기 위해 로그 기록 출력
		System.out.println("title : "+title+", company :  "+company+", site : "+site);
		News news = new News(title, company, site);
		MongoDAO mongoDAO = new MongoDAO();
		mongoDAO.insert(news);
		
		//모든 동작을 마치고 다시 home.jsp로 돌아감.
		return page;
	}

	
}
