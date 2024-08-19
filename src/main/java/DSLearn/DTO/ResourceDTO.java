package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.Resource;
import DSLearn.entities.enums.ResourceType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ResourceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @Min(value = 1, message = "Position must be greater than or equal to 1")
    @Max(value = 1000, message = "Position must be less than or equal to 1000")
    private Integer position;

    @NotBlank(message = "Image URL is required")
    @Size(min = 5, max = 255, message = "Image URL must be between 5 and 255 characters")
    private String imgUrl;

    @NotNull(message = "Type is required")
    private ResourceType type;

    @NotNull(message = "Offer is required")
    private OfferMinDTO offer;

	public ResourceDTO() {
	}

	public ResourceDTO(Long id, String title, String description, Integer position, String imgUrl, ResourceType type,
			OfferMinDTO offer) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.position = position;
		this.imgUrl = imgUrl;
		this.type = type;
		this.offer = offer;
	}

	public ResourceDTO(Resource entity) {
		id = entity.getId();
		title = entity.getTitle();
		description = entity.getDescription();
		position = entity.getPosition();
		imgUrl = entity.getImgUrl();
		type = entity.getType();
		offer = new OfferMinDTO(entity.getOffer());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

	public OfferMinDTO getOffer() {
		return offer;
	}

	public void setOffer(OfferMinDTO offer) {
		this.offer = offer;
	}

}
