package com.instrument.process.business;

public class InstrumentProcessDemo {
	//public static final String TOPIC = "TestInbound";
	public static final String TOPIC = "InboundKafkaTest";

	public static void main(String[] args) {

		SampleDisruptor disruptorThread = new SampleDisruptor(TOPIC);
		disruptorThread.start();

	}
}