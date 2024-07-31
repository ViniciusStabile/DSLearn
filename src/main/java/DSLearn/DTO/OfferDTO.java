package DSLearn.DTO;

import java.io.Serializable;
import java.time.Instant;

import DSLearn.entities.Course;
import DSLearn.entities.Offer;
import jakarta.validation.constraints.NotBlank;

public class OfferDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@NotBlank(message = "Required field")
	private String edition;

	@NotBlank(message = "Required field")
	private Instant startMoment;

	@NotBlank(message = "Required field")
	private Instant endMoment;

	@NotBlank(message = "Required field")
	private Course course;

	public OfferDTO() {
	}

	public OfferDTO(Long id, String edition, Instant startMoment, Instant endMoment, Course course) {
		this.id = id;
		this.edition = edition;
		this.startMoment = startMoment;
		this.endMoment = endMoment;
		this.course = course;
	}

	public OfferDTO(Offer entity) {
		id = entity.getId();
		edition = entity.getEdition();
		startMoment = entity.getStartMoment();
		endMoment = entity.getEndMoment();
		course = entity.getCourse();
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

	public Instant getStartMoment() {
		return startMoment;
	}

	public void setStartMoment(Instant startMoment) {
		this.startMoment = startMoment;
	}

	public Instant getEndMoment() {
		return endMoment;
	}

	public void setEndMoment(Instant endMoment) {
		this.endMoment = endMoment;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	

}
