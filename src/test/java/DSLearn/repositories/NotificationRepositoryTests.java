package DSLearn.repositories;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import DSLearn.entities.Notification;
import DSLearn.entities.User;

@DataJpaTest
public class NotificationRepositoryTests {

	@Autowired
	private NotificationRepository repository;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalNotifications;
	private Notification testNotification;
	private User testUser;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		countTotalNotifications = 3L;

		testUser = new User(null, "John Doe", "john.doe@gmail.com", "password123", new HashSet<>());
		testNotification = new Notification(null, "New task feedback", Instant.now(), false,
				"/offers/1/resource/1/sections/1", testUser);
	}

	@Test
	public void findAllShouldReturnAllNotifications() {
		List<Notification> result = repository.findAll();
		Assertions.assertEquals(countTotalNotifications, result.size());
	}

	@Test
	public void findByIdShouldReturnNotificationWhenIdExists() {
		Optional<Notification> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		Optional<Notification> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Notification notification = repository.save(testNotification);
		Assertions.assertNotNull(notification.getId());
		Assertions.assertEquals(countTotalNotifications + 1, notification.getId());
	}

	@Test
	public void updateShouldChangeAndPersistData() {
		Optional<Notification> notificationOptional = repository.findById(existingId);
		Assertions.assertTrue(notificationOptional.isPresent());
		Notification notification = notificationOptional.get();
		notification.setText("Updated feedback");

		Notification updatedNotification = repository.save(notification);
		Assertions.assertEquals("Updated feedback", updatedNotification.getText());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Notification> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
}