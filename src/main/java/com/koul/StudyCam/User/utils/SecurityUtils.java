package com.koul.StudyCam.user.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
	
	public static Long getCurrentUserId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsImpl userDetails) {
			return userDetails.getId();
		}
		throw new IllegalStateException("No authenticated user found");
	}
}
