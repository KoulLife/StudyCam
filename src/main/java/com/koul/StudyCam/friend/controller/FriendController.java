package com.koul.StudyCam.friend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.koul.StudyCam.common.dto.CommonPageResponse;
import com.koul.StudyCam.common.dto.CommonRequest;
import com.koul.StudyCam.common.dto.CommonResponse;
import com.koul.StudyCam.friend.dto.FriendResponse;
import com.koul.StudyCam.friend.service.FriendService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/friend")
@RestController
public class FriendController {

	private final FriendService friendService;

	// Get All Friend
	@GetMapping("/all")
	public ResponseEntity<CommonPageResponse<FriendResponse>> searchFriend(
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

		Page<FriendResponse> pageData = friendService.searchFriend(request);
		return ResponseEntity.ok(CommonPageResponse.from(pageData));
	}

	// Delete Friend
	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponse<String>> deleteFriend(
		@PathVariable Long id
	) {
		friendService.deleteFriend(id);
		return ResponseEntity.ok(CommonResponse.ok("Success Delete Friend."));
	}

	// Add Friend
	@PostMapping("/{friendId}")
	public ResponseEntity<CommonResponse<FriendResponse>> addFriend(@PathVariable Long friendId) {
		return ResponseEntity.ok(CommonResponse.ok(friendService.sendFriendRequest(friendId)));
	}

	// Check Friend Status
	@GetMapping("")
	public ResponseEntity<CommonPageResponse<FriendResponse>> getFriendByFriendStatus(
		@RequestParam(defaultValue = "ACCEPTED") String searchKeyword,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String sort
	) {
		CommonRequest request = new CommonRequest();
		request.setSearchKeyword(searchKeyword);
		request.setPage(page);
		request.setSize(size);
		request.setSort(sort);

		Page<FriendResponse> pageData = friendService.getFriendByFriendStatus(request);
		return ResponseEntity.ok(CommonPageResponse.from(pageData));
	}

	//
	// Accept or Reject Request
	@PutMapping("/{friendId}/request")
	public ResponseEntity<CommonResponse<String>> handleFriendRequest(
		@PathVariable Long friendId,
		@RequestParam boolean accept
	) {
		friendService.handleFriendRequest(friendId, accept);

		return ResponseEntity.ok(CommonResponse.ok("Friend request processed successfully"));
	}

}
