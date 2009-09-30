package org.jyme.domain;

import org.jyme.bus.StringUtils;

public class Excercise {
	private String name;
	private Series[] series;
	private final static String SEPARATOR = "/";

	public Excercise() {
		super();
	}

	public Excercise(String name, Series[] series) {
		super();
		this.name = name;
		this.series = series;
	}

	static Excercise fromString(String data) {
		Excercise excersise = new Excercise();
		String[] fields = StringUtils.split(data, SEPARATOR); 
		
		excersise.setName(fields[0]);
		
		Series[] series =  new Series[fields.length -1];
		for (int n = 1; n<fields.length;n++) {
			series[n - 1]= Series.fromString(fields[n]);
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
}
