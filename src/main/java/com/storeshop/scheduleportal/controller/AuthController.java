package com.storeshop.scheduleportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.storeshop.scheduleportal.dto.JwtRequestDto;
import com.storeshop.scheduleportal.exceptions.PasswordMismatchException;
import com.storeshop.scheduleportal.security.JwtHelper;
import com.storeshop.scheduleportal.util.SuccessResponse;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class AuthController {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtHelper jwtHelper;

	@PostMapping("/login")
	public Object login(@Valid @RequestBody JwtRequestDto loginRequest) throws PasswordMismatchException {

		UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

		if (!userDetails.getPassword().equals(loginRequest.getPassword())) {
			throw new PasswordMismatchException("Your password is wrong, please enter valid password!!");
		}

		String token = jwtHelper.generateToken(userDetails);

		List<Object> tokenData = new ArrayList<>();
		tokenData.add(token);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse("200", "SUCCESS", "User authenticate successfully.", tokenData));
	}

	@GetMapping("/portal/validate")
	public Object tokenCheck() {
		return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("200", "SUCCESS", "Valid token."));
	}
}
