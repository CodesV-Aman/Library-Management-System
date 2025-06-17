package com.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.LoginRequestDTO;
import com.library.dto.LoginResponseDTO;
import com.library.dto.RegisterRequestDTO;
import com.library.entity.User;
import com.library.services.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/registernormaluser")
	public ResponseEntity<User> registerNormalUser(@RequestBody RegisterRequestDTO registerRequestDTO) {

		return ResponseEntity.ok(authenticationService.registerNormalUser(registerRequestDTO));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

		return ResponseEntity.ok(authenticationService.loginRequestDTO(loginRequestDTO));
	}

}
