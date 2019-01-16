package com.lrs.core.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

	@GetMapping("/include/{pageName}")
	public String include(@PathVariable("pageName") String pageName){
		System.out.println("/include/"+pageName);
		return "include/"+pageName;
	}

	@GetMapping("/error/{pageNumber}")
	public String error(@PathVariable("pageNumber") String pageNumber){
		return "error/"+pageNumber;
	}
	
}
