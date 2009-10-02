package org.jyme.domain;

import org.jyme.bus.StringUtils;

public class Exercise {
	private String name;
	private Series[] series;
	private final static String SEPARATOR = "/";

	static Exercise fromString(String data) {
		Exercise excersise = new Exercise();
		String[] fields = StringUtils.split(data, SEPARATOR);

		if (fields.length > 0) {
			excersise.setName(fields[0]);
		}

		Series[] series = new Series[fields.length - 1];
		for (int n = 1; n < fields.length; n++) {
			series[n - 1] = Series.fromString(fields[n]);
		}
		excersise.setSeries(series);

		return excersise;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Series[] getSeries() {
		return series;
	}

	public void setSeries(Series[] series) {
		this.series = series;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(name);

		for (int n = 0; n < series.length; n++) {
			sb.append(SEPARATOR);
			sb.append(series[n]);
		}

		return sb.toString();
	}
}
