package com.koul.StudyCam.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koul.StudyCam.category.domain.Category;
import com.koul.StudyCam.category.dto.CategoryRequest;
import com.koul.StudyCam.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Transactional
	public Category createCategory(CategoryRequest request) {
		Category category = Category.builder()
			.categoryName(request.getCategoryName())
			.description(request.getDescription())
			.build();

		Category savedCategory = categoryRepository.save(category);
		return savedCategory;
	}

	@Transactional
	public Category updateCategory(Long id, CategoryRequest request) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Category not found"));

		category.update(request.getCategoryName(), request.getDescription());
		return category;
	}

	@Transactional
	public void deleteCategory(Long id) {
		categoryRepository.deleteById(id);
	}

}
