package it.eurotn.panjea.contabilita.rich.search;

import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti.ETipoRicercaSottoConto;
import it.eurotn.rich.binding.searchtext.SearchTextField;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.MaskFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.components.PatchedJFormattedTextField;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.rules.closure.Closure;

public class SottoContoSearchTextField extends SearchTextField {

	private static class FormattedFactory extends AbstractFormatterFactory {

		private AbstractFormatter formatter = null;

		@Override
		public AbstractFormatter getFormatter(JFormattedTextField tf) {
			if (formatter == null) {
				try {
					formatter = new MaskFormatter("###.###.######");
				} catch (ParseException e) {
					formatter = new DefaultFormatter();
				}
			}
			return formatter;
		}
	}

	private static class RicercaCodiceClosure implements Closure {

		@Override
		public Object call(Object textField) {
			SearchTextField searchTextField = (SearchTextField) textField;
			((JFormattedTextField) searchTextField.getTextField()).setFormatterFactory(formatter);
			return null;
		}

	}

	private static class RicercaDescrizioneClosure implements Closure {

		@Override
		public Object call(Object textField) {
			SearchTextField searchTextField = (SearchTextField) textField;
			((JFormattedTextField) searchTextField.getTextField()).setFormatterFactory(null);
			return null;
		}

	}

	private static FormattedFactory formatter;
	private static Map<ETipoRicercaSottoConto, Closure> tipoRicercheClosure;
	private ETipoRicercaSottoConto tipoRicerca;
	private JLabel labelDescrizione;

	static {
		formatter = new FormattedFactory();
		tipoRicercheClosure = new HashMap<ParametriRicercaSottoConti.ETipoRicercaSottoConto, Closure>();
		tipoRicercheClosure.put(ETipoRicercaSottoConto.DESCRIZIONE, new RicercaDescrizioneClosure());
		tipoRicercheClosure.put(ETipoRicercaSottoConto.CODICE, new RicercaCodiceClosure());
	}

	private static final long serialVersionUID = -6911058024872272889L;

	@Override
	protected JTextField createTextField() {
		PatchedJFormattedTextField textFormatted = new PatchedJFormattedTextField() {
			private static final long serialVersionUID = -2368706289443914000L;

			@Override
			protected void processFocusEvent(FocusEvent e) {
				super.processFocusEvent(e);
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
				if (tipoRicerca == ETipoRicercaSottoConto.CODICE) {
					switch (keyevent.getKeyChar()) {
					case KeyEvent.VK_PERIOD:
					case KeyEvent.VK_DECIMAL:
						setText(formatCodiceSottoConto());
						// keyevent.consume();
						break;
					default:
						break;
					}
				}
				super.processKeyEvent(keyevent);
			}
		};
		textFormatted.putClientProperty(CLIENT_PROPERTY, this);
		return textFormatted;
	}

	/**
	 * 
	 * @return Stringa inserita nella text formattata correttamente per il sottoConto (###.###.######)
	 */
	protected String formatCodiceSottoConto() {
		String textToFormat = getText();
		if (textToFormat.equals("   .   .      ")) {
			return "";
		}
		String[] codici = textToFormat.split("\\.");
		String codiceMastro = null;
		String codiceConto = null;
		String codiceSottoConto = null;
		StringBuffer codiceSottoContoFormat = new StringBuffer();
		for (int i = 0; i < codici.length; i++) {
			switch (i) {
			case 0:
				codiceMastro = codici[i].trim();
				break;
			case 1:
				codiceConto = codici[i].trim();
				break;
			case 2:
				codiceSottoConto = codici[i].trim();
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
			codiceSottoContoFormat.append(numberFormat.format(new Integer(codiceMastro))).append(".");
			if ((codiceConto == null) || (codiceConto.equals(StringUtils.EMPTY))) {
				return codiceSottoContoFormat.toString();
			}
			numberFormat = new DecimalFormat(Conto.DEFAULT_CODICE);
			codiceSottoContoFormat.append(numberFormat.format(new Integer(codiceConto))).append(".");
			if ((codiceSottoConto == null) || (codiceSottoConto.equals(StringUtils.EMPTY))) {
				return codiceSottoContoFormat.toString();
			}
			numberFormat = new DecimalFormat(SottoConto.DEFAULT_CODICE);
			codiceSottoContoFormat.append(numberFormat.format(new Integer(codiceSottoConto)));
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
		return codiceSottoContoFormat.toString();
	}

	/**
	 * @return label con il codice o la descrizione del sottoConto selezionato. Se la searchText contiene il conto la
	 *         label visualizza la descrizione e viceversa.
	 */
	public JLabel getLabelSottoConto() {
		labelDescrizione = new JLabel();
		labelDescrizione.setName(formModel.getId() + "." + formPropertyPath + "." + getPropertyRenderer());
		updateLabelDescrizione();
		return labelDescrizione;
	}

	@Override
	public String getSearchingText() {
		String text = super.getSearchingText();
		if (tipoRicerca == ETipoRicercaSottoConto.CODICE) {
			text = formatCodiceSottoConto();
		}
		return text;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		updateLabelDescrizione();
	}

	/**
	 * 
	 * @param tipoRicercaParam
	 *            tipoRicerca per il sottoConto. {@link ETipoRicercaSottoConto}
	 * @param propertyRender
	 *            nuova renderKey per il campo di ricerca.
	 */
	public void setTipoRicercaSottoConto(ETipoRicercaSottoConto tipoRicercaParam, String propertyRender) {
		setPropertyRenderer(propertyRender);
		this.tipoRicerca = tipoRicercaParam;
		tipoRicercheClosure.get(tipoRicerca).call(this);
		propertyChange(null);
		getTextField().selectAll();
		updateLabelDescrizione();
	}

	/**
	 * Aggiorna la label della descrizione/codice se è presente.
	 */
	private void updateLabelDescrizione() {
		if (labelDescrizione != null) {
			SottoConto sottoConto = (SottoConto) formModel.getValueModel(formPropertyPath).getValue();
			if (sottoConto == null) {
				labelDescrizione.setText(" ");
			} else if (tipoRicerca == ETipoRicercaSottoConto.CODICE) {
				// MAIL: in caso di nuovo può arrivare la descrizione null
				String descSottoConto = sottoConto.getDescrizione() != null ? sottoConto.getDescrizione() : "";
				labelDescrizione.setText(descSottoConto);
				GuiStandardUtils.truncateLabelIfLong(labelDescrizione);
			} else {
				labelDescrizione.setText(sottoConto.getSottoContoCodice());
			}
		}
	}
}
