package DSLearn.DTO;

import java.io.Serializable;
import java.time.Instant;

import DSLearn.entities.Offer;
import jakarta.validation.constraints.NotBlank;

public class OfferDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@NotBlank(message = "Required field")
	private String edition;

	private Instant startMoment;

	private Instant endMoment;

	private CourseDTO course;

	public OfferDTO() {
	}

	public OfferDTO(Long id, String edition, Instant startMoment, Instant endMoment, CourseDTO course) {
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
		course = new CourseDTO(entity.getCourse());
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

	public CourseDTO getCourse() {
		return course;
	}

	public void setCourse(CourseDTO course) {
		this.course = course;
	}

}
