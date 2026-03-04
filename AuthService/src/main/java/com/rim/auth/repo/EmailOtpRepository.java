package com.rim.auth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rim.auth.domain.EmailOtp;

@Repository
public interface EmailOtpRepository extends JpaRepository<EmailOtp, String> {

    Optional<EmailOtp> findTopByEmailOrderByExpiryTimeDesc(String email);
}
