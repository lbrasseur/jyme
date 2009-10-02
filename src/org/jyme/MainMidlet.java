package org.jyme;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.jyme.bus.RoutineManager;
import org.jyme.ui.FormManager;

public class MainMidlet extends MIDlet {
	private RoutineManager routineManager = RoutineManager.getInstance();
	private FormManager formManager = FormManager.getInstance();

	protected void startApp() throws MIDletStateChangeException {
		routineManager.setMidlet(this);
		formManager.setMidlet(this);

		try {
			routineManager.loadState();
			formManager.loadState();
		} catch (Exception e) {
			routineManager.resetState();
			formManager.resetState();
		}
	}

	protected void pauseApp() {
		routineManager.saveState();
		formManager.saveState();
	}

	public void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		routineManager.saveState();
		formManager.saveState();
	}
}
