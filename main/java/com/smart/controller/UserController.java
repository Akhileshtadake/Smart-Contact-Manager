package com.smart.controller;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;





@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	//method for adding common data 
	@ModelAttribute
	public void addCommonData(Model m, Principal principal) {

		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);
		System.out.println("USER" + user);
		
		m.addAttribute("user", user);
		
	}
	
	//dashboard home
	@GetMapping("/index")
	public String dashboard(Model m, Principal principal) {
		
		m.addAttribute("title","User Dashboard");
		return "normal/user_dashboard";
	}
	
	
	
	//open add form handler
	@GetMapping("/add_contact")
	public String openAddContactForm(Model m) {
		
		m.addAttribute("title", "Add contact");
		m.addAttribute("contact",new Contact());
		return "normal/add_contact";
	}
	
	//processing add contact form
	@PostMapping("/process-contact")
	public String processContact(
			@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, Principal principal ) {
		
		try {
			
			
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		
		//processing and uploading image file
		if(file.isEmpty()) {
			System.out.println("File is Empty");
			contact.setImage("contact.png");
		}
		else {
			//upload the file to folder and update the name to contact
			contact.setImage(file.getOriginalFilename());
			
			
			File saveFile =  new ClassPathResource("static/img").getFile();	
			
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("image is uploaded");
		}
		
		user.getContacts().add(contact);
		
		contact.setUser(user);
		
		this.userRepository.save(user);
		
		System.out.println("DATA" + contact);
		
		System.out.println("Added to data base");
		
		//message success.....
		
		
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("error" +e.getMessage());
			e.printStackTrace();
			//message error...
			
		}
		return "normal/add_contact";
	}
	
	//show contacts
	//per page =5[n]
	//current page=0[page]
	
	@GetMapping("/show_contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title","Show User Contacts");
		//contacts ki poori list bhejni hai
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		Pageable pageable = PageRequest.of(page, 3);
		
		Page<Contact> contacts = this.contactRepository.findcontactsByUser(user.getId(),pageable);
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		
		m.addAttribute("totalPages", contacts.getTotalPages());
		return "normal/show_contacts";
	}
	
	//showing particular contact details
	@GetMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model m, Principal principal) {
		
		
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		if(user.getId()==contact.getUser().getId()) {
			m.addAttribute("contact",contact);
			m.addAttribute("title",contact.getName());
		}
				
		return "normal/contact_detail";
	}
	
	//delete contact handler
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model m, Principal principal) {
		
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		
		//check...
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		
		
		if(user.getId()==contact.getUser().getId()) {
			
			contact.setUser(null);
			this.contactRepository.deleteContactById(cId);
			
		}
		
		return"redirect:/user/show_contacts/0";
	}
	
	
	//UPDATE CONTROL HANDLER...
	@PostMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId") Integer cId,Model m) {
		
		m.addAttribute("title","Update Contact");
		
		Contact contact = this.contactRepository.findById(cId).get();
		m.addAttribute("contact",contact);
		return "normal/update_form";
	}
	
	//updating the database
	@PostMapping("/process-update")
	public String processUpdate(
			@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, Principal principal ) {
		
		try {
			
			//old contact details
			Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get(); 
			if(file.isEmpty()) {

				contact.setImage(oldContactDetail.getImage());
			}
			else {
				//delete pic
				File deleteFile =  new ClassPathResource("static/img").getFile();
				File file1=new File(deleteFile,oldContactDetail.getImage());
				file1.delete();
				
				
				//update new pic
				File saveFile =  new ClassPathResource("static/img").getFile();	
				
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
			}
			User user = this.userRepository.getUserByUserName(principal.getName());
			
			contact.setUser(user);
			
			this.contactRepository.save(contact);
			
			
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("error" +e.getMessage());
				e.printStackTrace();
				//message error...
				
			}
		
			System.out.println("Contact name:"+contact.getName());
			System.out.println("Conatct id:"+contact.getcId());
		
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	//Your profile
	@GetMapping("/profile")
	public String yourProfile(Model m) {
		
		m.addAttribute("title","Your Profile");
		return "normal/profile";
	}
	
	//settings field...
	@GetMapping("/settings")
	public String openSettings(Model m) {
		
		
	m.addAttribute("title","Settings");
		return "normal/settings";
	}
	
	//change password handler..
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword, Principal principal ){
		
		System.out.println("OLD PASSWORD "+oldPassword);
		System.out.println("NEW PASSWORD "+newPassword);
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		if(this.passwordEncoder.matches(oldPassword, user.getPassword())) {
			
			//change the password
			user.setPassword(this.passwordEncoder.encode(newPassword));
			this.userRepository.save(user);
		}else {
			
			return "redirect:/user/settings";
		}
		
		return "redirect:/user/index";
	}
}