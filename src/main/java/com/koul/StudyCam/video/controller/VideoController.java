package com.koul.StudyCam.video.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.koul.StudyCam.video.service.VideoService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/video")
@RequiredArgsConstructor
@RestController
public class VideoController {

	@Value("${livekit.api.key}")
	private String apiKey = "study_cam_key";

	@Value("${livekit.api.secret}")
	private String apiSecret = "study_cam_secret_1234";

	private final VideoService videoService;

	@GetMapping("/token")
	public ResponseEntity<Map<String, String>> getToken(@RequestParam String room, @RequestParam String identity, @RequestParam String username) {
		String token = videoService.createAccessToken(apiKey, apiSecret, room, identity, username);
		return ResponseEntity.ok(Map.of("token", token));
	}

}
