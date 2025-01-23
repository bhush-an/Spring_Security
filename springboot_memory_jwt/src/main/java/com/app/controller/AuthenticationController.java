package com.app.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

	@GetMapping("/welcome")
	public String loginUser() {
		return "Welcome to In Memory Based Authentication! as per new springboot security version.";
	}
	
	@GetMapping("/customer")
	@Secured("ROLE_CUSTOMER")
	public String CustomerView() {
		return "Logged in as a CUSTOMER!";
	}
	
	@GetMapping("/admin")
	@Secured("ROLE_CUSTOMER")
	public String AdminView() {
		return "Logged in as a ADMIN!";
	}
	
	@GetMapping("/view")
	@Secured({"ROLE_CUSTOMER", "ROLE_ADMIN"})
	public String viewProfile() {
		return "Profile Page.";
	}
}






