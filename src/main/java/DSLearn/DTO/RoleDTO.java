package DSLearn.DTO;

import java.io.Serializable;

import DSLearn.entities.Role;

public class RoleDTO implements Serializable {

	
	private static final long serialVersionUID = 1L;

	private Long id;
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

