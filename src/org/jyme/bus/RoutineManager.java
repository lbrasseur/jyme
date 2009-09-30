package org.jyme.bus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import org.jyme.domain.Routine;

public class RoutineManager {
	private static RoutineManager instance = new RoutineManager();
	private MIDlet midlet;
	private final static String RS_NAME = "routines";
	private final static String BASE_URL_KEY = "routineBaseUrl";

	public static RoutineManager getInstance() {
		return instance;
	}

	public void loadRoutineFromUrl(final String urlName) {
		final String routineString = StringUtils
				.convertToUnix(getRoutineFromUrl(urlName));
		final Routine newRoutine = Routine.fromString(routineString);

		boolean found = execute(new RecordCallback() {
			public boolean doInRecord(int recordId, RecordStore recordStore,
					int iteration) throws RecordStoreException {
				Routine currentRoutine = Routine.fromString(new String(
						recordStore.getRecord(recordId)));

				if (currentRoutine.getName().equals(newRoutine.getName())) {
					recordStore.setRecord(recordId, routineString.getBytes(),
							0, routineString.length());
					return false;
				} else {
					return true;
				}
			}

		});

		if (!found) {
			execute(new RecordStoreCallback() {
				public Object doInRecordStore(RecordStore recordStore)
						throws RecordStoreException {
					recordStore.addRecord(routineString.getBytes(), 0,
							routineString.length());
					return null;
				}
			});
		}
	}

	public Routine[] loadRoutines() {
		return (Routine[]) execute(new RecordStoreCallback() {
			public Object doInRecordStore(RecordStore recordStore)
					throws RecordStoreException {
				final Routine[] routines = new Routine[recordStore
						.getNumRecords()];

				execute(recordStore, new RecordCallback() {
					public boolean doInRecord(int recordId,
							RecordStore recordStore, int iteration)
							throws RecordStoreException {
						routines[iteration] = Routine.fromString(new String(
								recordStore.getRecord(recordId)));
						return true;
					}
				});

				return routines;
			}
		});
	}

	public void deleteRoutine(final Routine routine) {
		execute(new RecordCallback() {
			public boolean doInRecord(int recordId, RecordStore recordStore,
					int iteration) throws RecordStoreException {
				Routine currentRoutine = Routine.fromString(new String(
						recordStore.getRecord(recordId)));

				if (currentRoutine.getName().equals(routine.getName())) {
					recordStore.deleteRecord(recordId);
					return false;
				} else {
					return true;
				}
			}

		});
	}

	private String getRoutineFromUrl(String name) {
		HttpConnection connection = null;
		InputStream inputstream = null;
		try {
			connection = (HttpConnection) Connector.open(midlet
					.getAppProperty(BASE_URL_KEY)
					+ name);
			// HTTP Request
			connection.setRequestMethod(HttpConnection.GET);
			connection.setRequestProperty("Content-Type", "//text plain");
			connection.setRequestProperty("Connection", "close");
			if (connection.getResponseCode() == HttpConnection.HTTP_OK) {
				System.out.println(connection.getHeaderField(0) + " "
						+ connection.getHeaderFieldKey(0));
				System.out.println("Header Field Date: "
						+ connection.getHeaderField("date"));
				String str;
				inputstream = connection.openInputStream();
				int length = (int) connection.getLength();
				if (length != -1) {
					byte incomingData[] = new byte[length];
					inputstream.read(incomingData);
					str = new String(incomingData);
				} else {
					ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
					int ch;
					while ((ch = inputstream.read()) != -1) {
						bytestream.write(ch);
					}
					str = new String(bytestream.toByteArray());
					bytestream.close();
				}
				return str;
			}

			throw new RuntimeException(
					"No se pudo abrir una conexion. Chequear el nombre");
		} catch (IOException error) {
			throw new RuntimeException(error.toString());
		} finally {
			if (inputstream != null) {
				try {
					inputstream.close();
				} catch (Exception error) {
					throw new RuntimeException(error.toString());
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception error) {
					throw new RuntimeException(error.toString());
				}
			}
		}

	}

	private Object execute(RecordStoreCallback recordStoreCallback) {
		RecordStore recordStore = null;
		try {
			recordStore = RecordStore.openRecordStore(RS_NAME, true);
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

	private boolean execute(final RecordCallback recordCallback) {
		Boolean b = (Boolean) execute(new RecordStoreCallback() {
			public Object doInRecordStore(RecordStore recordStore)
					throws RecordStoreException {
				return new Boolean(execute(recordStore, recordCallback));
			}
		});
		return b.booleanValue();
	}

	private boolean execute(RecordStore recordStore,
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

	private interface RecordStoreCallback {
		Object doInRecordStore(RecordStore recordStore)
				throws RecordStoreException;
	}

	private interface RecordCallback {
		boolean doInRecord(int recordId, RecordStore recordStore, int iteration)
				throws RecordStoreException;
	}

	public void setMidlet(MIDlet midlet) {
		this.midlet = midlet;
	}
}
