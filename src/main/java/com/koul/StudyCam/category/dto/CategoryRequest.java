package com.koul.StudyCam.category.dto;

import com.koul.StudyCam.category.domain.ECategory;

import lombok.Data;

@Data
public class CategoryRequest {
	private ECategory categoryName;
	private String description;
}
