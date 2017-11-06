package com.example.library.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE " +
            ":lastName IS NULL OR user.lastName = :lastName " +
            "ORDER BY user.lastName, user.firstName")
    List<User> findUsers(@Param("lastName") String lastName);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

}
