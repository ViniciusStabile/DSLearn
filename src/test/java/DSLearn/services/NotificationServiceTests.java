package DSLearn.services;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import DSLearn.DTO.NotificationDTO;
import DSLearn.entities.Notification;
import DSLearn.entities.Role;
import DSLearn.entities.User;
import DSLearn.repositories.NotificationRepository;
import DSLearn.repositories.UserRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class NotificationServiceTests {

	@InjectMocks
	private NotificationService service;

	@Mock
	private NotificationRepository repository;
	
	@Mock
	private UserRepository userRepository;

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Notification notification;
	private NotificationDTO notificationDTO;
	private User user;
	private Role role;
	private Set<Role> roles;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 200L;
		dependentId = 3L;
		role = new Role(1L, "ROLE_STUDENT");
		roles = new HashSet<>();
		roles.add(role);
		user = new User(1L, "Alex", "alex@gmail.com", "$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG",
				roles);
		notification = new Notification(existingId, "Notification Text", Instant.now(), false, "route", user);
		notificationDTO = new NotificationDTO(notification);

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(notification));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.findById(dependentId)).thenReturn(Optional.of(notification));
		Mockito.when(repository.save(Mockito.any(Notification.class))).thenReturn(notification);
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(notification);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		Mockito.when(userRepository.existsById(existingId)).thenReturn(true);
		Mockito.when(userRepository.existsById(nonExistingId)).thenReturn(true);
		Mockito.when(userRepository.existsById(dependentId)).thenReturn(true);
	}

	@Test
	public void findByIdShouldReturnNotificationDTOWhenIdExists() {
		NotificationDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(notificationDTO.getId(), result.getId());
		Assertions.assertEquals(notificationDTO.getText(), result.getText());
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	public void insertShouldReturnNotificationDTOWhenSuccessful() {
		NotificationDTO result = service.insert(notificationDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(notificationDTO.getId(), result.getId());
		Assertions.assertEquals(notificationDTO.getText(), result.getText());
	}

	@Test
	public void updateShouldReturnNotificationDTOWhenIdExists() {
		NotificationDTO result = service.update(existingId, notificationDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(notificationDTO.getId(), result.getId());
		Assertions.assertEquals(notificationDTO.getText(), result.getText());
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, notificationDTO);
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
