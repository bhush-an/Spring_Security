package com.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dto.LoginRequestDTO;
import com.app.dto.LoginResponseDTO;
import com.app.dto.RegistrationRequestDTO;
import com.app.dto.RegistrationResponseDTO;
import com.app.dto.ResponseDTO;
import com.app.entity.User;
import com.app.repository.UserRepository;
import com.app.utils.JwtUtils;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private JwtUtils utils;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UserRepository userRepo;
	
	@Value("${EXP_TIMEOUT}")
	private String jwtExpiry;

	@Override
	public ResponseDTO registerUser(@Valid RegistrationRequestDTO requestDTO) {
		User user = mapper.map(requestDTO, User.class);
		user.setPassword(encoder.encode(requestDTO.getPassword()));
		User persistentUser = userRepo.save(user);
		return ResponseDTO.builder().message("User registered successfully! User ID : " + persistentUser.getId())
				.registrationDetails(mapper.map(persistentUser, RegistrationResponseDTO.class))
				.build();
	}

	@Override
	public ResponseDTO loginUser(@Valid LoginRequestDTO requestDTO) {
		try {
			UsernamePasswordAuthenticationToken token =
					new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword());
			Authentication authenticationDetails = manager.authenticate(token);
			return ResponseDTO.builder().message("Authentication successful!")
					.loginDetails(LoginResponseDTO.builder()
							.jwt(utils.generateJwtToken(authenticationDetails))
							.expiresInMin((Long.parseLong(jwtExpiry)) / (60 * 1000))
							.build())
					.build();
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException(e.getMessage());
		}
	}

}
