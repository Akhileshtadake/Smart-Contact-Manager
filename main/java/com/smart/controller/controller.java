package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import jakarta.validation.Valid;

@Controller
public class controller {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired
	private UserRepository userRepository;
	
	//home-field
	@GetMapping("/")
	public String home(Model m) {
		
		m.addAttribute("title", "Home-SmartContactManager");
		return "home";
	}
	
	
	//about-field
	@GetMapping("/about")
	public String about(Model m) {
		
		m.addAttribute("title", "About-SmartContactManager");
		return "about";
	}
	
	
	//signup-field
	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title", "Register-SmartContactManager");
		m.addAttribute("user", new User());
		return "signup";
	}
	
	//this handler for registering user
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, @RequestParam(value="agreement", defaultValue = "false") boolean agreement, Model m, BindingResult result1 ){
		
		try {
			
			if(!agreement) {
				System.out.println("You have not agreed the terms and conditions");
			}
			
			if(result1.hasErrors()) {
				
				System.out.println("ERROR" + result1.toString());
				m.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement" + agreement);
			System.out.println("USER" + user);
			
			User result = this.userRepository.save(user);
			
			m.addAttribute("user", new User());
			
			m.addAttribute("user", user);
			return "signup";
			
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("user",user);
			return "signup";
		}
		
	}
	
	//handler for custom login 
	@GetMapping("/signin")
	public String customLogin(Model m) {
		m.addAttribute("title", "Login Page");
		return "login";
	}
	
	
}
