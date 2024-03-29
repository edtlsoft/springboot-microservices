package com.edtlsoft.microservices.items.controllers;

import com.edtlsoft.microservices.items.entities.Item;
import com.edtlsoft.microservices.items.entities.Product;
import com.edtlsoft.microservices.items.services.IItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RefreshScope
@RestController
public class ItemController {
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private Environment env;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    private IItemService itemService;

    @Value("${config.text}")
    private String configText;

    @GetMapping("/config")
    public ResponseEntity<?> config(@Value("${server.port}") String serverPort)
    {
        Map<String, String> json = new HashMap<>();
        json.put("configText", configText);
        json.put("serverPort", serverPort);

        if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
            json.put("authorName", env.getProperty("config.author"));
            json.put("authorEmail", env.getProperty("config.author_email"));
        }

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

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

    @CircuitBreaker(name="items", fallbackMethod = "fallback")
    @GetMapping("show/{id}/quantity/{quantity}")
    public Item show2(@PathVariable Long id, @PathVariable Integer quantity) {
        logger.info("Method show2:::::::::::::::::::::::::::::::::::");
        return itemService.findById(id, quantity);
    }

    @TimeLimiter(name="items", fallbackMethod = "fallback2")
    @GetMapping("show3/{id}/quantity/{quantity}")
    public CompletableFuture<Item> show3(@PathVariable Long id, @PathVariable Integer quantity) {
        logger.info("Method show3:::::::::::::::::::::::::::::::::::");
        return CompletableFuture.supplyAsync(() -> itemService.findById(id, quantity));
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

    public CompletableFuture<Item> fallback2(Long id, Integer quantity, Throwable throwable) {
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

        return CompletableFuture.supplyAsync(() -> item);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Product store(@RequestBody Product product)
    {
        return itemService.save(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product update(@RequestBody Product product, @PathVariable Long id)
    {
        return itemService.update(product, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)
    {
        itemService.delete(id);
    }

}
