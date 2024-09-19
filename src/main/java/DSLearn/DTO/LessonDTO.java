package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.Lesson;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LessonDTO implements Serializable {

	private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @Min(value = 1, message = "Position must be greater than or equal to 1")
    @Max(value = 1000, message = "Position must be less than or equal to 1000")
    private Integer position;
	
	
	public LessonDTO() {
		super();
	}

	public LessonDTO(Long id, String title, Integer position) {
		super();
		this.id = id;
		this.title = title;
		this.position = position;
	}
	
	public LessonDTO(Lesson dto) {
		id = dto.getId();
		title = dto.getTitle();
		position = dto.getPosition();
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

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
	

}
