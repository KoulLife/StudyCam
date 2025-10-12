package com.koul.StudyCam.feedback.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.koul.StudyCam.common.dto.CommonPageResponse;
import com.koul.StudyCam.common.dto.CommonRequest;
import com.koul.StudyCam.common.dto.CommonResponse;
import com.koul.StudyCam.feedback.domain.Feedback;
import com.koul.StudyCam.feedback.dto.FeedbackRequest;
import com.koul.StudyCam.feedback.dto.FeedbackResponse;
import com.koul.StudyCam.feedback.service.FeedbackService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/feedback")
@RequiredArgsConstructor
@RestController
public class FeedbackController {

	private final FeedbackService feedbackService;

	@PostMapping("")
	public ResponseEntity<CommonResponse<Feedback>> createFeedback(@RequestBody FeedbackRequest feedbackRequest) {
		return ResponseEntity.ok(CommonResponse.ok(feedbackService.createFeedback(feedbackRequest)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CommonResponse<Feedback>> updateFeedback(@PathVariable Long id, @RequestBody FeedbackRequest feedbackRequest) {
		return ResponseEntity.ok(CommonResponse.ok(feedbackService.updateFeedback(id, feedbackRequest)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponse<String>> deleteFeedback(@PathVariable Long id) {
		feedbackService.deleteFeedback(id);

		return ResponseEntity.ok(CommonResponse.ok("Feedback deleted successfully"));
	}

	@GetMapping("/today")
	public ResponseEntity<CommonResponse<Feedback>> getTodayFeedback() {
		return ResponseEntity.ok(CommonResponse.ok(feedbackService.getTodayFeedback()));
	}

	@GetMapping("/all")
	public ResponseEntity<CommonPageResponse<FeedbackResponse>> getAllFeedback(
		@RequestParam(defaultValue = "") String searchKeyword,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String sort
	) {
		CommonRequest request = new CommonRequest();
		request.setSearchKeyword(searchKeyword);
		request.setPage(page);
		request.setSize(size);
		request.setSort(sort);

		Page<FeedbackResponse> pageData = feedbackService.getAllFeedback(request);
		return ResponseEntity.ok(CommonPageResponse.from(pageData));
	}

}
