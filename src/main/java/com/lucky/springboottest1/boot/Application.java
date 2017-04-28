package com.lucky.springboottest1.boot;

import javax.servlet.Filter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootApplication(scanBasePackages = "com.lucky.springboottest1")
@MapperScan(value={"com.lucky.springboottest1.api.mapper.zzzz"})
public class Application extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(getClass());
	}
	@Bean
	@Order(2)
	public Filter characterEncodingFilter() {
		return new CharacterEncodingFilter("UTF-8", true);
	}
}
