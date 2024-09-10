package DSLearn.repositories;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import DSLearn.entities.Content;

@DataJpaTest
public class ContentRepositoryTests {

	@Autowired
	private ContentRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalContents;
	private Content testContent;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		countTotalContents = repository.count(); 

		testContent = new Content();
		testContent.setTextContent("Sample Text Content");
		testContent.setVideoUrl("https://example.com/sample-video");
		testContent.setTitle("Sample Content Title");
		testContent.setPosition(1); 
		entityManager.persist(testContent);
	}

	@Test
	public void findAllShouldReturnAllContents() {
		List<Content> result = repository.findAll();
		Assertions.assertEquals(countTotalContents + 1, result.size()); 
	}

	@Test
	public void findByIdShouldReturnContentWhenIdExists() {
		Optional<Content> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		Optional<Content> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Content content = new Content();
		content.setTextContent("New Text Content");
		content.setVideoUrl("https://example.com/new-video");
		content.setTitle("New Content Title");
		content.setPosition(2); 

		content = repository.save(content);
		Assertions.assertNotNull(content.getId());
		Assertions.assertEquals(countTotalContents + 2, repository.count()); 
	}

	@Test
	public void updateShouldChangeAndPersistData() {
		Optional<Content> contentOptional = repository.findById(existingId);
		Assertions.assertTrue(contentOptional.isPresent());
		Content content = contentOptional.get();
		content.setTitle("Updated Content Title");

		Content updatedContent = repository.save(content);
		Assertions.assertEquals("Updated Content Title", updatedContent.getTitle());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Content> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

}