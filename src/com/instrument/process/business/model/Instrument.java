package com.instrument.process.business.model;

public class Instrument {

	private int id;
	private char name;
	private long offset;
	private String type;

	public Instrument() {

	}

	public Instrument(int id, char name, String type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
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

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Instrument [id=" + id + ", name=" + name + ", offset=" + offset + ", type=" + type + "]";
	}

}
