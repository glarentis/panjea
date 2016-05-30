package it.eurotn.panjea.magazzino.rich.editors.manutenzionelistino;

import it.eurotn.panjea.anagrafica.domain.AbstractLayout;
import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.RigaManutenzioneListino;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.ITable;
import it.eurotn.rich.control.table.JecGroupTable;
import it.eurotn.rich.control.table.JecGroupableTableModel;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.layout.DefaultTableLayoutManager;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.utils.ByteArrayOutputStream;

import com.jidesoft.grid.SortableTreeTableModel;
import com.jidesoft.grid.TableColumnChooser;
import com.jidesoft.grid.TableModelWrapperUtils;

public class ManutenzioneListinoLayoutManager extends DefaultTableLayoutManager {

	private ETipoListino tipoListino;

	private static final String DEFAULT_LAYOUT = "Standard";
	private static final String SCAGLIONI_LAYOUT = "Listino a scaglioni";

	private TableLayout tableLayoutRef;

	/**
	 * Costruttore.
	 * 
	 * @param jideTableWidget
	 *            table widget
	 * @param tipoListino
	 *            tipo listino
	 */
	public ManutenzioneListinoLayoutManager(final JideTableWidget<?> jideTableWidget, final ETipoListino tipoListino) {
		super(jideTableWidget);
		this.tipoListino = tipoListino;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void applica(TableLayout layout) {

		// se non ho un layout di default vado a crearlo
		if (getLayouts().isEmpty()) {
			createDefaultLayout();
		}
		super.applica(tableLayoutRef);

		((JecGroupTable<RigaManutenzioneListino>) getTableWidget().getTable()).expandAll();
	}

	/**
	 * Crea il layout di default in base ai parametri di ricerca.
	 * 
	 * @return layout creato
	 */
	private AbstractLayout createDefaultLayout() {

		TableColumnChooser.resetColumnsToDefault(getTableWidget().getTable());

		tableLayoutRef = new TableLayout();
		tableLayoutRef.setChiave("risultatiRicercaManutenzioneListinoTablePageWidget");
		tableLayoutRef.setGlobal(true);
		tableLayoutRef.setUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());

		TableColumnChooser.resetColumnsToDefault(getTableWidget().getTable());
		JecGroupableTableModel actualTableModel;
		switch (tipoListino) {
		case SCAGLIONE:
			tableLayoutRef.setName(SCAGLIONI_LAYOUT);
			actualTableModel = (JecGroupableTableModel) TableModelWrapperUtils.getActualTableModel(getTableWidget()
					.getTable().getModel(), JecGroupableTableModel.class);
			actualTableModel.addGroupColumn(0);

			@SuppressWarnings("rawtypes")
			SortableTreeTableModel sortableTableModel = (SortableTreeTableModel) TableModelWrapperUtils
					.getActualTableModel(getTableWidget().getTable().getModel(), SortableTreeTableModel.class);
			sortableTableModel.sortColumn(0);
			break;

		default:
			tableLayoutRef.setName(DEFAULT_LAYOUT);
			TableColumnChooser.hideColumns(getTableWidget().getTable(), new int[] { 2 });
			actualTableModel = (JecGroupableTableModel) TableModelWrapperUtils.getActualTableModel(getTableWidget()
					.getTable().getModel(), JecGroupableTableModel.class);
			actualTableModel.addGroupColumn(1);

			break;
		}

		TableLayout layout = tableLayoutRef;

		OutputStream dataLayout = new ByteArrayOutputStream();

		((ITable<?>) this.tableWidget.getTable()).saveLayout(dataLayout);

		AbstractLayout al = null;
		if (layout != null) {
			layout.setData(dataLayout.toString());
			al = getTableLayoutCache().salva(layout);
		}

		return layout;

	}

	/**
	 * @return nome del layout in base al tipo listino
	 */
	private String getLayoutName() {

		switch (tipoListino) {
		case SCAGLIONE:
			return SCAGLIONI_LAYOUT;
		default:
			return DEFAULT_LAYOUT;
		}
	}

	@Override
	public List<TableLayout> getLayouts() {

		List<TableLayout> layoutsFiltered = new ArrayList<TableLayout>();
		List<TableLayout> layouts = super.getLayouts();

		for (TableLayout tableLayout : layouts) {
			if (tableLayout.getName() != null && tableLayout.getName().equals(getLayoutName())) {
				layoutsFiltered.add(tableLayout);
				tableLayoutRef = tableLayout;
				break;
			}
		}

		return layoutsFiltered;
	}

	@Override
	public boolean isReadOnly() {
		// sovrascrivo il metodo perchè l'utente non può inserire/modificare/cancellare nessun layout
		return Boolean.TRUE;
	}

	@Override
	public AbstractLayout salva(TableLayout layout) {
		return layout;
	}

	/**
	 * @param tipoListino
	 *            the tipoListino to set
	 */
	public void setTipoListino(ETipoListino tipoListino) {
		this.tipoListino = tipoListino;
	}
}
