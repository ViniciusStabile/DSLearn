package DSLearn.services;

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

import DSLearn.DTO.CourseDTO;
import DSLearn.entities.Course;
import DSLearn.repositories.CourseRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class CourseServiceTests {

	@InjectMocks
	private CourseService service;

	@Mock
	private CourseRepository repository;

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Course course;
	private CourseDTO courseDTO;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 200L;
		dependentId = 3L;
		course = new Course(existingId, "Course Name", "imgUri", "imgGrayUri");
		courseDTO = new CourseDTO(course);

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(course));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(Mockito.any(Course.class))).thenReturn(course);
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(course);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}

	@Test
	public void findByIdShouldReturnCourseDTOWhenIdExists() {
		CourseDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(courseDTO.getId(), result.getId());
		Assertions.assertEquals(courseDTO.getName(), result.getName());
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	public void insertShouldReturnCourseDTOWhenSuccessful() {
		CourseDTO result = service.insert(courseDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(courseDTO.getId(), result.getId());
		Assertions.assertEquals(courseDTO.getName(), result.getName());
	}
	

	@Test
	public void updateShouldReturnCourseDTOWhenIdExists() {
		CourseDTO result = service.update(existingId, courseDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(courseDTO.getId(), result.getId());
		Assertions.assertEquals(courseDTO.getName(), result.getName());
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, courseDTO);
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