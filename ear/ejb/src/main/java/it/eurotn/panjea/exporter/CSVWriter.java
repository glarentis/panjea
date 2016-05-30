package it.eurotn.panjea.exporter;

import java.io.Writer;
import java.util.Map;

import org.apache.commons.csv.writer.CSVField;

public class CSVWriter extends org.apache.commons.csv.writer.CSVWriter {

	private Writer writer;

	@Override
	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void writeRecord(Map map) {
		writeRecord(map, null);
	}

	/**
	 * Scrive il record usando la struttura specificata.
	 * 
	 * @param map
	 *            valori
	 * @param tipoRecord
	 *            struttuta da utilizzare. Se <code>null</code> verranno utilizzati tutti i CSVField definiti nel
	 *            CSVConfig associato
	 */
	@SuppressWarnings("rawtypes")
	public void writeRecord(Map map, String tipoRecord) {
		CSVField[] fields = ((CSVConfig) getConfig()).getFields(tipoRecord);
		try {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fields.length; i++) {
				if (map.containsKey(fields[i].getName())) {
					Object obj = map.get(fields[i].getName());
					String value = writeValue(fields[i], ((FormatterCSVField) fields[i]).getFormattedValue(obj));
					sb.append(value);
					if (!getConfig().isDelimiterIgnored() && fields.length != i + 1) {
						sb.append(getConfig().getDelimiter());
					}
				}
			}

			if (getConfig().isEndTrimmed()) {
				int i = sb.length() - 1;
				do {
					if (i < 0) {
						break;
					}
					// System.out.println("i : " + i);
					if (!Character.isWhitespace(sb.charAt(i))) {
						break;
					}
					sb.deleteCharAt(i);
					i--;
				} while (true);
			}
			sb.append("\n");
			String line = sb.toString();
			writer.write(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
