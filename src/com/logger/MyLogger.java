package com.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.PropertyConfigurator;

public class MyLogger {
	
	static{
		   SimpleDateFormat dateFormat = new SimpleDateFormat("MM-DD-yyyy-hh-mm-ss");
		   System.setProperty("current.date.time", dateFormat.format(new Date()));
	}
	public static void configureLog4j(){
		String log4jConfPath = "D:\\Automation\\PeopleSoft\\PeopleSoft_InstanceStatus\\log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
	}

}
