package com.bookstore.app.repo;

import com.bookstore.app.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CompanyRepo extends JpaRepository<Company, UUID> {
}
