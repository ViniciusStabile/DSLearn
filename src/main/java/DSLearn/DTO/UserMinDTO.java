package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.User;

public class UserMinDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;

	public UserMinDTO() {
	}

	public UserMinDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public UserMinDTO(User entity) {
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
