package com.rim.auth.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rim.auth.domain.Resource;


@Repository
public interface ResourceRepo extends JpaRepository<Resource, UUID>{

	boolean existsByResourceNameIgnoreCase(String resourceName);

	Optional<Resource> findByResourceNameIgnoreCase(String newName);

}
