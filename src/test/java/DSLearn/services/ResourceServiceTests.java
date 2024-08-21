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

import DSLearn.DTO.ResourceDTO;
import DSLearn.entities.Offer;
import DSLearn.entities.Resource;
import DSLearn.entities.enums.ResourceType;
import DSLearn.repositories.OfferRepository;
import DSLearn.repositories.ResourceRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ResourceServiceTests {

	@InjectMocks
	private ResourceService service;

	@Mock
	private ResourceRepository repository;
	
	@Mock
	private OfferRepository offerRepository;

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Resource resource;
	private ResourceDTO resourceDTO;
	private Offer offer;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 200L;
		dependentId = 3L;
		offer = new Offer(1L, "Edition 1", Instant.parse("2023-01-01T10:00:00Z"), Instant.parse("2023-12-31T10:00:00Z"),
				null);
		resource = new Resource(existingId, "Resource Title", "Description", 1, "imgUrl", ResourceType.LESSON_ONLY, offer);
		resourceDTO = new ResourceDTO(resource);

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(resource));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(Mockito.any(Resource.class))).thenReturn(resource);
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(resource);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		Mockito.when(offerRepository.findById(existingId)).thenReturn(Optional.of(offer));
	}

	@Test
	public void findByIdShouldReturnResourceDTOWhenIdExists() {
		ResourceDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(resourceDTO.getId(), result.getId());
		Assertions.assertEquals(resourceDTO.getTitle(), result.getTitle());
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	public void insertShouldReturnResourceDTOWhenSuccessful() {
		ResourceDTO result = service.insert(resourceDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(resourceDTO.getId(), result.getId());
		Assertions.assertEquals(resourceDTO.getTitle(), result.getTitle());
	}

	@Test
	public void updateShouldReturnResourceDTOWhenIdExists() {
		ResourceDTO result = service.update(existingId, resourceDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(resourceDTO.getId(), result.getId());
		Assertions.assertEquals(resourceDTO.getTitle(), result.getTitle());
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, resourceDTO);
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
