package com.instrument.process.business.eventhandlers;

import org.json.JSONObject;

import com.instrument.process.business.model.Instrument;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;

public class EventHandlerDisruptorQueueOne {

	public static EventHandler<Instrument> getEventHandlerDisruptorQueueOne(RingBuffer<JSONObject> ringBufferJson) {

		final EventHandler<Instrument> handlerDisruptorQueue = (final Instrument event, final long sequence,
				final boolean endOfBatch) -> {
			System.out.println("Sequence in Q1: " + sequence + " Message: " + event);
			long seq = ringBufferJson.next();
			JSONObject jsonObject = ringBufferJson.get(seq);
			jsonObject.put("Id", event.getId());
			jsonObject.put("Name", new Character(event.getName()));
			jsonObject.put("Offset", event.getOffset());
			jsonObject.put("Type", event.getType());
			ringBufferJson.publish(seq);
		};
		return handlerDisruptorQueue;

	}

}
