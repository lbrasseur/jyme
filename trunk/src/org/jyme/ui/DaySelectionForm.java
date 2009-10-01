package org.jyme.ui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import org.jyme.domain.Routine;

class DaySelectionForm extends BaseForm {
	public DaySelectionForm() {
		super("Seleccion de día");

		final Routine routine = getRoutineManager().getCurrentRoutine();
		
		final ChoiceGroup dayCg = new ChoiceGroup("", Choice.EXCLUSIVE);
		for (int n = 0; n < routine.getDays().length; n++) {
			dayCg.append(routine.getDays()[n].getName(), null);

			if (routine.getDays()[n].getName().equals(
					getRoutineManager().getCurrentDay().getName())) {
				dayCg.setSelectedIndex(n, true);
			}
		}

		final Command cmSelect = new Command("Seleccionar", Command.ITEM, 1);
		final Command cmBack = new Command("Volver", Command.ITEM, 1);

		addCommand(cmSelect);
		addCommand(cmBack);
		append(dayCg);

		setCommandListener(new CommandListener() {
			public void commandAction(Command command, Displayable displayable) {
				if (command == cmBack) {
					getFormManager().navigateToRoutineSelection();
				} else if (command == cmSelect) {
					getRoutineManager().setCurrentDay(dayCg.getSelectedIndex());
					getFormManager().navigateToNavigation();
				}
			}
		});
	}
}
