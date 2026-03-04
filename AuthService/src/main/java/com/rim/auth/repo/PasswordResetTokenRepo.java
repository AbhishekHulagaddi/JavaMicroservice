package com.rim.auth.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rim.auth.domain.PasswordResetToken;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, UUID>{

	List<PasswordResetToken> findByEmailAndUsedFalse(String email);

}
