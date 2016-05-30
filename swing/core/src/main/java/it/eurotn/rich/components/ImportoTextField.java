/**
 *
 */
package it.eurotn.rich.components;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.springframework.richclient.components.BigDecimalTextField;

import com.jidesoft.swing.LabeledTextField;

/**
 * Estensione di JTextField sulla falsariga di {@link BigDecimalTextField}.
 *
 * La differenza rispetto a {@link BigDecimalTextField} riguarda l'inizializzazione di un diverso Document e la
 * possibilità di variare l'attribuito nrOfDecimal di Document variando di conseguenza i formati di edit e unedit
 *
 * @author adriano
 * @version 1.0, 03/giu/08
 *
 */
public class ImportoTextField extends LabeledTextField {

	/**
	 * {@link FocusListener} per variare il formato da edit in unedit e viceversa.
	 *
	 * @author adriano
	 * @version 1.0, 03/giu/08
	 */
	private class FormatFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent arg0) {
			if (arg0.isTemporary()) {
				return;
			}
			try {
				try {
					value = BigDecimal.valueOf(format.parse(getText()).doubleValue()); // new
					// BigDecimal(format.parse(getText()).doubleValue());
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
				formattingText = true;
				if (value != null && getTextField().isEditable()) {
					setText(unformat.format(value));
				}
				if (ImportoTextField.this.isSelectAllOnFocusGained()) {
					ImportoTextField.this.getTextField().selectAll();
				}
			} finally {
				formattingText = false;
			}
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			if (arg0.isTemporary()) {
				return;
			}
			try {
				formattingText = true;
				if (value != null) {
					setText(format.format(value));
				}
			} catch (Exception e) {
				System.err.println(e);
			} finally {
				formattingText = false;
			}
		}

		// /**
		// * Format a string.
		// *
		// * @param toFormat
		// * Change to this format.
		// * @param fromFormat
		// * Current format to be changed.
		// * @param s
		// * String to be reformatted.
		// * @return String which holds the number in the new format.
		// */
		// private String format(NumberFormat toFormat, NumberFormat fromFormat, String s) {
		// if (!"".equals(s)) {
		// try {
		// String segn = "";
		// if (s.indexOf("-") > -1) {
		// segn = "-";
		// s = s.replace("-", "");
		// if (s.isEmpty()) {
		// s = "000000";
		// }
		// }
		// return segn + toFormat.format(fromFormat.parse(s));
		// } catch (ParseException pe) {
		// logger.warn("Il valore inserito " + getText() + " non e' un numero");
		// }
		// }
		// return null;
		// }

	}

	private static final long serialVersionUID = 1L;

	public static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols();

	private NumberFormat format;

	private NumberFormat unformat;

	private int nrOfDecimals;

	private boolean formattingText = false;

	/**
	 * Determina se all'evento focusGained viene selezionato tutto il valore contenuto nell'importoTextField.
	 */
	private boolean selectAllOnFocusGained = true;

	/**
	 * determina se è in corso il setter del text di TextField.
	 */
	private boolean settingText = false;

	private BigDecimal value;

	/**
	 * Costruttore.
	 */
	public ImportoTextField() {
		super();
		BigDecimalDocument importoDocument = new BigDecimalDocument();
		getTextField().setDocument(importoDocument);
		getTextField().addFocusListener(new FormatFocusListener());
		getTextField().setHorizontalAlignment(SwingConstants.RIGHT);
		format = new DecimalFormat("###,###,###,##0.######");
		unformat = new DecimalFormat("#0.#######");
	}

	@Override
	protected JTextField createTextField() {
		return new JTextField() {
			private static final long serialVersionUID = 4966452341866006782L;

			@Override
			protected void processKeyEvent(KeyEvent keyevent) {
				if (keyevent.getKeyChar() == KeyEvent.VK_PERIOD) {
					keyevent.setKeyCode(44);
					keyevent.setKeyChar(',');
				}
				super.processKeyEvent(keyevent);

				// eseguo un commit manuale del testo in quanto la jtextfield non lo gestisce come invece lo farebbe una
				// jformattedtextfield
				try {
					value = BigDecimal.valueOf(format.parse(getText()).doubleValue()); // new
					// BigDecimal(format.parse(getText()).doubleValue());
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}

		};
	}

	/**
	 * Getter di BigDecimal eseguendo il parse del valore contenuto nella casella di Testo.
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValue() {
		if ("".equals(getText())) {
			return null;
		}
		if ("-".equals(getText())) {
			return null;
		}
		Number n;
		try {
			n = format.parse(getText());
		} catch (ParseException e) {
			return null;
		}
		BigDecimal bd = BigDecimal.valueOf(n.doubleValue());
		return bd;
	}

	/**
	 * @return Returns the formattingText.
	 */
	public boolean isFormattingText() {
		return formattingText;
	}

	/**
	 * @return true (default) se all'evento focusGained viene selezionato tutto il valore contenuto
	 *         nell'importoTextField
	 */
	public boolean isSelectAllOnFocusGained() {
		return selectAllOnFocusGained;
	}

	/**
	 * @return Returns the settingText.
	 */
	public boolean isSettingText() {
		return settingText;
	}

	@Override
	public void setEnabled(boolean flag) {
		super.setEnabled(flag);
	}

	/**
	 * @return Returns the settingValue.
	 */
	// public boolean isSettingValue() {
	// return settingValue;
	// }
	/**
	 * @param format
	 *            The format to set.
	 */
	public void setFormat(NumberFormat format) {
		this.format = format;
	}

	/**
	 * set di nrOfDecimals e ridefinizione del suo formato.
	 *
	 * @param nrOfDecimals
	 *            il numero decimali
	 */
	public void setNrOfDecimals(int nrOfDecimals) {
		// set setto gli stessi numeri decimali esco.
		if (this.nrOfDecimals == nrOfDecimals) {
			return;
		}

		this.nrOfDecimals = nrOfDecimals;
		((BigDecimalDocument) getTextField().getDocument()).setNrOfDecimals(nrOfDecimals);
		StringBuffer formatString = new StringBuffer("###,###,###,##0");
		StringBuffer unformatString = new StringBuffer("#0");
		for (int i = 0; i < nrOfDecimals; i++) {
			if (i == 0) {
				formatString.append(".");
				unformatString.append(".");
			}
			formatString.append("0");
			unformatString.append("#");
		}
		format = new DecimalFormat(formatString.toString());
		unformat = new DecimalFormat(unformatString.toString());
	}

	/**
	 * set di NrOfNonDecimal.
	 *
	 * @param nrOfNonDecimals
	 *            numero di cifre per la parte intera
	 */
	public void setNrOfNonDecimals(int nrOfNonDecimals) {
		((BigDecimalDocument) getTextField().getDocument()).setNrOfNonDecimals(nrOfNonDecimals);
	}

	@Override
	public void setText(String text) {
		try {
			settingText = true;
			super.setText(text);
		} finally {
			settingText = false;
		}

	}

	/**
	 * @param unformat
	 *            The unformat to set.
	 */
	public void setUnformat(NumberFormat unformat) {
		this.unformat = unformat;
	}

	/**
	 * @param value
	 *            value per il text field
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
		String txt = null;
		if (value != null) {
			txt = this.format.format(value.doubleValue());
		}
		setText(txt);
	}

}
