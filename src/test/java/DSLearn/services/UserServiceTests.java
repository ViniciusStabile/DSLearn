package DSLearn.services;

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

import DSLearn.DTO.UserDTO;
import DSLearn.entities.Role;
import DSLearn.entities.User;
import DSLearn.repositories.RoleRepository;
import DSLearn.repositories.UserRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;

	@Mock
	private RoleRepository roleRepository;

	private Long existingId;
	private Long nonExistingId;
	private User user;
	private UserDTO userDTO;
	private Role role;
	Set<Role> roles;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		role = new Role(1L, "ROLE_STUDENT");
		roles = new HashSet<>();
		roles.add(role);
		user = new User(existingId, "Alex", "alex@gmail.com",
				"$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG", roles);
		userDTO = new UserDTO(user);

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(user));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

	}

	@Test
	public void findByIdShouldReturnUserDTOWhenIdExists() {
		UserDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(userDTO.getId(), result.getId());
		Assertions.assertEquals(userDTO.getName(), result.getName());
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	public void insertShouldReturnUserDTOWhenSuccessful() {
		Mockito.when(roleRepository.findByAuthority("ROLE_STUDENT")).thenReturn(role);
		Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);

		UserDTO result = service.insert(userDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(userDTO.getId(), result.getId());
		Assertions.assertEquals(userDTO.getName(), result.getName());
		Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(User.class));
	}

	@Test
	public void updateShouldReturnUserDTOWhenIdExists() {
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(user);
		Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);
		Mockito.when(roleRepository.getReferenceById(role.getId())).thenReturn(role);

		user.getRoles().add(role);

		UserDTO result = service.update(existingId, userDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(userDTO.getId(), result.getId());
		Assertions.assertEquals(userDTO.getName(), result.getName());
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(existingId);
		Mockito.verify(repository, Mockito.times(1)).save(user);
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, userDTO);
		});

		Mockito.verify(repository, Mockito.times(1)).getReferenceById(nonExistingId);
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.doNothing().when(repository).deleteById(existingId);

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		Mockito.verify(repository, Mockito.times(1)).existsById(existingId);
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

		Mockito.verify(repository, Mockito.times(1)).existsById(nonExistingId);
		Mockito.verify(repository, Mockito.never()).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIntegrityViolationOccurs() {
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(existingId);

		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(existingId);
		});

		Mockito.verify(repository, Mockito.times(1)).existsById(existingId);
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
}
