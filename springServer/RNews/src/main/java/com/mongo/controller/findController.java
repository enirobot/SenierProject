package com.mongo.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.ModelAndView;

import com.mongo.board.MongoDAO;
import com.mongo.board.News;

/**
 * Handles requests for the application home page.
 */
@Controller


public class findController {
	
	//collection 내의 모든 document를 출력하는 메서드
	//ModelAndView를 사용하여 list를 전송. 이 기능을 사용하면 리턴 값은 반드시 ModelAndView 여야만 함.
	@RequestMapping("findAll")		//@RequestMapping : 뷰에 있는 jsp 파일의 form-action과 일치하는 메서드가 실행된다.
	public ModelAndView FindAll(){
		
		//모델에 해당하는 MongoDAO.java에서 DB에 접속
		MongoDAO mongoDAO = new MongoDAO();
		//MongoDAO의 findAll을 이용하여 collection 내의 전체 document를 list에 저장
		List<News> list = mongoDAO.findAll();
	
		
		ModelAndView view = new ModelAndView("list");
		view.getModelMap().addAttribute("list",list);
		
		return view;
	}

	
	//조건에 해당하는 document만 출력
	@RequestMapping("find")
	public ModelAndView Find(HttpServletRequest request) throws Exception {
		MongoDAO mongoDAO = new MongoDAO();
		
		//한글 깨짐을 방지하기위한 인코딩
		request.setCharacterEncoding("UTF-8");
		
		//where는 조건, is는 조건에 해당하는 값
		String where = request.getParameter("where");
		String is = request.getParameter("is");
		
		List<News> list = mongoDAO.find(where,is);
		ModelAndView view = new ModelAndView("list");
		view.getModelMap().addAttribute("list",list);
		
		return view;

	}
	
}
