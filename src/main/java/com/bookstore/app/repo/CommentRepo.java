package com.bookstore.app.repo;

import com.bookstore.app.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CommentRepo extends JpaRepository<Comment, UUID> {
}
