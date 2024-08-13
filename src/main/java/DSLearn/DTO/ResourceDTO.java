package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.Resource;
import DSLearn.entities.enums.ResourceType;

public class ResourceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;
	private String description;
	private Integer position;
	private String imgUrl;
	private ResourceType type;
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