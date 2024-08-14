package DSLearn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import DSLearn.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
