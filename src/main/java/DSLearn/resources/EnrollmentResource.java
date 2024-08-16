package DSLearn.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import DSLearn.DTO.EnrollmentDTO;
import DSLearn.entities.Offer;
import DSLearn.entities.User;
import DSLearn.entities.pk.EnrollmentPK;
import DSLearn.services.EnrollmentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/enrollments")
public class EnrollmentResource {

	@Autowired
	private EnrollmentService service;

	@GetMapping
	public ResponseEntity<Page<EnrollmentDTO>> findAll(Pageable pageable) {
		Page<EnrollmentDTO> list = service.findAll(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{userId}/{offerId}")
	public ResponseEntity<EnrollmentDTO> findById(@PathVariable Long userId, @PathVariable Long offerId) {
		EnrollmentPK id = new EnrollmentPK(new User(userId, null, null, null, null),
				new Offer(offerId, null, null, null, null));
		EnrollmentDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	public ResponseEntity<EnrollmentDTO> insert(@Valid @RequestBody EnrollmentDTO dto) {
		EnrollmentDTO newDto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();
		return ResponseEntity.created(uri).body(newDto);

	}

	@PutMapping(value = "/{userId}/{offerId}")
	public ResponseEntity<EnrollmentDTO> update(@PathVariable Long userId, @PathVariable Long offerId,
			@Valid @RequestBody EnrollmentDTO dto) {
		EnrollmentPK id = new EnrollmentPK(new User(userId, null, null, null, null),
				new Offer(offerId, null, null, null, null));
		EnrollmentDTO newDto = service.update(id, dto);
		return ResponseEntity.ok().body(newDto);

	}

	@DeleteMapping(value = "/{userId}/{offerId}")
	public ResponseEntity<Void> delete(@PathVariable Long userId, @PathVariable Long offerId) {
		EnrollmentPK id = new EnrollmentPK(new User(userId, null, null, null, null),
				new Offer(offerId, null, null, null, null));
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}