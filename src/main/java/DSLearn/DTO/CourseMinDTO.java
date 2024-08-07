package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CourseMinDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@Size(min = 2, max = 60, message = "Minimum 6 characters and maximum 50")
	@NotBlank(message = "Required field")
	private String name;

	public CourseMinDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public CourseMinDTO(Course entity) {
		id = entity.getId();
		name = entity.getName();
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

}
