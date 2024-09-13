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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import DSLearn.DTO.CourseDTO;
import DSLearn.resources.CourseResource;
import DSLearn.services.CourseService;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;

@WebMvcTest(CourseResource.class)
public class CourseResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CourseService service;

	@Autowired
	private ObjectMapper objectMapper;

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private CourseDTO courseDTO;
	private PageImpl<CourseDTO> page;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 3L;
		courseDTO = new CourseDTO(existingId, "Course Name", "http://img.com/img.png", "http://img.com/img-gray.png");
		page = new PageImpl<>(List.of(courseDTO));

		when(service.findAll(any(Pageable.class))).thenReturn(page);

		when(service.findById(existingId)).thenReturn(courseDTO);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		when(service.insert(any())).thenReturn(courseDTO);

		when(service.update(eq(existingId), any())).thenReturn(courseDTO);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependentId);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		mockMvc.perform(get("/courses").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void findByIdShouldReturnCourseWhenIdExists() throws Exception {
		mockMvc.perform(get("/courses/{id}", existingId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.name").value("Course Name"));
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		mockMvc.perform(get("/courses/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnCreatedAndCourseDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(courseDTO);

		mockMvc.perform(post("/courses").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.name").value("Course Name"));
	}

	@Test
	public void updateShouldReturnCourseDTOWhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(courseDTO);

		mockMvc.perform(put("/courses/{id}", existingId).content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.name").value("Course Name"));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(courseDTO);

		mockMvc.perform(put("/courses/{id}", nonExistingId).content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		mockMvc.perform(delete("/courses/{id}", existingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		mockMvc.perform(delete("/courses/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

}
