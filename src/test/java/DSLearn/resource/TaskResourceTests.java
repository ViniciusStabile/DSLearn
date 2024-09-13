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

import DSLearn.DTO.TaskDTO;
import DSLearn.resources.TaskResource;
import DSLearn.services.TaskService;
import DSLearn.services.exceptions.ResourceNotFoundException;

@WebMvcTest(TaskResource.class)
public class TaskResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService service;

	@Autowired
	private ObjectMapper objectMapper;

	private TaskDTO taskDTO;
	private PageImpl<TaskDTO> page;

	private Long existingId;
	private Long nonExistingId;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;

		taskDTO = new TaskDTO(existingId, "Task Title", 1, "Task Description", 10, 5, 20.0);

		page = new PageImpl<>(List.of(taskDTO));

		Mockito.when(service.findAll(any(Pageable.class))).thenReturn(page);

		Mockito.when(service.findById(existingId)).thenReturn(taskDTO);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		Mockito.when(service.insert(any())).thenReturn(taskDTO);

		Mockito.when(service.update(eq(existingId), any())).thenReturn(taskDTO);
		Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

		Mockito.doNothing().when(service).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		mockMvc.perform(get("/tasks").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id").value(existingId))
				.andExpect(jsonPath("$.content[0].title").value("Task Title"))
				.andExpect(jsonPath("$.content[0].position").value(1))
				.andExpect(jsonPath("$.content[0].description").value("Task Description"))
				.andExpect(jsonPath("$.content[0].questCount").value(10))
				.andExpect(jsonPath("$.content[0].approvalCount").value(5))
				.andExpect(jsonPath("$.content[0].weight").value(20.0));
	}

	@Test
	public void findByIdShouldReturnTaskWhenIdExists() throws Exception {
		mockMvc.perform(get("/tasks/{id}", existingId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.title").value("Task Title"))
				.andExpect(jsonPath("$.position").value(1))
				.andExpect(jsonPath("$.description").value("Task Description"))
				.andExpect(jsonPath("$.questCount").value(10)).andExpect(jsonPath("$.approvalCount").value(5))
				.andExpect(jsonPath("$.weight").value(20.0));
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		mockMvc.perform(get("/tasks/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnCreatedAndTaskDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(taskDTO);

		mockMvc.perform(post("/tasks").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.title").value("Task Title"))
				.andExpect(jsonPath("$.position").value(1))
				.andExpect(jsonPath("$.description").value("Task Description"))
				.andExpect(jsonPath("$.questCount").value(10)).andExpect(jsonPath("$.approvalCount").value(5))
				.andExpect(jsonPath("$.weight").value(20.0));
	}

	@Test
	public void updateShouldReturnTaskDTOWhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(taskDTO);

		mockMvc.perform(put("/tasks/{id}", existingId).content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(existingId)).andExpect(jsonPath("$.title").value("Task Title"))
				.andExpect(jsonPath("$.position").value(1))
				.andExpect(jsonPath("$.description").value("Task Description"))
				.andExpect(jsonPath("$.questCount").value(10)).andExpect(jsonPath("$.approvalCount").value(5))
				.andExpect(jsonPath("$.weight").value(20.0));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(taskDTO);

		mockMvc.perform(put("/tasks/{id}", nonExistingId).content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		mockMvc.perform(delete("/tasks/{id}", existingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		mockMvc.perform(delete("/tasks/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
