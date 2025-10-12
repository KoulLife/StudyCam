package com.koul.StudyCam.feedback.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koul.StudyCam.common.dto.CommonRequest;
import com.koul.StudyCam.feedback.domain.Feedback;
import com.koul.StudyCam.feedback.dto.FeedbackRequest;
import com.koul.StudyCam.feedback.dto.FeedbackResponse;
import com.koul.StudyCam.feedback.repository.FeedbackRepository;
import com.koul.StudyCam.user.domain.User;
import com.koul.StudyCam.user.repository.UserRepository;
import com.koul.StudyCam.user.utils.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FeedbackService {

	private final FeedbackRepository feedbackRepository;

	private final UserRepository userRepository;

	@Transactional
	public Feedback createFeedback(FeedbackRequest request) {

		Long userId = SecurityUtils.getCurrentUserId();

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found"));

		LocalDate today = LocalDate.now();

		Feedback existingFeedback = feedbackRepository.findByUserAndDateRange(
			userId,
			today.atStartOfDay(),
			today.plusDays(1).atStartOfDay()
		).orElse(null);

		if (existingFeedback != null) {
			// Update Feedback
			existingFeedback.update(
				request.getContent(),
				LocalDateTime.now()
			);
			return existingFeedback;
		} else {
			// Create Feedback
			Feedback feedback = Feedback.builder()
				.content(request.getContent())
				.isDone(true)
				.createdAt(LocalDateTime.now())
				.user(user)
				.build();

			return feedbackRepository.save(feedback);
		}
	}

	@Transactional
	public Feedback updateFeedback(Long id, FeedbackRequest request) {
		Feedback feedback = feedbackRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("User not found"));
		feedback.update(request.getContent(), LocalDateTime.now());
		return feedback;
	}

	@Transactional
	public void deleteFeedback(Long id) {
		feedbackRepository.deleteById(id);
	}

	public Feedback getTodayFeedback() {

		Long userId = SecurityUtils.getCurrentUserId();

		LocalDate today = LocalDate.now();

		Feedback todayFeedback = feedbackRepository.findByUserAndDateRange(
			userId,
			today.atStartOfDay(),
			today.plusDays(1).atStartOfDay()
		).orElse(null);

		return todayFeedback;
	}

	public Page<FeedbackResponse> getAllFeedback(CommonRequest request) {

		Long userId = SecurityUtils.getCurrentUserId();

		String[] sortParams = request.getSort().split(",");
		String sortField = sortParams[0];
		Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
			? Sort.Direction.ASC
			: Sort.Direction.DESC;

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(direction, sortField));

		Page<Feedback> feedbackPage = feedbackRepository.findAllById(userId ,pageable);

		return feedbackPage.map(feedback -> FeedbackResponse.builder()
			.id(feedback.getId())
			.content(feedback.getContent())
			.isDone(feedback.isDone())
			.createdAt(feedback.getCreatedAt())
			.username(feedback.getUser().getUsername())
			.build());
	}

}
