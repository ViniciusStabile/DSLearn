package DSLearn.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import DSLearn.DTO.NotificationDTO;
import DSLearn.entities.Notification;
import DSLearn.entities.User;
import DSLearn.repositories.NotificationRepository;
import DSLearn.repositories.UserRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class NotificationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NotificationRepository repository;

	@Transactional(readOnly = true)
	public Page<NotificationDTO> findAll(Pageable page) {
		Page<Notification> list = repository.findAll(page);
		return list.map(x -> new NotificationDTO(x));

	}

	@Transactional(readOnly = true)
	public NotificationDTO findById(Long id) {
		Optional<Notification> obj = repository.findById(id);
		Notification entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new NotificationDTO(entity);

	}

	@Transactional
	public NotificationDTO insert(NotificationDTO dto) {
		Notification entity = new Notification();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new NotificationDTO(entity);

	}

	@Transactional
	public NotificationDTO update(Long id, NotificationDTO dto) {
		try {
			Notification entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new NotificationDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID not found");
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}

	public void copyDtoToEntity(NotificationDTO dto, Notification entity) {
		entity.setText(dto.getText());
		entity.setMoment(dto.getMoment());
		entity.setRead(dto.isRead());
		entity.setRoute(dto.getRoute());

		if (dto.getUser() != null && dto.getUser().getId() != null) {
			User user = userRepository.getReferenceById(dto.getUser().getId());
			entity.setUser(user);
		}

	}

}
