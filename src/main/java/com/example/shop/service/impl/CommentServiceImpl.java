package com.example.shop.service.impl;

import com.example.shop.model.entity.Comment;
import com.example.shop.model.entity.Product;
import com.example.shop.repository.CommentRepository;
import com.example.shop.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Comment initializeComment(String username, String message, String rating, Product product) {

        Comment comment = new Comment(username, message, Integer.parseInt(rating), LocalDateTime.now());
        comment.setProduct(product);
        this.commentRepository.saveAndFlush(comment);
        return comment;
    }

    @Override
    public String deleteCommentById(String id) {
        Comment comment = this.commentRepository.getCommentById(id).get();
        String productId = comment.getProduct().getId();
        comment.setProduct(null);
        this.commentRepository.saveAndFlush(comment);
        this.commentRepository.deleteById(id);
        return productId;
    }

    @Override
    public Integer getCommentsCount() {
        return Math.toIntExact(this.commentRepository.count());
    }
}
