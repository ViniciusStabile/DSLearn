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

import DSLearn.DTO.NotificationDTO;
import DSLearn.DTO.UserNotificationDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NotificationResourceIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalNotifications;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalNotifications = 3L;
	}

	@Test
	public void findAllShouldReturnSortedPageWhenPageSortByMoment() throws Exception {

		ResultActions result = mockMvc
				.perform(get("/notifications?page=0&size=12&sort=moment,asc").accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalNotifications));
		result.andExpect(jsonPath("$.content[0].moment").exists());
	}

	@Test
	public void findByIdShouldReturnNotificationWhenIdExists() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/notifications/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.text").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/notifications/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnNotificationDTOCreated() throws Exception {
		NotificationDTO notificationDTO = new NotificationDTO(existingId, "Updated text", Instant.now(), true,
				"/updated-route", new UserNotificationDTO(existingId, "new User"));
		String jsonBody = objectMapper.writeValueAsString(notificationDTO);

		ResultActions result = mockMvc.perform(post("/notifications").content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.text").value("Updated text"));
	}

	@Test
	public void updateShouldReturnNotificationDTOWhenIdExists() throws Exception {
		NotificationDTO notificationDTO = new NotificationDTO(existingId, "Updated text", Instant.now(), true,
				"/updated-route", new UserNotificationDTO(existingId, "new User"));
		String jsonBody = objectMapper.writeValueAsString(notificationDTO);

		ResultActions result = mockMvc.perform(put("/notifications/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.text").value("Updated text"));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		NotificationDTO notificationDTO = new NotificationDTO(nonExistingId, "Updated text", Instant.now(), true,
				"/updated-route", new UserNotificationDTO(existingId, "new User"));
		String jsonBody = objectMapper.writeValueAsString(notificationDTO);

		ResultActions result = mockMvc.perform(put("/notifications/{id}", nonExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/notifications/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/notifications/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
}
