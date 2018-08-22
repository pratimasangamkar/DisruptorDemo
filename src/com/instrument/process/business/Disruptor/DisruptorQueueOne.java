package com.instrument.process.business.Disruptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.kafka.clients.consumer.ConsumerRecords;

import com.instrument.process.business.model.Instrument;
import com.lmax.disruptor.RingBuffer;

public class DisruptorQueueOne {

	public Map<Integer, Instrument> publishDataQueueOne(ConsumerRecords<Integer, Instrument> records, RingBuffer<Instrument> ringBuffer,
			ExecutorService exec) {

		Map<Integer, Instrument> instrumentCache = new HashMap<>();
		records.forEach(record -> {
			System.out.println("Received message: (" + record.key() + ", " + record.value().toString() + ") at offset "
					+ record.offset());
			long sequence = ringBuffer.next();
			instrumentCache.put(record.key(), record.value());
			Instrument instrument = ringBuffer.get(sequence);
			instrument.setId(record.value().getId());
			instrument.setName(record.value().getName());
			instrument.setOffset(record.offset());
			instrument.setType(record.value().getType());
			ringBuffer.publish(sequence);
		});
	
		
		return instrumentCache;

	}

}
