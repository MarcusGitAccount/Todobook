package com.bookstore.app.repo;

import com.bookstore.app.entity.AuthenticationJWTToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface AuthenticationJWTTokenRepo extends JpaRepository<AuthenticationJWTToken, String> {

  Optional<AuthenticationJWTToken> findByUserEmail(String userEmail);
}
