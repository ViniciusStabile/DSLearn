package DSLearn.repositories;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import DSLearn.entities.Resource;
import DSLearn.entities.Section;
import DSLearn.entities.enums.ResourceType;

@DataJpaTest
public class SectionRepositoryTests {

	@Autowired
	private SectionRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalSections;
	private Section testSection;
	private Resource testResource;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		countTotalSections = 3L;

		testResource = new Resource(null, "Sample Resource", "Sample Description", 1,
				"https://example.com/resource.png", ResourceType.LESSON_ONLY, null);
		entityManager.persist(testResource);

		testSection = new Section(null, "New Section", "Section Description", 1, "https://example.com/section.png",
				testResource, null);
	}

	@Test
	public void findAllShouldReturnAllSections() {
		List<Section> result = repository.findAll();
		Assertions.assertEquals(countTotalSections, result.size());
	}

	@Test
	public void findByIdShouldReturnSectionWhenIdExists() {
		Optional<Section> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		Optional<Section> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Section section = repository.save(testSection);
		Assertions.assertNotNull(section.getId());
		Assertions.assertEquals(countTotalSections + 1, section.getId());
	}

	@Test
	public void updateShouldChangeAndPersistData() {
		Optional<Section> sectionOptional = repository.findById(existingId);
		Assertions.assertTrue(sectionOptional.isPresent());
		Section section = sectionOptional.get();
		section.setTitle("Updated Title");

		Section updatedSection = repository.save(section);
		Assertions.assertEquals("Updated Title", updatedSection.getTitle());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Section> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

}
