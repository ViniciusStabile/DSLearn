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

import DSLearn.entities.Offer;
import DSLearn.entities.Resource;
import DSLearn.entities.enums.ResourceType;

@DataJpaTest
public class ResourceRepositoryTests {

	@Autowired
	private ResourceRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalResources;
	private Resource testResource;
	private Offer testOffer;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		countTotalResources = 2L;

		testOffer = new Offer(null, "2.0", Instant.parse("2022-11-20T23:00:00Z"), Instant.parse("2023-11-20T23:00:00Z"),
				null);
		entityManager.persist(testOffer);

		testResource = new Resource(null, "New Resource", "New Description", 1, "https://example.com/image.png",
				ResourceType.LESSON_ONLY, testOffer);
	}

	@Test
	public void findAllShouldReturnAllResources() {
		List<Resource> result = repository.findAll();
		Assertions.assertEquals(countTotalResources, result.size());
	}

	@Test
	public void findByIdShouldReturnResourceWhenIdExists() {
		Optional<Resource> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		Optional<Resource> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Resource resource = repository.save(testResource);
		Assertions.assertNotNull(resource.getId());
		Assertions.assertEquals(countTotalResources + 1, resource.getId());
	}

	@Test
	public void updateShouldChangeAndPersistData() {
		Optional<Resource> resourceOptional = repository.findById(existingId);
		Assertions.assertTrue(resourceOptional.isPresent());
		Resource resource = resourceOptional.get();
		resource.setTitle("Updated Title");

		Resource updatedResource = repository.save(resource);
		Assertions.assertEquals("Updated Title", updatedResource.getTitle());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Resource> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

}
