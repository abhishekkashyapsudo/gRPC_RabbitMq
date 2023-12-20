package com.nagp.order.service;

import com.nagp.products.proto.OrderServiceGrpc.OrderServiceImplBase;
import com.nagp.products.proto.Products.OrderResponse;
import com.nagp.products.proto.Products.PlaceOrderRequest;
import com.nagp.products.proto.Products.UpdateOrderRequest;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

@GrpcService
public class OrderService extends OrderServiceImplBase {

	private static final String PLACE_ORDER_EXCHANGE = "place_order_fanout";
	private static final String UPDATE_ORDER_EXCHANGE = "update_order_topic";

	@Override
	public void placeOrder(PlaceOrderRequest request, StreamObserver<OrderResponse> responseObserver) {

		String greeting = new StringBuilder().append("Hello, Following product request for ")
				.append(request.getQuantity()).append(" quantity placed successfully <br/> ")

				.append(request.getProduct()).append(" ").toString();

		OrderResponse response = OrderResponse.newBuilder().setMessage(greeting).build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
		publishPlaceOrderEvent(request);

	}

	@Override
	public void updateOrder(UpdateOrderRequest request, StreamObserver<OrderResponse> responseObserver) {

		String greeting = new StringBuilder().append("Hello, Following product ").append(request.getProducts())
				.append("<br/> updated with quantity ").append(request.getQuantity()).append(" by ")
				.append(request.getUpdaterId()).append(" <br/> ")

				.append(" ").toString();

		OrderResponse response = OrderResponse.newBuilder().setMessage(greeting).build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
		publishUpdateOrderEvent(request);

	}

	public void publishPlaceOrderEvent(PlaceOrderRequest orderRequest) {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost"); // Replace with your RabbitMQ server address
			try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
				channel.exchangeDeclare(PLACE_ORDER_EXCHANGE, "fanout");
				String message = "OrderCreated: " + orderRequest.getProduct() + "( Quantity: " + orderRequest.getQuantity() + ")";
				channel.basicPublish(PLACE_ORDER_EXCHANGE, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
				System.out.println(" [x] Sent '" + message + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void publishUpdateOrderEvent(UpdateOrderRequest orderRequest) {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost"); // Replace with your RabbitMQ server address
			try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
				channel.exchangeDeclare(UPDATE_ORDER_EXCHANGE, "topic");
				String message = "OrderUpdated by "+orderRequest.getUpdaterId() +": " + orderRequest.getProducts() + "( Quantity: " + orderRequest.getQuantity() + ")";
				channel.basicPublish(UPDATE_ORDER_EXCHANGE, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
				System.out.println(" [x] Sent '" + message + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
