package com.koul.StudyCam.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonResponse<T> {
	private boolean success;
	private String message;
	private T data;

	public static <T> CommonResponse<T> ok(T data) {
		return CommonResponse.<T>builder()
			.success(true)
			.message("success")
			.data(data)
			.build();
	}

	public static <T> CommonResponse<T> fail(String message) {
		return CommonResponse.<T>builder()
			.success(false)
			.message(message)
			.build();
	}
}
