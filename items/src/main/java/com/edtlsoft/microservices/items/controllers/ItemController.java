package com.edtlsoft.microservices.items.controllers;

import com.edtlsoft.microservices.items.entities.Item;
import com.edtlsoft.microservices.items.services.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {

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
        return itemService.findById(id, quantity);
    }
}
