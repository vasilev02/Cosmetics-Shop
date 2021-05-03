package com.example.shop.aop;

import com.example.shop.model.service.productServiceModels.ProductAddServiceModel;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Aspect
@Component
public class ProductAspect {

    @Pointcut("execution(* com.example.shop.service.impl.ProductServiceImpl.addProduct(..))")
    public void trackAddingProduct() {
    }

    @AfterReturning(pointcut = "trackAddingProduct()", returning = "productAddServiceModel")
    public void loggingProductAfterReturning(JoinPoint joinPoint, Object productAddServiceModel) throws IOException {
        ProductAddServiceModel productDetails = (ProductAddServiceModel) productAddServiceModel;
        FileWriter myWriter = new FileWriter("src/main/java/com/example/shop/log/AddingProductsDetails.log", true);
        myWriter.write(String.format("Product with name {%s} and producer {%s} was added !%n", productDetails.getName(), productDetails.getProducer()));
        myWriter.close();
    }

}
