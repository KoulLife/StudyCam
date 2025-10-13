package com.koul.StudyCam.user.domain;

import java.util.HashSet;
import java.util.Set;

import com.koul.StudyCam.friend.domain.Friend;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Getter
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_name", nullable = false, unique = true)
	private String username;

	@Column(name = "password", nullable = false, unique = true)
	private String password;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_role",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserCategory> userCategories = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Friend> friends = new HashSet<>();

	@OneToMany(mappedBy = "friend", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Friend> followers = new HashSet<>();

	@Builder
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public void updateRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
