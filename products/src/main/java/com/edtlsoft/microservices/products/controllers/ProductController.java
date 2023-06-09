package com.edtlsoft.microservices.products.controllers;

import com.edtlsoft.microservices.products.entities.Product;
import com.edtlsoft.microservices.products.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping
    public List<Product> index() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product show(@PathVariable Long id) {
        return productService.findById(id);
    }
}
