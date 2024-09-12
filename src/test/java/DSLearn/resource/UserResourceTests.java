package DSLearn.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import DSLearn.DTO.UserDTO;
import DSLearn.resources.UserResource;
import DSLearn.services.UserService;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;

@WebMvcTest(UserResource.class)
public class UserResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService service;

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private UserDTO userDTO;
	private PageImpl<UserDTO> page;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;

		userDTO = new UserDTO(1L, "John Doe", "john.doe@example.com", "password123");
		page = new PageImpl<>(List.of(userDTO));

		when(service.findAll(any())).thenReturn(page);
		when(service.findById(existingId)).thenReturn(userDTO);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		when(service.update(eq(existingId), any())).thenReturn(userDTO);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

		when(service.insert(any())).thenReturn(userDTO);

		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependentId);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void findByIdShouldReturnUserWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/users/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.email").exists());
		result.andExpect(jsonPath("$.password").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		mockMvc.perform(get("/users/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnUserDTOCreated() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(userDTO);

		ResultActions result = mockMvc.perform(post("/users").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.email").exists());
		result.andExpect(jsonPath("$.password").exists());
	}

	@Test
	public void updateShouldReturnUserDTOWhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(userDTO);

		ResultActions result = mockMvc.perform(put("/users/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.email").exists());
		result.andExpect(jsonPath("$.password").exists());
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(userDTO);

		ResultActions result = mockMvc.perform(put("/users/{id}", nonExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/users/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform(delete("/users/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
}