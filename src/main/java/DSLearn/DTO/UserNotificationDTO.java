package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserNotificationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

	public UserNotificationDTO() {
	}

	public UserNotificationDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public UserNotificationDTO(User entity) {
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
