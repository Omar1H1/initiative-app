package com.Initiative.app.repository;

import com.Initiative.app.enums.RoleEnum;
import com.Initiative.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    Optional<User> findByActivationCode(String activationCode);

    List<User> findByRole(RoleEnum roleEnum);
}
