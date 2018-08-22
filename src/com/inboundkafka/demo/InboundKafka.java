package com.inboundkafka.demo;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.inboundkafka.demo.model.Instrument;

public class InboundKafka extends Thread {
	private final KafkaProducer<Integer, Instrument> producer;
	private final String topic;

	public static final String KAFKA_SERVER_URL = "localhost";
	public static final int KAFKA_SERVER_PORT = 9092;
	public static final String CLIENT_ID = "InboundKafka";
	public static int messageNo = 0;
	public static char instName = 'A';

	public InboundKafka(String topic) {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", KAFKA_SERVER_URL + ":" + KAFKA_SERVER_PORT);
		properties.put("client.id", CLIENT_ID);
		properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		properties.put("value.serializer", "com.inboundkafka.demo.model.InstrumentSerializer");
		properties.put("zookeeper.connect", "localhost:2181");
		producer = new KafkaProducer<>(properties);
		this.topic = topic;
	}

	public void run() {
		int id = 1;
		while (instName <= 'Z') {
			Instrument instrument = new Instrument(id, instName);
			try {
				producer.send(new ProducerRecord<>(topic, messageNo, instrument)).get();
				System.out.println("Sent message: (" + messageNo + ", " + instrument.toString() + ")");
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				// handle the exception
			}
			id++;
			instName++;
			messageNo++;
		}
	}
}