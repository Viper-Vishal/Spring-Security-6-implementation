package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {
	private record Product(Integer productId, String productName, double price) {}
	
	List<Product> products = new ArrayList<>(
				List.of(
						new Product(1, "iPhone", 120.0),
						new Product(1, "mac pro", 1220.0)
						)
			);
	
	@GetMapping
	public List<Product> getProducts(){
		return products;
	}
	
	@PostMapping
	public Product saveProduct(@RequestBody Product product) {
		products.add(product);
		return product;
	}
}
