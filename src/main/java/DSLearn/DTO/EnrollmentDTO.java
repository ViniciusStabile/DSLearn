package DSLearn.DTO;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import DSLearn.entities.Enrollment;

public class EnrollmentDTO {

	private EnrollmentPKDTO id;
	private Instant enrollMoment;
	private Instant refundMoment;
	private boolean available;
	private boolean onlyUpdate;

	private Set<LessonDTO> lessonsDone = new HashSet<>();

	public EnrollmentDTO() {
	}

	public EnrollmentDTO(EnrollmentPKDTO id, Instant enrollMoment, Instant refundMoment, boolean available,
			boolean onlyUpdate) {
		this.id = id;
		this.enrollMoment = enrollMoment;
		this.refundMoment = refundMoment;
		this.available = available;
		this.onlyUpdate = onlyUpdate;
	}

	public EnrollmentDTO(Enrollment entity) {
		this.id = new EnrollmentPKDTO(new UserDTO(entity.getStudent()), new OfferDTO(entity.getOffer()));
		this.enrollMoment = entity.getEnrollMoment();
		this.refundMoment = entity.getRefundMoment();
		this.available = entity.isAvailable();
		this.onlyUpdate = entity.isOnlyUpdate();
		entity.getLessonsDone().forEach(lesson -> this.lessonsDone.add(new LessonDTO(lesson)));
	}

	public EnrollmentPKDTO getId() {
		return id;
	}

	public void setId(EnrollmentPKDTO id) {
		this.id = id;
	}

	public Instant getEnrollMoment() {
		return enrollMoment;
	}

	public void setEnrollMoment(Instant enrollMoment) {
		this.enrollMoment = enrollMoment;
	}

	public Instant getRefundMoment() {
		return refundMoment;
	}

	public void setRefundMoment(Instant refundMoment) {
		this.refundMoment = refundMoment;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isOnlyUpdate() {
		return onlyUpdate;
	}

	public void setOnlyUpdate(boolean onlyUpdate) {
		this.onlyUpdate = onlyUpdate;
	}

	public Set<LessonDTO> getLessonsDone() {
		return lessonsDone;
	}

	public void setLessonsDone(Set<LessonDTO> lessonsDone) {
		this.lessonsDone = lessonsDone;
	}

}