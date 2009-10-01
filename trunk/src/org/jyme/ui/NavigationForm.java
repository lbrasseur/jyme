package org.jyme.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.StringItem;

import org.jyme.domain.Exercise;
import org.jyme.domain.Series;

class NavigationForm extends BaseForm {
	private StringItem excersise;
	private StringItem quantity;
	private StringItem repetitions;
	private StringItem weigth;

	public NavigationForm() {
		super("Ejercicio");

		final Command cmNext = new Command("Siguiente", Command.ITEM, 1);
		final Command cmPrevious = new Command("Anterior", Command.BACK, 1);
		final Command cmBack = new Command("Volver", Command.ITEM, 2);

		excersise = new StringItem("Ejercicio: ", "");
		quantity = new StringItem("Cantidad: ", "");
		repetitions = new StringItem("Repeticiones: ", "");
		weigth = new StringItem("Peso: ", "");

		append(excersise);
		append("\n");
		append(quantity);
		append("\n");
		append(repetitions);
		append("\n");
		append(weigth);

		addCommand(cmNext);
		addCommand(cmPrevious);
		addCommand(cmBack);

		setCommandListener(new CommandListener() {

			public void commandAction(Command command, Displayable displayable) {
				if (command == cmPrevious) {
					getRoutineManager().navigateBack();
					updateDisplay();
				} else if (command == cmNext) {
					getRoutineManager().navigateForward();
					updateDisplay();
				}else if (command == cmBack) {
					getFormManager().navigateToDaySelection();
				}
			}
		});

		updateDisplay();
	}

	private void updateDisplay() {
		Exercise excer = getRoutineManager().getCurrentExercise();
		Series series = getRoutineManager().getCurrentSeries();
		excersise.setText(excer.getName());
		quantity.setText(String.valueOf(series.getQuantity()));
		repetitions.setText(String.valueOf(series.getRepetitions()));
		weigth.setText(String.valueOf(series.getWeight()));
	}
}
