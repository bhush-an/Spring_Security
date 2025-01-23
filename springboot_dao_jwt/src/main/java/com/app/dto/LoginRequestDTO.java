package com.app.dto;

import jakarta.validation.constraints.NotBlank;
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
public class LoginRequestDTO {

	@NotBlank(message = "Please mention username.")
	private String username;
	
	@NotBlank(message = "Please mention password.")
	private String password;
}
