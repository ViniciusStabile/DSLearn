package DSLearn.services;

import static org.mockito.ArgumentMatchers.any;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import DSLearn.DTO.EnrollmentDTO;
import DSLearn.DTO.EnrollmentPKDTO;
import DSLearn.DTO.OfferMinDTO;
import DSLearn.DTO.UserMinDTO;
import DSLearn.entities.Enrollment;
import DSLearn.entities.Offer;
import DSLearn.entities.User;
import DSLearn.entities.pk.EnrollmentPK;
import DSLearn.repositories.EnrollmentRepository;
import DSLearn.repositories.OfferRepository;
import DSLearn.repositories.UserRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class EnrollmentServiceTests {

	@InjectMocks
	private EnrollmentService service;

	@Mock
	private EnrollmentRepository repository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private OfferRepository offerRepository;

	private User user;
	private User userDependentId;
	private Offer offer;
	private Enrollment enrollment;
	private EnrollmentDTO enrollmentDTO;
	private EnrollmentPK enrollmentPK;
	private EnrollmentPK enrollmentDependentPK;
	private EnrollmentPKDTO enrollmentPKDTO;
	private EnrollmentPK nonExistingPK;
	private EnrollmentPK existingPK;                                                                                   

	@BeforeEach
	void setUp() throws Exception {
		user = new User(1L, "alex", "alex@gmail.com", "abcde", null);
		userDependentId = new User(2L, "Mario", "Mario@gmail.com", "abcdef", null);
		offer = new Offer(1L, "Edition 1", Instant.parse("2023-01-01T10:00:00Z"), Instant.parse("2023-12-31T10:00:00Z"),
				null);

		enrollmentPK = new EnrollmentPK(user, offer);
		enrollmentDependentPK = new EnrollmentPK(userDependentId,offer);
		enrollment = new Enrollment(user, offer, Instant.now(), null, true, false);

		UserMinDTO userMinDTO = new UserMinDTO(user.getId(), user.getName());
		OfferMinDTO offerMinDTO = new OfferMinDTO(offer.getId(), offer.getEdition());
		enrollmentPKDTO = new EnrollmentPKDTO(userMinDTO, offerMinDTO);
		enrollmentDTO = new EnrollmentDTO(enrollmentPKDTO, Instant.now(), null, true, false);
		nonExistingPK = new EnrollmentPK(new User(2L, "Non-existing", "non@example.com", "12345", null),
				offer);
		existingPK = new EnrollmentPK(user, offer);

		PageImpl<Enrollment> page = new PageImpl<>(List.of(enrollment));

		Mockito.when(repository.findById(enrollmentPK)).thenReturn(Optional.of(enrollment));
		Mockito.when(repository.findAll((Pageable) any())).thenReturn(page);
		Mockito.when(userRepository.getReferenceById(user.getId())).thenReturn(user);
		Mockito.when(offerRepository.getReferenceById(offer.getId())).thenReturn(offer);
		Mockito.when(repository.existsById(enrollmentPK)).thenReturn(true);
		Mockito.when(repository.findById(nonExistingPK)).thenReturn(Optional.empty());
		Mockito.when(repository.save(any())).thenReturn(enrollment);
		
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(enrollmentDependentPK);
		
		Mockito.when(repository.existsById(enrollmentDependentPK)).thenReturn(true);
		Mockito.when(repository.getReferenceById(existingPK)).thenReturn(enrollment);
		Mockito.when(repository.save(Mockito.any(Enrollment.class))).thenReturn(enrollment);
		Mockito.when(repository.getReferenceById(nonExistingPK)).thenThrow(EntityNotFoundException.class);
		Mockito.doNothing().when(repository).deleteById(enrollmentPK);
	}

	@Test
	public void findByIdShouldReturnEnrollmentDTOWhenIdExists() {
		EnrollmentDTO result = service.findById(enrollmentPK);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(enrollmentDTO.getId().getUser().getId(), result.getId().getUser().getId());
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingPK);
		});
	}

	@Test
	public void findAllShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 12);
		Page<EnrollmentDTO> result = service.findAll(pageable);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
	}

	@Test
	public void insertShouldPersistWithAutoIncrementWhenIdIsNull() {
		EnrollmentDTO result = service.insert(enrollmentDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId().getUser().getId(), enrollmentDTO.getId().getUser().getId());
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
	    Assertions.assertThrows(DatabaseException.class, () -> {
	        service.delete(enrollmentDependentPK);
	    });
	}

	@Test
	public void updateShouldReturnEnrollmentDTOWhenIdExists() {
		EnrollmentDTO result = service.update(existingPK, enrollmentDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(enrollmentDTO.getId().getUser().getId(), result.getId().getUser().getId());
		Assertions.assertEquals(enrollmentDTO.getId().getUser().getName(), result.getId().getUser().getName());
		Assertions.assertEquals(enrollmentDTO.getId().getOffer().getId(), result.getId().getOffer().getId());
		Assertions.assertEquals(enrollmentDTO.getId().getOffer().getEdition(), result.getId().getOffer().getEdition());
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingPK, enrollmentDTO);
		});
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(enrollmentPK);
		});

	}
}