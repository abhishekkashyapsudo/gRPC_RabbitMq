package com.nagp.products.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagp.products.model.Product;
import com.nagp.products.proto.OrderServiceGrpc;
import com.nagp.products.proto.Products.OrderResponse;
import com.nagp.products.proto.Products.PlaceOrderRequest;
import com.nagp.products.proto.Products.UpdateOrderRequest;
import com.nagp.products.repo.ProductsRepository;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Service
public class ProductService {

	@Autowired
	private ProductsRepository productsRepository;

	public String placeOrder(String id, int quantity) {
		Product product = productsRepository.getProduct(id);
		if (product == null) {
			return "No product with passed id exists";
		}
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8082).usePlaintext().build();
		OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
		OrderResponse orderResponse = stub.placeOrder(
				PlaceOrderRequest.newBuilder().setProduct(createProduct(product)).setQuantity(quantity).build());

		channel.shutdown();
		return orderResponse.getMessage();
	}

	private com.nagp.products.proto.Products.Product createProduct(Product product) {
		return com.nagp.products.proto.Products.Product.newBuilder().setId(product.getId()).setColor(product.getColor())
				.setDescription(product.getDescription()).setPrice(product.getPrice()).build();
	}

	public String updateOrder(String id, int quantity) {
		Product product = productsRepository.getProduct(id);
		if (product == null) {
			return "No product with passed id exists";
		}
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8082).usePlaintext().build();
		OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
		OrderResponse orderResponse = stub.updateOrder(UpdateOrderRequest.newBuilder()
				.setProducts(createProduct(product)).setUpdaterId("Abhishek").setQuantity(quantity).build());

		channel.shutdown();
		return orderResponse.getMessage();

	}

}
