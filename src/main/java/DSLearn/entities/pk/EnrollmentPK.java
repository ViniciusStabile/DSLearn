package DSLearn.entities.pk;

import java.io.Serializable;
import java.util.Objects;

import DSLearn.entities.Offer;
import DSLearn.entities.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class EnrollmentPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "offer_id")
	private Offer offer;
	
	public EnrollmentPK() {
		
	}

	public EnrollmentPK(User user, Offer offer) {
		super();
		this.user = user;
		this.offer = offer;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	@Override
	public int hashCode() {
		return Objects.hash(offer, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnrollmentPK other = (EnrollmentPK) obj;
		return Objects.equals(offer, other.offer) && Objects.equals(user, other.user);
	}

	
	
}
