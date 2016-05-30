package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti.carrello;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.tesoreria.domain.AreaAccredito;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.service.exception.DataRichiestaDopoIncassoException;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.dialog.InputApplicationDialog;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class AccreditiTableComponent extends AbstractCarrelloTableComponent {

	protected class AccreditiTableModel extends DefaultBeanTableModel<SituazioneEffetto> {

		private static final long serialVersionUID = -8952665682968202446L;

		private ConverterContext numberPrezzoContext;

		{
			numberPrezzoContext = new NumberWithDecimalConverterContext();
			numberPrezzoContext.setUserObject(new Integer(2));
		}

		/**
		 * Costruttore.
		 */
		public AccreditiTableModel() {
			super("accreditiTableModel", new String[] { "effetto.dataValuta", "rapportoBancario",
					"areaEffetto.documento", "entita", "effetto.statoEffetto", "dataScadenza",
					"effetto.importo.importoInValutaAzienda" }, SituazioneEffetto.class);
		}

		@Override
		public ConverterContext getConverterContextAt(int row, int column) {
			switch (column) {
			case 6:
				return numberPrezzoContext;
			default:
				return null;
			}
		}
	}

	protected JideTableWidget<SituazioneEffetto> tableWidget;

	@Override
	public void addSituazioneEffetti(List<SituazioneEffetto> situazioneEffetti, ITesoreriaBD tesoreriaBD) {
		for (SituazioneEffetto situazioneEffetto : situazioneEffetti) {
			tableWidget.replaceOrAddRowObject(situazioneEffetto, situazioneEffetto, null);
		}
	}

	@Override
	protected JComponent createControl() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		tableWidget = new JideTableWidget<SituazioneEffetto>("accreditiTable", new AccreditiTableModel());
		tableWidget.setAggregatedColumns(new String[] { "rapportoBancario", "effetto.dataValuta" });
		rootPanel.add(tableWidget.getComponent(), BorderLayout.CENTER);

		return rootPanel;
	}

	@Override
	boolean generaAreeTesoreria(ITesoreriaBD tesoreriaBD) {

		if (tableWidget.getRows().isEmpty()) {
			return false;
		}

		List<Effetto> effetti = new ArrayList<Effetto>();

		for (SituazioneEffetto situazioneEffetto : tableWidget.getRows()) {
			effetti.add(situazioneEffetto.getEffetto());
		}

		List<AreaAccredito> areeAccredito = new ArrayList<AreaAccredito>();
		Date dataScritturaPosticipata = null;
		try {
			// creo di default gli accrediti, se sono in caso di scrittura posticipata verrà chiesta all'utente la data
			// da impostare al documento
			areeAccredito = tesoreriaBD.creaAreeAccredito(effetti, null);
		} catch (DataRichiestaDopoIncassoException e) {
			// apro il dialogo con la richiesta della data per scrittura posticipata
			InputApplicationDialog inputDialog = new InputApplicationDialog(new Documento(), "dataDocumento");
			inputDialog.setInputLabelMessage("dataDocumento");
			inputDialog.setTitle("Imposta data");
			inputDialog.showDialog();

			dataScritturaPosticipata = (Date) inputDialog.getInputValue();
			// se annullo il valore sarà null quindi non faccio altro, la list sarà vuota
			if (dataScritturaPosticipata != null) {
				try {
					areeAccredito = tesoreriaBD.creaAreeAccredito(effetti, dataScritturaPosticipata);
				} catch (DataRichiestaDopoIncassoException e1) {
					// di qui non ci passa più
					logger.error("-->errore. Non dovrebbe più passare!!!! Controlla", e1);
				}
			} else {
				return false;
			}
		}
		if (!areeAccredito.isEmpty()) {
			List<AreaTesoreria> aree = new ArrayList<AreaTesoreria>();
			for (AreaAccredito areaAccredito : areeAccredito) {
				aree.add(areaAccredito);
			}

			LifecycleApplicationEvent event = new OpenEditorEvent(
					ParametriRicercaAreeTesoreria.creaParametriRicercaAreeTesoreria(aree));
			Application.instance().getApplicationContext().publishEvent(event);
		}
		return true;
	}

	@Override
	List<SituazioneEffetto> getSituazioniEffetti() {
		return tableWidget.getRows();
	}

	@Override
	void removeAll() {
		tableWidget.setRows(new ArrayList<SituazioneEffetto>());
	}

	@Override
	void removeSelected() {
		List<SituazioneEffetto> listSelected = tableWidget.getSelectedObjects();
		for (SituazioneEffetto situazioneEffetto : listSelected) {
			tableWidget.removeRowObject(situazioneEffetto);
		}
	}

}
