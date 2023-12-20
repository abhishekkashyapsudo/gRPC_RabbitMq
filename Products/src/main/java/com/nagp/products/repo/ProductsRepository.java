package com.nagp.products.repo;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagp.products.model.Product;


@Repository
public class ProductsRepository {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private static final String PRODUCTS_FILE = "products.json";

	@Autowired
	private ResourceLoader resourceLoader;

	private List<Product> products;

	@PostConstruct
	public void init() {
		try {
			products = readProducts();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Product> readProducts() throws IOException {
		org.springframework.core.io.Resource jsonResource = resourceLoader.getResource("classpath:" + PRODUCTS_FILE);

		return objectMapper.readValue(jsonResource.getInputStream(), new TypeReference<List<Product>>() {
		});
	}

	public Product getProduct(String id) {
		return products.stream().filter(product -> id.equals(product.getId())).findFirst().orElse(null);
	}

}
