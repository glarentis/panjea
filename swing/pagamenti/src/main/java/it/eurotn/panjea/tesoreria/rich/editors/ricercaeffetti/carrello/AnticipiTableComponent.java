package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti.carrello;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.tesoreria.domain.AreaAnticipo;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.forms.ricercaeffetti.ParametriCreazioneAnticipiForm;
import it.eurotn.panjea.tesoreria.util.BancaDataValutaDistintaSituazioneEffettoComparator;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.panjea.tesoreria.util.SituazioneRigaAnticipo;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GroupingList;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.JideTable;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.validation.ValidationObject;
import com.jidesoft.validation.ValidationResult;
import com.jidesoft.validation.Validator;

public class AnticipiTableComponent extends AbstractCarrelloTableComponent {

	private class AnticipiTableModel extends DefaultBeanTableModel<SituazioneRigaAnticipo> {

		private static final long serialVersionUID = -8133413845512443280L;

		private ConverterContext numberPrezzoContext;
		private EditorContext numberPrezzoEditorContext;

		{
			numberPrezzoContext = new NumberWithDecimalConverterContext();
			numberPrezzoContext.setUserObject(new Integer(2));

			numberPrezzoEditorContext = new EditorContext("DecimalNumberEditorContext", 2);
		}

		/**
		 * Costruttore.
		 */
		public AnticipiTableModel() {
			super("anticipiTable", new String[] { "rigaAnticipo.areaAnticipo.documento.rapportoBancarioAzienda",
					"rigaAnticipo.dataValuta", "rigaAnticipo.areaEffetti.documento", "rigaAnticipo.importoAnticipato",
					"importoGiaAnticipato" }, SituazioneRigaAnticipo.class);
		}

		@Override
		public ConverterContext getConverterContextAt(int row, int column) {
			switch (column) {
			case 3:
			case 4:
				return numberPrezzoContext;
			default:
				return null;
			}
		}

		@Override
		public EditorContext getEditorContextAt(int row, int column) {
			switch (column) {
			case 3:
				return numberPrezzoEditorContext;
			default:
				return null;
			}
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			switch (column) {
			case 3:
				return true;
			default:
				return false;
			}
		}
	}

	private JideTableWidget<SituazioneRigaAnticipo> tableWidget;

	private ParametriCreazioneAnticipiForm parametriCreazioneAnticipiForm;

	@Override
	public void addSituazioneEffetti(List<SituazioneEffetto> situazioneEffetti, ITesoreriaBD tesoreriaBD) {

		EventList<SituazioneEffetto> eventList = new BasicEventList<SituazioneEffetto>();
		eventList.addAll(situazioneEffetti);

		GroupingList<SituazioneEffetto> groupingList = new GroupingList<SituazioneEffetto>(eventList,
				new BancaDataValutaDistintaSituazioneEffettoComparator());

		for (List<SituazioneEffetto> list : groupingList) {
			BigDecimal importo = tesoreriaBD.caricaImportoAnticipato(list.get(0).getAreaEffetto(), list.get(0)
					.getRapportoBancario(), list.get(0).getEffetto().getDataValuta());

			// faccio una nuova lista perchè list è una eventlist quindi non serializzabile
			List<SituazioneEffetto> effetti = new ArrayList<SituazioneEffetto>();
			effetti.addAll(list);
			SituazioneRigaAnticipo situazioneRigaAnticipo = new SituazioneRigaAnticipo(effetti, importo);
			tableWidget.replaceOrAddRowObject(situazioneRigaAnticipo, situazioneRigaAnticipo, null);
		}
	}

	@Override
	protected JComponent createControl() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		tableWidget = new JideTableWidget<SituazioneRigaAnticipo>("anticipiTable", new AnticipiTableModel());
		tableWidget.setAggregatedColumns(new String[] { "rapportoBancario" });
		((JideTable) tableWidget.getTable()).addValidator(new Validator() {

			private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

			@Override
			public ValidationResult validating(ValidationObject validationobject) {
				ValidationResult result = ValidationResult.OK;
				if (validationobject.getNewValue() instanceof BigDecimal) {
					BigDecimal importoAntCarrello = (BigDecimal) validationobject.getNewValue();
					SituazioneRigaAnticipo situazioneRigaAnticipo = tableWidget.getSelectedObject();

					BigDecimal totDoc = situazioneRigaAnticipo.getRigaAnticipo().getAreaAnticipo().getDocumento()
							.getTotale().getImportoInValutaAzienda();
					if (totDoc.subtract(importoAntCarrello).subtract(situazioneRigaAnticipo.getImportoGiaAnticipato())
							.compareTo(BigDecimal.ZERO) < 0) {
						result = new ValidationResult(1, false, ValidationResult.FAIL_BEHAVIOR_PERSIST,
								"l'importo anticipo non è valido. Importo massimo: "
										+ decimalFormat.format(totDoc.subtract(situazioneRigaAnticipo
												.getImportoGiaAnticipato())));
					}
				}
				return result;
			}
		});
		rootPanel.add(tableWidget.getComponent(), BorderLayout.CENTER);

		parametriCreazioneAnticipiForm = new ParametriCreazioneAnticipiForm();
		rootPanel.add(parametriCreazioneAnticipiForm.getControl(), BorderLayout.NORTH);

		return rootPanel;
	}

	@Override
	boolean generaAreeTesoreria(ITesoreriaBD tesoreriaBD) {

		if (parametriCreazioneAnticipiForm.getFormModel().getHasErrors() || tableWidget.getRows().isEmpty()) {
			return false;
		}

		List<SituazioneRigaAnticipo> listRighe = new ArrayList<SituazioneRigaAnticipo>();
		listRighe.addAll(tableWidget.getRows());

		AreaAnticipo areaAnticipo = tesoreriaBD.creaAreaAnticipo(listRighe);

		LifecycleApplicationEvent event = new OpenEditorEvent(
				ParametriRicercaAreeTesoreria.creaParametriRicercaAreeTesoreria(areaAnticipo));
		Application.instance().getApplicationContext().publishEvent(event);

		return true;
	}

	@Override
	List<SituazioneEffetto> getSituazioniEffetti() {

		List<SituazioneEffetto> situazioniEffetto = new ArrayList<SituazioneEffetto>();
		List<SituazioneRigaAnticipo> righeAnticipoCarrello = tableWidget.getRows();

		for (SituazioneRigaAnticipo situazioneRigaAnticipo : righeAnticipoCarrello) {
			situazioniEffetto.addAll(situazioneRigaAnticipo.getSituazioneEffetti());
		}
		return situazioniEffetto;
	}

	@Override
	void removeAll() {
		tableWidget.setRows(new ArrayList<SituazioneRigaAnticipo>());

		ITesoreriaBD tesoreriaBD = RcpSupport.getBean("tesoreriaBD");

		boolean numeroDocumentoRichiesto = true;
		try {
			TipoDocumentoBasePartite tipoDocumentoBase = tesoreriaBD
					.caricaTipoDocumentoBase(TipoDocumentoBasePartite.TipoOperazione.ANTICIPO);

			String registro = tipoDocumentoBase.getTipoAreaPartita().getTipoDocumento().getRegistroProtocollo();
			if (registro != null && !registro.isEmpty()) {
				numeroDocumentoRichiesto = false;
			}
		} catch (TipoDocumentoBaseException e) {
			// non esiste il tipo documento base quindi lascio il numero documento come non richiesto
			numeroDocumentoRichiesto = true;
		}
		parametriCreazioneAnticipiForm.getFormModel().getValueModel("numeroDocumentoRichiesto")
				.setValue(numeroDocumentoRichiesto);
	}

	@Override
	void removeSelected() {
		List<SituazioneRigaAnticipo> listSelected = tableWidget.getSelectedObjects();

		for (SituazioneRigaAnticipo situazioneRigaAnticipo : listSelected) {
			tableWidget.removeRowObject(situazioneRigaAnticipo);
		}
	}

}
