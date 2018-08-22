package com.instrument.process.business.util;

import com.instrument.process.business.model.Instrument;
import com.lmax.disruptor.EventFactory;

public class InstrumentFactory {
	public final static EventFactory<Instrument> EVENT_FACTORY_INSTRUMENT = new EventFactory<Instrument>() {
		public Instrument newInstance() {
			return new Instrument();
		}
	};

}
