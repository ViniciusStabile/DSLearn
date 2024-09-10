package DSLearn.repositories;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import DSLearn.entities.Enrollment;
import DSLearn.entities.Offer;
import DSLearn.entities.User;
import DSLearn.entities.pk.EnrollmentPK;

@DataJpaTest
public class EnrollmentRepositoryTests {

	@Autowired
	private EnrollmentRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	private EnrollmentPK existingId;
	private EnrollmentPK nonExistingId;
	private long countTotalEnrollments;
	private User user;
	private Offer offer;

	@BeforeEach
	void setUp() throws Exception {
		user = entityManager.persist(new User(null, "John Doe", "john.doe@example.com", "password123", null));
		offer = entityManager.persist(
				new Offer(null, "Course Edition 1", Instant.now(), Instant.now().plus(30, ChronoUnit.DAYS), null));

		existingId = new EnrollmentPK(user, offer);
		nonExistingId = new EnrollmentPK(new User(999L, "Invalid", "invalid@example.com", "password", null),
				new Offer(999L, "Invalid Edition", Instant.now(), Instant.now().plus(30, ChronoUnit.DAYS), null));

		countTotalEnrollments = repository.count();

		Enrollment enrollment = new Enrollment(user, offer, Instant.now(), null, true, false);
		entityManager.persist(enrollment);
	}

	@Test
	public void findAllShouldReturnAllEnrollments() {
		List<Enrollment> result = repository.findAll();
		Assertions.assertEquals(countTotalEnrollments + 1, result.size());
	}

	@Test
	public void findByIdShouldReturnEnrollmentWhenIdExists() {
		Optional<Enrollment> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		Optional<Enrollment> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Enrollment enrollment = new Enrollment(user, offer, Instant.now(), null, true, false);

		enrollment = repository.save(enrollment);
		Assertions.assertNotNull(enrollment.getStudent());
		Assertions.assertNotNull(enrollment.getOffer());
		Assertions.assertEquals(countTotalEnrollments + 1, repository.count());
	}

	@Test
	public void updateShouldChangeAndPersistData() {
		Optional<Enrollment> enrollmentOptional = repository.findById(existingId);
		Assertions.assertTrue(enrollmentOptional.isPresent());
		Enrollment enrollment = enrollmentOptional.get();
		enrollment.setAvailable(false);

		Enrollment updatedEnrollment = repository.save(enrollment);
		Assertions.assertFalse(updatedEnrollment.isAvailable());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Enrollment> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

}