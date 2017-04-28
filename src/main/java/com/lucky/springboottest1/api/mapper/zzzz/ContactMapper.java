package com.lucky.springboottest1.api.mapper.zzzz;

import java.util.List;

import com.lucky.springboottest1.api.model.zzzz.Contact;

public interface ContactMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Contact record);

    int insertSelective(Contact record);

    Contact selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Contact record);

    int updateByPrimaryKey(Contact record);
    
    List<Contact> selectAll();
}