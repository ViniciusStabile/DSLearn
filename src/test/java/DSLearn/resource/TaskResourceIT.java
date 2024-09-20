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

import DSLearn.DTO.TaskDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalTasks;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 4L;
        nonExistingId = 1000L;
        countTotalTasks = 1L; 
    }

    @Test
    public void findAllShouldReturnSortedPageWhenPageSortByTitle() throws Exception {
        ResultActions result = mockMvc
                .perform(get("/tasks?page=0&size=12&sort=title,asc").accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(countTotalTasks));
        result.andExpect(jsonPath("$.content[0].title").exists());
    }

    @Test
    public void findByIdShouldReturnTaskWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/tasks/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.questCount").exists());
        result.andExpect(jsonPath("$.approvalCount").exists());
        result.andExpect(jsonPath("$.weight").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(get("/tasks/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnTaskDTOCreated() throws Exception {
        TaskDTO taskDTO = new TaskDTO(null, "Tarefa capitulo 1", 4, "Tarefa para casa", 5, 3, 50.0);
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        ResultActions result = mockMvc.perform(post("/tasks").content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.description").value("Tarefa para casa"));
        result.andExpect(jsonPath("$.questCount").value(5));
        result.andExpect(jsonPath("$.approvalCount").value(3));
        result.andExpect(jsonPath("$.weight").value(50.0));
    }

    @Test
    public void updateShouldReturnTaskDTOWhenIdExists() throws Exception {
    	 TaskDTO taskDTO = new TaskDTO(null, "Tarefa capitulo 1", 4, "Tarefa para casa", 5, 3, 50.0);
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        ResultActions result = mockMvc.perform(put("/tasks/{id}", existingId).content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.description").value("Tarefa para casa"));
        result.andExpect(jsonPath("$.questCount").value(5));
        result.andExpect(jsonPath("$.approvalCount").value(3));
        result.andExpect(jsonPath("$.weight").value(50.0));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
    	 TaskDTO taskDTO = new TaskDTO(null, "Tarefa capitulo 1", 4, "Tarefa para casa", 5, 3, 50.0);
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        ResultActions result = mockMvc.perform(put("/tasks/{id}", nonExistingId).content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(delete("/tasks/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(delete("/tasks/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }
}
