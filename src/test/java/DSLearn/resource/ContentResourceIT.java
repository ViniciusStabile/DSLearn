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

import DSLearn.DTO.ContentDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ContentResourceIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalContents;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalContents = 3L;
	}

	@Test
	public void findAllShouldReturnSortedPageWhenPageSortByTitle() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/contents?page=0&size=12&sort=title,asc").accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalContents));
		result.andExpect(jsonPath("$.content[0].title").exists());
	}

	@Test
	public void findByIdShouldReturnContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/contents/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.title").exists());
		result.andExpect(jsonPath("$.textContent").exists());
		result.andExpect(jsonPath("$.videoUri").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform(get("/contents/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnContentDTOCreated() throws Exception {
		ContentDTO contentDTO = new ContentDTO(null, "New Content", 1, "Sample Text Content", "http://videouri.com");
		String jsonBody = objectMapper.writeValueAsString(contentDTO);

		ResultActions result = mockMvc.perform(post("/contents").content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.title").value("New Content"));
	}

	@Test
	public void updateShouldReturnContentDTOWhenIdExists() throws Exception {
		ContentDTO contentDTO = new ContentDTO(existingId, "Updated Content", 2, "Updated Text Content",
				"http://updatedvideouri.com");
		String jsonBody = objectMapper.writeValueAsString(contentDTO);

		ResultActions result = mockMvc.perform(put("/contents/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.title").value("Updated Content"));
		result.andExpect(jsonPath("$.textContent").value("Updated Text Content"));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ContentDTO contentDTO = new ContentDTO(nonExistingId, "Updated Content", 2, "Updated Text Content",
				"http://updatedvideouri.com");
		String jsonBody = objectMapper.writeValueAsString(contentDTO);

		ResultActions result = mockMvc.perform(put("/contents/{id}", nonExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/contents/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/contents/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
}
