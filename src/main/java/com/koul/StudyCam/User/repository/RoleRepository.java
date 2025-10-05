package com.koul.StudyCam.User.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyCam.User.domain.ERole;
import com.koul.StudyCam.User.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
