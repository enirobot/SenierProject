package com.team.news.controller;

import com.team.news.MongoDB.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.team.news.MongoDB.News;
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
}
