package com.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.driver.Driver;
import com.logger.MyLogger;
import com.login.Email_Gmail;
import com.login.Login;
import com.model.ExlData;
import com.util.ConfigReader;
import com.util.ExcelReader;

public class Main_Gmail {
	
	ExcelReader excelReaderObj;
	ConfigReader configReaderObj;
	Driver driverObj;
	Login loginObj;
	Email_Gmail emailObj;
	Properties prop;
	String configFilePath = "D:\\Automation\\PeopleSoft\\PeopleSoft_InstanceStatus\\config.properties";
	String excelFilePath = "D:\\Automation\\PeopleSoft\\PeopleSoft_InstanceStatus\\TestData.xlsx";
	List<ExlData> excelDataList = new ArrayList<ExlData>();
	WebDriver driver;
	static Logger log = Logger.getLogger(Main_Gmail.class);

	public Main_Gmail(){
		excelReaderObj = new ExcelReader();
		configReaderObj = new ConfigReader();
		driverObj = new Driver();
		loginObj = new Login();
		emailObj = new Email_Gmail();
	}
	
	void setup() throws IOException{
		prop=configReaderObj.readConfigFile(configFilePath);
		excelDataList=excelReaderObj.readExcelFile(excelFilePath);
		//Excel data
		for(int i=0;i<excelDataList.size();i++){
			log.info(excelDataList.get(i).getUrl() +" "+excelDataList.get(i).getAppType() +" "+excelDataList.get(i).getClient() +" "+excelDataList.get(i).getDbType() 
					+" "+excelDataList.get(i).getEmail() +" "+excelDataList.get(i).getEmail());
		}
		
		driver=driverObj.getDriver(prop.getProperty("browser"));
		driver.manage().window().maximize();
	}
	
	void login() throws InterruptedException{
		//login to urls
		excelDataList = loginObj.login(excelDataList, driver);
		
		//Close the dirver
		loginObj.closeDriver(driver);
		System.out.println("Chrome driver closed successfully");
	}
	
	void sendEmail() throws AddressException, MessagingException{
		emailObj.configureEmail(prop, excelDataList);
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, AddressException, MessagingException{
		MyLogger.configureLog4j();
		Main_Gmail mainObj = new Main_Gmail();
		mainObj.setup();
		mainObj.login();
		mainObj.sendEmail();
	}

}
