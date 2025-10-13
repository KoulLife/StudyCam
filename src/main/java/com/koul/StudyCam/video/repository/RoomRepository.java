package com.koul.StudyCam.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyCam.video.domain.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
