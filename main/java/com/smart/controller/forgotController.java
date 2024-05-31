package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.service.EmailService;

@Controller
public class forgotController {

	@Autowired
	private EmailService emailService;
	
	Random random = new Random(1000);
	
	//email id form open handler...
	@GetMapping("/forgot")
	public String openEmailForm() {
		
		
		
		return "forgot_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email) {
		
		
		System.out.println("EMAIL "+email);
		
		//generating otp 0f 4 digit..
		
		
		
		int otp = random.nextInt(999999);
		
		System.out.println("OTP "+otp);
		
		//write code for sent otp to email...
		
		
		return "verify_otp";
	}
}
