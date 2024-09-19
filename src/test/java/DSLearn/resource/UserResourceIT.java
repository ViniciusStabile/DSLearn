package DSLearn.resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import DSLearn.DTO.UserDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserResourceIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Long existingId;
	private Long nonExistingId;
	private UserDTO userDTO;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		userDTO = new UserDTO(existingId, "Alex","alex@gmail.com","Password");

	}

	@Test
	public void findByIdShouldReturnUserWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/users/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("Alex"));
		result.andExpect(jsonPath("$.email").value("alex@gmail.com"));
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform(get("/users/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnUserDTOCreated() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(userDTO);

		ResultActions result = mockMvc.perform(post("/users").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("Alex"));
		result.andExpect(jsonPath("$.email").value("alex@gmail.com"));
	}

	@Test
	public void updateShouldReturnUserDTOWhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(userDTO);

		ResultActions result = mockMvc.perform(put("/users/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("Alex"));
		result.andExpect(jsonPath("$.email").value("alex@gmail.com"));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
 
		userDTO.setId(nonExistingId);
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
