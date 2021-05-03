package com.example.shop.aop;

import com.example.shop.model.entity.Comment;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Aspect
@Component
public class CommentAspect {

    @Pointcut("execution(* com.example.shop.service.impl.CommentServiceImpl.initializeComment(..))")
    public void trackAddingComment() {
    }

    @AfterReturning(pointcut = "trackAddingComment()", returning = "comment")
    public void loggingProductAfterReturning(JoinPoint joinPoint, Object comment) throws IOException {
        Comment commentDetails = (Comment) comment;
        FileWriter myWriter = new FileWriter("src/main/java/com/example/shop/log/AddingCommentsDetails.log", true);
        myWriter.write(String.format("Username: {%s} added comment with rating {%s} and message {%s} !%n", commentDetails.getUsername(), commentDetails.getRating(), commentDetails.getMessage()));
        myWriter.close();
    }

}
