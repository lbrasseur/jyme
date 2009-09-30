package org.jyme.bus;

public final class StringUtils {
	private StringUtils() {
	}

	public static String convertToUnix(String data) {
		StringBuffer sb = new StringBuffer(data);
		for (int n = 0; n < sb.length(); n++) {
			if (sb.charAt(n) == '\r') {
				sb.deleteCharAt(n);
				n--;
			}
		}
		return sb.toString();
	}

	public static String[] split(String data, String separator) {
		int separatorCount = 0;
		int separatorPos = data.indexOf(separator);
		int beginPos = 0;
		while (separatorPos > 0) {
			separatorCount++;

			beginPos = separatorPos = separatorPos + separator.length();
			separatorPos = data.indexOf(separator, beginPos);
		}

		String[] elements = new String[separatorCount + 1];

		separatorCount = 0;
		separatorPos = data.indexOf(separator);
		beginPos = 0;
		while (separatorPos > 0) {
			elements[separatorCount] = data.substring(beginPos, separatorPos);

			separatorCount++;
			beginPos = separatorPos = separatorPos + separator.length();
			separatorPos = data.indexOf(separator, beginPos);
		}
		elements[separatorCount] = data.substring(beginPos);

		return elements;
	}
}
