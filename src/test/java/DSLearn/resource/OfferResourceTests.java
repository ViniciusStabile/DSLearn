package DSLearn.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

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

import DSLearn.DTO.CourseMinDTO;
import DSLearn.DTO.OfferDTO;
import DSLearn.resources.OfferResource;
import DSLearn.services.OfferService;
import DSLearn.services.exceptions.ResourceNotFoundException;

@WebMvcTest(OfferResource.class)
public class OfferResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService service;

    @Autowired
    private ObjectMapper objectMapper;

    private OfferDTO offerDTO;
    private PageImpl<OfferDTO> page;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;

        offerDTO = new OfferDTO(existingId, "Edition 1", Instant.parse("2023-01-01T00:00:00Z"),
                                Instant.parse("2023-12-31T23:59:59Z"), new CourseMinDTO(1L, "Course 1"));
        page = new PageImpl<>(java.util.List.of(offerDTO));

        Mockito.when(service.findAll(any(Pageable.class))).thenReturn(page);

        Mockito.when(service.findById(existingId)).thenReturn(offerDTO);
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.insert(any())).thenReturn(offerDTO);

        Mockito.when(service.update(eq(existingId), any())).thenReturn(offerDTO);
        Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(service).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/offers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingId))
                .andExpect(jsonPath("$.content[0].edition").value("Edition 1"))
                .andExpect(jsonPath("$.content[0].startMoment").value("2023-01-01T00:00:00Z"))
                .andExpect(jsonPath("$.content[0].endMoment").value("2023-12-31T23:59:59Z"))
                .andExpect(jsonPath("$.content[0].course.id").value(1L))
                .andExpect(jsonPath("$.content[0].course.name").value("Course 1"));
    }

    @Test
    public void findByIdShouldReturnOfferWhenIdExists() throws Exception {
        mockMvc.perform(get("/offers/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.edition").value("Edition 1"))
                .andExpect(jsonPath("$.startMoment").value("2023-01-01T00:00:00Z"))
                .andExpect(jsonPath("$.endMoment").value("2023-12-31T23:59:59Z"))
                .andExpect(jsonPath("$.course.id").value(1L))
                .andExpect(jsonPath("$.course.name").value("Course 1"));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/offers/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnCreatedAndOfferDTO() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(offerDTO);

        mockMvc.perform(post("/offers")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.edition").value("Edition 1"))
                .andExpect(jsonPath("$.startMoment").value("2023-01-01T00:00:00Z"))
                .andExpect(jsonPath("$.endMoment").value("2023-12-31T23:59:59Z"))
                .andExpect(jsonPath("$.course.id").value(1L))
                .andExpect(jsonPath("$.course.name").value("Course 1"));
    }

    @Test
    public void updateShouldReturnOfferDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(offerDTO);

        mockMvc.perform(put("/offers/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.edition").value("Edition 1"))
                .andExpect(jsonPath("$.startMoment").value("2023-01-01T00:00:00Z"))
                .andExpect(jsonPath("$.endMoment").value("2023-12-31T23:59:59Z"))
                .andExpect(jsonPath("$.course.id").value(1L))
                .andExpect(jsonPath("$.course.name").value("Course 1"));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(offerDTO);

        mockMvc.perform(put("/offers/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
	}

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        mockMvc.perform(delete("/offers/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    
}
