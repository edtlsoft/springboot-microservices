package com.edtlsoft.microservices.products.controllers;

import com.edtlsoft.microservices.products.entities.Product;
import com.edtlsoft.microservices.products.services.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private IProductService productService;

    @Autowired
    private Environment env;

    @GetMapping
    public List<Product> index() {
        return productService.findAll().stream()
                .map(product -> {
                    product.setPort(Integer.parseInt(env.getProperty("local.server.port")));
                    return product;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Product show(@PathVariable Long id) {
        logger.info("Product with id: " + id);

        if (id.equals(10L)) {
            logger.info("Product with id 10 == " + id);
            throw new IllegalStateException("Product not found!");
        }
        
        if (id.equals(7L)) {
            logger.info("Product with id 7 == " + id);
            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
                logger.info("InterruptedException.message::" + e.getMessage());
                e.printStackTrace();
            }
        }

        Product product = productService.findById(id);
        product.setPort(Integer.parseInt(env.getProperty("local.server.port")));

        return product;
    }
}
