/**
 * 
 */
package it.eurotn.panjea.bi.rich.editors.analisi.detail;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.sql.detail.RigaDettaglioAnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jidesoft.pivot.HeaderTableModel;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotField;

/**
 * @author fattazzo
 * 
 */
public class AnalisiBiDetailTablePage extends AbstractTablePageEditor<RigaDettaglioAnalisiBi> {

	public static final String PAGE_ID = "analisiBiDetailTablePage";
	private static final int SIZEPAGE = 10000;

	private IBusinessIntelligenceBD businessIntelligenceBD;

	private IPivotDataModel pivotDataModel;
	private int columnSelected;
	private int rowSelected;
	// Indica se viene cambiata la selezione. Se sono nascosto non aggiorno,
	// Però appena diventa visibile se la selezione è cambiata devo aggiornare.
	private boolean selectionChanged;

	private AnalisiBi analisiBi;

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 * @param tableModel
	 */
	protected AnalisiBiDetailTablePage() {
		super("analisiBiDetailTablePage", new AnalisiBiDetailTableModel());
		getTable().getOverlayTable().setEnableCancelAction(true);
	}

	@Override
	public Collection<RigaDettaglioAnalisiBi> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public void processTableData(Collection<RigaDettaglioAnalisiBi> results) {
		if (!isVisible()) {
			return;
		}
		if (!selectionChanged) {
			return;
		}

		selectionChanged = false;

		// svuoto la tabella per averla pulita nel caso in cui eseguo nuovo command
		setRows(new ArrayList<RigaDettaglioAnalisiBi>());

		if (rowSelected == -1 || columnSelected == -1) {
			return;
		}

		// se ho selezionato una cella allora rieseguo la ricerca
		getTable().getOverlayTable().startSearch();
		boolean fine = false;
		int page = 1;
		getTable().getOverlayTable().setCancel(false);
		List<RigaDettaglioAnalisiBi> dettaglio = new ArrayList<RigaDettaglioAnalisiBi>(10000);

		// Recupero i fields per i filtri
		Map<Colonna, Object[]> fields = new HashMap<Colonna, Object[]>();
		HeaderTableModel headerColumn = pivotDataModel.getColumnHeaderTableModel();
		for (int i = 0; i < pivotDataModel.getColumnFields().length; i++) {
			boolean ricercaValore = i > 0 ? headerColumn.isExpanded(i - 1, columnSelected) : true;
			if (ricercaValore) {
				Colonna colonna = analisiBi.getAnalisiLayout().getFields()
						.get(pivotDataModel.getColumnFields()[i].getName()).getColonna();
				fields.put(colonna, new Object[] { headerColumn.getValueAt(i, columnSelected) });
			} else {
				// Il dettaglio deve venire filtrato se sul field, anche se non è visibile, ci sono dei filtri.
				// Inserisco eventuali filtri sul field
				Colonna colonna = analisiBi.getAnalisiLayout().getFields()
						.get(pivotDataModel.getColumnFields()[i].getName()).getColonna();
				fields.put(colonna, pivotDataModel.getColumnFields()[i].getSelectedPossibleValues());
			}
		}

		HeaderTableModel headerRow = pivotDataModel.getRowHeaderTableModel();
		for (int i = 0; i < headerRow.getColumnCount(); i++) {
			// Se la colonna precedente è espanda allora elaboro il valore del campo, altrimenti
			// passo il valore di eventuali filtri.
			// Il primo campo lo considero sempre
			boolean ricercaValore = i > 0 ? headerRow.isExpanded(rowSelected, i - 1) : true;
			if (ricercaValore) {
				Colonna colonna = analisiBi.getAnalisiLayout().getFields()
						.get(pivotDataModel.getRowFields()[i].getName()).getColonna();
				fields.put(colonna, new Object[] { headerRow.getValueAt(rowSelected, i) });
			} else {
				// Il dettaglio deve venire filtrato se sul field, anche se non è visibile, ci sono dei filtri.
				// Inserisco eventuali filtri sul field
				Colonna colonna = analisiBi.getAnalisiLayout().getFields()
						.get(pivotDataModel.getRowFields()[i].getName()).getColonna();
				fields.put(colonna, pivotDataModel.getRowFields()[i].getSelectedPossibleValues());
			}
		}

		// se la misura è una sola prendo quella altrimenti vado a vedere su quale misura si trova la cella selezionata
		Colonna colonnaMisura = null;
		if (analisiBi.getAnalisiLayout().getMisure().size() == 1) {
			colonnaMisura = analisiBi.getAnalisiLayout().getMisure().get(0).getColonna();
		} else {
			PivotField misuraField = pivotDataModel.getColumnHeaderTableModel().getFieldAt(
					pivotDataModel.getColumnHeaderTableModel().getRowCount() - 1, columnSelected);
			colonnaMisura = analisiBi.getAnalisiLayout().getFields().get(misuraField.getName()).getColonna();
		}

		// AnalisiBIDomain dataWarehouseModel = ((AnalisiBIDomain) pivotDataModel.getDataSource()).getAnalisiBIDomain();
		// dataWarehouseModel.setDetailFiltered(fields);

		try {
			do {
				List<RigaDettaglioAnalisiBi> dettaglioPagina = businessIntelligenceBD.drillThrough(analisiBi, fields,
						colonnaMisura, page, SIZEPAGE);
				fine = dettaglioPagina.size() < SIZEPAGE;
				dettaglio.addAll(dettaglioPagina);
				dettaglioPagina = new ArrayList<RigaDettaglioAnalisiBi>();
				page++;
				// se chiude la pagina la getTable va a null.
			} while (getTable() != null && !getTable().getOverlayTable().isCancel() && !fine);
			if (getTable() != null) {
				setRows(dettaglio);
				dettaglio = null;
			}
		} finally {
			if (getTable() != null) {
				getTable().getOverlayTable().stopSearch();
			}
		}
	}

	@Override
	public Collection<RigaDettaglioAnalisiBi> refreshTableData() {
		return Collections.emptyList();
	}

	/**
	 * @param analisiBi
	 *            the analisiBi to set
	 */
	public void setAnalisiBi(AnalisiBi analisiBi) {
		this.analisiBi = analisiBi;
	}

	/**
	 * @param businessIntelligenceBD
	 *            The businessIntelligenceBD to set.
	 */
	public void setBusinessIntelligenceBD(IBusinessIntelligenceBD businessIntelligenceBD) {
		this.businessIntelligenceBD = businessIntelligenceBD;
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof IPivotDataModel) {
			pivotDataModel = (IPivotDataModel) object;
		}
	}

	/**
	 * Setta la posizione della selezione nella pivot table.
	 * 
	 * @param paramRowSelected
	 *            riga selezionata
	 * @param paramColumnSelected
	 *            colonna selezionata
	 */
	public void setSelection(int paramRowSelected, int paramColumnSelected) {
		if (paramRowSelected != rowSelected || paramColumnSelected != columnSelected) {
			selectionChanged = true;
		}
		rowSelected = paramRowSelected;
		columnSelected = paramColumnSelected;
		refreshData();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		refreshData();
	}

}
