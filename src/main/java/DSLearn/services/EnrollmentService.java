package DSLearn.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import DSLearn.DTO.EnrollmentDTO;
import DSLearn.DTO.LessonDTO;
import DSLearn.DTO.OfferMinDTO;
import DSLearn.DTO.UserMinDTO;
import DSLearn.entities.Enrollment;
import DSLearn.entities.Lesson;
import DSLearn.entities.Offer;
import DSLearn.entities.User;
import DSLearn.entities.pk.EnrollmentPK;
import DSLearn.repositories.EnrollmentRepository;
import DSLearn.repositories.LessonRepository;
import DSLearn.repositories.OfferRepository;
import DSLearn.repositories.UserRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class EnrollmentService {

	@Autowired
	private EnrollmentRepository repository;

	@Autowired
	private LessonRepository lessonRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OfferRepository offerRepository;
	
	

	@Transactional(readOnly = true)
	public Page<EnrollmentDTO> findAll(Pageable page) {
		Page<Enrollment> list = repository.findAll(page);
		return list.map(x -> new EnrollmentDTO(x));

	}

	@Transactional(readOnly = true)
	public EnrollmentDTO findById(EnrollmentPK id) {
		Optional<Enrollment> obj = repository.findById(id);
		Enrollment entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new EnrollmentDTO(entity);

	}

	@Transactional
	public EnrollmentDTO insert(EnrollmentDTO dto) {
		Enrollment entity = new Enrollment();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new EnrollmentDTO(entity);

	}

	@Transactional
	public EnrollmentDTO update(EnrollmentPK id, EnrollmentDTO dto) {
		try {
			Enrollment entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new EnrollmentDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID not found");
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(EnrollmentPK id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}

	public void copyDtoToEntity(EnrollmentDTO dto, Enrollment entity) {
		entity.setEnrollMoment(dto.getEnrollMoment());
		entity.setRefundMoment(dto.getRefundMoment());
		entity.setAvailable(dto.isAvailable());
		entity.setOnlyUpdate(dto.isOnlyUpdate());
		
		UserMinDTO user = dto.getId().getUser();
		User userEntity = userRepository.getReferenceById(user.getId());
		entity.setStudent(userEntity);
		
		OfferMinDTO offer = dto.getId().getOffer();
		Offer offerEntity = offerRepository.getReferenceById(offer.getId());
		entity.setOffer(offerEntity);
		
		entity.getLessonsDone().clear();
		for (LessonDTO lessonDTO : dto.getLessonsDone()) {
		    Lesson lesson = lessonRepository.getReferenceById(lessonDTO.getId());
		    entity.getLessonsDone().add(lesson);
		}

		//UserMinDTO user = dto.getId().getUser();
		//OfferMinDTO offer = dto.getId().getOffer();

		//entity.getStudent().setId(user.getId());
		//entity.getOffer().setId(offer.getId());

		//entity.getLessonsDone().clear();
		//for (LessonDTO lessonDTO : dto.getLessonsDone()) {
		//Lesson lesson = LessonRepository.getReferenceById(lessonDTO.getId());
		//entity.getLessonsDone().add(lesson);
		}

	}


