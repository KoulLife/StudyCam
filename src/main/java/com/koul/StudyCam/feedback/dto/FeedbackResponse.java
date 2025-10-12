package com.koul.StudyCam.feedback.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponse {
	private Long id;
	private String content;
	private boolean isDone;
	private LocalDateTime createdAt;
	private String username;
}
