package com.bookstore.app.repo;

import com.bookstore.app.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CategoryRepo extends JpaRepository<Category, UUID> {
}
