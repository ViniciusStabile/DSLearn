package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RoleDTO implements Serializable {

	private static final long serialVersionUID = 1L;


	private Long id;

	@NotBlank(message = "Authority is required")
	@Size(min = 2, max = 50, message = "Authority must be between 2 and 50 characters")
	private String authority;

	public RoleDTO() {

	}

	public RoleDTO(Long id, String authority) {
		super();
		this.id = id;
		this.authority = authority;
	}

	public RoleDTO(Role entity) {
		id = entity.getId();
		authority = entity.getAuthority();
	}

	public Long getId() {
		return id;
	}

	public String getAuthority() {
		return authority;
	}

}
