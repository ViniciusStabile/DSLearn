package DSLearn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import DSLearn.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
