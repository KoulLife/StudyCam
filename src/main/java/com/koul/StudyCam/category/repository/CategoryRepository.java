package com.koul.StudyCam.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyCam.category.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findById(Long id);
}
