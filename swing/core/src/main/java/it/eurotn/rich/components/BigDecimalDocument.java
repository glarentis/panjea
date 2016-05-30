/**
 *
 */
package it.eurotn.rich.components;

import java.text.DecimalFormatSymbols;
import java.util.Arrays;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.apache.log4j.Logger;
import org.springframework.richclient.components.BigDecimalTextField;

/**
 * Document che eredita da {@link PlainDocument} sulla falsariga della inner class Document contenuta in
 * {@link BigDecimalTextField}
 * 
 * Rispetto a quella classe gestisce entrambi i separatori decimali, sia il puntio che la virgola.
 * 
 * @author adriano
 * @version 1.0, 05/giu/08
 * 
 */
public class BigDecimalDocument extends PlainDocument {

	private Logger logger = Logger.getLogger(BigDecimalDocument.class);

	private static final long serialVersionUID = 1L;

	private int nrOfNonDecimals = 15;

	private int nrOfDecimals = 2;

	private boolean negativeSign = true;

	private DecimalFormatSymbols symbols = new DecimalFormatSymbols();

	private char decimalSeparator;

	private char[] decimalsSeparatorInput = { '.', ',' };

	/**
	 *
	 */
	public BigDecimalDocument() {
		this(15, 2, true);
	}

	/**
	 * Costruttore.
	 * 
	 * @param nrOfNonDecimals
	 *            numero cifre non decimali
	 * @param nrOfDecimals
	 *            numero di cifre decimali
	 * @param negativeSign
	 *            <code>true</code> per usare il segno in caso d inumero negativo
	 */
	public BigDecimalDocument(final int nrOfNonDecimals, final int nrOfDecimals, final boolean negativeSign) {
		super();
		this.nrOfNonDecimals = nrOfNonDecimals;
		this.nrOfDecimals = nrOfDecimals;
		this.negativeSign = negativeSign;
		decimalSeparator = symbols.getDecimalSeparator();

	}

	/**
	 * @return Returns the nrOfDecimals.
	 */
	public int getNrOfDecimals() {
		return nrOfDecimals;
	}

	/**
	 * @return Returns the nrOfNonDecimals.
	 */
	public int getNrOfNonDecimals() {
		return nrOfNonDecimals;
	}

	@Override
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		// first doing the single keys, then review what can be used for cut/paste actions

		if ("-".equals(str)) {
			if (this.negativeSign) { // set - or flip to + if it's already there
				if ((this.getLength() == 0) || !this.getText(0, 1).equals("-")) {
					super.insertString(0, str, a);
				} else if (!(this.getLength() == 0) && this.getText(0, 1).equals("-")) {
					super.remove(0, 1);
				}
			}
			return;
		} else if ("+".equals(str)) {
			if (this.negativeSign && (!(this.getLength() == 0) && this.getText(0, 1).equals("-"))) {
				super.remove(0, 1);
			}
			return;
		} else if ((str.length() == 1) && (Arrays.binarySearch(decimalsSeparatorInput, str.charAt(0)) >= 0)) {
			// keypressed = '.' || ',' -> verifica il numero di decimali presenti e che non sia già presente un
			// separatore decimale
			if ((nrOfDecimals > 0) && (nrOfDecimals >= (getLength() - offset))
					&& (getText(0, getLength()).indexOf(this.decimalsSeparatorInput[0]) == -1)
					&& (getText(0, getLength()).indexOf(this.decimalsSeparatorInput[1]) == -1)) {
				super.insertString(offset, Character.toString(this.decimalSeparator), a);
			}
			return;
		}
		String s = getText(0, offset) + str;
		if (offset < getLength()) {
			s += getText(offset, getLength() - offset);
		}

		boolean isNegative = s.startsWith("-");
		char[] sarr = isNegative ? s.substring(1).toCharArray() : s.toCharArray();
		int sep = -1;
		int numberLength = 0; // count numbers, no special characters
		for (int i = 0; i < sarr.length; i++) {
			if (sarr[i] == this.decimalSeparator) {
				if (sep != -1) {// double decimalseparator??
					logger.warn("Error while inserting string: " + s + "[pos=" + i + "]" + " Double decimalseparator?");
					return;
				}
				sep = i;
				if (numberLength > this.nrOfNonDecimals) {// too many digits left of decimal separator
					logger.warn("Error while inserting string: " + s + "[pos=" + i + "]" + " Too many non decimals? ["
							+ this.nrOfNonDecimals + "]");
					return;
				} else if ((sarr.length - sep - 1) > this.nrOfDecimals) {// too many digits right of decimal separator
					logger.warn("Error while inserting string: " + s + "[pos=" + i + "]" + " Too many decimals? ["
							+ this.nrOfDecimals + "]");
					return;
				}
			} else if (sarr[i] == symbols.getGroupingSeparator()) {
				// ignore character
				if (logger.isDebugEnabled()) {
					logger.debug("--> carattere di separazione");
				}
			} else if (!Character.isDigit(sarr[i])) {// non digit, no grouping/decimal separator not allowed
				logger.warn("Error while inserting string: " + s + "[pos=" + i + "]"
						+ " String contains character that is no digit or separator?");
				return;
			} else {
				++numberLength;
			}
		}
		if ((sep == -1) && (numberLength > this.nrOfNonDecimals)) {// no separator, number too big
			logger.warn("Error while inserting string: " + s + " Too many non decimals? [" + this.nrOfNonDecimals + "]");
			return;
		}
		super.insertString(offset, str, a);
	}

	/**
	 * @return Returns the negativeSign.
	 */
	public boolean isNegativeSign() {
		return negativeSign;
	}

	@Override
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
	}

	/**
	 * @param negativeSign
	 *            The negativeSign to set.
	 */
	public void setNegativeSign(boolean negativeSign) {
		this.negativeSign = negativeSign;
	}

	/**
	 * @param nrOfDecimals
	 *            The nrOfDecimals to set.
	 */
	public void setNrOfDecimals(int nrOfDecimals) {
		this.nrOfDecimals = nrOfDecimals;
	}

	/**
	 * @param nrOfNonDecimals
	 *            The nrOfNonDecimals to set.
	 */
	public void setNrOfNonDecimals(int nrOfNonDecimals) {
		this.nrOfNonDecimals = nrOfNonDecimals;
	}

}
