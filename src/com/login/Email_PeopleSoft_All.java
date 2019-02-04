package com.login;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.model.ExlData;
import com.util.ExcelReader;

public class Email_PeopleSoft_All {
	
	static Logger log = Logger.getLogger(Email_Gmail.class);
	Session session;
	String client;

	public void configureEmail(Properties prop, List<ExlData> excelDataList) throws AddressException, MessagingException{
		String fromEmailID = prop.getProperty("fromEmail");
		String emailPwd = "FGHJ5214fghj";
		String hostName = prop.getProperty("hostName");
		String portNumber = prop.getProperty("portNumber");
		
		//CODE TO SEND EMAIL - mail.ciber.com
		Properties prop2 = new Properties();
		prop2.put("mail.smtp.starttsl.enable","true");
        prop2.put("mail.transport.protocol", "smtp");  
        prop2.put("mail.smtp.host", hostName);
        prop2.put("mail.smtp.auth", "true");
        prop2.put("mail.smtp.port", portNumber);
        prop2.put("mail.smtp.debug", "true");
        prop2.put("mail.smtp.ssl.enable", "false");
        session = Session.getInstance(prop2,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmailID, emailPwd);
			}
		  });
		sendEmail(prop, excelDataList, session);
	}
	
	void sendEmail(Properties prop, List<ExlData> excelDataList, Session session) throws AddressException, MessagingException{
		
		Set<String> uniqueClients = new HashSet<String>();
		Map<String, List<ExlData>> uniqueClientDataList = new HashMap<String, List<ExlData>>();
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
		  	message.setContent(generateEmailBody(prop, message, uniqueClientDataList.get(client)), "text/html");
		  	Transport.send(message);  
		}
		System.out.println("message sent successfully....");
		log.info("Email sent successfully");
	}
	
	
	String generateEmailBody(Properties prop, Message message, List<ExlData> clientBasedList) throws AddressException, MessagingException{
		
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
		message.setSubject(client +"  "+ "Instance Status : " + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(date) +" "+cal.getTimeZone().getDisplayName(false, TimeZone.SHORT));
		
		//Add Table 
		String text="<table width='100%' border='1' align='center'>"
					+ "<tr align='center'>"
		            + "<td><b>"+ExcelReader.label_URL+"<b></td>"
		            + "<td><b>"+ExcelReader.label_AppType+"<b></td>"
		            + "<td><b>"+ExcelReader.label_Client+"<b></td>"
		            + "<td><b>"+ExcelReader.label_DBType+"<b></td>"
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
