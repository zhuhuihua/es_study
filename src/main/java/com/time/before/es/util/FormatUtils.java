package com.time.before.es.util;


import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class FormatUtils {
	
	public static DateTimeFormatter dfd = DateTimeFormat.forPattern("yyyy-MM-dd");
	static DateTimeFormatter format = DateTimeFormat .forPattern("yyyy-MM-dd HH:mm:ss");  
	 
//	HdfsDataOutputStream
//	DataOutputStream


    //时间解析  
   
	//yyyy-MM-dd HH:mm:ss EE 代表星期
	
	public static String parse(String in, int n){
		
		DateTime dateTime = DateTime.parse(in, format);
		dateTime = dateTime.plus(n * 86400000);
		String date = dateTime.toString("yyyy-MM-dd"); 
		System.out.println(date);
		return date;
		
	}
	
	public static void main(String args[]){
		
		parse("2012-12-21 23:22:45", 1);
	}

}
