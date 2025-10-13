package com.koul.StudyCam.feedback.service;

import com.koul.StudyCam.feedback.domain.Feedback;
import com.koul.StudyCam.feedback.dto.FeedbackRequest;
import com.koul.StudyCam.feedback.repository.FeedbackRepository;
import com.koul.StudyCam.user.domain.User;
import com.koul.StudyCam.user.repository.UserRepository;
import com.koul.StudyCam.user.utils.UserDetailsImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(FeedbackService.class)
class FeedbackServiceIntegrationTest {

	@Autowired private FeedbackService feedbackService;
	@Autowired private FeedbackRepository feedbackRepository;
	@Autowired private UserRepository userRepository;

	private User user;

	@BeforeEach
	void setup() {
		user = User.builder()
			.username("testUser")
			.password("1234")
			.email("test@test.com")
			.build();
		user = userRepository.save(user);

		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(
				new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), List.of()),
				null,
				List.of()
			);

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@DisplayName("오늘 피드백이 없으면 새로 생성된다")
	@Test
	void createFeedback_whenNoExistingFeedback_thenCreatesNewFeedback() {
		// given
		FeedbackRequest request = new FeedbackRequest();
		request.setContent("오늘의 피드백");

		// when
		Feedback result = feedbackService.createFeedback(request);

		// then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getContent()).isEqualTo("오늘의 피드백");
		assertThat(result.getUser().getId()).isEqualTo(user.getId());
		assertThat(feedbackRepository.findAll()).hasSize(1);
	}

	@DisplayName("오늘 피드백이 이미 있으면 기존 피드백이 수정된다")
	@Test
	void createFeedback_whenExistingFeedbackExists_thenUpdatesFeedback() {
		// given
		FeedbackRequest firstRequest = new FeedbackRequest();
		firstRequest.setContent("first content");

		feedbackService.createFeedback(firstRequest);

		// when
		FeedbackRequest secondRequest = new FeedbackRequest();
		secondRequest.setContent("second content");

		Feedback updated = feedbackService.createFeedback(secondRequest);

		// then
		List<Feedback> all = feedbackRepository.findAll();
		assertThat(all).hasSize(1);
		assertThat(updated.getContent()).isEqualTo("second content");
	}
}
