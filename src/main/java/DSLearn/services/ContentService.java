package DSLearn.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import DSLearn.DTO.ContentDTO;
import DSLearn.entities.Content;
import DSLearn.repositories.ContentRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ContentService {

	@Autowired
	private ContentRepository repository;

	@Transactional(readOnly = true)
	public Page<ContentDTO> findAll(Pageable page) {
		Page<Content> list = repository.findAll(page);
		return list.map(x -> new ContentDTO(x));

	}

	@Transactional(readOnly = true)
	public ContentDTO findById(Long id) {
		Optional<Content> obj = repository.findById(id);
		Content entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ContentDTO(entity);

	}

	@Transactional
	public ContentDTO insert(ContentDTO dto) {
		Content entity = new Content();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ContentDTO(entity);

	}

	@Transactional
	public ContentDTO update(Long id, ContentDTO dto) {
		try {
			Content entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ContentDTO(entity);
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

	public void copyDtoToEntity(ContentDTO dto, Content entity) {
		entity.setTitle(dto.getTitle());
		entity.setPosition(dto.getPosition());
		entity.setTextContent(dto.getTextContent());
		entity.setVideoUrl(dto.getVideoUri());
		

	}

}
