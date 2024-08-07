package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.Section;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SectionDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@Size(min = 2, max = 60, message = "Minimum 6 characters and maximum 50")
	@NotBlank(message = "Required field")
	private String title;
	private String description;
	private Integer position;
	private String imgUrl;
	private ResourceMinDTO resource;
	private SectionDTO prerequisite;

	public SectionDTO() {

	}

	public SectionDTO(Long id, String title, String description, Integer position, String imgUrl,
			ResourceMinDTO resource, SectionDTO prerequisite) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.position = position;
		this.imgUrl = imgUrl;
		this.resource = resource;
		this.prerequisite = prerequisite;
	}

	public SectionDTO(Section entity) {

		id = entity.getId();
		title = entity.getTitle();
		description = entity.getDescription();
		position = entity.getPosition();
		imgUrl = entity.getImgUrl();
		resource = new ResourceMinDTO(entity.getResource());
		if (entity.getPrerequisite() != null) {
			prerequisite = new SectionDTO(entity.getPrerequisite());
		}
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

	public ResourceMinDTO getResource() {
		return resource;
	}

	public void setResource(ResourceMinDTO resource) {
		this.resource = resource;
	}

	public SectionDTO getPrerequisite() {
		return prerequisite;
	}

	public void setPrerequisite(SectionDTO prerequisite) {
		this.prerequisite = prerequisite;
	}

}
