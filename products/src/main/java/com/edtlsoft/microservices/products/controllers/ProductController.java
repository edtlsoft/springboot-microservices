package com.edtlsoft.microservices.products.controllers;

import com.edtlsoft.microservices.products.entities.Product;
import com.edtlsoft.microservices.products.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class ProductController {

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
        Product product = productService.findById(id);
        product.setPort(Integer.parseInt(env.getProperty("local.server.port")));

        return product;
    }
}
