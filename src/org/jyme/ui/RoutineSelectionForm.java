package org.jyme.ui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import org.jyme.bus.RoutineManager;
import org.jyme.domain.Routine;

class RoutineSelectionForm extends Form {
	private FormManager formManager = FormManager.getInstance();

	public RoutineSelectionForm(final Routine[] routines) {
		super("Seleccion de rutina");
		final ChoiceGroup routineCg = new ChoiceGroup("", Choice.EXCLUSIVE);
		for (int n = 0; n < routines.length; n++) {
			routineCg.append(routines[n].getName(), null);
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
					formManager.navigateToExit();
				} else if (command == cmAdd) {
					formManager.navigateToRoutineLoad();
				} else if (command == cmDelete) {
					formManager.confirmate("Desea borrar la rutina?",
							new CommandListener() {
								public void commandAction(Command command,
										Displayable displayable) {
									if (command.getCommandType() == Command.OK) {
										RoutineManager
												.getInstance()
												.deleteRoutine(
														routines[routineCg
																.getSelectedIndex()]);
									}
									formManager.navigateToRoutineSelection();
								}
							});

				} else if (command == cmSelect) {
					if (routineCg.getSelectedIndex() >= 0) {
						formManager.navigateToDaySelection(routines[routineCg
								.getSelectedIndex()]);
					}
				}
			}
		});
	}
}
