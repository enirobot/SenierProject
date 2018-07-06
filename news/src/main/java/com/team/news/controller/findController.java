package com.team.news.controller;

import com.team.news.MongoDB.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.team.news.MongoDB.News;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * DB 테스트할 때 사용되는 컨트롤러
 */

@RestController
public class findController {

    private final NewsRepository repository;

    @Autowired
    public findController(NewsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/findAll")
	public List<News> findAll() {
	    return repository.findAll();
	}

	@GetMapping("/findNew")
    public List<News> find() {
	    return repository.findByDateGreaterThanEqual("2018/07/03 20:59");
    }
    
}
