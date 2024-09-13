package DSLearn.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import DSLearn.DTO.OfferMinDTO;
import DSLearn.DTO.ResourceDTO;
import DSLearn.entities.enums.ResourceType;
import DSLearn.resources.ResourceController;
import DSLearn.services.ResourceService;
import DSLearn.services.exceptions.ResourceNotFoundException;

@WebMvcTest(ResourceController.class)
public class ResourceControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ResourceService service;

	@Autowired
	private ObjectMapper objectMapper;

	private ResourceDTO resourceDTO;
	private PageImpl<ResourceDTO> page;

	private Long existingId;
	private Long nonExistingId;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;

		resourceDTO = new ResourceDTO(existingId, "Sample Title", "Sample Description", 1,
				"http://example.com/image.png", ResourceType.LESSON_ONLY, new OfferMinDTO(1L, "Sample Offer"));

		page = new PageImpl<>(java.util.List.of(resourceDTO));

		Mockito.when(service.findAll(any(Pageable.class))).thenReturn(page);

		Mockito.when(service.findById(existingId)).thenReturn(resourceDTO);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		Mockito.when(service.insert(any())).thenReturn(resourceDTO);

		Mockito.when(service.update(eq(existingId), any())).thenReturn(resourceDTO);
		Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

		Mockito.doNothing().when(service).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		mockMvc.perform(get("/resources").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id").value(existingId))
				.andExpect(jsonPath("$.content[0].title").value("Sample Title"))
				.andExpect(jsonPath("$.content[0].description").value("Sample Description"))
				.andExpect(jsonPath("$.content[0].position").value(1))
				.andExpect(jsonPath("$.content[0].imgUrl").value("http://example.com/image.png"))
				.andExpect(jsonPath("$.content[0].type").value("LESSON_ONLY"));

	}

	@Test
	public void findByIdShouldReturnResourceWhenIdExists() throws Exception {
		mockMvc.perform(get("/resources/{id}", existingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(existingId))
				.andExpect(jsonPath("$.title").value("Sample Title"))
				.andExpect(jsonPath("$.description").value("Sample Description"))
				.andExpect(jsonPath("$.position").value(1))
				.andExpect(jsonPath("$.imgUrl").value("http://example.com/image.png"))
				.andExpect(jsonPath("$.type").value("LESSON_ONLY")).andExpect(jsonPath("$.offer.id").value(1L));
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		mockMvc.perform(get("/resources/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnCreatedAndResourceDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(resourceDTO);

		mockMvc.perform(post("/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.title").value("Sample Title"))
				.andExpect(jsonPath("$.description").value("Sample Description"))
				.andExpect(jsonPath("$.position").value(1))
				.andExpect(jsonPath("$.imgUrl").value("http://example.com/image.png"))
				.andExpect(jsonPath("$.type").value("LESSON_ONLY")).andExpect(jsonPath("$.offer.id").value(1L));

	}

	@Test
	public void updateShouldReturnResourceDTOWhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(resourceDTO);

		mockMvc.perform(put("/resources/{id}", existingId).content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.title").value("Sample Title"))
				.andExpect(jsonPath("$.description").value("Sample Description"))
				.andExpect(jsonPath("$.position").value(1))
				.andExpect(jsonPath("$.imgUrl").value("http://example.com/image.png"))
				.andExpect(jsonPath("$.type").value("LESSON_ONLY")).andExpect(jsonPath("$.offer.id").value(1L));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(resourceDTO);

		mockMvc.perform(put("/resources/{id}", nonExistingId).content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		mockMvc.perform(delete("/resources/{id}", existingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		mockMvc.perform(delete("/resources/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
