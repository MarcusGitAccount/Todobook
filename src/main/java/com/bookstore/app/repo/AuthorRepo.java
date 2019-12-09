package com.bookstore.app.repo;

import com.bookstore.app.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AuthorRepo extends JpaRepository<Author, UUID> {
}
