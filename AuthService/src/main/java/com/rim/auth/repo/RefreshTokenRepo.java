package com.rim.auth.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rim.auth.domain.RefreshToken;
import com.rim.auth.domain.User;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, UUID> {

  //  Optional<RefreshToken> findByTokenAndRevokedFalse(String token);

//    void deleteByEmail(String email);

	Optional<RefreshToken> findByTokenAndRevokedFalse(String refreshToken);

	void deleteByUser(User user);
}
