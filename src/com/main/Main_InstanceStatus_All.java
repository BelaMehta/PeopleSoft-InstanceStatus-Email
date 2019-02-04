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
import com.login.Email_PeopleSoft_All;
import com.login.Login;
import com.model.ExlData;
import com.util.ConfigReader;
import com.util.ExcelReader;

public class Main_InstanceStatus_All {
	
	ExcelReader excelReaderObj;
	ConfigReader configReaderObj;
	Driver driverObj;
	Login loginObj;
	Email_PeopleSoft_All allEmailObj;
	Properties prop;
	String configFilePath = "D:\\PeopleSoft\\config\\config.properties";
	List<ExlData> excelDataList = new ArrayList<ExlData>();
	WebDriver driver;
	static Logger log = Logger.getLogger(Main_InstanceStatus_All.class);

	public Main_InstanceStatus_All(){
		excelReaderObj = new ExcelReader();
		configReaderObj = new ConfigReader();
		driverObj = new Driver();
		loginObj = new Login();
		allEmailObj = new Email_PeopleSoft_All();
	}
	
	void setup() throws IOException{
		prop=configReaderObj.readConfigFile(configFilePath);
		excelDataList=excelReaderObj.readExcelFile(prop.getProperty("excelFilePath"));
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
		allEmailObj.configureEmail(prop, excelDataList);
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, AddressException, MessagingException{
		MyLogger.configureLog4j();
		Main_InstanceStatus_All mainObj = new Main_InstanceStatus_All();
		mainObj.setup();
		mainObj.login();
		mainObj.sendEmail();
	}

}
