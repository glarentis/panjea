package it.eurotn.panjea.ordini.rich.editors.ordiniimportati;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class DefaultNumberFormatterFactory extends DefaultFormatterFactory {

	private static final long serialVersionUID = 9138085095013810404L;

	/**
	 * Default constructor.
	 * 
	 * @param pattern
	 *            pattern
	 * @param numeroDecimali
	 *            numero decimali
	 */
	public DefaultNumberFormatterFactory(final String pattern, final Integer numeroDecimali) {
		super();
		initialize(pattern, numeroDecimali);
	}

	/**
	 * Inizializza il formatter.
	 * 
	 * @param pattern
	 *            pattern
	 * @param numeroDecimali
	 *            numero decimali
	 */
	private void initialize(String pattern, Integer numeroDecimali) {
		if (numeroDecimali != null) {
			if (numeroDecimali.intValue() != 0) {
				if (numeroDecimali.intValue() > 6 || numeroDecimali.intValue() < 0) {
					numeroDecimali = 6;
				}
				pattern = pattern + "." + "000000000000000".substring(0, numeroDecimali.intValue());
			}
		}

		NumberFormatter iNumberFormatter = new NumberFormatter();

		DecimalFormat iDecimalFormat = (DecimalFormat) NumberFormat.getNumberInstance();
		iDecimalFormat.applyPattern(pattern);

		iNumberFormatter.setFormat(iDecimalFormat);

		iNumberFormatter.setAllowsInvalid(false);
		iNumberFormatter.setValueClass(BigDecimal.class);
		iNumberFormatter.setCommitsOnValidEdit(true);

		this.setDefaultFormatter(iNumberFormatter);
		this.setDisplayFormatter(iNumberFormatter);
	}

}
