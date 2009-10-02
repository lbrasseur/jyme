package org.jyme.domain;

import org.jyme.bus.StringUtils;

public class Routine {
	private String name;
	private Day[] days;
	private final static String SEPARATOR = "\n-\n";

	public static Routine fromString(String data) {
		Routine routine = new Routine();
		String[] fields = StringUtils.split(data, SEPARATOR);

		if (fields.length > 0) {
			routine.setName(fields[0]);
		}

		Day[] days = new Day[fields.length - 1];
		for (int n = 1; n < fields.length; n++) {
			days[n - 1] = Day.fromString(fields[n]);
		}
		routine.setDays(days);

		return routine;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Day[] getDays() {
		return days;
	}

	public void setDays(Day[] days) {
		this.days = days;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(name);

		for (int n = 0; n < days.length; n++) {
			sb.append(SEPARATOR);
			sb.append(days[n]);
		}

		return sb.toString();
	}
}
