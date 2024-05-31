package com.smart.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;


@Service
public class EmailService {
	
	public boolean sendEmail(String subject, String message, String to, Address from) {
		
		boolean f = false;
		
		String fromString = "techsoftindia2018@gmail.com";
		
		//variable for gmail
		
		String host ="smtp.gmail.com";
		
		//get the system properties
		
		Properties properties = System.getProperties();
		System.out.println("properties "+ properties);
		
		//setting important information to properties object
		
		
		//host set
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		//Step1: to get the session object..
		Session session = Session.getInstance(properties, new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("techsoftindia2018@gmail.com", "");
			}
		});
		
		//Step2: compose the message [text, multi media]
		MimeMessage m = new MimeMessage(session);
		
		try {
			
			//from email
			m.setFrom(from);
			
			//adding recipient to message
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			//adding subject to message
			m.setSubject(subject);
			
			//adding text to message;
			m.setText(message);
			
			//send
			
			//Step3: send the message using Transport Class..
			Transport.send(m);
			
			System.out.println("Sent success.................");
			f=true;
		} catch(Exception e) {
			e.printStackTrace();
		} 
		
		return f;
		
	}
}
