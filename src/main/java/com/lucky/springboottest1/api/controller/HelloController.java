package com.lucky.springboottest1.api.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@RequestMapping(value = "/hello", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public String hello(String name){
		return "Hello "+name+" !";
	}

}
