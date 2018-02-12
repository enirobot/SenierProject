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
	
	//collection ���� ��� document�� ����ϴ� �޼���
	//ModelAndView�� ����Ͽ� list�� ����. �� ����� ����ϸ� ���� ���� �ݵ�� ModelAndView ���߸� ��.
	@RequestMapping("findAll")		//@RequestMapping : �信 �ִ� jsp ������ form-action�� ��ġ�ϴ� �޼��尡 ����ȴ�.
	public ModelAndView FindAll(){
		
		//�𵨿� �ش��ϴ� MongoDAO.java���� DB�� ����
		MongoDAO mongoDAO = new MongoDAO();
		//MongoDAO�� findAll�� �̿��Ͽ� collection ���� ��ü document�� list�� ����
		List<News> list = mongoDAO.findAll();
	
		
		ModelAndView view = new ModelAndView("list");
		view.getModelMap().addAttribute("list",list);
		
		return view;
	}

	
	//���ǿ� �ش��ϴ� document�� ���
	@RequestMapping("find")
	public ModelAndView Find(HttpServletRequest request) throws Exception {
		MongoDAO mongoDAO = new MongoDAO();
		
		//�ѱ� ������ �����ϱ����� ���ڵ�
		request.setCharacterEncoding("UTF-8");
		
		//where�� ����, is�� ���ǿ� �ش��ϴ� ��
		String where = request.getParameter("where");
		String is = request.getParameter("is");
		
		List<News> list = mongoDAO.find(where,is);
		ModelAndView view = new ModelAndView("list");
		view.getModelMap().addAttribute("list",list);
		
		return view;

	}
	
}
