package DSLearn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import DSLearn.entities.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

}
