package com.koul.StudyCam.friend.service;

import com.koul.StudyCam.friend.domain.Friend;
import com.koul.StudyCam.friend.domain.FriendStatus;
import com.koul.StudyCam.friend.dto.FriendResponse;
import com.koul.StudyCam.friend.repository.FriendRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(FriendService.class)
@Transactional
class FriendServiceIntegrationTest {

	@Autowired private FriendService friendService;
	@Autowired private FriendRepository friendRepository;
	@Autowired private UserRepository userRepository;

	private User userA;
	private User userB;

	@BeforeEach
	void setup() {
		// given
		userA = User.builder()
			.username("userA")
			.password("PassA123!")
			.email("userA@test.com")
			.build();
		userRepository.save(userA);

		userB = User.builder()
			.username("userB")
			.password("PassB123!")
			.email("userB@test.com")
			.build();
		userRepository.save(userB);

		// mock current login user as userA
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(
				new UserDetailsImpl(userA.getId(), userA.getUsername(), userA.getEmail(), userA.getPassword(), List.of()),
				null,
				List.of()
			);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@DisplayName("Should send a friend request successfully")
	@Test
	void sendFriendRequest_success() {
		// when
		FriendResponse response = friendService.sendFriendRequest(userB.getId());

		// then
		assertThat(response).isNotNull();
		assertThat(response.getFriend()).isEqualTo("userB");

		Friend saved = friendRepository.findAll().get(0);
		assertThat(saved.getFriendStatus()).isEqualTo(FriendStatus.PENDING);
	}

	@DisplayName("Should update status to ACCEPTED when friend request is approved")
	@Test
	void handleFriendRequest_accept() {
		// given
		friendService.sendFriendRequest(userB.getId());

		// switch authentication → userB (receiver)
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(
				new UserDetailsImpl(userB.getId(), userB.getUsername(), userB.getEmail(), userB.getPassword(), List.of()),
				null,
				List.of()
			);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// when
		friendService.handleFriendRequest(userA.getId(), true);

		// then
		Friend relation = friendRepository.findAll().get(0);
		assertThat(relation.getFriendStatus()).isEqualTo(FriendStatus.ACCEPTED);
	}

	@DisplayName("Should update status to BLOCKED when friend request is rejected")
	@Test
	void handleFriendRequest_reject() {
		// given
		friendService.sendFriendRequest(userB.getId());

		// switch authentication → userB
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(
				new UserDetailsImpl(userB.getId(), userB.getUsername(), userB.getEmail(), userB.getPassword(), List.of()),
				null,
				List.of()
			);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// when
		friendService.handleFriendRequest(userA.getId(), false);

		// then
		Friend relation = friendRepository.findAll().get(0);
		assertThat(relation.getFriendStatus()).isEqualTo(FriendStatus.BLOCKED);
	}

	@DisplayName("Should delete existing friend relationship successfully")
	@Test
	void deleteFriend_success() {
		// given
		friendService.sendFriendRequest(userB.getId());

		// switch authentication → userA
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(
				new UserDetailsImpl(userA.getId(), userA.getUsername(), userA.getEmail(), userA.getPassword(), List.of()),
				null,
				List.of()
			);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// when
		friendService.deleteFriend(userB.getId());

		// then
		assertThat(friendRepository.findAll()).isEmpty();
	}

	@DisplayName("Should throw exception when deleting non-existing friend relationship")
	@Test
	void deleteFriend_notFound_throwsException() {
		// when / then
		assertThatThrownBy(() -> friendService.deleteFriend(userB.getId()))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("Friend relationship not found");
	}
}
