package DSLearn.repositories;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import DSLearn.entities.Course;

@DataJpaTest
public class CourseRepositoryTests {

	@Autowired
	private CourseRepository repository;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalCourses;
	private Course testCourse;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		countTotalCourses = 3L;

		testCourse = new Course(null, "Python", "https://example.com/python.png",
				"https://example.com/python-gray.png");
	}

	@Test
	public void findAllShouldReturnAllCourses() {
		List<Course> result = repository.findAll();
		Assertions.assertEquals(countTotalCourses, result.size());
	}

	@Test
	public void findByIdShouldReturnCourseWhenIdExists() {
		Optional<Course> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		Optional<Course> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Course course = repository.save(testCourse);
		Assertions.assertNotNull(course.getId());
		Assertions.assertEquals(countTotalCourses + 1, course.getId());
	}

	@Test
	public void updateShouldChangeAndPersistData() {
		Optional<Course> courseOptional = repository.findById(existingId);
		Assertions.assertTrue(courseOptional.isPresent());
		Course course = courseOptional.get();
		course.setName("Updated Course");

		Course updatedCourse = repository.save(course);
		Assertions.assertEquals("Updated Course", updatedCourse.getName());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Course> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
}