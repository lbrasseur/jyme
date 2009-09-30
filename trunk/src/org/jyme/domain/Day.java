package org.jyme.domain;

import org.jyme.bus.StringUtils;

public class Day {
	private String name;
	private Excercise[] excercises;
	private Routine routine;
	private final static String SEPARATOR = "\n";

	public Day() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Day(String name, Excercise[] excercises) {
		super();
		this.name = name;
		this.excercises = excercises;
	}

	static Day fromString(String data) {
		Day day = new Day();
		String[] fields = StringUtils.split(data, SEPARATOR); 
		
		day.setName(fields[0]);
		
		Excercise[] excersises =  new Excercise[fields.length -1];
		for (int n = 1; n<fields.length;n++) {
			excersises[n - 1]= Excercise.fromString(fields[n]);
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

	public Excercise[] getExcercises() {
		return excercises;
	}

	public void setExcercises(Excercise[] excercises) {
		this.excercises = excercises;
	}

	public Routine getRoutine() {
		return routine;
	}

	public void setRoutine(Routine routine) {
		this.routine = routine;
	}
}
