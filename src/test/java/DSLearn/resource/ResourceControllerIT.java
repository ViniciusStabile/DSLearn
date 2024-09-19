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

import DSLearn.DTO.OfferMinDTO;
import DSLearn.DTO.ResourceDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ResourceControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalResources;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalResources = 2L;
	}

	@Test
	public void findAllShouldReturnSortedPageWhenPageSortByTitle() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/resources?page=0&size=12&sort=title,asc").accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalResources));
		result.andExpect(jsonPath("$.content[0].title").exists());
	}

	@Test
	public void findByIdShouldReturnResourceWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/resources/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.title").exists());
		result.andExpect(jsonPath("$.type").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/resources/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnResourceDTOCreated() throws Exception {
		OfferMinDTO offerMinDTO = new OfferMinDTO(1L, "Offer Name");
		ResourceDTO resourceDTO = new ResourceDTO(null, "New Resource", "Resource Description", 1,
				"http://imageurl.com/img.png", DSLearn.entities.enums.ResourceType.LESSON_ONLY, offerMinDTO);
		String jsonBody = objectMapper.writeValueAsString(resourceDTO);

		ResultActions result = mockMvc.perform(post("/resources").content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.title").value("New Resource"));
	}

	@Test
	public void updateShouldReturnResourceDTOWhenIdExists() throws Exception {
		OfferMinDTO offerMinDTO = new OfferMinDTO(1L, "Updated Offer");
		ResourceDTO resourceDTO = new ResourceDTO(existingId, "Updated Resource", "Updated Description", 2,
				"http://newimageurl.com/img.png", DSLearn.entities.enums.ResourceType.LESSON_ONLY, offerMinDTO);
		String jsonBody = objectMapper.writeValueAsString(resourceDTO);

		ResultActions result = mockMvc.perform(put("/resources/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.title").value("Updated Resource"));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		OfferMinDTO offerMinDTO = new OfferMinDTO(1L, "Updated Offer");
		ResourceDTO resourceDTO = new ResourceDTO(nonExistingId, "Updated Resource", "Updated Description", 2,
				"http://newimageurl.com/img.png", DSLearn.entities.enums.ResourceType.LESSON_ONLY, offerMinDTO);
		String jsonBody = objectMapper.writeValueAsString(resourceDTO);

		ResultActions result = mockMvc.perform(put("/resources/{id}", nonExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/resources/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/resources/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
}
