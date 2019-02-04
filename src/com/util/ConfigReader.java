package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.logger.MyLogger;

public class ConfigReader {
	
	static Logger log = Logger.getLogger(ConfigReader.class);
	
	public Properties readConfigFile(String filepath) throws IOException{
		log.info("Reading data from config file");
		Properties prop = new Properties();
		File file = new File(filepath);
		FileInputStream f1 = new FileInputStream(file);
		prop.load(f1);
		log.info("Data from config file loaded successfully...");
		return prop;
	}

}
