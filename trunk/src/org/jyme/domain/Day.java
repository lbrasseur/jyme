package org.jyme.domain;

import org.jyme.bus.StringUtils;

public class Day {
	private String name;
	private Exercise[] excercises;
	private final static String SEPARATOR = "\n";

	static Day fromString(String data) {
		Day day = new Day();
		String[] fields = StringUtils.split(data, SEPARATOR);

		if (fields.length > 0) {
			day.setName(fields[0]);
		}

		Exercise[] excersises = new Exercise[fields.length - 1];
		for (int n = 1; n < fields.length; n++) {
			excersises[n - 1] = Exercise.fromString(fields[n]);
		}
		day.setExcercises(excersises);

		return day;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Exercise[] getExcercises() {
		return excercises;
	}

	public void setExcercises(Exercise[] excercises) {
		this.excercises = excercises;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(name);

		for (int n = 0; n < excercises.length; n++) {
			sb.append(SEPARATOR);
			sb.append(excercises[n]);
		}

		return sb.toString();
	}
}
