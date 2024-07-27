package DSLearn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import DSLearn.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByAuthority(String authority);
	
}
