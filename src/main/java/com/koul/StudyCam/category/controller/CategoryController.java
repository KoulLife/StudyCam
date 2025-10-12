package com.koul.StudyCam.category.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.koul.StudyCam.category.domain.Category;
import com.koul.StudyCam.category.dto.CategoryRequest;
import com.koul.StudyCam.category.service.CategoryService;
import com.koul.StudyCam.common.dto.CommonResponse;

import lombok.RequiredArgsConstructor;

@RequestMapping("/category")
@RequiredArgsConstructor
@RestController
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping("")
	public ResponseEntity<CommonResponse<List<Category>>> getAllCategories() {
		return ResponseEntity.ok(CommonResponse.ok(categoryService.getAllCategories()));
	}

	@PostMapping("")
	public ResponseEntity<CommonResponse<Category>> createCategory(@RequestBody CategoryRequest categoryRequest) {
		return ResponseEntity.ok(CommonResponse.ok(categoryService.createCategory(categoryRequest)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CommonResponse<Category>> updateCategory(
		@PathVariable Long id,
		@RequestBody CategoryRequest categoryRequest) {
		return ResponseEntity.ok(CommonResponse.ok(categoryService.updateCategory(id, categoryRequest)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponse<String>> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.ok(CommonResponse.ok("Category deleted successfully"));
	}

}
