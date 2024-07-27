package DSLearn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import DSLearn.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
