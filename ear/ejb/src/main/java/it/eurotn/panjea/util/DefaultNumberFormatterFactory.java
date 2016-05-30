package it.eurotn.panjea.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class DefaultNumberFormatterFactory extends DefaultFormatterFactory {

	private static final long serialVersionUID = 9138085095013810404L;
	private String pattern;
	private Integer numeroDecimaliQta;
	private Class<?> classNumber;
	private boolean allowNullValue;

	/**
	 * Default constructor.
	 * 
	 * @param pattern
	 *            pattern del formatter
	 * @param numeroDecimaliQta
	 *            numero decimali per il formatter
	 * @param classNumber
	 *            classe ritornata dal factory.
	 */
	public DefaultNumberFormatterFactory(final String pattern, final Integer numeroDecimaliQta,
			final Class<?> classNumber) {
		super();
		this.pattern = pattern;
		this.numeroDecimaliQta = numeroDecimaliQta;
		this.classNumber = classNumber;
		this.allowNullValue = false;
		init();
	}

	/**
	 * Default constructor.
	 * 
	 * @param pattern
	 *            pattern del formatter
	 * @param numeroDecimaliQta
	 *            numero decimali per il formatter
	 * @param classNumber
	 *            classe ritornata dal factory.
	 * @param allowNullValue
	 *            abilita la gestione dei valori a null
	 */
	public DefaultNumberFormatterFactory(final String pattern, final Integer numeroDecimaliQta,
			final Class<?> classNumber, final boolean allowNullValue) {
		super();
		this.pattern = pattern;
		this.numeroDecimaliQta = numeroDecimaliQta;
		this.classNumber = classNumber;
		this.allowNullValue = allowNullValue;
		init();
	}

	/**
	 * @param numberFormatter
	 *            numberFormatter
	 */
	public void configureNumberFormatter(NumberFormatter numberFormatter) {
	}

	/**
	 * @return number formatter da utilizzare per la visualizzazione.
	 */
	public NumberFormatter getNumberFormatter() {
		if (!allowNullValue) {
			return new NumberFormatter();
		} else {
			return new NumberFormatter() {
				private static final long serialVersionUID = 1337314227409927670L;

				@Override
				public Object stringToValue(String text) throws ParseException {
					if (text == null || "".equals(text)) {
						return null;
					}
					return super.stringToValue(text);
				}

				@Override
				public String valueToString(Object iv) throws ParseException {
					if ((iv == null)) {
						return "";
					} else {
						return super.valueToString(iv);
					}
				}
			};
		}
	}

	/**
	 * inizializza il formatter.
	 */
	private void init() {
		if (numeroDecimaliQta != null) {
			if (numeroDecimaliQta.intValue() != 0) {
				if (numeroDecimaliQta.intValue() > 6 || numeroDecimaliQta.intValue() < 0) {
					numeroDecimaliQta = 6;
				}
				pattern = pattern + "." + "000000000000000".substring(0, numeroDecimaliQta.intValue());
			}
		}

		NumberFormatter iNumberFormatter = getNumberFormatter();

		DecimalFormat iDecimalFormat = (DecimalFormat) NumberFormat.getNumberInstance();
		iDecimalFormat.applyPattern(pattern);

		iNumberFormatter.setFormat(iDecimalFormat);

		iNumberFormatter.setAllowsInvalid(false);
		iNumberFormatter.setValueClass(classNumber);
		iNumberFormatter.setCommitsOnValidEdit(true);
		configureNumberFormatter(iNumberFormatter);
		this.setDefaultFormatter(iNumberFormatter);
		this.setDisplayFormatter(iNumberFormatter);
	}

	/**
	 * @return Returns the allowNullValue.
	 */
	public boolean isAllowNullValue() {
		return allowNullValue;
	}

}
