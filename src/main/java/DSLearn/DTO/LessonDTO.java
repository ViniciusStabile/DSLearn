package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.Lesson;

public class LessonDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;
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
