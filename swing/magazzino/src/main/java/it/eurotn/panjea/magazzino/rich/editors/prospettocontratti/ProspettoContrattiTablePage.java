package it.eurotn.panjea.magazzino.rich.editors.prospettocontratti;

import it.eurotn.panjea.magazzino.rich.editors.contrattisede.ContrattiSedeTablePage;
import it.eurotn.rich.control.table.JideTableWidget.TableType;

public class ProspettoContrattiTablePage extends ContrattiSedeTablePage {

	public static final String PAGE_ID = "prospettoContrattiTablePage";

	/**
	 * Costruttore.
	 * 
	 */
	public ProspettoContrattiTablePage() {
		super(PAGE_ID, new ProspettoContrattiTableModel());
		getTable().setTableType(TableType.AGGREGATE);
		getTable().setAggregatedColumns(new String[] { "entita" });
		getTable().setPropertyCommandExecutor(new OpenEditorContrattoCommand());
	}

}
