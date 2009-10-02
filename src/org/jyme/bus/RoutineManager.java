package org.jyme.bus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import org.jyme.data.DataManager;
import org.jyme.data.RecordCallback;
import org.jyme.data.RecordStoreCallback;
import org.jyme.domain.Day;
import org.jyme.domain.Exercise;
import org.jyme.domain.Routine;
import org.jyme.domain.Series;

public class RoutineManager {
	private static RoutineManager instance = new RoutineManager();
	private DataManager dataManager = DataManager.getInstance();
	private MIDlet midlet;
	private final static String RS_NAME = "routines";
	private final static String RS_STATE_NAME = "routineState";
	private final static String BASE_URL_KEY = "routineBaseUrl";
	private Routine[] routines;
	private int currentRoutine;
	private int currentDay;
	private int currentExercise;
	private int currentSeries;

	private RoutineManager() {
	}

	public static RoutineManager getInstance() {
		return instance;
	}

	public void loadRoutineFromUrl(final String urlName) {
		final String routineString = StringUtils
				.convertToUnix(getRoutineFromUrl(urlName));
		final Routine newRoutine = Routine.fromString(routineString);

		boolean found = dataManager.execute(RS_NAME, new RecordCallback() {
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
			dataManager.execute(RS_NAME, new RecordStoreCallback() {
				public Object doInRecordStore(RecordStore recordStore)
						throws RecordStoreException {
					recordStore.addRecord(routineString.getBytes(), 0,
							routineString.length());
					return null;
				}
			});
		}
		loadRoutines();
	}

	public void deleteRoutine(final Routine routine) {
		dataManager.execute(RS_NAME, new RecordCallback() {
			public boolean doInRecord(int recordId, RecordStore recordStore,
					int iteration) throws RecordStoreException {
				Routine currentRoutine = Routine.fromString(new String(
						recordStore.getRecord(recordId)));

				if (currentRoutine.getName().equals(routine.getName())) {
					recordStore.deleteRecord(recordId);
					loadRoutines();
					return false;
				} else {
					return true;
				}
			}

		});
	}

	public void navigateForward() {
		if (currentSeries < getCurrentExercise().getSeries().length - 1) {
			currentSeries++;
		} else if (currentExercise < getCurrentDay().getExcercises().length - 1) {
			currentExercise++;
			currentSeries = 0;
		}
	}

	public void navigateBack() {
		if (currentSeries > 0) {
			currentSeries--;
		} else if (currentExercise > 0) {
			currentExercise--;
			currentSeries = (byte) (getCurrentExercise().getSeries().length - 1);
		}
	}

	public void saveState() {
		dataManager.setUniqueRecord(RS_STATE_NAME, new byte[] {
				(byte) currentRoutine, (byte) currentDay,
				(byte) currentExercise, (byte) currentSeries });
	}

	public void loadState() {
		loadRoutines();
		byte[] record = dataManager.getUniqueRecord(RS_STATE_NAME);
		if (record != null) {
			currentRoutine = record[0];
			currentDay = record[1];
			currentExercise = record[2];
			currentSeries = record[3];
		}
	}

	public void resetState() {
		currentRoutine = 0;
		currentDay = 0;
		currentExercise = 0;
		currentSeries = 0;
		saveState();
		loadState();
	}

	public Routine[] getRoutines() {
		if (routines == null) {
			loadRoutines();
		}
		return routines;
	}

	public Routine getCurrentRoutine() {
		return routines[currentRoutine];
	}

	public Day getCurrentDay() {
		return getCurrentRoutine().getDays()[currentDay];
	}

	public Exercise getCurrentExercise() {
		return getCurrentDay().getExcercises()[currentExercise];
	}

	public Series getCurrentSeries() {
		return getCurrentExercise().getSeries()[currentSeries];
	}

	public void setCurrentRoutine(int currentRoutine) {
		this.currentRoutine = limit(currentRoutine, getRoutines());
		setCurrentDay(0);
	}

	public void setCurrentDay(int currentDay) {
		this.currentDay = limit(currentDay, getCurrentRoutine().getDays());
		setCurrentExercise(0);
	}

	public void setCurrentExercise(int currentExercise) {
		this.currentExercise = limit(currentExercise, getCurrentDay()
				.getExcercises());
		setCurrentSeries(0);
	}

	public void setCurrentSeries(int currentSeries) {
		this.currentSeries = limit(currentSeries, getCurrentExercise()
				.getSeries());
	}

	private void loadRoutines() {
		dataManager.execute(RS_NAME, new RecordStoreCallback() {
			public Object doInRecordStore(RecordStore recordStore)
					throws RecordStoreException {
				currentRoutine = 0;
				currentDay = 0;
				currentExercise = 0;
				currentSeries = 0;

				routines = new Routine[recordStore.getNumRecords()];

				dataManager.execute(recordStore, new RecordCallback() {
					public boolean doInRecord(int recordId,
							RecordStore recordStore, int iteration)
							throws RecordStoreException {
						routines[iteration] = Routine.fromString(new String(
								recordStore.getRecord(recordId)));
						return true;
					}
				});

				return null;
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

	private int limit(int value, Object[] array) {
		return value < array.length ? value : array.length - 1;
	}

	public void setMidlet(MIDlet midlet) {
		this.midlet = midlet;
	}
}
