package com.inboundkafka.demo.model;

public class Instrument {

	private int id;
	private char name;
	
	public Instrument() {
		
	}
	
	public Instrument(int id, char name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public char getName() {
		return name;
	}

	public void setName(char name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Instrument [id=" + id + ", name=" + name + "]";
	}

	
}
