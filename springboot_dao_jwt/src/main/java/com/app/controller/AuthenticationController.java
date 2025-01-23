package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.LoginRequestDTO;
import com.app.dto.RegistrationRequestDTO;
import com.app.dto.ResponseDTO;
import com.app.service.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
	
	@Autowired
	private IUserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<?> userRegistration(@RequestBody @Valid RegistrationRequestDTO requestDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(requestDTO));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@RequestBody @Valid LoginRequestDTO requestDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.loginUser(requestDTO));
	}
	
	@GetMapping("/welcome")
	public ResponseEntity<?> welcome() {
		return ResponseEntity.ok(ResponseDTO.builder()
				.message("Welcome to DAO Based Authentication - New Spring Security Version").build());
	}
	
	@GetMapping("/customer")
	@PreAuthorize("hasRole(ROLE_CUSTOMER)")
	public ResponseEntity<?> customer() {
		return ResponseEntity.ok(ResponseDTO.builder()
				.message("Welcome to DAO Based Authentication - CUSTOMER").build());
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole(ROLE_ADMIN)")
	public ResponseEntity<?> admin() {
		return ResponseEntity.ok(ResponseDTO.builder()
				.message("Welcome to DAO Based Authentication - ADMIN").build());
	}
	
	@GetMapping("/view")
	@PreAuthorize("hasRole(ROLE_ADMIN) or hasRole(ROLE_CUSTOMER)")
	public ResponseEntity<?> view() {
		return ResponseEntity.ok(ResponseDTO.builder()
				.message("Welcome to DAO Based Authentication - View Profile Page").build());
	}

}
