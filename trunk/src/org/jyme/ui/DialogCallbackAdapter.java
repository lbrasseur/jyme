package org.jyme.ui;

public abstract class DialogCallbackAdapter implements DialogCallback {
	public void onOk() {
	}

	public void onOk(String value) {
		onOk();
	}

	public void onCancel() {
	}

	public void afterBoth() {
	}
}
