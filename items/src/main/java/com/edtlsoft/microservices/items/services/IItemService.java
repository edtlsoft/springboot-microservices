package com.edtlsoft.microservices.items.services;

import com.edtlsoft.microservices.items.entities.Item;
import com.edtlsoft.microservices.items.entities.Product;

import java.util.List;

public interface IItemService {

    public List<Item> findAll();

    public Item findById(Long id, Integer quantity);

    public Product save(Product product);

    public Product update(Product product, Long id);

    public void delete(Long id);

}
