package DSLearn.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import DSLearn.DTO.OfferDTO;
import DSLearn.entities.Course;
import DSLearn.entities.Offer;
import DSLearn.repositories.CourseRepository;
import DSLearn.repositories.OfferRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class OfferService {

	@Autowired
	private OfferRepository repository;

	@Autowired
	private CourseRepository courseRepository;

	@Transactional(readOnly = true)
	public Page<OfferDTO> findAll(Pageable page) {
		Page<Offer> list = repository.findAll(page);
		return list.map(x -> new OfferDTO(x));

	}

	@Transactional(readOnly = true)
	public OfferDTO findById(Long id) {
		Optional<Offer> obj = repository.findById(id);
		Offer entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new OfferDTO(entity);

	}

	@Transactional
	public OfferDTO insert(OfferDTO dto) {
		Offer entity = new Offer();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new OfferDTO(entity);

	}

	@Transactional
	public OfferDTO update(Long id, OfferDTO dto) {
		try {
			Offer entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new OfferDTO(entity);
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

	private void copyDtoToEntity(OfferDTO dto, Offer entity) {
		entity.setId(dto.getId());
		entity.setEdition(dto.getEdition());
		entity.setStartMoment(dto.getStartMoment());
		entity.setEndMoment(dto.getEndMoment());

		if (dto.getCourse() != null) {
			Long courseId = dto.getCourse().getId();
			Course course = courseRepository.findById(courseId)
					.orElseThrow(() -> new ResourceNotFoundException("Course not found for ID: " + courseId));
			entity.setCourse(course);
		} else {
			entity.setCourse(null);
		}

	}
}
