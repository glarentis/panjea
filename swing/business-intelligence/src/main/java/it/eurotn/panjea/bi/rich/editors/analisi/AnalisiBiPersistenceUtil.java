package it.eurotn.panjea.bi.rich.editors.analisi;

import it.eurotn.panjea.bi.domain.analisi.AnalisiAnnoFiltro;
import it.eurotn.panjea.bi.domain.analisi.AnalisiFiltro;
import it.eurotn.panjea.bi.domain.analisi.AnalisiValueSelected;
import it.eurotn.panjea.rich.filters.PeriodoBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jidesoft.filter.BetweenFilter;
import com.jidesoft.filter.Filter;
import com.jidesoft.filter.FilterFactoryManager;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.jidesoft.pivot.PivotTablePane;

/**
 * Gestice la persistenza dei vari campi dell'analisi.
 *
 * @author giangi
 *
 */
public final class AnalisiBiPersistenceUtil {

	/**
	 *
	 * @param dataModel
	 *            data model sul quale applicare i filtri
	 * @param filtri
	 *            filtri da applicare
	 */
	public static void applicaFiltri(IPivotDataModel dataModel, Map<String, AnalisiValueSelected> filtri) {
		// azzero i filtri
		for (PivotField field : dataModel.getFields()) {
			field.setFilter(null);
			field.setSelectedPossibleValues(null);
		}

		for (Entry<String, AnalisiValueSelected> entry : filtri.entrySet()) {
			PivotField field = dataModel.getField(entry.getValue().getNomeCampo());
			if (entry.getValue() instanceof AnalisiFiltro) {
				AnalisiFiltro analisiFiltro = (AnalisiFiltro) entry.getValue();
				Filter<?> filter = FilterFactoryManager.getDefaultInstance().createFilter(
						analisiFiltro.getFilterFactoryName(), field.getType(), analisiFiltro.getParameter());
				filter.setName(analisiFiltro.getNome());
				field.setFilter(filter);
			} else {
				// Salvo tutti i possibili valori come Stringa. Se però sono interi
				// devo ritrasformarli nel tipo del field
				if (field.getType() == short.class) {
					Short[] selectedValue = new Short[entry.getValue().getParameter().length];
					for (int i = 0; i < selectedValue.length; i++) {
						selectedValue[i] = new Short(entry.getValue().getParameter()[i].toString());
					}
					field.setSelectedPossibleValues(selectedValue);
				} else if (field.getType() == int.class) {
					Integer[] selectedValue = new Integer[entry.getValue().getParameter().length];
					for (int i = 0; i < selectedValue.length; i++) {
						selectedValue[i] = new Integer(entry.getValue().getParameter()[i].toString());
					}
					field.setSelectedPossibleValues(selectedValue);
				} else {
					field.setSelectedPossibleValues(entry.getValue().getParameter());
				}
			}
		}
	}

	/**
	 * Esegue il resize della tabelle delle colonne e dei dati utilizzando come larghezza delle celle la larghezza
	 * massima tra quella delle colonne e delle righe. Se viene richiesto anche alla tabella delle righe verrà applicato
	 * il resize ( indipendente dalla dimensione di colonne e dati ).
	 *
	 * @param pivotTablePane
	 *            pivot table pane
	 * @param resizeRow
	 *            <code>true</code> per eseguire il resize delle righe
	 */
	public static void autoResizeAllColumns(PivotTablePane pivotTablePane, boolean resizeRow) {

		if (resizeRow) {
			TableUtils.autoResizeAllColumns(pivotTablePane.getRowHeaderTable());
		}

		int maxDataWidth = 0;
		int maxColumnWidth = 0;

		int[] dataWidth = TableUtils.autoResizeAllColumns(pivotTablePane.getDataTable());
		int[] columnWidth = TableUtils.autoResizeAllColumns(pivotTablePane.getColumnHeaderTable());

		// trovo il valore massimo per le celle delle colonne e dati
		if (dataWidth != null && dataWidth.length > 0) {
			Arrays.sort(dataWidth);
			maxDataWidth = dataWidth[dataWidth.length - 1];
		}
		if (columnWidth != null && columnWidth.length > 0) {
			Arrays.sort(columnWidth);
			maxColumnWidth = columnWidth[columnWidth.length - 1];
		}

		// dimensione massima delle celle
		int maxCellWidth = Math.max(maxDataWidth, maxColumnWidth);

		// ridimensiono le colonne
		for (int i = 0; i < columnWidth.length; i++) {
			columnWidth[i] = maxCellWidth;
		}
		TableUtils.autoResizeAllColumns(pivotTablePane.getColumnHeaderTable(), columnWidth, false);

		// ridimensiono i dati
		for (int i = 0; i < dataWidth.length; i++) {
			dataWidth[i] = maxCellWidth;
		}
		TableUtils.autoResizeAllColumns(pivotTablePane.getDataTable(), dataWidth, false);
	}

	/**
	 *
	 * @param pivotDataModel
	 *            dataModel con i filtri da salvare
	 * @return mappa con i filtri per ogni campo filtrati.
	 */
	public static Map<String, AnalisiValueSelected> creaFiltri(IPivotDataModel pivotDataModel) {
		List<PivotField> fieldToFilter = new ArrayList<PivotField>();
		if (pivotDataModel.getColumnFields() != null) {
			fieldToFilter.addAll(Arrays.asList(pivotDataModel.getColumnFields()));
		}
		if (pivotDataModel.getRowFields() != null) {
			fieldToFilter.addAll(Arrays.asList(pivotDataModel.getRowFields()));
		}
		if (pivotDataModel.getFilterFields() != null) {
			fieldToFilter.addAll(Arrays.asList(pivotDataModel.getFilterFields()));
		}

		Map<String, AnalisiValueSelected> filtri = new HashMap<String, AnalisiValueSelected>();

		for (PivotField pivotField : fieldToFilter) {
			AnalisiValueSelected valueFilter = null;
			if (pivotField.getSelectedPossibleValues() != null) {
				valueFilter = creaFiltroValueSelected(pivotField);
			} else if (pivotField.getFilter() instanceof PeriodoBuilder) {
				valueFilter = creaFiltroCustom(pivotField);
			}
			if (valueFilter != null) {
				valueFilter.setNomeCampo(pivotField.getName());
				filtri.put(pivotField.getName(), valueFilter);
			}
		}
		return filtri;
	}

	private static AnalisiValueSelected creaFiltroCustom(PivotField pivotField) {
		AnalisiFiltro filtro = new AnalisiFiltro();
		if (pivotField.getFilter() instanceof BetweenFilter<?>) {
			BetweenFilter<?> betweenFilter = (BetweenFilter<?>) pivotField.getFilter();
			filtro.setParametro1(betweenFilter.getValue1().toString());
			filtro.setParametro2(betweenFilter.getValue2().toString());
		} else {
			if (pivotField.getFilter().getName().split("\\|").length == 2) {
				filtro.setParametro1(pivotField.getFilter().getName().split("\\|")[1].trim());
			}
		}
		filtro.setNome(pivotField.getFilter().getName());
		filtro.setTipoPeriodo(((PeriodoBuilder) pivotField.getFilter()).getPeriodo().getTipoPeriodo());
		filtro.setFilterFactoryName(pivotField.getFilter().getFilterFactoryName());
		return filtro;
	}

	private static AnalisiValueSelected creaFiltroValueSelected(PivotField pivotField) {
		AnalisiValueSelected valueFilter = new AnalisiValueSelected();
		if (pivotField.getName().equals("data_Anno")) {
			valueFilter = new AnalisiAnnoFiltro();
		}
		valueFilter.setParameter(pivotField.getSelectedPossibleValues());
		return valueFilter;
	}

	/**
	 * Classe di Util. Il costruttore deve essere privato
	 */
	private AnalisiBiPersistenceUtil() {
	}

}
