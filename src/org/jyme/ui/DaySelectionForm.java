package org.jyme.ui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import org.jyme.domain.Routine;

class DaySelectionForm extends Form {
	public DaySelectionForm(final Routine routine) {
		super("Seleccion de día");

		final ChoiceGroup dayCg = new ChoiceGroup("", Choice.EXCLUSIVE);
		for (int n = 0; n < routine.getDays().length; n++) {
			dayCg.append(routine.getDays()[n].getName(), null);
		}

		final Command cmSelect = new Command("Seleccionar", Command.SCREEN, 1);
		final Command cmBack = new Command("Volver", Command.BACK, 1);

		addCommand(cmSelect);
		addCommand(cmBack);
		append(dayCg);

		setCommandListener(new CommandListener() {
			public void commandAction(Command command, Displayable displayable) {
				if (command == cmBack) {
					FormManager.getInstance().navigateToRoutineSelection();
				} else if (command == cmSelect) {
					FormManager.getInstance().navigateToNavigation(
							routine.getDays()[dayCg.getSelectedIndex()]);
				}
			}
		});
	}
}
