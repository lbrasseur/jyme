package org.jyme;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.jyme.bus.RoutineManager;
import org.jyme.ui.FormManager;

public class MainMidlet extends MIDlet {
	protected void startApp() throws MIDletStateChangeException {
		RoutineManager.getInstance().setMidlet(this);
		FormManager formManager = FormManager.getInstance();
		formManager.setMidlet(this);
		formManager.navigateToRoutineSelection();
	}

	protected void pauseApp() {
	}

	public void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
	}
}
