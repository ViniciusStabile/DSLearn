package DSLearn.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import DSLearn.DTO.SectionDTO;
import DSLearn.entities.Resource;
import DSLearn.entities.Section;
import DSLearn.entities.enums.ResourceType;
import DSLearn.repositories.ResourceRepository;
import DSLearn.repositories.SectionRepository;
import DSLearn.services.exceptions.DatabaseException;
import DSLearn.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class SectionServiceTests {

    @InjectMocks
    private SectionService service;

    @Mock
    private SectionRepository repository;
    
    @Mock
    private ResourceRepository resourceRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private Section section;
    private SectionDTO sectionDTO;
    private Resource resource;
    private Section prerequisite;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 200L;
        dependentId = 3L;
        resource = new Resource(1L, "Resource Title", "Description", 1, "imgUrl", ResourceType.LESSON_TASK, null);
        prerequisite = new Section(2L, "Prerequisite Title", "Prerequisite Description", 2, "Prerequisite imgUrl", resource, null);
        section = new Section(existingId, "Section Title", "Description", 1, "imgUrl", resource, prerequisite);
        sectionDTO = new SectionDTO(section);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(section));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(resourceRepository.findById(existingId)).thenReturn(Optional.of(resource));
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(section);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
        Mockito.when(resourceRepository.getReferenceById(resource.getId())).thenReturn(resource);
        Mockito.when(repository.save(Mockito.any(Section.class))).thenReturn(section);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        Mockito.when(repository.save(Mockito.any(Section.class))).thenReturn(section);
    }

    @Test
    public void findByIdShouldReturnSectionDTOWhenIdExists() {
        SectionDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(sectionDTO.getId(), result.getId());
        Assertions.assertEquals(sectionDTO.getTitle(), result.getTitle());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void insertShouldReturnSectionDTOWhenSuccessful() {
        SectionDTO result = service.insert(sectionDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(sectionDTO.getId(), result.getId());
        Assertions.assertEquals(sectionDTO.getTitle(), result.getTitle());
    }

    @Test
    public void updateShouldReturnSectionDTOWhenIdExists() {
        SectionDTO result = service.update(existingId, sectionDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(sectionDTO.getId(), result.getId());
        Assertions.assertEquals(sectionDTO.getTitle(), result.getTitle());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, sectionDTO);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIntegrityViolationOccurs() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }
}
