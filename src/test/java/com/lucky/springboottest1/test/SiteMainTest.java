package com.lucky.springboottest1.test;

import org.springframework.boot.builder.SpringApplicationBuilder;

import com.lucky.springboottest1.boot.Application;

public class SiteMainTest {
	public static void main(String[] args) {
		new SpringApplicationBuilder().sources(Application.class)
			//.profiles("local")
			.run(args);
	}
}
