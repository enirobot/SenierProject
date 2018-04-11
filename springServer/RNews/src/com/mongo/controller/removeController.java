package com.mongo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mongo.board.MongoDAO;

/**
 * Handles requests for the application home page.
 */
@Controller


public class removeController {
	
	//조건에 맞는 document를 지우는 메서드. 여기서는 id가 조건이다.
	@RequestMapping("remove")
	public String remove(HttpServletRequest request) throws Exception {
		String page = "home";
		MongoDAO mongoDAO = new MongoDAO();
		
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		
		mongoDAO.findAndRemove(id);
		
		return page;
	}

	
}
