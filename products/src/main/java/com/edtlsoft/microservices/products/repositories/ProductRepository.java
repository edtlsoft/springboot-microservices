package com.edtlsoft.microservices.products.repositories;

import com.edtlsoft.microservices.products.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
