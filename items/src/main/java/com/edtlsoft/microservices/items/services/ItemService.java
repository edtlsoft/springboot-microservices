package com.edtlsoft.microservices.items.services;

import com.edtlsoft.microservices.items.entities.Item;
import com.edtlsoft.microservices.items.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ItemService implements IItemService {

    @Autowired
    private RestTemplate clientRest;

    @Override
    public List<Item> findAll() {
        List<Product> products = Arrays.asList(clientRest.getForObject("http://products-service/", Product[].class));

        return products
                .stream()
                .map(product -> new Item(product, 1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {
        Map<String, String> pathVariables = new HashMap<String, String>();
        pathVariables.put("id", id.toString());
        Product product = clientRest.getForObject("http://products-service/{id}", Product.class, pathVariables);

        return new Item(product, quantity);
    }

    @Override
    public Product save(Product product) {
        HttpEntity<Product> payload = new HttpEntity<Product>(product);
        ResponseEntity<Product> response = clientRest.exchange("http://products-service/", HttpMethod.POST, payload, Product.class);
        Product productResponse = response.getBody();

        return productResponse;
    }

    @Override
    public Product update(Product product, Long id) {
        HttpEntity<Product> payload = new HttpEntity<Product>(product);
        Map<String, String> pathVariables = new HashMap<String, String>();
        pathVariables.put("id", id.toString());
        ResponseEntity<Product> response = clientRest.exchange(
                "http://products-service/{id}",
                HttpMethod.PUT,
                payload,
                Product.class,
                pathVariables
        );
        Product productResponse = response.getBody();

        return productResponse;
    }

    @Override
    public void delete(Long id) {
        Map<String, String> pathVariables = new HashMap<String, String>();
        pathVariables.put("id", id.toString());
        clientRest.delete("http://products-service/{id}", pathVariables);
    }
}
