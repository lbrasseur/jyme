package org.jyme.domain;

import org.jyme.bus.StringUtils;

public class Series {
	private int quantity;
	private int repetitions;
	private int weight;
	private final static String SEPARATOR = ",";

	static Series fromString(String data) {
		Series series = new Series();
		String[] fields = StringUtils.split(data, SEPARATOR);

		if (fields.length > 0) {
			series.setQuantity(Integer.parseInt(fields[0]));
		}
		if (fields.length > 1) {
			series.setRepetitions(Integer.parseInt(fields[1]));
		}
		if (fields.length > 2) {
			series.setWeight(Integer.parseInt(fields[2]));
		}

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
