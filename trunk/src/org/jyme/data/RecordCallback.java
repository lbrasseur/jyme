package org.jyme.data;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public interface RecordCallback {
	boolean doInRecord(int recordId, RecordStore recordStore, int iteration)
			throws RecordStoreException;
}
