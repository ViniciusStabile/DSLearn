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

import DSLearn.DTO.TaskDTO;
import DSLearn.entities.Resource;
import DSLearn.entities.Section;
import DSLearn.entities.Task;
import DSLearn.entities.enums.ResourceType;
import DSLearn.repositories.TaskRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class TaskServiceTests {

	@InjectMocks
	private TaskService service;

	@Mock
	private TaskRepository repository;

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private Task task;
	private TaskDTO taskDTO;
	private PageImpl<Task> page;

	@BeforeEach
	void setUp() throws Exception {
	    existingId = 1L;
	    nonExistingId = 2L;
	    dependentId = 3L;

	    Resource resource = new Resource(1L, "Resource Title", "Resource Description", 1, "imgUrl", ResourceType.LESSON_TASK, null);
	    Section prerequisite = new Section(2L, "Prerequisite Title", "Prerequisite Description", 1, "imgUrl", resource, null);
	    Section section = new Section(1L, "Section Title", "Section Description", 1, "imgUrl", resource, prerequisite);

	    task = new Task(existingId, "Task Title", 1, section, "Task Description", 10, 5, 1.5, Instant.now());
	    taskDTO = new TaskDTO(task);
	    page = new PageImpl<>(List.of(task));

	    Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(task));
	    Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
	    Mockito.when(repository.findAll((Pageable) any())).thenReturn(page);
	    Mockito.when(repository.save(any())).thenReturn(task);
	    Mockito.when(repository.getReferenceById(existingId)).thenReturn(task);
	    Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
	    Mockito.when(repository.existsById(existingId)).thenReturn(true);
	    Mockito.when(repository.existsById(dependentId)).thenReturn(true);
	    Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
	    Mockito.doNothing().when(repository).deleteById(existingId);
	    Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	@Test
	public void findByIdShouldReturnTaskDTOWhenIdExists() {
		TaskDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingId);
		Assertions.assertEquals(result.getTitle(), task.getTitle());
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	public void findAllShouldReturnPagedTaskDTO() {
		Pageable pageable = PageRequest.of(0, 12);

		Page<TaskDTO> result = service.findAll(pageable);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getSize(), 1);
		Assertions.assertEquals(result.iterator().next().getTitle(), task.getTitle());
	}

	@Test
	public void insertShouldReturnTaskDTO() {
		TaskDTO result = service.insert(taskDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), task.getId());
	}

	@Test
	public void updateShouldReturnTaskDTOWhenIdExists() {
		TaskDTO result = service.update(existingId, taskDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingId);
		Assertions.assertEquals(result.getTitle(), taskDTO.getTitle());
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, taskDTO);
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
	public void deleteShouldThrowDatabaseExceptionWhenDependentIdExists() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
	}
}
