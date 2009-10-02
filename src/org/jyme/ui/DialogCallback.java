package org.jyme.ui;

public interface DialogCallback {
	void onOk();
	void onOk(String value);
	void onCancel();
	void afterBoth();
}
