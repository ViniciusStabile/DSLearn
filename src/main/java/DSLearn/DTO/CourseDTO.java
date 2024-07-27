package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CourseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@Size(min = 2, max = 60, message = "Minimum 6 characters and maximum 50")
	@NotBlank(message = "Required field")
	private String name;

	@NotBlank(message = "Required field")
	private String imgUri;

	@NotBlank(message = "Required field")
	private String imgGrayUri;

	public CourseDTO(Long id, String name, String imgUri, String imgGrayUri) {
		this.id = id;
		this.name = name;
		this.imgUri = imgUri;
		this.imgGrayUri = imgGrayUri;
	}

	public CourseDTO(Course entity) {
		id = entity.getId();
		name = entity.getName();
		imgUri = entity.getImgUri();
		imgGrayUri = entity.getImgGrayUri();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgUri() {
		return imgUri;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}

	public String getImgGrayUri() {
		return imgGrayUri;
	}

	public void setImgGrayUri(String imgGrayUri) {
		this.imgGrayUri = imgGrayUri;
	}

}
