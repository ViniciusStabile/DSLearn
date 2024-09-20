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

import DSLearn.DTO.ResourceMinDTO;
import DSLearn.DTO.SectionDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SectionResourceIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalSections;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalSections = 3L; 
	}

	@Test
	public void findAllShouldReturnSortedPageWhenPageSortByTitle() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/sections?page=0&size=12&sort=title,asc").accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalSections));
		result.andExpect(jsonPath("$.content[0].title").exists());
	}

	@Test
	public void findByIdShouldReturnSectionWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/sections/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.title").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform(get("/sections/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnSectionDTOCreated() throws Exception {
		ResourceMinDTO resourceMinDTO = new ResourceMinDTO(1L, "Resource Title");
		SectionDTO sectionDTO = new SectionDTO(null, "New Section", "New Description", 1, "http://imageurl.com",
				resourceMinDTO, null);
		String jsonBody = objectMapper.writeValueAsString(sectionDTO);

		ResultActions result = mockMvc.perform(post("/sections").content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.title").value("New Section"));
	}

	@Test
	public void updateShouldReturnSectionDTOWhenIdExists() throws Exception {
		ResourceMinDTO resourceMinDTO = new ResourceMinDTO(1L, "Updated Resource");
		SectionDTO sectionDTO = new SectionDTO(existingId, "Updated Section", "Updated Description", 2,
				"http://updatedimageurl.com", resourceMinDTO, null);
		String jsonBody = objectMapper.writeValueAsString(sectionDTO);

		ResultActions result = mockMvc.perform(put("/sections/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.title").value("Updated Section"));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResourceMinDTO resourceMinDTO = new ResourceMinDTO(1L, "Updated Resource");
		SectionDTO sectionDTO = new SectionDTO(nonExistingId, "Updated Section", "Updated Description", 2,
				"http://updatedimageurl.com", resourceMinDTO, null);
		String jsonBody = objectMapper.writeValueAsString(sectionDTO);

		ResultActions result = mockMvc.perform(put("/sections/{id}", nonExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/sections/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/sections/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
}
