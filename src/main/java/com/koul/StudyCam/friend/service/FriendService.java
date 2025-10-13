package com.koul.StudyCam.friend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koul.StudyCam.common.dto.CommonRequest;
import com.koul.StudyCam.friend.domain.Friend;
import com.koul.StudyCam.friend.domain.FriendStatus;
import com.koul.StudyCam.friend.dto.FriendResponse;
import com.koul.StudyCam.friend.repository.FriendRepository;
import com.koul.StudyCam.user.domain.User;
import com.koul.StudyCam.user.repository.UserRepository;
import com.koul.StudyCam.user.utils.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FriendService {

	private final UserRepository userRepository;

	private final FriendRepository friendRepository;

	public Page<FriendResponse> searchFriend(CommonRequest request) {

		Long userId = SecurityUtils.getCurrentUserId();

		String[] sortParams = request.getSort().split(",");
		String sortField = sortParams[0];
		Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
			? Sort.Direction.ASC
			: Sort.Direction.DESC;

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(direction, sortField));

		Page<Friend> friendPage = friendRepository.findAllByUserId(userId, pageable);

		return friendPage.map(friend -> FriendResponse.builder()
			.id(friend.getId())
			.friend(friend.getFriend().getUsername())
			.createdAt(friend.getCreatedAt())
			.build());
	}
	public Page<FriendResponse> getFriendByFriendStatus(CommonRequest request) {

		Long userId = SecurityUtils.getCurrentUserId();

		String[] sortParams = request.getSort().split(",");
		String sortField = sortParams[0];
		Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
			? Sort.Direction.ASC
			:Sort.Direction.DESC;

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(direction, sortField));

		String keword = request.getSearchKeyword();

		Page<Friend> friendPage = friendRepository.searchFriendsByUserId(userId, keword, pageable);

		return friendPage.map(friend -> FriendResponse.builder()
			.id(friend.getId())
			.friend(friend.getFriend().getUsername())
			.createdAt(friend.getCreatedAt())
			.build());
	}

	public void handleFriendRequest(Long friendId, boolean accept) {

		Long userId = SecurityUtils.getCurrentUserId();

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found"));

		User target = userRepository.findById(friendId)
			.orElseThrow(() -> new RuntimeException("Target not found"));

		Friend friendRelation = friendRepository.findByUserAndFriendOrUserAndFriend(user, target, target, user)
			.orElseThrow(() -> new RuntimeException("Friend relationship not found"));

		FriendStatus setFriendStatus = (accept) ? FriendStatus.ACCEPTED : FriendStatus.BLOCKED;

		friendRelation.updateStatus(setFriendStatus);

		friendRepository.save(friendRelation);
	}

	@Transactional
	public FriendResponse sendFriendRequest(Long targetId) {

		Long userId = SecurityUtils.getCurrentUserId();

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found"));

		User target = userRepository.findById(targetId)
			.orElseThrow(() -> new RuntimeException("Target not found"));

		Friend friendRequest = Friend.builder()
			.user(user)
			.friend(target)
			.friendStatus(FriendStatus.PENDING)
			.build();

		Friend savedFriend = friendRepository.save(friendRequest);

		return FriendResponse.builder()
			.id(savedFriend.getId())
			.friend(savedFriend.getFriend().getUsername())
			.createdAt(savedFriend.getCreatedAt())
			.build();
	}

	@Transactional
	public void deleteFriend(Long targetId) {

		Long userId = SecurityUtils.getCurrentUserId();

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found"));

		User target = userRepository.findById(targetId)
			.orElseThrow(() -> new RuntimeException("Target not found"));

		boolean exists = friendRepository.existsByUserAndFriend(user, target)
			|| friendRepository.existsByUserAndFriend(target, user);

		if (!exists) {
			throw new RuntimeException("Friend relationship not found");
		}

		friendRepository.deleteByUserAndFriendOrUserAndFriend(user, target, target, user);
	}
}
