package com.bookstore.app.repo;

import com.bookstore.app.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

public interface BookRepo extends JpaRepository<Book, UUID> {

  Optional<Book> findByName(@Param("name") String name);
}
