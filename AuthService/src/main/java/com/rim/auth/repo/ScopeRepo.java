package com.rim.auth.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rim.auth.domain.Scope;
@Repository
public interface ScopeRepo extends JpaRepository<Scope, UUID>{

	boolean existsByScopeNameIgnoreCase(String scopeName);

	Optional<Scope> findByScopeNameIgnoreCase(String newName);

}
