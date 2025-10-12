package com.koul.StudyCam.feedback.domain;

import java.time.LocalDateTime;

import com.koul.StudyCam.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "feedback")
@Entity
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "content")
	private String content;

	@Column(name = "is_done")
	private boolean isDone = false;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Builder
	public Feedback(String content, boolean isDone, User user, LocalDateTime createdAt) {
		this.content = content;
		this.isDone = isDone;
		this.createdAt = createdAt;
		this.user = user;
	}

	public void update(String content, LocalDateTime createdAt) {
		this.content = content;
		this.createdAt = createdAt;
	}

}
