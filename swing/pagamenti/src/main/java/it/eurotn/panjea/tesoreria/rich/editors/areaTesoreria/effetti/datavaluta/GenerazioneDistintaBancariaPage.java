/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.effetti.datavaluta;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti.RaggruppamentoEffetti;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.rich.forms.ParametriCreazioneAreaEffettiForm;
import it.eurotn.panjea.tesoreria.rich.pm.ParametriCreazioneAreaEffetti;
import it.eurotn.rich.control.table.style.FocusCellStyle;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.dialog.AbstractDialogPage;

import com.jidesoft.grid.ExpandableRow;
import com.jidesoft.grid.Row;
import com.jidesoft.grid.TreeTable;

/**
 * @author Leonardo
 *
 */
public class GenerazioneDistintaBancariaPage extends AbstractDialogPage {

	public class AssegnaDataCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand command) {
			ParametriRicercaSomma parametri = (ParametriRicercaSomma) parametriRicercaSommaForm.getFormObject();
			Date dataValuta = parametri.getDataValutaDaAssegnare();
			int[] indexSelectedRow = treeTable.getSelectedRows();
			for (int i : indexSelectedRow) {
				Row row = treeTable.getRowAt(i);
				if (row.getClass().equals(EffettoRow.class)) {
					row.setValueAt(dataValuta, 1);
				}
			}
			treeTable.updateUI();
		}

		@Override
		public boolean preExecution(ActionCommand command) {
			return true;
		}

	}

	private class ColumnModelListener implements TableColumnModelListener {

		@Override
		public void columnAdded(TableColumnModelEvent e) {
		}

		@Override
		public void columnMarginChanged(ChangeEvent e) {
		}

		@Override
		public void columnMoved(TableColumnModelEvent e) {
			// Raggruppo solamente se sto spostando sulla prima colonna
			int colonnaModello = treeTable.convertColumnIndexToModel(e.getToIndex());
			if (e.getToIndex() != 0 || colonnaModello > 1) {
				return;
			}
			treeTable.sortColumn(colonnaModello);
			if (colonnaModello == 1) {
				treeTableModel.createRow(RaggruppamentoEffetti.DATA_VALUTA);
			} else {
				treeTableModel.createRow(RaggruppamentoEffetti.DATA_SCADENZA);
			}
			treeTable.expandAll();
		}

		@Override
		public void columnRemoved(TableColumnModelEvent e) {
		}

		@Override
		public void columnSelectionChanged(ListSelectionEvent e) {
		}

	}

	private class CombinazioneSelezionataIndexPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
			ParametriRicercaSomma parametri = (ParametriRicercaSomma) parametriRicercaSommaForm.getFormObject();
			List<Effetto> combinazione = parametri.getCombinazioneSelezionata();
			treeTable.clearSelection();
			treeTable.setFillsSelection(false);

			for (Effetto effetto : combinazione) {
				for (ExpandableRow riga : treeTableModel.getRows()) {
					if (riga instanceof EffettoRow && ((EffettoRow) riga).getEffetto().equals(effetto)) {
						treeTable.addSelectedRow(riga);
					}
				}

			}
		}

	}

	private class RaggruppaBancheValueChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			boolean raggruppa = (boolean) arg0.getNewValue();
			if (raggruppa) {
				treeTableModel = new GenerazioneDataValutaEffettiBancaTreeTableModel(areaEffetti);
			} else {
				treeTableModel = new GenerazioneDataValutaEffettiTreeTableModel(areaEffetti);
			}
			treeTableModel.createRow();
			treeTable.setModel(treeTableModel);
			treeTable.expandAll();
		}
	}

	/**
	 * Costruttore.
	 *
	 * @param areaEffetti
	 *            areaEffetti con gli effetti da raggruppare.
	 */
	public static final String PAGE_ID = "generazioneDistintaBancariaPage";
	private static final String LABEL_KEY_NUMERO_EFFETTI = "numeroEffetti";

	private static Logger logger = Logger.getLogger(GenerazioneDistintaBancariaPage.class);
	private ParametriCreazioneAreaEffettiForm parametriCreazioneAreaEffettiForm = null;
	private ParametriRicercaSommaForm parametriRicercaSommaForm = null;
	private int numeroEffetti = -1;
	private AreaEffetti areaEffetti;
	private GenerazioneDataValutaEffettiTreeTableModel treeTableModel;

	private TreeTable treeTable;

	/**
	 *
	 * @param areaEffetti
	 *            areaEffetti gestita dalla pagina.
	 */
	public GenerazioneDistintaBancariaPage(final AreaEffetti areaEffetti) {
		super(PAGE_ID);
		this.areaEffetti = areaEffetti;
		// ricrea il treeModel espandendo tutto e azzera il formObject con un
		// nuovo oggetto
		RapportoBancarioAzienda rapportoBancarioAzienda = areaEffetti.getDocumento().getRapportoBancarioAzienda();
		Integer giorniBanca = rapportoBancarioAzienda.getGiorniBanca();

		if (giorniBanca == null) {
			logger.warn("--> I Giorni banca per il rapporto bancario non sono impostati, imposto 0");
			giorniBanca = new Integer(0);
		}

		// assegno agli effettiDaRaggruppare una data Valuta=data Scadenza della
		// rata del suo primo pagamento
		for (Effetto effetto : areaEffetti.getEffetti()) {
			effetto.setDataValuta(effetto.getPagamenti().iterator().next().getRata().getDataScadenza());
			effetto.addGiorniBanca(giorniBanca);
		}

		parametriRicercaSommaForm = new ParametriRicercaSommaForm(areaEffetti);
		parametriCreazioneAreaEffettiForm = new ParametriCreazioneAreaEffettiForm();
		// Creo i parametri e setto le spese di presentazione
		ParametriCreazioneAreaEffetti parametriCreazioneAreaEffetti = new ParametriCreazioneAreaEffetti();
		if (rapportoBancarioAzienda.getSpesePresentazione() != null) {
			BigDecimal spesePresentazione = rapportoBancarioAzienda.getSpesePresentazione().multiply(
					new BigDecimal(areaEffetti.getEffetti().size()));
			parametriCreazioneAreaEffetti.setSpese(spesePresentazione);
		}
		parametriCreazioneAreaEffetti.setSpeseDistinta(ObjectUtils.defaultIfNull(
				rapportoBancarioAzienda.getSpeseDistinta(), BigDecimal.ZERO));
		parametriCreazioneAreaEffettiForm.setFormObject(parametriCreazioneAreaEffetti);
		treeTableModel = new GenerazioneDataValutaEffettiBancaTreeTableModel(areaEffetti);
		treeTableModel.createRow(RaggruppamentoEffetti.DATA_SCADENZA);
		numeroEffetti = areaEffetti.getEffetti().size();
		treeTable = new TreeTable(treeTableModel);
		treeTable.setFocusCellStyle(new FocusCellStyle());
		treeTable.expandAll();
		treeTable.setMultiColumnSortable(false);
		treeTable.sortColumn(0);
		treeTable.getColumnModel().addColumnModelListener(new ColumnModelListener());
		treeTable.setRowHeight(20);
	}

	/**
	 * Aggiunge il guarded al form model.
	 *
	 * @param guarded
	 *            guarded
	 */
	public void addFormGuard(Guarded guarded) {
		new PanjeaFormGuard(parametriCreazioneAreaEffettiForm.getFormModel(), guarded);
	}

	@Override
	protected JComponent createControl() {
		JPanel mainPanel = getComponentFactory().createPanel(new BorderLayout());
		JPanel pannelloSuperiore = new JPanel(new VerticalLayout(10));
		pannelloSuperiore.add(parametriCreazioneAreaEffettiForm.getControl());
		pannelloSuperiore.add(parametriRicercaSommaForm.getControl());

		parametriRicercaSommaForm.getAssegnaDataCommand().addCommandInterceptor(new AssegnaDataCommandInterceptor());

		parametriRicercaSommaForm.getValueModel("combinazioneSelezionataIndex").addValueChangeListener(
				new CombinazioneSelezionataIndexPropertyChange());
		mainPanel.add(pannelloSuperiore, BorderLayout.NORTH);
		mainPanel.add(getComponentFactory().createScrollPane(treeTable), BorderLayout.CENTER);
		mainPanel.add(getNumeroEffettiControl(), BorderLayout.SOUTH);
		parametriCreazioneAreaEffettiForm.getValueModel("raggruppaBanche").addValueChangeListener(
				new RaggruppaBancheValueChangeListener());
		return mainPanel;
	}

	/**
	 *
	 * @return effetti con la nuova data valuta.
	 */
	public Set<Effetto> getEffetti() {
		return areaEffetti.getEffetti();
	}

	/**
	 *
	 * @return componente contenente il numero effetti dell'area
	 */
	public JComponent getNumeroEffettiControl() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());

		JTextField textField = getComponentFactory().createTextField();
		// creo un textfield per mantenere la coerenza con gli altri campi, lo
		// rendo non editabile
		// dato che riporta solo l'informazione di quanti effetti � composta la
		// distinta
		textField.setText(numeroEffetti + "");
		textField.setEditable(false);
		textField.setColumns(4);

		String labelNrEffetti = getMessage(LABEL_KEY_NUMERO_EFFETTI);
		JLabel label = getComponentFactory().createLabelFor(labelNrEffetti, textField);

		JPanel panelNumeroEffetti = getComponentFactory().createPanel();
		panelNumeroEffetti.add(label);
		panelNumeroEffetti.add(textField);

		panel.add(panelNumeroEffetti, BorderLayout.LINE_END);
		return panel;
	}

	/**
	 *
	 * @return parametri per la creazione della distinta
	 */
	public ParametriCreazioneAreaEffetti getParametriCreazioneAreaEffetti() {
		return (ParametriCreazioneAreaEffetti) parametriCreazioneAreaEffettiForm.getFormObject();
	}

}
