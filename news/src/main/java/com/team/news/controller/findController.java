package com.team.news.controller;

import com.team.news.mongoDB.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.team.news.mongoDB.News;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Handles requests for the application home page.
 */

@RestController
public class findController {

    @Autowired
	private NewsRepository repository;

	@GetMapping("/findAll")
	public List<News> findAll() {
	    return repository.findAll();
	}

	@GetMapping("/findNew")
    public List<News> find() {
	    return repository.findByArticleDateGreaterThanEqual("2018/07/03 20:59");
    }
}
