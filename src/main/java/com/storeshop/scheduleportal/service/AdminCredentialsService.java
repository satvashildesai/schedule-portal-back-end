package com.storeshop.scheduleportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storeshop.scheduleportal.entity.AdminCredentials;
import com.storeshop.scheduleportal.repository.AdminCredentialsRepository;

@Service
public class AdminCredentialsService {

	@Autowired
	AdminCredentialsRepository credentialsRepo;

	public AdminCredentials findByUsername(String username) {
		return credentialsRepo.findByUsername(username);
	}

}
