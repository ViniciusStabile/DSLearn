package DSLearn.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import DSLearn.DTO.CourseDTO;
import DSLearn.entities.Course;
import DSLearn.repositories.CourseRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CourseService {

	@Autowired
	private CourseRepository repository;

	@Transactional(readOnly = true)
	public Page<CourseDTO> findAll(Pageable page) {
		Page<Course> list = repository.findAll(page);
		return list.map(x -> new CourseDTO(x));

	}

	@Transactional(readOnly = true)
	public CourseDTO findById(Long id) {
		Optional<Course> obj = repository.findById(id);
		Course entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new CourseDTO(entity);

	}

	@Transactional
	public CourseDTO insert(CourseDTO dto) {
		Course entity = new Course();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new CourseDTO(entity);

	}

	@Transactional
	public CourseDTO update(Long id, CourseDTO dto) {
		try {
			Course entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new CourseDTO(entity);
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

	public void copyDtoToEntity(CourseDTO dto, Course entity) {
		entity.setName(dto.getName());
		entity.setImgUri(dto.getImgUri());
		entity.setImgGrayUri(dto.getImgGrayUri());

	}

}
