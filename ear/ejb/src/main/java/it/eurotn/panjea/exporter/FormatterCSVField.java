package it.eurotn.panjea.exporter;

import java.text.DecimalFormat;
import java.text.Format;

import org.apache.commons.csv.writer.CSVField;

public class FormatterCSVField extends CSVField {

	private Format format;

	/**
	 * Costruttore.
	 * 
	 * @param name
	 *            nome
	 * @param size
	 *            size
	 */
	public FormatterCSVField(final String name, final int size) {
		this(name, size, null);
	}

	/**
	 * Costruttore.
	 * 
	 * @param name
	 *            nome
	 * @param size
	 *            size
	 * @param format
	 *            format da utilizzare per formattare i dati
	 */
	public FormatterCSVField(final String name, final int size, final Format format) {
		super(name, size);
		this.format = format;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FormatterCSVField other = (FormatterCSVField) obj;
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!getName().equals(other.getName())) {
			return false;
		}
		return true;
	}

	/**
	 * Restituisce il valore formattato secondo il formatter definito.
	 * 
	 * @param object
	 *            object
	 * @return valore formattato
	 */
	public String getFormattedValue(Object object) {

		String result = "";

		if (object != null) {
			result = (format == null) ? object.toString() : format.format(object);
			if (format instanceof DecimalFormat) {
				result = result.replace(".", "");
			}
		}

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}
}
