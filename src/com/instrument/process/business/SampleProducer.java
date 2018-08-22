package com.instrument.process.business;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.json.JSONObject;

import com.instrument.process.business.partitioner.SampleProducerPartition;

public class SampleProducer extends Thread {
	private final KafkaProducer<Integer, String> producer;
	private final String topic;

	public static final String KAFKA_SERVER_URL = "localhost";
	public static final int KAFKA_SERVER_PORT = 9092;
	public static final String CLIENT_ID = "OutboundOffsetKafka";
	public static int count = 0;
	
	public SampleProducer(String topic) {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", KAFKA_SERVER_URL + ":" + KAFKA_SERVER_PORT);
		properties.put("client.id", CLIENT_ID);
		properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		/*properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, SampleProducerPartition.class.getCanonicalName());
		properties.put("partitions.0", "Spread");
		properties.put("partitions.1", "Leg");
		properties.put("partitions.2", "Outright");*/
		producer = new KafkaProducer<>(properties);
		this.topic = topic;
	}

	public void putJsonObject(JSONObject json) {
	//	String type = (String)json.get("type");
		String jsonString = json.toString();
		producer.send(new ProducerRecord<>(topic, count, jsonString), new DemoCallBack(jsonString));
		System.out.println("Sent message in Json on Oubound Kafka: " + json);
		count++;
	}

}

class DemoCallBack implements Callback {

	private final String message;

	public DemoCallBack(String message) {
		this.message = message;
	}


	@Override
	public void onCompletion(RecordMetadata metadata, Exception exception) {
		// TODO Auto-generated method stub
		if (metadata != null) {
			System.out.println("In sample producer:  message(" + message + ") sent to partition(" + metadata.partition()
					+ "), " + "offset(" + metadata.offset());
		} else {
			exception.printStackTrace();
		}
		
	}
}
