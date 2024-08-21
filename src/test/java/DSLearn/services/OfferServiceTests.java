package DSLearn.services;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import DSLearn.DTO.OfferDTO;
import DSLearn.entities.Course;
import DSLearn.entities.Offer;
import DSLearn.repositories.CourseRepository;
import DSLearn.repositories.OfferRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class OfferServiceTests {

	@InjectMocks
	private OfferService service;

	@Mock
	private OfferRepository repository;
	
	@Mock
	private CourseRepository courseRepository;

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Long existingCourseId;
	private Offer offer;
	private OfferDTO offerDTO;
	private Course course;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 200L;
		dependentId = 3L;
		existingCourseId = 1L;
		course = new Course(1L, "Course Name", "imgUri", "imgGrayUri");
		offer = new Offer(existingId, "Edition 1", Instant.parse("2023-01-01T10:00:00Z"),
				Instant.parse("2023-12-31T10:00:00Z"), course);
		offerDTO = new OfferDTO(offer);

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(offer));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(Mockito.any(Offer.class))).thenReturn(offer);
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(offer);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		Mockito.when(courseRepository.findById(existingCourseId)).thenReturn(Optional.of(course));
	}

	@Test
	public void findByIdShouldReturnOfferDTOWhenIdExists() {
		OfferDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(offerDTO.getId(), result.getId());
		Assertions.assertEquals(offerDTO.getEdition(), result.getEdition());
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	public void insertShouldReturnOfferDTOWhenSuccessful() {
		OfferDTO result = service.insert(offerDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(offerDTO.getId(), result.getId());
		Assertions.assertEquals(offerDTO.getEdition(), result.getEdition());
	}

	@Test
	public void updateShouldReturnOfferDTOWhenIdExists() {
		OfferDTO result = service.update(existingId, offerDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(offerDTO.getId(), result.getId());
		Assertions.assertEquals(offerDTO.getEdition(), result.getEdition());
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, offerDTO);
		});
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIntegrityViolationOccurs() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
	}
}
