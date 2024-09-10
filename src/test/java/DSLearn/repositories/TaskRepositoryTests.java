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

import DSLearn.entities.Task;

@DataJpaTest
public class TaskRepositoryTests {

	@Autowired
	private TaskRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalTasks;
	private Task testTask;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 4L;
		nonExistingId = 100L;
		countTotalTasks = repository.count();

		testTask = new Task(null, "Task Title", 1, null, "Sample description", 5, 3, 1.0,
				Instant.parse("2020-12-25T23:00:00Z"));
		entityManager.persist(testTask);
	}

	@Test
	public void findAllShouldReturnAllTasks() {
		List<Task> result = repository.findAll();
		Assertions.assertEquals(countTotalTasks + 1, result.size());
	}

	@Test
	public void findByIdShouldReturnTaskWhenIdExists() {
		Optional<Task> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		Optional<Task> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Task task = new Task(null, "New Task Title", 2, null, "New description", 10, 7, 5.0,
				Instant.parse("2022-10-10T23:00:00Z"));

		task = repository.save(task);
		Assertions.assertNotNull(task.getId());
		Assertions.assertEquals(countTotalTasks + 2, repository.count());
	}

	@Test
	public void updateShouldChangeAndPersistData() {
		Optional<Task> taskOptional = repository.findById(existingId);
		Assertions.assertTrue(taskOptional.isPresent());
		Task task = taskOptional.get();
		task.setTitle("Updated Task Title");

		Task updatedTask = repository.save(task);
		Assertions.assertEquals("Updated Task Title", updatedTask.getTitle());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Task> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

}