package org.jyme.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;

import org.jyme.bus.RoutineManager;

class RoutineLoadForm extends BaseForm {
	public RoutineLoadForm() {
		super("Nueva rutina");

		final TextField routineName = new TextField("Nombre", "", 20, 0);

		final Command cmAdd = new Command("Agregar", Command.ITEM, 1);
		final Command cmExit = new Command("Volver", Command.BACK, 1);

		addCommand(cmAdd);
		addCommand(cmExit);
		append(routineName);

		setCommandListener(new CommandListener() {
			public void commandAction(Command command, Displayable displayable) {
				if (command == cmExit) {
					FormManager.getInstance().navigateToRoutineSelection();
				} else if (command == cmAdd) {
					new Thread() {
						public void run() {
							RoutineManager.getInstance().loadRoutineFromUrl(
									routineName.getString());
							FormManager.getInstance()
									.navigateToRoutineSelection();
						}
					}.start();
				}
			}
		});
	}
}
