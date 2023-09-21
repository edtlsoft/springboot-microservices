package com.edtlsoft.microservices.items.controllers;

import com.edtlsoft.microservices.items.entities.Item;
import com.edtlsoft.microservices.items.entities.Product;
import com.edtlsoft.microservices.items.services.IItemService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    private IItemService itemService;

    @GetMapping("/")
    public List<Item> index(
        @RequestHeader(name = "token-request-header", required = false) String tokenRequestHeader, 
        @RequestParam(name = "token-request-parameter", required = false) String tokenRequestParameter
    ) {
        System.out.println("Token-Request-Header: " + tokenRequestHeader);
        System.out.println("Token-Request-Parameter: " + tokenRequestParameter);
        return itemService.findAll();
    }

    @GetMapping("/{id}/quantity/{quantity}")
    public Item show(@PathVariable Long id, @PathVariable Integer quantity) {
        return circuitBreakerFactory
            .create("items")
            .run(
                () -> itemService.findById(id, quantity), 
                throwable -> fallback(id, quantity, throwable)
            );
    }

    public Item fallback(Long id, Integer quantity, Throwable throwable) {
        System.out.println("Fallback");
        logger.info(throwable.getMessage());
        Product product = new Product();
        product.setId(id);
        product.setName("Product default");
        product.setPrice(50.00);
        product.setPort(0);

        Item item = new Item();
        item.setProduct(product);
        item.setQuantity(quantity);
        return item;
    }
}
