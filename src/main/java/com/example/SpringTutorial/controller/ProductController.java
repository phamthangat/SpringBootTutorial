package com.example.SpringTutorial.controller;

import com.example.SpringTutorial.models.Product;
import com.example.SpringTutorial.models.ResponseObject;
import com.example.SpringTutorial.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductController {
    // DI = Dependency Injecttion
    @Autowired
    private ProductRepository repository;

    @GetMapping("")
    List<Product> getAllProduct(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    //let return 1 object with data, message, status
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query product successfully", foundProduct)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("false", "Cannot find product with id = " + id, "")
                );
    }
    @PostMapping("/insert")
    //insert new product with Post method
    //2 product must not have the same name
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        if(foundProducts.size() > 0 ) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert product successfully", repository.save(newProduct))
        );
    }

    // update, upsert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product updateProduct = repository.findById(id)
                .map(product -> {
                    product.setId(newProduct.getId());
                    product.setProductName(newProduct.getProductName());
                    product.setYear(newProduct.getYear());
                    product.setPrice(newProduct.getPrice());
                    return repository.save(product);
                }).orElseGet(() -> {
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update Product successfully", updateProduct)
        );
    }
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exists = repository.existsById(id);
        if(exists) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete Product successfully", "")
            );
        }
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "Cannot find product to delete", "")
        );
    }

    @GetMapping("/product1")
    List<Product> Product(){
        return  List.of(
                new Product("Macbook", 2020, 2400.0,""),
                new Product("Ipad2", 2021, 599.0,"")
        );
    }
    @GetMapping("/product2")
    List<String> getProduct(){
        return  List.of("iphone", "ipad");
    }
    @GetMapping ("/Hello world")
    public String get() {
        return "Hello world";
    }
}