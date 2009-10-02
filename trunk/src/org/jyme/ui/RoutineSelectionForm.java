package org.jyme.ui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import org.jyme.domain.Routine;

class RoutineSelectionForm extends BaseForm {
	public RoutineSelectionForm() {
		super("Seleccion de rutina");

		final Routine[] routines = getRoutineManager().getRoutines();

		final ChoiceGroup routineCg = new ChoiceGroup("", Choice.EXCLUSIVE);
		for (int n = 0; n < routines.length; n++) {
			routineCg.append(routines[n].getName(), null);

			if (routines[n].getName().equals(
					getRoutineManager().getCurrentRoutine().getName())) {
				routineCg.setSelectedIndex(n, true);
			}
		}

		final Command cmSelect = new Command("Seleccionar", Command.ITEM, 1);
		final Command cmAdd = new Command("Agregar", Command.ITEM, 1);
		final Command cmDelete = new Command("Borrar", Command.ITEM, 1);
		final Command cmExit = new Command("Salir", Command.EXIT, 1);

		addCommand(cmSelect);
		addCommand(cmAdd);
		addCommand(cmDelete);
		addCommand(cmExit);
		append(routineCg);

		setCommandListener(new CommandListener() {
			public void commandAction(Command command, Displayable displayable) {
				if (command == cmExit) {
					getFormManager().navigateToExit();
				} else if (command == cmAdd) {
					getFormManager().navigateToRoutineLoad();
				} else if (command == cmDelete) {
					getFormManager().confirmate("Desea borrar la rutina?",
							new DialogCallbackAdapter() {
								public void onOk() {
									getRoutineManager().deleteRoutine(
											routines[routineCg
													.getSelectedIndex()]);
								}

								public void afterBoth() {
									getFormManager()
											.navigateToRoutineSelection();
								}
							});
				} else if (command == cmSelect) {
					if (routineCg.getSelectedIndex() >= 0) {
						getRoutineManager().setCurrentRoutine(
								routineCg.getSelectedIndex());
						getFormManager().navigateToDaySelection();
					}
				}
			}
		});
	}
}
