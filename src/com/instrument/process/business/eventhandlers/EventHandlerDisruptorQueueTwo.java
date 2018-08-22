package com.instrument.process.business.eventhandlers;

import org.json.JSONObject;

import com.instrument.process.business.SampleProducer;
import com.lmax.disruptor.EventHandler;

public class EventHandlerDisruptorQueueTwo {

	private static SampleProducer sampleProducer = new SampleProducer("OutboundKafkaTest");

	public static EventHandler<JSONObject> getEventHandlerDisruptorQueueTwo() {

		final EventHandler<JSONObject> handlerDisruptorQueueJson = (final JSONObject event, final long sequence,
				final boolean endOfBatch) -> {
			System.out.println("Sequence in Q2: " + "Json: " + event);
			sampleProducer.putJsonObject(event);
		};
		return handlerDisruptorQueueJson;
	}
}
