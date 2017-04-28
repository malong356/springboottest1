package com.lucky.springboottest1.api.service;

import java.util.List;
import java.util.Map;

import com.lucky.springboottest1.api.model.zzzz.Contact;

public interface ContactService {
	List<Contact> selectAll();
	Map<String,Object> twoDb();
}
