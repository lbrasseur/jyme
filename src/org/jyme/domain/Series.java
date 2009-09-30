package org.jyme.domain;

import org.jyme.bus.StringUtils;

public class Series {
	private int quantity;
	private int repetitions;
	private int weight;
	private final static String SEPARATOR = ",";
	
	public Series() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Series(int quantity, int repetitions, int weight) {
		super();
		this.quantity = quantity;
		this.repetitions = repetitions;
		this.weight = weight;
	}

	static Series fromString(String data) {
		Series series = new Series();
		String[] fields = StringUtils.split(data, SEPARATOR); 
		
		series.setQuantity(Integer.parseInt(fields[0]));
		series.setRepetitions(Integer.parseInt(fields[1]));
		series.setWeight(Integer.parseInt(fields[2]));
		
		return series;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}
