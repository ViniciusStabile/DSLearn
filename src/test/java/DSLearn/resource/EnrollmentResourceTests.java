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

import DSLearn.DTO.EnrollmentDTO;
import DSLearn.DTO.EnrollmentPKDTO;
import DSLearn.DTO.OfferMinDTO;
import DSLearn.DTO.UserMinDTO;
import DSLearn.entities.Offer;
import DSLearn.entities.User;
import DSLearn.entities.pk.EnrollmentPK;
import DSLearn.resources.EnrollmentResource;
import DSLearn.services.EnrollmentService;
import DSLearn.services.exceptions.ResourceNotFoundException;

@WebMvcTest(EnrollmentResource.class)
public class EnrollmentResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EnrollmentService service;

	@Autowired
	private ObjectMapper objectMapper;

	private EnrollmentDTO enrollmentDTO;
	private EnrollmentPKDTO enrollmentPKDTO;

	private User user;

	private Offer offer;

	private EnrollmentPK nonExistingPK;
	private EnrollmentPK existingPK;

	private PageImpl<EnrollmentDTO> page;

	@BeforeEach
	void setUp() throws Exception {

		user = new User(1L, "alex", "alex@gmail.com", "abcde", null);

		offer = new Offer(1L, "Edition 1", Instant.parse("2023-01-01T10:00:00Z"), Instant.parse("2023-12-31T10:00:00Z"),
				null);

		UserMinDTO userMinDTO = new UserMinDTO(user.getId(), user.getName());
		OfferMinDTO offerMinDTO = new OfferMinDTO(offer.getId(), offer.getEdition());
		enrollmentPKDTO = new EnrollmentPKDTO(userMinDTO, offerMinDTO);
		enrollmentDTO = new EnrollmentDTO(enrollmentPKDTO, Instant.now(), null, true, false);

		nonExistingPK = new EnrollmentPK(new User(2L, "Non-existing", "non@example.com", "12345", null), offer);
		existingPK = new EnrollmentPK(user, offer);

		page = new PageImpl<>(List.of(enrollmentDTO));

		Mockito.when(service.findAll(any(Pageable.class))).thenReturn(page);

		Mockito.when(service.findById(existingPK)).thenReturn(enrollmentDTO);
		Mockito.when(service.findById(nonExistingPK)).thenThrow(ResourceNotFoundException.class);

		Mockito.when(service.insert(any())).thenReturn(enrollmentDTO);

		Mockito.when(service.update(eq(existingPK), any())).thenReturn(enrollmentDTO);
		Mockito.when(service.update(eq(nonExistingPK), any())).thenThrow(ResourceNotFoundException.class);

		Mockito.doNothing().when(service).delete(existingPK);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingPK);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		mockMvc.perform(get("/enrollments").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id.user.id").value(1L))
				.andExpect(jsonPath("$.content[0].id.user.name").value("alex"))
				.andExpect(jsonPath("$.content[0].id.offer.id").value(1L))
				.andExpect(jsonPath("$.content[0].id.offer.edition").value("Edition 1"))
				.andExpect(jsonPath("$.content[0].enrollMoment").exists())
				.andExpect(jsonPath("$.content[0].available").value(true))
				.andExpect(jsonPath("$.content[0].onlyUpdate").value(false));
	}

	@Test
	public void findByIdShouldReturnEnrollmentWhenIdExists() throws Exception {
		String url = String.format("/enrollments/%d/%d", user.getId(), offer.getId());

		mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id.user.id").value(1L)).andExpect(jsonPath("$.id.user.name").value("alex"))
				.andExpect(jsonPath("$.id.offer.id").value(1L)).andExpect(jsonPath("$.enrollMoment").exists())
				.andExpect(jsonPath("$.available").value(true)).andExpect(jsonPath("$.onlyUpdate").value(false));
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String url = String.format("/enrollments/%d/%d", nonExistingPK.getUser().getId(), offer.getId());

		mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnCreatedAndEnrollmentDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(enrollmentDTO);

		mockMvc.perform(post("/enrollments").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id.user.id").value(1L)).andExpect(jsonPath("$.id.user.name").value("alex"))
				.andExpect(jsonPath("$.id.offer.id").value(1L)).andExpect(jsonPath("$.onlyUpdate").value(false));
	}

	@Test
	public void updateShouldReturnEnrollmentDTOWhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(enrollmentDTO);
		String url = String.format("/enrollments/%d/%d", user.getId(), offer.getId());

		mockMvc.perform(
				put(url).content(jsonBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id.user.id").value(1L))
				.andExpect(jsonPath("$.id.user.name").value("alex")).andExpect(jsonPath("$.id.offer.id").value(1L))
				.andExpect(jsonPath("$.enrollMoment").exists()).andExpect(jsonPath("$.available").value(true))
				.andExpect(jsonPath("$.onlyUpdate").value(false));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(enrollmentDTO);
		String url = String.format("/enrollments/%d/%d", nonExistingPK.getUser().getId(), offer.getId());

		mockMvc.perform(
				put(url).content(jsonBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		String url = String.format("/enrollments/%d/%d", user.getId(), offer.getId());

		mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String url = String.format("/enrollments/%d/%d", nonExistingPK.getUser().getId(), offer.getId());

		mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}
}
