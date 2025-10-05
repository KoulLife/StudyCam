package com.koul.StudyCam.User.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyCam.User.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
