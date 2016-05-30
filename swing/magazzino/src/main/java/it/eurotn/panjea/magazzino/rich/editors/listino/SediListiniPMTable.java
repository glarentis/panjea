package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.rich.control.table.JideTableWidget;

public class SediListiniPMTable extends JideTableWidget<SedeListiniPM> {

	/**
	 * Costruttore.
	 * 
	 */
	public SediListiniPMTable() {
		super("SediListiniPMTable", new SediListiniPMTableModel());
		setTableType(TableType.GROUP);
		setAggregatedColumns(new String[] { "sedeMagazzino.sedeEntita.entita" });
	}

}
