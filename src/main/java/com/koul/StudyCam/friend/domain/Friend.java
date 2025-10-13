package com.koul.StudyCam.friend.domain;

import java.time.LocalDateTime;

import com.koul.StudyCam.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "friend")
@Getter
@Entity
public class Friend {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "friend_id", nullable = false)
	private User friend;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private FriendStatus friendStatus;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Builder
	public Friend(User user, User friend, FriendStatus friendStatus) {
		this.user = user;
		this.friend = friend;
		this.friendStatus = friendStatus;
	}

	public void updateStatus(FriendStatus newStatus) {
		this.friendStatus = newStatus;
	}

}
