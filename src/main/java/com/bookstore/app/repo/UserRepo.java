package com.bookstore.app.repo;

import com.bookstore.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
//  @Query("select u from User u where u.email = email")
  Optional<User> findByEmail(@Param("email") String email);
}
