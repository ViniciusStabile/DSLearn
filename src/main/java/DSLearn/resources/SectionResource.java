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

import DSLearn.DTO.SectionDTO;
import DSLearn.services.SectionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/sections")
public class SectionResource {

	@Autowired
	private SectionService service;

	@GetMapping
	public ResponseEntity<Page<SectionDTO>> findAll(Pageable pageable) {
		Page<SectionDTO> list = service.findAll(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<SectionDTO> findById(@PathVariable Long id) {
		SectionDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	public ResponseEntity<SectionDTO> insert(@Valid @RequestBody SectionDTO dto) {
		SectionDTO newDto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();
		return ResponseEntity.created(uri).body(newDto);

	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<SectionDTO> update(@PathVariable Long id, @Valid @RequestBody SectionDTO dto) {
		SectionDTO newDto = service.update(id, dto);
		return ResponseEntity.ok().body(newDto);

	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<SectionDTO> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}