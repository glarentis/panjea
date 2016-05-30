package it.eurotn.panjea.contabilita.rich.search;

import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.rich.binding.searchtext.SearchTextField;

import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.MaskFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.components.PatchedJFormattedTextField;

public class ContoSearchTextField extends SearchTextField {

	private static class FormattedFactory extends AbstractFormatterFactory {

		private AbstractFormatter formatter = null;

		@Override
		public AbstractFormatter getFormatter(JFormattedTextField tf) {
			if (formatter == null) {
				try {
					formatter = new MaskFormatter("###.###");
				} catch (ParseException e) {
					formatter = new DefaultFormatter();
				}
			}
			return formatter;
		}
	}

	private static final long serialVersionUID = 3676673782542927045L;

	@Override
	protected JTextField createTextField() {
		JFormattedTextField formattedTextField = new PatchedJFormattedTextField() {
			private static final long serialVersionUID = -2368706289443914000L;

			@Override
			protected void processKeyEvent(KeyEvent keyevent) {
				switch (keyevent.getKeyChar()) {
				case KeyEvent.VK_PERIOD:
				case KeyEvent.VK_DECIMAL:
					setText(formatCodice());
					break;
				default:
					break;
				}
				super.processKeyEvent(keyevent);
			}
		};
		formattedTextField.setFormatterFactory(new FormattedFactory());
		formattedTextField.putClientProperty(CLIENT_PROPERTY, this);
		return formattedTextField;
	}

	/**
	 * 
	 * @return Stringa inserita nella text formattata correttamente per il sottoConto (###.###.######)
	 */
	protected String formatCodice() {
		String textToFormat = getText();
		if (textToFormat.equals("   .   ")) {
			return "";
		}
		String[] codici = textToFormat.split("\\.");
		String codiceMastro = null;
		String codiceConto = null;
		StringBuffer codiceContoFormat = new StringBuffer();
		for (int i = 0; i < codici.length; i++) {
			switch (i) {
			case 0:
				codiceMastro = codici[i].trim();
				break;
			case 1:
				codiceConto = codici[i].trim();
				break;
			default:
				break;
			}
		}
		if ((codiceMastro == null) || (codiceMastro.equals(StringUtils.EMPTY))) {
			return textToFormat;
		}

		try {
			NumberFormat numberFormat = new DecimalFormat(Mastro.DEFAULT_CODICE);
			codiceContoFormat.append(numberFormat.format(new Integer(codiceMastro))).append(".");
			if ((codiceConto == null) || (codiceConto.equals(StringUtils.EMPTY))) {
				return codiceContoFormat.toString();
			}
			numberFormat = new DecimalFormat(Conto.DEFAULT_CODICE);
			codiceContoFormat.append(numberFormat.format(new Integer(codiceConto)));
			if ((codiceConto == null) || (codiceConto.equals(StringUtils.EMPTY))) {
				return codiceContoFormat.toString();
			}
		} catch (NumberFormatException e) {
			// faccio il catch dell'eccezione perchè può essere sollevata a causa della maschera
			// sulla formatted. Esempio: se ho inserito
			// 001.001.000001
			// e cancello la seconda cifra con la maschera applicata ottengo questo risultato
			// 0 1.001.000001 anzichè
			// 01.001.000001
			// questo provoca poi la NumberFormatException cercando di formattare la stringa in un
			// numero
			return textToFormat;
		}
		return codiceContoFormat.toString();
	}

	@Override
	public String getSearchingText() {
		return formatCodice();
	}

}
