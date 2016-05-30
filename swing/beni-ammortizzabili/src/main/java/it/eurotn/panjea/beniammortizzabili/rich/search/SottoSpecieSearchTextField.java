package it.eurotn.panjea.beniammortizzabili.rich.search;

import it.eurotn.rich.binding.searchtext.SearchTextField;

import java.awt.event.FocusEvent;
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

public class SottoSpecieSearchTextField extends SearchTextField {

	private static class FormattedFactory extends AbstractFormatterFactory {

		private AbstractFormatter formatter = null;

		@Override
		public AbstractFormatter getFormatter(JFormattedTextField tf) {
			if (formatter == null) {
				try {
					formatter = new MaskFormatter("**.**");
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
			protected void processFocusEvent(FocusEvent e) {
				super.processFocusEvent(e);

				// ignore temporary focus event
				if (e.isTemporary()) {
					return;
				}
				if (e.getID() == FocusEvent.FOCUS_LOST) {
					select(0, 0);
				} else {
					select(getDocument().getStartPosition().getOffset(), getDocument().getEndPosition().getOffset());
				}
			}

			@Override
			protected void processKeyEvent(KeyEvent keyevent) {
				switch (keyevent.getKeyChar()) {
				case KeyEvent.VK_PERIOD:
				case KeyEvent.VK_DECIMAL:
					setText(formatCodice());
					// keyevent.consume();
					break;
				default:
					break;
				}
				super.processKeyEvent(keyevent);
			}
		};
		formattedTextField.putClientProperty(CLIENT_PROPERTY, this);
		formattedTextField.setFormatterFactory(new FormattedFactory());
		return formattedTextField;
	}

	/**
	 * @return Stringa inserita nella text formattata correttamente per il sottoConto (###.###.######)
	 */
	protected String formatCodice() {
		String textToFormat = getText();
		if (textToFormat.equals("  .  ")) {
			return "";
		}
		String[] codici = textToFormat.split("\\.");
		String codiceSpecie = null;
		String codiceSottoSpecie = null;
		StringBuffer codiceContoFormat = new StringBuffer();
		for (int i = 0; i < codici.length; i++) {
			switch (i) {
			case 0:
				codiceSpecie = codici[i].trim();
				break;
			case 1:
				codiceSottoSpecie = codici[i].trim();
				break;
			default:
				break;
			}
		}
		if ((codiceSpecie == null) || (codiceSpecie.equals(StringUtils.EMPTY))) {
			return textToFormat;
		}

		try {
			NumberFormat numberFormat = new DecimalFormat("00");
			codiceContoFormat.append(numberFormat.format(new Integer(codiceSpecie))).append(".");
			if ((codiceSottoSpecie == null) || (codiceSottoSpecie.equals(StringUtils.EMPTY))) {
				return codiceContoFormat.toString();
			}
			numberFormat = new DecimalFormat("00");
			codiceContoFormat.append(numberFormat.format(new Integer(codiceSottoSpecie)));
		} catch (NumberFormatException e) {
			// le specie e le sottospecie possono essere anche codici alfabetici
			codiceContoFormat = codiceContoFormat.append(codiceSpecie);
			// il codice specie e sottospecie possono essere solo di 2 caratteri
			if (codiceSpecie.length() == 2) {
				codiceContoFormat = codiceContoFormat.append(".");
			}
			if ((codiceSottoSpecie == null) || (codiceSottoSpecie.equals(StringUtils.EMPTY))) {
				return codiceContoFormat.toString();
			}
			codiceContoFormat = codiceContoFormat.append(codiceSottoSpecie);
		}
		return codiceContoFormat.toString();
	}

	@Override
	public String getSearchingText() {
		return formatCodice();
	}

}
