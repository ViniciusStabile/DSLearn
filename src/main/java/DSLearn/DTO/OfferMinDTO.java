package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.Offer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OfferMinDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "Edition is required")
    @Size(min = 2, max = 50, message = "Edition must be between 2 and 50 characters")
    private String edition;

	public OfferMinDTO() {
	}

	public OfferMinDTO(Long id, String edition) {
		this.id = id;
		this.edition = edition;

	}

	public OfferMinDTO(Offer entity) {
		id = entity.getId();
		edition = entity.getEdition();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

}
