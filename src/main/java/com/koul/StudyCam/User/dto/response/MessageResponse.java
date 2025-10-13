package com.koul.StudyCam.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class MessageResponse {
	private String message;

	@Builder
	public MessageResponse(String message) {
		this.message = message;
	}
}
