package com.koul.StudyCam.video.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.koul.StudyCam.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "room")
@Getter
@Entity
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	@Column(name = "room_name", nullable = false)
	private String roomName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "host_user_id", nullable = false)
	private User hostUser;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "room_user",
		joinColumns = @JoinColumn(name = "room_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private Set<User> participants = new HashSet<>();

	@Column(name = "is_active")
	private boolean isActive;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Builder
	public Room(String roomName, User hostUser, boolean isActive, LocalDateTime createdAt) {
		this.roomName = roomName;
		this.hostUser = hostUser;
		this.isActive = isActive;
		this.createdAt = createdAt;
	}
}
