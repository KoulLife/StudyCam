package com.koul.StudyCam.feedback.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.koul.StudyCam.feedback.domain.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

	Optional<Feedback> findById(Long id);

	@Query("SELECT f FROM Feedback f " +
		"WHERE f.user.id = :userId " +
		"AND f.createdAt BETWEEN :start AND :end")
	Optional<Feedback> findByUserAndDateRange(
		@Param("userId") Long userId,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);

	@Query("SELECT f FROM Feedback f WHERE f.user.id = :id")
	Page<Feedback> findAllById(@Param("id") Long id, Pageable pageable);
}
