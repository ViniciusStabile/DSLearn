package DSLearn.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class Lesson implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String title;
	private String position;
	private Section section;

	private Set<Enrollment> enrollmentDone = new HashSet<>();

	public Lesson() {

	}

	public Lesson(Long id, String title, String position, Section section) {
		this.id = id;
		this.title = title;
		this.position = position;
		this.section = section;
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Set<Enrollment> getEnrollmentDone() {
		return enrollmentDone;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lesson other = (Lesson) obj;
		return Objects.equals(id, other.id);
	}

}
