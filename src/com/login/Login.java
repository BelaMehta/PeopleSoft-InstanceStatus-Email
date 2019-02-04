package com.login;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.model.ExlData;
import com.util.ConfigReader;

public class Login {
	
	static Logger log = Logger.getLogger(Login.class);
	
	public List<ExlData> login(List<ExlData> excelDataList, WebDriver driver) throws InterruptedException{
		String result=null;
		for(int i=0;i<excelDataList.size();i++){
		//Get data from each row
			String url = excelDataList.get(i).getUrl();
			String username = excelDataList.get(i).getUsername();
			String password = excelDataList.get(i).getPassword();
			String appType = excelDataList.get(i).getAppType();
			driver.navigate().to(url);
			log.info(url +" : launched");
			driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
			if(appType.equalsIgnoreCase("PS")){
				List<WebElement> userName = driver.findElements(By.id("userid"));
				if(userName.isEmpty()){
					System.out.println("URL is not reachable; Please verify connectivity");
					result = "Fail : URL is not reachable; Please verify connectivity";
				}
				else{
					driver.findElement(By.id("userid")).sendKeys(username);
					driver.findElement(By.id("pwd")).sendKeys(password);
					driver.findElement(By.name("Submit")).click();
					List<WebElement> psElements = driver.findElements(By.id("login_error"));
					if(psElements.isEmpty()){
						result = "PASS : Login successful; Instance is up and running";
						driver.findElement(By.xpath("//a[@id='PT_ACTION_MENU$PIMG']/img")).click();
						Thread.sleep(10);
						driver.findElement(By.id("PT_LOGOUT_MENU")).click();}
					else{
						String loginError = driver.findElement(By.id("login_error")).getText();
						//String loginError = "The application server is down at this time.";
						if(loginError.contains("Your User ID and/or Password are invalid."))
							result = "PASS : Instance is up and running";
						else
							result = "FAIL : " + loginError; 
						}
						System.out.println(result);
					}
				excelDataList.get(i).setResult(result);
				log.info(result);
			}
			else if(appType.equalsIgnoreCase("EBS")){
				List<WebElement> userName = driver.findElements(By.xpath("//input[@id='usernameField']"));
				if(userName.isEmpty()){
					System.out.println("Application didn't launch");
					result = "Fail : URL is not reachable; Please verify connectivity";
				}
				else{
					driver.findElement(By.xpath("//input[@id='usernameField']")).sendKeys(username);
					driver.findElement(By.xpath("//input[@id='passwordField']")).sendKeys(password);
					driver.findElement(By.xpath("//button[text()='Log In']")).click();
					List<WebElement> ebsElements = driver.findElements(By.id("errorBox"));
					Thread.sleep(2000);
					if(ebsElements.isEmpty()){
						result = "PASS : Login is successful. Instance is up and running";
						}
					else{
						String loginError = driver.findElement(By.id("errorBox")).getText();
						if(loginError.contains("Login failed. Please verify your login information or contact the system administrator."))
							result = "PASS : Instance is up and running";
						else
							result = "FAIL : " + loginError;
						}
						System.out.println(result);
						}
						excelDataList.get(i).setResult(result);
						log.info(result);
			 	}
			}
		return excelDataList;
	}
	
	public void closeDriver(WebDriver driver){
		driver.close();
		try {
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
