package com.lucky.springboottest1.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.lucky.springboottest1.api.mapper.yyyy.CityMapper;
import com.lucky.springboottest1.api.mapper.zzzz.ContactMapper;
import com.lucky.springboottest1.api.model.yyyy.City;
import com.lucky.springboottest1.api.model.zzzz.Contact;
import com.lucky.springboottest1.api.service.ContactService;
@Service
public class ContactServiceImpl implements ContactService {
	@Autowired
	private ContactMapper contactMapper;
	@Autowired
	private CityMapper cityMapper;
	@Override
	public List<Contact> selectAll() {
		return contactMapper.selectAll();
	}
	@Override
	public Map<String, Object> twoDb() {
		List<Contact> list = contactMapper.selectAll();
		System.out.println("@@@@@@@@@"+JSON.toJSONString(list));
		City city = cityMapper.selectByPrimaryKey(1);
		Map<String, Object> map = new HashMap<>();
		map.put("listContact", list);
		map.put("city", city);
		return map;
	}

}
