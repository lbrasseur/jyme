package org.jyme.ui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import org.jyme.bus.RoutineManager;
import org.jyme.domain.Day;
import org.jyme.domain.Routine;

public class FormManager {
	private static FormManager instance = new FormManager();
	private MIDlet midlet;
	private Display display;

	private FormManager() {
	}

	public static FormManager getInstance() {
		return instance;
	}

	public void navigateToRoutineSelection() {
		Routine[] routines = RoutineManager.getInstance().loadRoutines();

		display.setCurrent(new RoutineSelectionForm(routines));
	}

	public void navigateToDaySelection(Routine routine) {
		if (routine.getDays().length == 1) {
			navigateToNavigation(routine.getDays()[0]);
		} else {
			display.setCurrent(new DaySelectionForm(routine));
		}
	}

	public void navigateToNavigation(Day day) {
		display.setCurrent(new NavigationForm(day));
	}

	public void navigateToRoutineLoad() {
		display.setCurrent(new RoutineLoadForm());
	}

	public void navigateToExit() {
		confirmate("Desea salir?", new CommandListener() {
			public void commandAction(Command command, Displayable displayable) {
				if (command.getCommandType() == Command.OK) {
					midlet.notifyDestroyed();
				} else {
					navigateToRoutineSelection();
				}
			}
		});
	}

	public void confirmate(String message, CommandListener commandListener) {
		final Command cmdOK = new Command("OK", Command.OK, 0);
		Command cmdCancel = new Command("Cancel", Command.CANCEL, 0);

		Alert confirm = new Alert(message);
		confirm.setType(AlertType.CONFIRMATION);
		confirm.addCommand(cmdOK);
		confirm.addCommand(cmdCancel);
		confirm.setTimeout(Alert.FOREVER);
		display.setCurrent(confirm);

		confirm.setCommandListener(commandListener);
	}

	public void setMidlet(MIDlet midlet) {
		this.midlet = midlet;
		display = Display.getDisplay(midlet);
	}
}
