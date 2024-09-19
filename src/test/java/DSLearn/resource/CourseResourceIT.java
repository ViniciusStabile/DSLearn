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

import DSLearn.DTO.CourseDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CourseResourceIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalCourses;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalCourses = 3L; 
	}

	@Test
	public void findAllShouldReturnSortedPageWhenPageSortByName() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/courses?page=0&size=12&sort=name,asc").accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalCourses));
		result.andExpect(jsonPath("$.content[0].name").exists());
	}

	@Test
	public void findByIdShouldReturnCourseWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/courses/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform(get("/courses/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnCourseDTOCreated() throws Exception {
		CourseDTO courseDTO = new CourseDTO(null, "New Course", "http://img.com/new-img.png",
				"http://img.com/new-img-gray.png");
		String jsonBody = objectMapper.writeValueAsString(courseDTO);

		ResultActions result = mockMvc.perform(post("/courses").content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("New Course"));
		result.andExpect(jsonPath("$.imgUri").value("http://img.com/new-img.png"));
		result.andExpect(jsonPath("$.imgGrayUri").value("http://img.com/new-img-gray.png"));
	}

	@Test
	public void updateShouldReturnCourseDTOWhenIdExists() throws Exception {
		CourseDTO courseDTO = new CourseDTO(existingId, "Updated Course", "http://img.com/updated-img.png",
				"http://img.com/updated-img-gray.png");
		String jsonBody = objectMapper.writeValueAsString(courseDTO);

		ResultActions result = mockMvc.perform(put("/courses/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("Updated Course"));
		result.andExpect(jsonPath("$.imgUri").value("http://img.com/updated-img.png"));
		result.andExpect(jsonPath("$.imgGrayUri").value("http://img.com/updated-img-gray.png"));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		CourseDTO courseDTO = new CourseDTO(nonExistingId, "Updated Course", "http://img.com/updated-img.png",
				"http://img.com/updated-img-gray.png");
		String jsonBody = objectMapper.writeValueAsString(courseDTO);

		ResultActions result = mockMvc.perform(put("/courses/{id}", nonExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/courses/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/courses/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
}
