package com.nagp.notif1;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class OrderCreationConsumer {

	private final static String PLACE_ORDER_EXCHANGE = "place_order_fanout";
	private final static String QUEUE_NAME = "order_creation_queue";

	public static void main(String[] args) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost"); // Replace with your RabbitMQ server address

		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			// Declare the exchange as durable
			channel.exchangeDeclare(PLACE_ORDER_EXCHANGE, "fanout", false);
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			// Bind the queue to the exchange
			channel.queueBind(QUEUE_NAME, PLACE_ORDER_EXCHANGE, "");

			System.out.println(" [*] Waiting for messages. To exit press Ctrl+C");
			while (true) {
				GetResponse response = channel.basicGet(QUEUE_NAME, true);

				if (response != null) {
					String message = new String(response.getBody(), "UTF-8");
					System.out.println(" [Notification1] Received '" + message + "'");
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}