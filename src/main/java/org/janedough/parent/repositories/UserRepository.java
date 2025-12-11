package org.janedough.parent.repositories;

import org.janedough.parent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername (String username);

    Optional<User> findByPhoneNumber(String phoneNumber);
}
