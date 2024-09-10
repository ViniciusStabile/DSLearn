package DSLearn.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import DSLearn.entities.Course;
import DSLearn.entities.Offer;

@DataJpaTest
public class OfferRepositoryTests {

	@Autowired
	private OfferRepository repository;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalOffers;
	private Offer testOffer;

	

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		countTotalOffers = 2L;

		Course course = new Course(1L, "Java", "https://example.com/java.png", "https://example.com/java-gray.png");

		testOffer = new Offer(null, "2.0", Instant.parse("2022-11-20T23:00:00Z"), Instant.parse("2023-11-20T23:00:00Z"),
				course);
	}

	@Test
	public void findAllShouldReturnAllOffers() {
		List<Offer> result = repository.findAll();
		Assertions.assertEquals(countTotalOffers, result.size());
	}

	@Test
	public void findByIdShouldReturnOfferWhenIdExists() {
		Optional<Offer> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		Optional<Offer> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Offer offer = repository.save(testOffer);
		Assertions.assertNotNull(offer.getId());
		Assertions.assertEquals(countTotalOffers + 1, offer.getId());
	}

	@Test
	public void updateShouldChangeAndPersistData() {
		Optional<Offer> offerOptional = repository.findById(existingId);
		Assertions.assertTrue(offerOptional.isPresent());
		Offer offer = offerOptional.get();
		offer.setEdition("Updated Edition");

		Offer updatedOffer = repository.save(offer);
		Assertions.assertEquals("Updated Edition", updatedOffer.getEdition());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Offer> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

}
