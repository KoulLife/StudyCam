package com.koul.StudyCam.category.domain;

import java.util.HashSet;
import java.util.Set;

import com.koul.StudyCam.user.domain.UserCategory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
@Getter
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "category_name", nullable = false)
	private ECategory categoryName;

	@Column(name = "description", length = 200)
	private String description;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserCategory> userCategories = new HashSet<>();

	@Builder
	public Category(ECategory categoryName, String description) {
		this.categoryName = categoryName;
		this.description = description;
	}

	public void update(ECategory categoryName, String description) {
		this.categoryName = categoryName;
		this.description = description;
	}
}
