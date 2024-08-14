package DSLearn.DTO;

import DSLearn.entities.pk.EnrollmentPK;

public class EnrollmentPKDTO {

	private UserDTO user;
	private OfferDTO offer;

	public EnrollmentPKDTO() {
	}

	public EnrollmentPKDTO(UserDTO user, OfferDTO offer) {
		this.user = user;
		this.offer = offer;
	}

	public EnrollmentPKDTO(EnrollmentPK entity) {
		user = new UserDTO(entity.getUser());
		offer = new OfferDTO(entity.getOffer());
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public OfferDTO getOffer() {
		return offer;
	}

	public void setOffer(OfferDTO offer) {
		this.offer = offer;
	}
}