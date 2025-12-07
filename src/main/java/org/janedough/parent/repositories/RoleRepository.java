package org.janedough.parent.repositories;

import org.janedough.parent.model.AppRole;
import org.janedough.parent.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(AppRole appRole);
}
