package com.lucky.springboottest1.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucky.springboottest1.api.mapper.zzzz.ContactMapper;
import com.lucky.springboottest1.api.model.zzzz.Contact;
import com.lucky.springboottest1.api.service.ContactService;
@Service
public class ContactServiceImpl implements ContactService {
	@Autowired
	private ContactMapper contactMapper;
	@Override
	public List<Contact> selectAll() {
		return contactMapper.selectAll();
	}

}
