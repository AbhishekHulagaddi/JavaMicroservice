package com.rim.auth.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.rim.auth.domain.User;
@Repository
public interface AuthUserRepo extends JpaRepository<User, UUID>,JpaSpecificationExecutor<User> {
	
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    List<User> findByRole_RoleIdAndActiveTrue(UUID roleId);

}
