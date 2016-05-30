/**
 *
 */
package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;
import it.eurotn.rich.components.ImportoTextField;
import it.eurotn.rich.dialog.InputApplicationDialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.components.BigDecimalTextField;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

/**
 * Pannello composto da una label e un textField per visualizzare un importo {@link BigDecimal}, pannello usato nelle
 * classi {@link CarrelloPagamentiTablePage} e {@link RisultatiRicercaRatePage}.
 * 
 * @author Leonardo
 */
public class TotaleImportoRatePerPagamentoPanel extends JPanel implements TableModelListener {

	public class TotalePagamentiTextField extends ImportoTextField {
		private static final long serialVersionUID = 7399206224924441404L;
		private BigDecimalTextField bigDecimalTextField = null;

		@Override
		protected void showContextMenu() {
			InputApplicationDialog dialog = new InputApplicationDialog("Importo massimo carrello", Application
					.instance().getActiveWindow().getControl());
			bigDecimalTextField = new BigDecimalTextField(10, 2, true, BigDecimal.class);
			dialog.setInputField(bigDecimalTextField);
			((BigDecimalTextField) dialog.getInputField()).setValue(importoMassimo);
			((BigDecimalTextField) dialog.getInputField()).setColumns(10);
			((BigDecimalTextField) dialog.getInputField()).setHorizontalAlignment(SwingConstants.RIGHT);
			dialog.setInputLabelMessage("Valore massimo dei pagamenti");
			dialog.setFinishAction(new Closure() {

				@Override
				public Object call(Object arg0) {
					importoMassimo = BigDecimal.ZERO;
					BigDecimal val = (BigDecimal) bigDecimalTextField.getValue();
					if (val != null) {
						importoMassimo = val;
					}

					// if (importoMassimo.compareTo(BigDecimal.ZERO) != 0 && importoMassimo.compareTo(totalePagamenti) <
					// 0) {
					if (checkImportoMassimoRaggiunto(totalePagamenti)) {
						MessageDialog importoSuperatoDialog = new MessageDialog("Importo maggiore",
								"Il carrello contiene già pagamenti superiori all'importo di blocco.<br>Rimuovere manualmente le rate.");
						importoSuperatoDialog.setPreferredSize(new Dimension(250, 150));
						importoSuperatoDialog.showDialog();
					}
					updatePanel();
					return null;
				}
			});
			dialog.showDialog();
		}
	}

	private static final long serialVersionUID = -2475522379337791471L;
	private static final String TOTALE_PAGAMENTI_KEY = "totalePagamenti";
	private static final String TOTALE_PARZIALE_PAGAMENTI_KEY = "totaleParzialePagamenti";

	private CarrelloPagamentiTableModel carrelloPagamentiTableModel;

	private ImportoTextField textFieldTotale;
	private ImportoTextField textFieldTotaleParziale;

	private BigDecimal importoMassimo;
	private BigDecimal totalePagamenti;
	private BigDecimal totaleParzialePagamenti;

	private DecimalFormat importoFormat;
	private ValutaAziendaCache valutaAziendaCache;
	private String codiceValuta;

	/**
	 * Costruttore pannello.
	 * 
	 * @param carrelloPagamentiTableModel
	 *            carrelloPagamentiTableModel
	 */
	public TotaleImportoRatePerPagamentoPanel(final CarrelloPagamentiTableModel carrelloPagamentiTableModel) {
		super();
		initialize();
		this.valutaAziendaCache = RcpSupport.getBean(ValutaAziendaCache.BEAN_ID);
		this.carrelloPagamentiTableModel = carrelloPagamentiTableModel;
	}

	/**
	 * Controlla se l'importo inserito ha raggiunto il tetto massimo impostato.
	 * 
	 * @param importo
	 *            l'importo da verificare
	 * @return true se importo massimo raggiunto
	 */
	public boolean checkImportoMassimoRaggiunto(BigDecimal importo) {

		// esco subito se non ho impostato un importo massimo
		if (importoMassimo.compareTo(BigDecimal.ZERO) == 0) {
			return false;
		}

		return (importoMassimo.signum() == 1 && importoMassimo.compareTo(totalePagamenti) <= 0)
				|| (importoMassimo.signum() == -1 && importoMassimo.compareTo(totalePagamenti) >= 0);
	}

	/**
	 * @return Returns the importoMassimo.
	 */
	public BigDecimal getImportoMassimo() {
		return importoMassimo;
	}

	/**
	 * Aggiunge al pannello label e textField.
	 */
	private void initialize() {
		importoFormat = new DecimalFormat("###,##0.00");
		importoMassimo = BigDecimal.ZERO;
		totalePagamenti = BigDecimal.ZERO;
		totaleParzialePagamenti = BigDecimal.ZERO;
		codiceValuta = "";
		ComponentFactory componentFactory = (ComponentFactory) Application.services()
				.getService(ComponentFactory.class);
		MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

		setLayout(new FlowLayout(FlowLayout.RIGHT));

		// totale
		textFieldTotale = new TotalePagamentiTextField();
		textFieldTotale.getLabel().setIcon(RcpSupport.getIcon("rigaChiusa.icon"));
		textFieldTotale.getLabel().setName("importoMassimoLabel");
		textFieldTotale.setColumns(10);
		textFieldTotale.setFocusable(false);

		// totale parziale
		textFieldTotaleParziale = new ImportoTextField() {
			private static final long serialVersionUID = -3322361255484529751L;

			@Override
			protected void showContextMenu() {
				carrelloPagamentiTableModel.resetTotaleParziale();
				totaleParzialePagamenti = BigDecimal.ZERO;
				updatePanel();
			}
		};
		textFieldTotaleParziale.getLabel().setIcon(RcpSupport.getIcon("rimuovi.icon"));
		textFieldTotaleParziale.getLabel().setText("Azzera totale parziale");
		textFieldTotaleParziale.getLabel().setName("azzeraTotaleParzialeLabel");
		textFieldTotaleParziale.setName("azzeraTotaleParzialeTextField");
		textFieldTotaleParziale.getLabel().setToolTipText("Azzera totale parziale");
		textFieldTotaleParziale.setColumns(10);

		JLabel labelTotParzialeRate = componentFactory.createLabelFor(
				messageSource.getMessage(TOTALE_PARZIALE_PAGAMENTI_KEY, null, Locale.getDefault()),
				textFieldTotaleParziale);
		JLabel labelTotRate = componentFactory.createLabelFor(
				messageSource.getMessage(TOTALE_PAGAMENTI_KEY, null, Locale.getDefault()), textFieldTotale);

		add(labelTotParzialeRate);
		add(textFieldTotaleParziale);

		add(labelTotRate);
		add(textFieldTotale);
	}

	/**
	 * Reset dei totali del pannello.
	 */
	public void resetTotali() {
		importoMassimo = BigDecimal.ZERO;
		totalePagamenti = BigDecimal.ZERO;
		totaleParzialePagamenti = BigDecimal.ZERO;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		totalePagamenti = ((CarrelloPagamentiTableModel) e.getSource()).getTotalePagamenti();
		totaleParzialePagamenti = ((CarrelloPagamentiTableModel) e.getSource()).getTotaleParzialePagamenti();
		codiceValuta = ((CarrelloPagamentiTableModel) e.getSource()).getCodiceValuta();
		updatePanel();
	}

	/**
	 * Aggiorna i valori dei controlli.
	 */
	private void updatePanel() {
		String labelText = "";
		StringBuilder sb = new StringBuilder();
		if (importoMassimo.compareTo(BigDecimal.ZERO) != 0) {
			sb.append("(Blocco:");
			sb.append(importoFormat.format(importoMassimo));
			sb.append(" Rimanenza:");
			sb.append(importoFormat.format(importoMassimo.subtract(totalePagamenti)));
			sb.append(")");
			labelText = sb.toString();
		}
		String simboloValuta = "";
		Integer numeroDecimali = 2;
		if (codiceValuta != null) {
			ValutaAzienda valutaAzienda = valutaAziendaCache.caricaValutaAzienda(codiceValuta);
			numeroDecimali = valutaAzienda.getNumeroDecimali();
			simboloValuta = valutaAzienda.getSimbolo();
		}
		textFieldTotale.setLabelText(labelText + simboloValuta);
		textFieldTotale.setNrOfDecimals(numeroDecimali);
		textFieldTotale.setValue(totalePagamenti);

		textFieldTotaleParziale.setLabelText(simboloValuta);
		textFieldTotaleParziale.setNrOfDecimals(numeroDecimali);
		textFieldTotaleParziale.setValue(totaleParzialePagamenti);
	}

}
