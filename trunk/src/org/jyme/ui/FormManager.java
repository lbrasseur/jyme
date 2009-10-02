package org.jyme.ui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

import org.jyme.data.DataManager;

public class FormManager {
	private static FormManager instance = new FormManager();
	private DataManager dataManager = DataManager.getInstance();
	private final static String RS_STATE_NAME = "formState";
	private final static String CHIVO_KEY = "chivo";
	private MIDlet midlet;
	private Display display;
	private final int FORM_ROUTINE_SELECTION = 0;
	private final int FORM_DAY_SELECTION = 1;
	private final int FORM_NAVIGATION = 2;
	private int currentForm = FORM_ROUTINE_SELECTION;

	private FormManager() {
	}

	public static FormManager getInstance() {
		return instance;
	}

	public void navigateToRoutineSelection() {
		currentForm = FORM_ROUTINE_SELECTION;
		navigateToCurrentForm();
	}

	public void navigateToDaySelection() {
		currentForm = FORM_DAY_SELECTION;
		navigateToCurrentForm();
	}

	public void navigateToNavigation() {
		currentForm = FORM_NAVIGATION;
		navigateToCurrentForm();
	}

	public void navigateToRoutineLoad() {
		display.setCurrent(new RoutineLoadForm());
	}

	public void navigateToExit() {
		confirmate("Desea salir?", new DialogCallbackAdapter() {
			public void onOk() {
				midlet.notifyDestroyed();
			}

			public void onCancel() {
				navigateToRoutineSelection();
			}
		});
	}

	public void confirmate(String message, final DialogCallback dialogCallback) {
		final Command cmdOK = new Command("OK", Command.OK, 0);
		Command cmdCancel = new Command("Cancel", Command.CANCEL, 0);

		Alert confirm = new Alert(message);
		confirm.setType(AlertType.CONFIRMATION);
		confirm.addCommand(cmdOK);
		confirm.addCommand(cmdCancel);
		confirm.setTimeout(Alert.FOREVER);
		display.setCurrent(confirm);

		confirm.setCommandListener(new CommandListener() {
			public void commandAction(Command command, Displayable displayable) {
				if (command.getCommandType() == Command.OK) {
					dialogCallback.onOk();
				} else {
					dialogCallback.onCancel();
				}
				dialogCallback.afterBoth();
			}
		});
	}

	public void prompt(String message, String label, String value,
			final DialogCallback dialogCallback) {
		final Command cmdOK = new Command("OK", Command.OK, 0);
		Command cmdCancel = new Command("Cancel", Command.CANCEL, 0);

		Form prompt = new Form(message);
		prompt.addCommand(cmdOK);
		prompt.addCommand(cmdCancel);
		final TextField field = new TextField(label + ": ", value, 50, 0);
		prompt.append(field);
		display.setCurrent(prompt);

		prompt.setCommandListener(new CommandListener() {
			public void commandAction(Command command, Displayable displayable) {
				if (command.getCommandType() == Command.OK) {
					dialogCallback.onOk(field.getString());
				} else {
					dialogCallback.onCancel();
				}
				dialogCallback.afterBoth();
			}
		});
	}

	public void setMidlet(MIDlet midlet) {
		this.midlet = midlet;
		display = Display.getDisplay(midlet);
	}

	public void saveState() {
		dataManager.setUniqueRecord(RS_STATE_NAME,
				new byte[] { (byte) currentForm });
	}

	public void loadState() {
		byte[] record = dataManager.getUniqueRecord(RS_STATE_NAME);
		if (record != null) {
			currentForm = record[0];
		}
		navigateToCurrentForm();
	}

	public void resetState() {
		currentForm = 0;
		saveState();
		loadState();
	}

	private void navigateToCurrentForm() {
		Form form = null;
		switch (currentForm) {
		case FORM_ROUTINE_SELECTION:
			form = new RoutineSelectionForm();
			form.append(getChivo());
			break;
		case FORM_DAY_SELECTION:
			form = new DaySelectionForm();
			form.append(getChivo());
			break;

		case FORM_NAVIGATION:
			form = new NavigationForm();
			break;
		}
		display.setCurrent(form);
	}

	private String getChivo() {
		return "\n" + midlet.getAppProperty(CHIVO_KEY);
	}
}
