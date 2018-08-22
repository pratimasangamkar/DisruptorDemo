package com.inboundkafka.demo;

public class InboundKafkaDemo {
	public static final String TOPIC = "InboundKafkaTest";

	public static void main(String[] args) {

		InboundKafka producerThread = new InboundKafka(TOPIC);
		producerThread.start();

	}
}