package DSLearn.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import DSLearn.DTO.ResourceDTO;
import DSLearn.entities.Offer;
import DSLearn.entities.Resource;
import DSLearn.repositories.OfferRepository;
import DSLearn.repositories.ResourceRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ResourceService {

	@Autowired
	private ResourceRepository repository;

	@Autowired
	private OfferRepository offerRepository;

	@Transactional(readOnly = true)
	public Page<ResourceDTO> findAll(Pageable page) {
		Page<Resource> list = repository.findAll(page);
		return list.map(x -> new ResourceDTO(x));

	}

	@Transactional(readOnly = true)
	public ResourceDTO findById(Long id) {
		Optional<Resource> obj = repository.findById(id);
		Resource entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ResourceDTO(entity);

	}

	@Transactional
	public ResourceDTO insert(ResourceDTO dto) {
		Resource entity = new Resource();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ResourceDTO(entity);

	}

	@Transactional
	public ResourceDTO update(Long id, ResourceDTO dto) {
		try {
			Resource entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ResourceDTO(entity);
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

	private void copyDtoToEntity(ResourceDTO dto, Resource entity) {
		entity.setId(dto.getId());
		entity.setTitle(dto.getTitle());
		entity.setDescription(dto.getDescription());
		entity.setPosition(dto.getPosition());
		entity.setImgUrl(dto.getImgUrl());
		entity.setType(dto.getType());

		if (dto.getOffer() != null) {
			Long offerId = dto.getOffer().getId();
			Offer offer = offerRepository.findById(offerId)
					.orElseThrow(() -> new ResourceNotFoundException("Offer not found for ID: " + offerId));
			entity.setOffer(offer);
		} else {
			entity.setOffer(null);
		}

	}

}
