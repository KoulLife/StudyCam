package com.koul.StudyCam.common.dto;

import java.util.Map;

import lombok.Data;

@Data
public class CommonRequest {
	private String searchKeyword;
	private int page = 0;
	private int size = 10;
	private String sort = "createdAt,desc";
}
