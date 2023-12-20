package com.nagp.notif2;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.Connection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.Channel;


public class OrderCreationAndUpdationConsumer {

	private final static String PLACE_ORDER_EXCHANGE = "place_order_fanout";
	private static final String UPDATE_ORDER_EXCHANGE = "update_order_topic";
	private final static String PLACE_ORDER_QUEUE_NAME = "order_creation_queue";
	private final static String UPDATE_ORDER_QUEUE_NAME = "order_updation_queue";

	public static void main(String[] args) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost"); // Replace with your RabbitMQ server address
        ExecutorService executorService = Executors.newFixedThreadPool(2);

		executorService.submit(() -> {
			placeOrderNotif(factory);
		});
		executorService.submit(() -> {
			updateOrderNotif(factory);
		});
        executorService.shutdown();

	}

	private static void updateOrderNotif(ConnectionFactory factory) {
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			// Declare the exchange as durable
			channel.exchangeDeclare(UPDATE_ORDER_EXCHANGE, "topic", false);
			channel.queueDeclare(UPDATE_ORDER_QUEUE_NAME, false, false, false, null);

			// Bind the queue to the exchange
			channel.queueBind(UPDATE_ORDER_QUEUE_NAME, UPDATE_ORDER_EXCHANGE, "");

			System.out.println(" [*] Waiting for Update messages. To exit press Ctrl+C");
			while (true) {
				GetResponse response = channel.basicGet(UPDATE_ORDER_QUEUE_NAME, true);

				if (response != null) {
					String message = new String(response.getBody(), "UTF-8");
					System.out.println(" [Notification2] Received '" + message + "'");
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void placeOrderNotif(ConnectionFactory factory) {
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			// Declare the exchange as durable
			channel.exchangeDeclare(PLACE_ORDER_EXCHANGE, "fanout", false);
			channel.queueDeclare(PLACE_ORDER_QUEUE_NAME, false, false, false, null);

			// Bind the queue to the exchange
			channel.queueBind(PLACE_ORDER_QUEUE_NAME, PLACE_ORDER_EXCHANGE, "");

			System.out.println(" [*] Waiting for Create messages. To exit press Ctrl+C");
			while (true) {
				GetResponse response = channel.basicGet(PLACE_ORDER_QUEUE_NAME, true);

				if (response != null) {
					String message = new String(response.getBody(), "UTF-8");
					System.out.println(" [Notification2] Received '" + message + "'");
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}