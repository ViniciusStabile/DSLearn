package DSLearn.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import DSLearn.DTO.SectionDTO;
import DSLearn.entities.Resource;
import DSLearn.entities.Section;
import DSLearn.repositories.ResourceRepository;
import DSLearn.repositories.SectionRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class SectionService {

	@Autowired
	private SectionRepository repository;

	@Autowired
	private ResourceRepository resourceRepository;

	@Transactional(readOnly = true)
	public Page<SectionDTO> findAll(Pageable page) {
		Page<Section> list = repository.findAll(page);
		return list.map(x -> new SectionDTO(x));

	}

	@Transactional(readOnly = true)
	public SectionDTO findById(Long id) {
		Optional<Section> obj = repository.findById(id);
		Section entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new SectionDTO(entity);

	}

	@Transactional
	public SectionDTO insert(SectionDTO dto) {
		Section entity = new Section();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new SectionDTO(entity);

	}

	@Transactional
	public SectionDTO update(Long id, SectionDTO dto) {
		try {
			Section entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new SectionDTO(entity);
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

	public void copyDtoToEntity(SectionDTO dto, Section entity) {
		entity.setTitle(dto.getTitle());
		entity.setDescription(dto.getDescription());
		entity.setPosition(dto.getPosition());
		entity.setImgUrl(dto.getImgUrl());

		if (dto.getResource() != null && dto.getResource().getId() != null) {
			Resource resource = resourceRepository.getReferenceById(dto.getResource().getId());
			entity.setResource(resource);
		}

		if (dto.getPrerequisite() != null && dto.getPrerequisite().getId() != null) {
			Section prerequisite = new Section();
			prerequisite.setId(dto.getPrerequisite().getId());
			entity.setPrerequisite(prerequisite);

		}

	}
}
