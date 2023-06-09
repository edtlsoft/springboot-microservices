package com.edtlsoft.microservices.items.services;

import com.edtlsoft.microservices.items.entities.Item;

import java.util.List;

public interface IItemService {

    public List<Item> findAll();

    public Item findById(Long id, Integer quantity);

}
