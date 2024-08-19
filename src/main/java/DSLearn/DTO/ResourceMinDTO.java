package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ResourceMinDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "ID is required")
	private Long id;

	@NotBlank(message = "Title is required")
	@Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
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
