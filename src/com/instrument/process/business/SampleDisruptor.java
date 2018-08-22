package com.instrument.process.business;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.json.JSONObject;

import com.instrument.process.business.Disruptor.DisruptorQueueOne;
import com.instrument.process.business.eventhandlers.EventHandlerDisruptorQueueOne;
import com.instrument.process.business.eventhandlers.EventHandlerDisruptorQueueTwo;
import com.instrument.process.business.model.Instrument;
import com.instrument.process.business.util.InstrumentFactory;
import com.instrument.process.business.util.JsonFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.outboundkafka.consumer.OutboundKafka;

import kafka.utils.ShutdownableThread;

public class SampleDisruptor extends ShutdownableThread {
	private final KafkaConsumer<Integer, Instrument> consumer;
	private final String topic;

	public static final String KAFKA_SERVER_URL = "localhost";
	public static final int KAFKA_SERVER_PORT = 9092;
	public static final String CLIENT_ID = "SampleDisruptor";
	public static boolean flag = true;

	public SampleDisruptor(String topic) {
		super("KafkaConsumerExample", false);
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URL + ":" + KAFKA_SERVER_PORT);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, CLIENT_ID);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.IntegerDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"com.instrument.process.business.model.InstrumentDeserializer");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumer = new KafkaConsumer<>(props);
		this.topic = topic;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doWork() {
		consumer.subscribe(Collections.singletonList(this.topic));
		ConsumerRecords<Integer, Instrument> records = consumer.poll(1000);
		long offSetCount = 0;
		if (flag) {
			Set<TopicPartition> partitions = consumer.assignment();
			Map<TopicPartition, Long> offsets = consumer.endOffsets(partitions);
			for (TopicPartition partition : offsets.keySet()) {
				offSetCount = offsets.get(partition);
				System.out.println("Partition: "+partition.partition());
				//System.out.printf("partition %s is at %d\n", partition.topic(), offsets.get(partition));
				callOutbound(offSetCount,partition.partition());
			}
			flag = false;
		}

		ExecutorService exec = Executors.newCachedThreadPool();

		DisruptorQueueOne disruptorQueueOne = new DisruptorQueueOne();

		Disruptor<Instrument> disruptorQueue = new Disruptor<Instrument>(InstrumentFactory.EVENT_FACTORY_INSTRUMENT,
				1024, exec);
		Disruptor<JSONObject> disruptorQueueJson = new Disruptor<JSONObject>(JsonFactory.EVENT_FACTORY_JSON, 1024,
				exec);

		disruptorQueueJson.handleEventsWith(EventHandlerDisruptorQueueTwo.getEventHandlerDisruptorQueueTwo());
		RingBuffer<JSONObject> ringBufferJson = disruptorQueueJson.start();

		disruptorQueue.handleEventsWith(EventHandlerDisruptorQueueOne.getEventHandlerDisruptorQueueOne(ringBufferJson));
		RingBuffer<Instrument> ringBuffer = disruptorQueue.start();

		Map<Integer, Instrument> instrumentCache = disruptorQueueOne.publishDataQueueOne(records, ringBuffer, exec);

		disruptorQueue.shutdown();
		disruptorQueueJson.shutdown();

		consumer.commitAsync();

		exec.shutdown();

		//consumer.close();
	}

	private void callOutbound(long offSetCount, int partition) {
		// TODO Auto-generated method stub
		OutboundKafka outbound = new OutboundKafka("OutboundKafkaTest", offSetCount, partition);
		System.out.println("In Call: "+partition);
		outbound.start();

	}

}
