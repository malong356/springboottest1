package com.lucky.springboottest1.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucky.springboottest1.api.model.zzzz.Contact;
import com.lucky.springboottest1.api.service.ContactService;

@RestController
@RequestMapping(value = "/contact", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class ContactController {
	@Autowired
	private ContactService contactService;
	@RequestMapping(value = "/selectAll")
	public List<Contact> selectAll(){
		return contactService.selectAll();
	}
	@RequestMapping(value = "/twoDb")
	public Map<String, Object> twoDb(){
		return contactService.twoDb();
	}
}
