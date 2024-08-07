package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.Resource;

public class ResourceMinDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;

	public ResourceMinDTO() {
	}

	public ResourceMinDTO(Long id, String title) {
		this.id = id;
		this.title = title;

	}

	public ResourceMinDTO(Resource entity) {
		id = entity.getId();
		title = entity.getTitle();

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

}
