package org.jyme.ui;

import javax.microedition.lcdui.Form;

import org.jyme.bus.RoutineManager;

class BaseForm extends Form {
	protected BaseForm(String title) {
		super(title);
	}

	protected RoutineManager getRoutineManager() {
		return RoutineManager.getInstance();
	}

	protected FormManager getFormManager() {
		return FormManager.getInstance();
	}
}
