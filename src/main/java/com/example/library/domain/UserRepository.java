package com.example.library.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE " +
            ":lastName IS NULL OR user.lastName = :lastName")
    Set<User> findUsers(@Param("lastName") String lastName);


}
