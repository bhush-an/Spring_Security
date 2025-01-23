package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDTO {

	@NotBlank(message = "Please mention username.")
	private String username;
	
	@NotBlank(message = "Please mention password.")
	private String password;
	
	@NotNull(message = "Please choose any one role : ROLE_ADMIN / ROLE_CUSTOMER")
	private String role;
}
