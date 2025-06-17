package com.library.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.dto.LoginRequestDTO;
import com.library.dto.LoginResponseDTO;
import com.library.dto.RegisterRequestDTO;
import com.library.entity.User;
import com.library.jwt.JwtService;
import com.library.repository.UserRepository;

@Service
public class AuthenticationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public User registerNormalUser(RegisterRequestDTO registerRequestDTO) {

		
		if (registerRequestDTO.getPassword() == null || registerRequestDTO.getPassword().isBlank()) {
	        throw new IllegalArgumentException("Password cannot be null or empty");
	    }
		
		if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
			throw new RuntimeException("User already registered");

		}

		Set<String> roles = new HashSet<String>();
		roles.add("ROLE_USER");

		User user = new User();
		user.setUsername(registerRequestDTO.getUsername());
		user.setEmail(registerRequestDTO.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
		user.setRoles(roles);
		return userRepository.save(user);

	}

	public LoginResponseDTO loginRequestDTO(LoginRequestDTO loginRequestDTO) {

		
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

		User user = userRepository.findByUsername(loginRequestDTO.getUsername())
				.orElseThrow(() -> new RuntimeException("User Not Found"));

		String token = jwtService.generateToken(user);

		return LoginResponseDTO.builder()
				.token(token)
				.username(user.getUsername())
				.roles(user.getRoles())
				.build();
	}

	public User registerAdminUser(RegisterRequestDTO registerRequestDTO) {

		if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
			throw new RuntimeException("User already registered");

		}

		Set<String> roles = new HashSet<String>();
		roles.add("ROLE_ADMIN");
		roles.add("ROLE_USER");

		User user = new User();
		user.setUsername(registerRequestDTO.getUsername());
		user.setEmail(registerRequestDTO.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
		user.setRoles(roles);
		return userRepository.save(user);

	}

}
