package com.rim.auth.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rim.auth.domain.Permission;
import com.rim.auth.domain.Resource;
import com.rim.auth.domain.Role;
import com.rim.auth.domain.Scope;
@Repository
public interface PermissionRepo extends JpaRepository<Permission, UUID>{

	Optional<Permission> findByRoleAndResourceAndScopeAndActiveTrueAndPermissionStatusTrue(
	        Role role,
	        Resource resource,
	        Scope scope
	);

}
