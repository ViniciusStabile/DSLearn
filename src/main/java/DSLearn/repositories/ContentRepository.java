package DSLearn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import DSLearn.entities.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {

}
