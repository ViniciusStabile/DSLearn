package DSLearn.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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

import DSLearn.DTO.ResourceMinDTO;
import DSLearn.DTO.SectionDTO;
import DSLearn.resources.SectionResource;
import DSLearn.services.SectionService;
import DSLearn.services.exceptions.ResourceNotFoundException;

@WebMvcTest(SectionResource.class)
public class SectionResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SectionService service;

	@Autowired
	private ObjectMapper objectMapper;

	private SectionDTO sectionDTO;
	private PageImpl<SectionDTO> page;

	private Long existingId;
	private Long nonExistingId;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;

		sectionDTO = new SectionDTO(existingId, "Section Title", "Section Description", 1,
				"http://example.com/image.png", new ResourceMinDTO(1L, "Sample Resource"), null);

		page = new PageImpl<>(List.of(sectionDTO));

		Mockito.when(service.findAll(any(Pageable.class))).thenReturn(page);

		Mockito.when(service.findById(existingId)).thenReturn(sectionDTO);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		Mockito.when(service.insert(any())).thenReturn(sectionDTO);

		Mockito.when(service.update(eq(existingId), any())).thenReturn(sectionDTO);
		Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

		Mockito.doNothing().when(service).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		mockMvc.perform(get("/sections").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id").value(existingId))
				.andExpect(jsonPath("$.content[0].title").value("Section Title"))
				.andExpect(jsonPath("$.content[0].description").value("Section Description"))
				.andExpect(jsonPath("$.content[0].position").value(1))
				.andExpect(jsonPath("$.content[0].imgUrl").value("http://example.com/image.png"))
				.andExpect(jsonPath("$.content[0].resource.id").value(1L));
	}

	@Test
	public void findByIdShouldReturnSectionWhenIdExists() throws Exception {
		mockMvc.perform(get("/sections/{id}", existingId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.title").value("Section Title"))
				.andExpect(jsonPath("$.description").value("Section Description"))
				.andExpect(jsonPath("$.position").value(1))
				.andExpect(jsonPath("$.imgUrl").value("http://example.com/image.png"))
				.andExpect(jsonPath("$.resource.id").value(1L));
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		mockMvc.perform(get("/sections/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnCreatedAndSectionDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(sectionDTO);

		mockMvc.perform(post("/sections").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.title").value("Section Title"))
				.andExpect(jsonPath("$.description").value("Section Description"))
				.andExpect(jsonPath("$.position").value(1))
				.andExpect(jsonPath("$.imgUrl").value("http://example.com/image.png"))
				.andExpect(jsonPath("$.resource.id").value(1L));

	}

	@Test
	public void updateShouldReturnSectionDTOWhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(sectionDTO);

		mockMvc.perform(put("/sections/{id}", existingId).content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.title").value("Section Title"))
				.andExpect(jsonPath("$.description").value("Section Description"))
				.andExpect(jsonPath("$.position").value(1))
				.andExpect(jsonPath("$.imgUrl").value("http://example.com/image.png"))
				.andExpect(jsonPath("$.resource.id").value(1L));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(sectionDTO);

		mockMvc.perform(put("/sections/{id}", nonExistingId).content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		mockMvc.perform(delete("/sections/{id}", existingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

}
