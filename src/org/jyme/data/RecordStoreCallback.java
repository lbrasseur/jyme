package org.jyme.data;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public interface RecordStoreCallback {
	Object doInRecordStore(RecordStore recordStore) throws RecordStoreException;
}
