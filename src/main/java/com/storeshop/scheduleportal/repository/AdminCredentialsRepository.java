package com.storeshop.scheduleportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storeshop.scheduleportal.entity.AdminCredentials;

@Repository
public interface AdminCredentialsRepository extends JpaRepository<AdminCredentials, Long> {

	AdminCredentials findByUsername(String username);
}
