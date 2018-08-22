package com.outboundkafka.consumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import kafka.utils.ShutdownableThread;

public class OutboundKafka extends ShutdownableThread {

	private final KafkaConsumer<Integer, String> consumer;
	private final String topic;

	public static final String KAFKA_SERVER_URL = "localhost";
	public static final int KAFKA_SERVER_PORT = 9092;
	public static final String CLIENT_ID = "OutboundOffsetKafka";
	private boolean flag = true;
	private long offsetCount;
	private int partition;

	public OutboundKafka(String topic, long offSetCountInitial, int partitionNumber) {
		super("kafkaConsumer", false);
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URL + ":" + KAFKA_SERVER_PORT);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, CLIENT_ID);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.IntegerDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumer = new KafkaConsumer<>(props);
		this.topic = topic;
		this.offsetCount = offSetCountInitial;
		this.partition = partitionNumber;
	}

	@Override
	public void doWork() {
		//consumer.subscribe(Collections.singletonList(this.topic));
		System.out.println("OffsetCount:"+offsetCount);
	  
		if (flag) {
			System.out.println("in if: "+offsetCount+" Partition: "+partition);
			TopicPartition tp = new TopicPartition(topic, partition);
			consumer.assign(Arrays.asList(tp));
			consumer.poll(1000);
			consumer.seek(tp, offsetCount);
			flag = false;
		}
		ConsumerRecords<Integer, String> records = consumer.poll(1000);
		for (ConsumerRecord<Integer, String> record : records) {
			//if(record.offset() >= offsetCount) { 
				System.out.println("Received message in Outbound Kafka: (" + record.key() + ", " + record.value()
						+ ") at offset " + record.offset());
				//offsetCount = record.offset() + 1;
			//}
		} 
		
		consumer.commitAsync();
		//consumer.close();
	}

}
