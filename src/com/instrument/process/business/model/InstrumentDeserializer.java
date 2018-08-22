package com.instrument.process.business.model;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InstrumentDeserializer implements Deserializer<Instrument> {

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Instrument deserialize(String arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		Instrument instrument = null;
		try {
			instrument = mapper.readValue(arg1, Instrument.class);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return instrument;
	}

}
