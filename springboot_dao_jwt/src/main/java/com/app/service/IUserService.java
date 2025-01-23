package com.app.service;

import com.app.dto.LoginRequestDTO;
import com.app.dto.RegistrationRequestDTO;
import com.app.dto.ResponseDTO;

import jakarta.validation.Valid;

public interface IUserService {

	ResponseDTO registerUser(@Valid RegistrationRequestDTO requestDTO);

	ResponseDTO loginUser(@Valid LoginRequestDTO requestDTO);

}
