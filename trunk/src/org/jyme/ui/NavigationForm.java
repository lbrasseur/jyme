package org.jyme.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import org.jyme.domain.Day;
import org.jyme.domain.Excercise;
import org.jyme.domain.Series;

class NavigationForm extends Form {
	private int currentExcercise = 0;
	private int currentSeries = 0;
	private StringItem excersise;
	private StringItem quantity;
	private StringItem repetitions;
	private StringItem weigth;
	private Day day;

	public NavigationForm(final Day day) {
		super("Ejercicio");
		this.day = day;

		final Command cmNext = new Command("Siguiente", Command.SCREEN, 1);
		final Command cmPrevious = new Command("Anterior", Command.BACK, 2);

		excersise = new StringItem("Ejercicio", "");
		quantity = new StringItem("Cantidad", "");
		repetitions = new StringItem("Repeticiones", "");
		weigth = new StringItem("Peso", "");

		append(excersise);
		append(quantity);
		append(repetitions);
		append(weigth);

		addCommand(cmNext);
		addCommand(cmPrevious);

		setCommandListener(new CommandListener() {

			public void commandAction(Command command, Displayable displayable) {
				if (command == cmPrevious) {
					if (currentSeries > 0) {
						currentSeries--;
					} else if (currentExcercise > 0) {
						currentExcercise--;
						currentSeries = day.getExcercises()[currentExcercise]
								.getSeries().length - 1;
					} else {
						FormManager.getInstance().navigateToDaySelection(
								day.getRoutine());
					}
				} else if (command == cmNext) {
					if (currentSeries < day.getExcercises()[currentExcercise]
							.getSeries().length - 1) {
						currentSeries++;
					} else if (currentExcercise < day.getExcercises().length - 1) {
						currentExcercise++;
						currentSeries = 0;
					}
				}
				updateDisplay();
			}
		});

		updateDisplay();
	}

	private void updateDisplay() {
		Excercise excer = day.getExcercises()[currentExcercise];
		Series series = excer.getSeries()[currentSeries];
		excersise.setText(excer.getName());
		quantity.setText(String.valueOf(series.getQuantity()));
		repetitions.setText(String.valueOf(series.getRepetitions()));
		weigth.setText(String.valueOf(series.getWeight()));
	}
}
