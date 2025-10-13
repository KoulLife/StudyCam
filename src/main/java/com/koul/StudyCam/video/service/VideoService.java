package com.koul.StudyCam.video.service;

import org.springframework.stereotype.Service;

import io.livekit.server.AccessToken;
import io.livekit.server.CanPublish;
import io.livekit.server.CanSubscribe;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;

@Service
public class VideoService {

	public String createAccessToken(String apiKey, String apiSecret, String room, String identity, String username) {
		AccessToken token = new AccessToken(apiKey, apiSecret);
		token.setName(username);
		token.setIdentity(identity);

		token.addGrants(
			new RoomJoin(true),
			new RoomName(room),
			new CanPublish(true),
			new CanSubscribe(true)
		);

		return token.toJwt();
	}

}
