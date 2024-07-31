package DSLearn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import DSLearn.entities.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {

}
