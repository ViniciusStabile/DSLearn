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

import DSLearn.DTO.ContentDTO;
import DSLearn.resources.ContentResource;
import DSLearn.services.ContentService;
import DSLearn.services.exceptions.ResourceNotFoundException;

@WebMvcTest(ContentResource.class)
public class ContentResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ContentService service;

	@Autowired
	private ObjectMapper objectMapper;

	private ContentDTO contentDTO;
	private PageImpl<ContentDTO> page;

	private Long existingId;
	private Long nonExistingId;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;

		contentDTO = new ContentDTO(existingId, "Content Title", 1, "Text content", "http://example.com/video.mp4");

		page = new PageImpl<>(List.of(contentDTO));

		Mockito.when(service.findAll(any(Pageable.class))).thenReturn(page);

		Mockito.when(service.findById(existingId)).thenReturn(contentDTO);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		Mockito.when(service.insert(any())).thenReturn(contentDTO);

		Mockito.when(service.update(eq(existingId), any())).thenReturn(contentDTO);
		Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

		Mockito.doNothing().when(service).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		mockMvc.perform(get("/contents").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id").value(existingId))
				.andExpect(jsonPath("$.content[0].title").value("Content Title"))
				.andExpect(jsonPath("$.content[0].position").value(1))
				.andExpect(jsonPath("$.content[0].textContent").value("Text content"))
				.andExpect(jsonPath("$.content[0].videoUri").value("http://example.com/video.mp4"));
	}

	@Test
	public void findByIdShouldReturnContentWhenIdExists() throws Exception {
		mockMvc.perform(get("/contents/{id}", existingId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.title").value("Content Title"))
				.andExpect(jsonPath("$.position").value(1)).andExpect(jsonPath("$.textContent").value("Text content"))
				.andExpect(jsonPath("$.videoUri").value("http://example.com/video.mp4"));
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		mockMvc.perform(get("/contents/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnCreatedAndContentDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(contentDTO);

		mockMvc.perform(post("/contents").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.title").value("Content Title"))
				.andExpect(jsonPath("$.position").value(1)).andExpect(jsonPath("$.textContent").value("Text content"))
				.andExpect(jsonPath("$.videoUri").value("http://example.com/video.mp4"));
	}

	@Test
	public void updateShouldReturnContentDTOWhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(contentDTO);

		mockMvc.perform(put("/contents/{id}", existingId).content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.title").value("Content Title"))
				.andExpect(jsonPath("$.position").value(1)).andExpect(jsonPath("$.textContent").value("Text content"))
				.andExpect(jsonPath("$.videoUri").value("http://example.com/video.mp4"));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(contentDTO);

		mockMvc.perform(put("/contents/{id}", nonExistingId).content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		mockMvc.perform(delete("/contents/{id}", existingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		mockMvc.perform(delete("/contents/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
