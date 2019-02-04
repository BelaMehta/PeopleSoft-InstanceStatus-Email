import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Main_Gmail {
	
	static{
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-DD-yyyy-hh-mm-ss");
        System.setProperty("current.date.time", dateFormat.format(new Date()));
    }
	
	static Logger log = Logger.getLogger(Main_Gmail.class);
	
	List<ExlData> excelDataList = new ArrayList<ExlData>();
	Set<String> uniqueClients = new HashSet<String>();
	Map<String, List<ExlData>> uniqueClientDataList = new HashMap<String, List<ExlData>>();
	WebDriver driver;
	Properties prop;
	Session session;
	String client;
	static String label_URL, label_UserName, label_Password, label_AppType, label_Client, label_DBType, label_TO, label_CC;
	
	
	public static void main(String[] args) throws IOException, InterruptedException, AddressException, MessagingException {
		Main_Gmail obj = new Main_Gmail();
		String log4jConfPath = "D:\\Automation\\PeopleSoft\\Learning\\log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		log.info("Loading config.properties file");
		obj.loadProperties();
		log.info("Config property file loaded successfully");
		obj.readDataFromExcelFile();
		obj.getDriver();
		obj.login();
		//obj.setupEmail();
	}
	
	//Load config.properties file & get browsername, fromEmail ID, emailPassword
	void loadProperties() throws IOException{
		prop = new Properties();
		File file = new File("D:\\Automation\\PeopleSoft\\Learning\\config.properties");
		FileInputStream f1 = new FileInputStream(file);
		prop.load(f1);
		System.out.println("Browser : "+prop.getProperty("browser"));
		System.out.println("From EmailID : "+prop.getProperty("fromEmail"));
		System.out.println("CC_COPHI : "+prop.getProperty("cc_COPHI"));
	}
	
	//Read data from excel file and hold it in the excel class object
	void readDataFromExcelFile() throws IOException{
		
		FileInputStream fisExcel = new FileInputStream("D:\\Automation\\PeopleSoft\\Learning\\TestData.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fisExcel);
		XSSFSheet sheet = workbook.getSheet("URLs");
		int totalRow = sheet.getLastRowNum();
		int totalColumn = sheet.getRow(0).getLastCellNum();
		Row headerRow = sheet.getRow(0);
		
		//Read Column Labels and store in static variables
		for(int col=0;col<totalColumn;col++){
			Cell headerCell = headerRow.getCell(col);
			switch(col){
			case 0:
				label_URL=headerCell.getStringCellValue();
				break;
			case 1:
				label_UserName=headerCell.getStringCellValue();
				break;
			case 2:
				label_Password=headerCell.getStringCellValue();
				break;
			case 3:
				label_AppType=headerCell.getStringCellValue();
				break;
			case 4:
				label_Client=headerCell.getStringCellValue();
				break;
			case 5:
				label_DBType=headerCell.getStringCellValue();
				break;
			case 6:
				label_TO=headerCell.getStringCellValue();
				break;
			case 7:
				label_CC=headerCell.getStringCellValue();
				break;
			}
		}
		
		System.out.println(label_URL +" "+label_AppType +" "+label_Client +" "+label_DBType);
	
		//iterate through each row
		for(int rowIterator=1;rowIterator<=totalRow;rowIterator++){
			ExlData excelData = new ExlData();
			Row row = sheet.getRow(rowIterator);
			//Iterate through each column
			for(int colIterator=0;colIterator<totalColumn;colIterator++){
				//System.out.println(row.getCell(colIterator).getStringCellValue());
				
				switch(colIterator){
					case 0:
						excelData.setUrl(row.getCell(colIterator).getStringCellValue());
						break;
					case 1:
						excelData.setUsername(row.getCell(colIterator).getStringCellValue());
						break;
					case 2:
						excelData.setPassword(row.getCell(colIterator).getStringCellValue());
						break;
					case 3:
						excelData.setAppType(row.getCell(colIterator).getStringCellValue());
						break;
					case 4:
						excelData.setClient(row.getCell(colIterator).getStringCellValue());
						break;
					case 5:
						excelData.setDbType(row.getCell(colIterator).getStringCellValue());
						break;
					case 6:
						excelData.setEmail(row.getCell(colIterator).getStringCellValue());
						break;
					case 7:
						excelData.setCCEmail(row.getCell(colIterator).getStringCellValue());
						break;
				}
			}
			excelDataList.add(excelData);
		}
		
		//Read list data
		for(int i=0;i<excelDataList.size();i++){
			System.out.println(excelDataList.get(i).getUrl() +"  "+excelDataList.get(i).getAppType() +"  "+excelDataList.get(i).getClient() +"  "+excelDataList.get(i).getDbType() +" "+excelDataList.get(i).getEmail() +" "+excelDataList.get(i).getCCEmail());
		}
		workbook.close();
	}
	
	//Define drivers
	void getDriver(){
		String browser;
		browser = prop.getProperty("browser");
		if(browser.equalsIgnoreCase("chrome")){
			System.setProperty("webdriver.chrome.driver", "D:\\Automation\\PeopleSoft\\Learning\\Drivers\\chromedriver.exe");
			//System.setProperty("webdriver.chrome.driver", "D:\\PeopleSoft\\drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		}
		else if(browser.equalsIgnoreCase("firefox")){
			driver = new FirefoxDriver();
		}
		driver.manage().window().maximize(); 
	}
	
	//Business logic - login to each URLs and store the result
	void login() throws InterruptedException{
		String result=null;
		for(int i=0;i<excelDataList.size();i++){
			//Get data from each row
			String url = excelDataList.get(i).getUrl();
			String username = excelDataList.get(i).getUsername();
			String password = excelDataList.get(i).getPassword();
			String appType = excelDataList.get(i).getAppType();
			driver.navigate().to(url);
			driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
			if(appType.equalsIgnoreCase("PS")){
				List<WebElement> userName = driver.findElements(By.id("userid"));
				if(userName.isEmpty()){
					System.out.println("URL is not reachable; Please verify connectivity");
					result = "Fail : Application did not launch";
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
								//result = "FAIL";
						}
					System.out.println(result);
					//excelDataList.get(i).setResult(result);
				}
					excelDataList.get(i).setResult(result);
				}
			 else if(appType.equalsIgnoreCase("EBS")){
				List<WebElement> userName = driver.findElements(By.xpath("//input[@id='usernameField']"));
				if(userName.isEmpty()){
					System.out.println("Application didn't launch");
					result = "Fail : Application did not launch";
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
						//excelDataList.get(i).setResult(result);
						}
						excelDataList.get(i).setResult(result);
			 		}
				}
		
		driver.close();
		try {
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//code to setup SMPT & Port for email functionality
	void setupEmail() throws AddressException, MessagingException{
		//String fromEmailID = prop.getProperty("fromEmail");
		//String emailPwd = prop.getProperty("emailPassword");
		String fromEmailID = "mehtabelaj@gmail.com";
		String emailPwd = "Loveislife1!";
		String hostName = prop.getProperty("hostName");
		String portNumber = prop.getProperty("portNumber");
		Properties props = new Properties();
		
		//CODE TO SEND EMAIL - mail.ciber.com
		/*
		props.put("mail.smtp.starttsl.enable","true");
		props.put("mail.transport.protocol", "smtp");  
		props.put("mail.smtp.host", hostName);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", portNumber);
		props.put("mail.smtp.debug", "true");
		props.put("mail.smtp.ssl.enable", "false");*/
        
		//CODE TO SEND EMAIL - gmail.com
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
	    props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
	    props.put("mail.smtp.socketFactory.fallback", "false");
		session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmailID, emailPwd);
			}
		  });
		sendEmail();
	}
	
	//Code to send the email - Client specific 
		void sendEmail() throws AddressException, MessagingException{
		String fromEmail = prop.getProperty("fromEmail");
		
		//Get unique client
		for(int i=0;i<excelDataList.size();i++){
			uniqueClients.add(excelDataList.get(i).getClient());
		}
		
		//get unique client data from excel
		Iterator<String> itr = uniqueClients.iterator();
		while(itr.hasNext()){
			String client = itr.next();
			List<ExlData> tempList = new ArrayList<ExlData>();
			for(int i=0;i<excelDataList.size();i++){
				if(client.equals(excelDataList.get(i).getClient())){
					tempList.add(excelDataList.get(i));
				}
			}
			uniqueClientDataList.put(client, tempList);
		}
		
		//Compose email for unique clients
		Iterator<String> itr1 = uniqueClients.iterator();
		while(itr1.hasNext()){
			client = itr1.next();
			MimeMessage message = new MimeMessage(session);  
	  		message.setFrom(new InternetAddress(fromEmail));
	  		message.setContent(generateEmailBody(message, uniqueClientDataList.get(client)), "text/html");
	  		Transport.send(message);  
		}
			System.out.println("message sent successfully...."); 
	}
	
	//Code to generate email body
	String generateEmailBody(Message message, List<ExlData> clientBasedList) throws AddressException, MessagingException{
		
		//Read POC Email id from config file
		String emailNote = prop.getProperty("emailNote");
		String regards = prop.getProperty("regards");
		
		//Add recipients based on clients
		message.addRecipients(Message.RecipientType.TO,
				InternetAddress.parse(clientBasedList.get(0).getEmail()));
		
		//Add CC based on clients
			message.addRecipients(Message.RecipientType.CC,
				InternetAddress.parse(clientBasedList.get(0).getCCEmail()));
		
		//Email Subject
		java.util.Date date= new java.util.Date();
		Calendar cal = Calendar.getInstance();
		message.setSubject(client +"  "+ "Instance Status : " + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(date) +" "+cal.getTimeZone());
		
		String urlLabel="URL";
		
		//Add Table 
		String text="<table width='100%' border='1' align='center'>"
                + "<tr align='center'>"
                + "<td><b>"+label_URL+"<b></td>"
                + "<td><b>"+label_AppType+"<b></td>"
                + "<td><b>"+label_Client+"<b></td>"
                + "<td><b>"+label_DBType+"<b></td>"
                + "<td><b>Status<b></td>"
                + "</tr>";
		
		//Login status with result
		for(int i=0;i<clientBasedList.size();i++){
				String color = clientBasedList.get(i).getResult().contains("PASS")?"WHITE":"RED";
				text = text + "<tr align='left'>"+"<td>"+clientBasedList.get(i).getUrl()+ "</td>"
					+"<td>"+clientBasedList.get(i).getAppType()+ "</td>"
					+"<td>"+clientBasedList.get(i).getClient()+ "</td>"
					+"<td>"+clientBasedList.get(i).getDbType()+ "</td>"
					+"<td bgcolor ="+color+">"+clientBasedList.get(i).getResult()+ "</td>"
					+"</tr>";
			}
		text = text + "</table><br><br>Thanks & Regards, <br>" +regards
				+ "<br><br>"+emailNote +"<br>";
		return text;
	}
}	
