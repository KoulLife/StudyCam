package com.koul.StudyCam.friend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.koul.StudyCam.friend.domain.Friend;
import com.koul.StudyCam.user.domain.User;

public interface FriendRepository extends JpaRepository<Friend, Long> {

	boolean existsByUserAndFriend(User user, User friend);

	@Query("SELECT f FROM Friend f WHERE f.user.id = :id AND f.friendStatus = 'ACCEPT'")
	Page<Friend> findAllByUserId(@Param("id") Long id, Pageable pageable);

	void deleteByUserAndFriendOrUserAndFriend(User user1, User friend1, User user2, User friend2);

	@Query("""
 		SELECT f
 		FROM Friend f
 		WHERE f.user.id = :userId AND f.friendStatus = :keyword
	""")
	Page<Friend> searchFriendsByUserId(
		@Param("userId") Long userId,
		@Param("keyword") String keyword, Pageable pageable);

	Optional<Friend> findByUserAndFriendOrUserAndFriend(User user1, User friend1, User user2, User friend2);
}
