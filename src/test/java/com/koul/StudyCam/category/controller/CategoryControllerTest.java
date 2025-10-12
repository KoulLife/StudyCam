package com.koul.StudyCam.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koul.StudyCam.category.domain.Category;
import com.koul.StudyCam.category.domain.ECategory;
import com.koul.StudyCam.category.dto.CategoryRequest;
import com.koul.StudyCam.category.repository.CategoryRepository;
import com.koul.StudyCam.user.domain.User;
import com.koul.StudyCam.user.repository.UserRepository;
import com.koul.StudyCam.user.utils.JwtUtils;
import com.koul.StudyCam.user.utils.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoryControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@Autowired private CategoryRepository categoryRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private JwtUtils jwtUtils;

	private String token;
	private CategoryRequest request;

	@BeforeEach
	void setup() {
		// Create User
		User user = User.builder()
			.username("testUser")
			.email("test@test.com")
			.password("1234")
			.build();
		userRepository.save(user);

		UserDetailsImpl userDetails = UserDetailsImpl.build(user);

		Authentication authentication =
			new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		token = jwtUtils.generateJwtToken(authentication);

		request = new CategoryRequest();
		request.setCategoryName(ECategory.DEVELOPMENT);
		request.setDescription("개발 관련 스터디");
	}

	@DisplayName("카테고리 생성")
	@Test
	void createCategory() throws Exception {
		mockMvc.perform(post("/category")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data.categoryName").value("DEVELOPMENT"))
			.andExpect(jsonPath("$.data.description").value("개발 관련 스터디"));
	}

	@DisplayName("카테고리 전체 조회 성공")
	@Test
	void getAllCategories() throws Exception {
		categoryRepository.saveAll(List.of(
			Category.builder()
				.categoryName(ECategory.DEVELOPMENT)
				.description("개발").build(),
			Category.builder()
				.categoryName(ECategory.DESIGN)
				.description("디자인").build()
		));

		mockMvc.perform(get("/category")
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data.length()").value(2))
			.andExpect(jsonPath("$.data[0].categoryName").exists());
	}

	@DisplayName("카테고리 수정")
	@Test
	void updateCategory() throws Exception {
		// given
		Category category = categoryRepository.save(Category.builder()
			.categoryName(ECategory.DEVELOPMENT)
			.description("Old description").build());

		CategoryRequest updateRequest = new CategoryRequest();
		updateRequest.setCategoryName(ECategory.DESIGN);
		updateRequest.setDescription("New description");

		// when / then
		mockMvc.perform(put("/category/{id}", category.getId())
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data.categoryName").value("DESIGN"))
			.andExpect(jsonPath("$.data.description").value("New description"));
	}

	@DisplayName("카테고리 삭제")
	@Test
	void deleteCategory() throws Exception {
		// given
		Category category = categoryRepository.save(Category.builder()
			.categoryName(ECategory.DEVELOPMENT)
			.description("삭제 테스트").build());

		// when / then
		mockMvc.perform(delete("/category/{id}", category.getId())
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true));

		// DB 확인
		org.assertj.core.api.Assertions.assertThat(categoryRepository.findById(category.getId())).isEmpty();
	}
}
