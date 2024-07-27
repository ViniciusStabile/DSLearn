package DSLearn.DTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import DSLearn.entities.Role;
import DSLearn.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@Size(min = 2, message = "Minimum 2 characters")
	@NotBlank(message = "Required field")
	private String name;

	@NotBlank(message = "Required field")
	@Email(message = "Invalid imail")
	private String email;

	@Size(min = 6, max = 50, message = "Minimum 6 characters and maximum 50")
	@NotBlank(message = "Required field")
	private String password;

	Set<RoleDTO> roles = new HashSet<>();

	public UserDTO() {

	}

	public UserDTO(Long id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public UserDTO(User entity) {
		id = entity.getId();
		name = entity.getName();
		email = entity.getEmail();
		password = entity.getPassword();

		for (Role role : entity.getRoles()) {
			RoleDTO roleDTO = new RoleDTO(role);
			this.roles.add(roleDTO);
		}
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

}
