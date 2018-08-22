package com.instrument.process.business.util;

import org.json.JSONObject;

import com.lmax.disruptor.EventFactory;

public class JsonFactory {
	public final static EventFactory<JSONObject> EVENT_FACTORY_JSON = new EventFactory<JSONObject>() {
		public JSONObject newInstance() {
			return new JSONObject();
		}
	};
}
