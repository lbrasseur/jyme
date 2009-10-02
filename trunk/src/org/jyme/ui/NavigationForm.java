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
		final Command cmChangeQuantity = new Command("Cambiar cantidad",
				Command.ITEM, 3);
		final Command cmChangeRepetitions = new Command("Cambiar repeticiones",
				Command.ITEM, 4);
		final Command cmChangeWeigth = new Command("Cambiar peso",
				Command.ITEM, 5);

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
		addCommand(cmChangeQuantity);
		addCommand(cmChangeRepetitions);
		addCommand(cmChangeWeigth);

		setCommandListener(new CommandListener() {
			public void commandAction(Command command, Displayable displayable) {
				final Series series = getRoutineManager().getCurrentSeries();
				if (command == cmPrevious) {
					getRoutineManager().navigateBack();
					updateDisplay();
				} else if (command == cmNext) {
					getRoutineManager().navigateForward();
					updateDisplay();
				} else if (command == cmBack) {
					getFormManager().navigateToDaySelection();
				} else if (command == cmChangeQuantity) {
					getFormManager().prompt("Cambiar la cantidad de series",
							"Cantidad de series",
							String.valueOf(series.getQuantity()),
							new DialogCallbackAdapter() {
								public void onOk(String value) {
									try {
										series.setQuantity(Integer
												.parseInt(value));
										getRoutineManager().saveRoutine(
												getRoutineManager()
														.getCurrentRoutine());
									} catch (NumberFormatException e) {
									}
								}

								public void afterBoth() {
									getFormManager().navigateToNavigation();
								}
							});
				} else if (command == cmChangeRepetitions) {
					getFormManager().prompt("Cambiar las repeticiones",
							"Repeticiones",
							String.valueOf(series.getRepetitions()),
							new DialogCallbackAdapter() {
								public void onOk(String value) {
									try {
										series.setRepetitions(Integer
												.parseInt(value));
										getRoutineManager().saveRoutine(
												getRoutineManager()
														.getCurrentRoutine());
									} catch (NumberFormatException e) {
									}
								}

								public void afterBoth() {
									getFormManager().navigateToNavigation();
								}
							});
				} else if (command == cmChangeWeigth) {
					getFormManager().prompt("Cambiar el peso", "Peso",
							String.valueOf(series.getWeight()),
							new DialogCallbackAdapter() {
								public void onOk(String value) {
									try {
										series.setWeight(Integer
												.parseInt(value));
										getRoutineManager().saveRoutine(
												getRoutineManager()
														.getCurrentRoutine());
									} catch (NumberFormatException e) {
									}
								}

								public void afterBoth() {
									getFormManager().navigateToNavigation();
								}
							});
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
