package DSLearn.DTO;

import DSLearn.entities.pk.EnrollmentPK;
import jakarta.validation.constraints.NotNull;

public class EnrollmentPKDTO {

	@NotNull(message = "User is required")
    private UserMinDTO user;

    @NotNull(message = "Offer is required")
    private OfferMinDTO offer;
	public EnrollmentPKDTO() {
	}

	public EnrollmentPKDTO(UserMinDTO user, OfferMinDTO offer) {
		this.user = user;
		this.offer = offer;
	}

	public EnrollmentPKDTO(EnrollmentPK entity) {
		user = new UserMinDTO(entity.getUser());
		offer = new OfferMinDTO(entity.getOffer());
	}

	public UserMinDTO getUser() {
		return user;
	}

	public void setUser(UserMinDTO user) {
		this.user = user;
	}

	public OfferMinDTO getOffer() {
		return offer;
	}

	public void setOffer(OfferMinDTO offer) {
		this.offer = offer;
	}

}