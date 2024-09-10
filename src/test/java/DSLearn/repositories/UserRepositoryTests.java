package DSLearn.repositories;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import DSLearn.entities.Role;
import DSLearn.entities.User;

@DataJpaTest
public class UserRepositoryTests {

	@Autowired
	private UserRepository repository;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalUsers;
	private User testUser;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		countTotalUsers = 3L;

		testUser = new User(null, "John Doe", "john.doe@gmail.com", "password123", new HashSet<>());
	}

	@Test
	public void findAllShouldReturnAllUsers() {
		List<User> result = repository.findAll();
		Assertions.assertEquals(countTotalUsers, result.size());
	}

	@Test
	public void findByIdShouldReturnUserWhenIdExists() {
		Optional<User> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		Optional<User> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		User user = repository.save(testUser);
		Assertions.assertNotNull(user.getId());
		Assertions.assertEquals(countTotalUsers + 1, user.getId());
	}

	@Test
	public void saveShouldAddRolesToUser() {
		Role role = new Role(1L, "ROLE_STUDENT");
		testUser.addRole(role);
		User savedUser = repository.save(testUser);

		Optional<User> foundUser = repository.findById(savedUser.getId());
		Assertions.assertTrue(foundUser.isPresent());
		Assertions.assertTrue(foundUser.get().getRoles().contains(role));
	}

	@Test
	public void updateShouldChangeAndPersistData() {
		Optional<User> userOptional = repository.findById(existingId);
		Assertions.assertTrue(userOptional.isPresent());
		User user = userOptional.get();
		user.setName("Updated Name");

		User updatedUser = repository.save(user);
		Assertions.assertEquals("Updated Name", updatedUser.getName());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<User> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

}