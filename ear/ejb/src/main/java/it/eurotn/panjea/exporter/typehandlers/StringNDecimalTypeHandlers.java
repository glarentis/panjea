package it.eurotn.panjea.exporter.typehandlers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class StringNDecimalTypeHandlers implements TypeHandler {

	private int numDecimali = 0;
	private String operazione = "*";
	private String format = "";
	private Map<String, OperazioneDecimaliCommand<String>> operazioniFormat = new HashMap<String, OperazioneDecimaliCommand<String>>();

	private Map<String, OperazioneDecimaliCommand<String>> operazioniParse = new HashMap<String, OperazioneDecimaliCommand<String>>();

	/**
	 * Costruttore.
	 */
	public StringNDecimalTypeHandlers() {
		operazioniFormat.put("*", new OperazioneDecimaliMoltiplicaCommand<String>());
		operazioniFormat.put("/", new OperazioneDecimaliDividiCommand<String>());
		operazioniParse.put("*", new OperazioneDecimaliDividiCommand<String>());
		operazioniParse.put("/", new OperazioneDecimaliMoltiplicaCommand<String>());
	}

	@Override
	public String format(Object value) {
		String returnString = "0";
		if (value != null) {
			returnString = value.toString();
			BigDecimal result;
			if (numDecimali == 0) {
				result = new BigDecimal(returnString);
			} else {
				result = operazioniFormat.get(getOperazione()).execute(returnString, numDecimali);
			}
			DecimalFormat decimalFormat = new DecimalFormat();
			if (!format.isEmpty()) {
				decimalFormat = new DecimalFormat(format);
			}
			DecimalFormatSymbols simboloDecimale = new DecimalFormatSymbols();
			simboloDecimale.setDecimalSeparator(',');
			decimalFormat.setDecimalFormatSymbols(simboloDecimale);
			returnString = decimalFormat.format(result);
		}
		return returnString;
	}

	/**
	 * @return Returns the format.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @return the numDecimali
	 */
	public int getNumDecimali() {
		return numDecimali;
	}

	/**
	 * @return Returns the operazione.
	 */
	public String getOperazione() {
		return operazione;
	}

	@Override
	public Class<?> getType() {
		return String.class;
	}

	@Override
	public Object parse(String s) throws TypeConversionException {

		String returnValue = "0";

		if (s != null && !s.trim().isEmpty()) {

			s = s.trim();

			try {
				double ordineDiGrandezza = 1;
				if (numDecimali != 0) {
					ordineDiGrandezza = Math.pow(10.0, numDecimali);
				}
				BigDecimal returnValueDecimal = new BigDecimal(s).divide(BigDecimal.valueOf(ordineDiGrandezza));

				DecimalFormat decimalFormat = new DecimalFormat();
				if (!format.isEmpty()) {
					decimalFormat = new DecimalFormat(format);
				}
				DecimalFormatSymbols simboloDecimale = new DecimalFormatSymbols();
				simboloDecimale.setDecimalSeparator(',');
				decimalFormat.setDecimalFormatSymbols(simboloDecimale);
				returnValue = decimalFormat.format(returnValueDecimal);
			} catch (Exception e) {
				returnValue = "0";
			}
		}
		return returnValue;
	}

	/**
	 * @param format
	 *            The format to set.
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @param numDecimali
	 *            the numDecimali to set
	 */
	public void setNumDecimali(int numDecimali) {
		this.numDecimali = numDecimali;
	}

	/**
	 * @param operazione
	 *            operazione da effettuare sui numeri decimali quando si esporta (viene invertita nell'importazione); *
	 *            per moltiplicare i numeri decimali <br/>
	 *            / per dividere i numeri decimali
	 */
	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}

}
