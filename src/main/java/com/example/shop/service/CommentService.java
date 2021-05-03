package com.example.shop.service;

import com.example.shop.model.entity.Comment;
import com.example.shop.model.entity.Product;

public interface CommentService {
    Comment initializeComment(String username, String message, String rating, Product product);

    String deleteCommentById(String id);

    Integer getCommentsCount();
}
