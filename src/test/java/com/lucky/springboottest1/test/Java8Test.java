package com.lucky.springboottest1.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class Java8Test {
	@Test
	public void testJava8(){
		List<String> ls = Arrays.asList("Lambdas","Default Method","Stream API","Date and Time Api");
		// 这里很重要，因为输出语句后面没有那个分号！！！
		ls.forEach(item -> System.out.println(item));
	}
	@Test
	public void testJavaA(){
//		String a = "0.00";
//		System.out.println(Double.valueOf(a) == 0);
		long sum = 0L;
		for(int i=0; i <Integer.MAX_VALUE; i++){
			sum+=i;
		}
		System.out.println(Integer.MAX_VALUE);
		System.out.println(sum);
	}
	
	@Test
	public void testMap(){
		Map<String,Object> map = new HashMap<>();
		for (Map.Entry<String,Object> entry : map.entrySet()) {
			  
		}
	}
	@Test
	public void testSet(){
		Set<String> set = new HashSet<>();
		for (String string : set) {
			
		}
		List list = new ArrayList<>();
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
