package org.jyme.data;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class DataManager {
	private static DataManager instance = new DataManager();

	public static DataManager getInstance() {
		return instance;
	}

	public Object execute(String rsName, RecordStoreCallback recordStoreCallback) {
		RecordStore recordStore = null;
		try {
			recordStore = RecordStore.openRecordStore(rsName, true);
			return recordStoreCallback.doInRecordStore(recordStore);
		} catch (RecordStoreException e) {
			throw new RuntimeException(e.toString());
		} finally {
			if (recordStore != null) {
				try {
					recordStore.closeRecordStore();
				} catch (RecordStoreException e) {
					throw new RuntimeException(e.toString());
				}
			}
		}
	}

	public boolean execute(String rsName, final RecordCallback recordCallback) {
		Boolean b = (Boolean) execute(rsName, new RecordStoreCallback() {
			public Object doInRecordStore(RecordStore recordStore)
					throws RecordStoreException {
				return new Boolean(execute(recordStore, recordCallback));
			}
		});
		return b.booleanValue();
	}

	public boolean execute(RecordStore recordStore,
			RecordCallback recordCallback) throws RecordStoreException {
		int n = 0;
		RecordEnumeration re = recordStore.enumerateRecords(null, null, false);
		boolean cont = true;
		while (cont && re.hasNextElement()) {
			cont = recordCallback.doInRecord(re.nextRecordId(), recordStore, n);
			n++;
		}
		return !cont;
	}

	public byte[] getUniqueRecord(String rsName) {
		return (byte[]) execute(rsName, new RecordStoreCallback() {
			public Object doInRecordStore(RecordStore recordStore)
					throws RecordStoreException {
				RecordEnumeration re = recordStore.enumerateRecords(null, null,
						false);
				if (re.hasNextElement()) {
					return re.nextRecord();
				} else {
					return null;
				}
			}
		});
	}

	public void setUniqueRecord(String rsName, final byte[] data) {
		execute(rsName, new RecordStoreCallback() {
			public Object doInRecordStore(RecordStore recordStore)
					throws RecordStoreException {
				RecordEnumeration re = recordStore.enumerateRecords(null, null,
						false);
				if (re.hasNextElement()) {
					recordStore.setRecord(re.nextRecordId(), data, 0,
							data.length);
				} else {
					recordStore.addRecord(data, 0, data.length);
				}
				return null;
			}
		});
	}
	
	public void deleteRecordStore(String rsName) {
		try {
			RecordStore.deleteRecordStore(rsName);
		} catch (RecordStoreException e) {
			throw new RuntimeException(e.toString());
		}
	}
}
