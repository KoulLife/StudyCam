package com.koul.StudyCam.common.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonPageResponse<T> {
	private boolean success;
	private String message;
	private List<T> content;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private boolean hasNext;
	private boolean hasPrevious;

	public static <T> CommonPageResponse<T> from(Page<T> pageData) {
		return CommonPageResponse.<T>builder()
			.success(true)
			.message("success")
			.content(pageData.getContent())
			.page(pageData.getNumber())
			.size(pageData.getSize())
			.totalElements(pageData.getTotalElements())
			.totalPages(pageData.getTotalPages())
			.hasNext(pageData.hasNext())
			.hasPrevious(pageData.hasPrevious())
			.build();
	}
}
