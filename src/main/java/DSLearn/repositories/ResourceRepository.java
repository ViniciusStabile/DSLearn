package DSLearn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import DSLearn.entities.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

}
