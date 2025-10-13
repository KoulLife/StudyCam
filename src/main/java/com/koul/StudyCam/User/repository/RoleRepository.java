package com.koul.StudyCam.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyCam.user.domain.ERole;
import com.koul.StudyCam.user.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
