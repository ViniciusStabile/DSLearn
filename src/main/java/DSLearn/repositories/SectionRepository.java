package DSLearn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import DSLearn.entities.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

}
