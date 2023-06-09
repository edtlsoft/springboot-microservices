package com.edtlsoft.microservices.products.services;

import com.edtlsoft.microservices.products.entities.Product;

import java.util.List;

public interface IProductService {

    public List<Product> findAll();

    public Product findById(Long id);

}
