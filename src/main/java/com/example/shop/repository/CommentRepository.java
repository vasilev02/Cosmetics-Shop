package com.example.shop.repository;

import com.example.shop.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    Optional<Comment> getCommentById(String id);
}
