package DSLearn.DTO;

import java.io.Serializable;
import java.time.Instant;

import DSLearn.entities.Offer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OfferDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "Edition is required")
    private String edition;

    @NotNull(message = "Start moment is required")
    private Instant startMoment;

    @NotNull(message = "End moment is required")
    private Instant endMoment;

    @NotNull(message = "Course is required")
    private CourseMinDTO course;

	public OfferDTO() {
	}

	public OfferDTO(Long id, String edition, Instant startMoment, Instant endMoment, CourseMinDTO course) {
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
		course = new CourseMinDTO(entity.getCourse());
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

	public CourseMinDTO getCourse() {
		return course;
	}

	public void setCourse(CourseMinDTO course) {
		this.course = course;
	}

}
