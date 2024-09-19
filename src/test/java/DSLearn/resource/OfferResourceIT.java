package DSLearn.resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

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

import DSLearn.DTO.CourseMinDTO;
import DSLearn.DTO.OfferDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OfferResourceIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalOffers;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalOffers = 2L; 
	}

	@Test
	public void findAllShouldReturnSortedPageWhenPageSortByEdition() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/offers?page=0&size=12&sort=edition,asc").accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalOffers));
		result.andExpect(jsonPath("$.content[0].edition").exists());
	}

	@Test
	public void findByIdShouldReturnOfferWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/offers/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.edition").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform(get("/offers/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnOfferDTOCreated() throws Exception {
		CourseMinDTO courseMinDTO = new CourseMinDTO(1L, "Course Name");
		OfferDTO offerDTO = new OfferDTO(null, "New Edition", Instant.now(), Instant.now().plusSeconds(86400),
				courseMinDTO);
		String jsonBody = objectMapper.writeValueAsString(offerDTO);

		ResultActions result = mockMvc.perform(post("/offers").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.edition").value("New Edition"));
	}

	@Test
	public void updateShouldReturnOfferDTOWhenIdExists() throws Exception {
		CourseMinDTO courseMinDTO = new CourseMinDTO(1L, "Updated Course");
		OfferDTO offerDTO = new OfferDTO(existingId, "Updated Edition", Instant.now(), Instant.now().plusSeconds(86400),
				courseMinDTO);
		String jsonBody = objectMapper.writeValueAsString(offerDTO);

		ResultActions result = mockMvc.perform(put("/offers/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.edition").value("Updated Edition"));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		CourseMinDTO courseMinDTO = new CourseMinDTO(1L, "Updated Course");
		OfferDTO offerDTO = new OfferDTO(nonExistingId, "Updated Edition", Instant.now(),
				Instant.now().plusSeconds(86400), courseMinDTO);
		String jsonBody = objectMapper.writeValueAsString(offerDTO);

		ResultActions result = mockMvc.perform(put("/offers/{id}", nonExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/offers/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/offers/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
}
