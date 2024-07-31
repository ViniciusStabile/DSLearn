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

import DSLearn.DTO.OfferDTO;
import DSLearn.services.OfferService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/offers")
public class OfferResource {

	@Autowired
	private OfferService service;

	@GetMapping
	public ResponseEntity<Page<OfferDTO>> findAll(Pageable pageable) {
		Page<OfferDTO> list = service.findAll(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<OfferDTO> findById(@PathVariable Long id) {
		OfferDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	public ResponseEntity<OfferDTO> insert(@Valid @RequestBody OfferDTO dto) {
		OfferDTO newDto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();
		return ResponseEntity.created(uri).body(newDto);

	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<OfferDTO> update(@PathVariable Long id, @Valid @RequestBody OfferDTO dto) {
		OfferDTO newDto = service.update(id, dto);
		return ResponseEntity.ok().body(newDto);

	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<OfferDTO> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
}