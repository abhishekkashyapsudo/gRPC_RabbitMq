package com.nagp.products.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagp.products.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
    private ProductService productService;

    @GetMapping("/placeOrder")
    public String placeOrder(@RequestParam String id, @RequestParam int quantity) {
        return productService.placeOrder(id, quantity);
    }
    
    @GetMapping("/updateOrder")
    public String updateOrder(@RequestParam String id, @RequestParam int quantity) {
        return productService.updateOrder(id, quantity);
    }
}
