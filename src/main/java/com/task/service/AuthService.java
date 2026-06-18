package com.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.task.dto.AuthRequest;
import com.task.dto.AuthResponse;
import com.task.dto.RegisterRequest;
import com.task.entity.User;
import com.task.repository.UserRepository;
import com.task.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	
	

	public String register(RegisterRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new IllegalArgumentException("Username already taken");
		}
		User user = User.builder().username(request.getUsername()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).build();
		userRepository.save(user);
		return "User registered successfully";
	}

	public AuthResponse login(AuthRequest request) {
		User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Invalid username or password");
		}
		String token = jwtUtil.generateToken(user.getUsername());
		return new AuthResponse(token, user.getUsername());
	}

	
}
