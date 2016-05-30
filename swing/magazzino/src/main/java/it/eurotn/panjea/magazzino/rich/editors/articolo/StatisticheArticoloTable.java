package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.StatisticaArticolo;
import it.eurotn.rich.control.table.JideTableWidget;

public class StatisticheArticoloTable extends JideTableWidget<StatisticaArticolo> {

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	/**
	 * Costruttore.
	 *
	 * @param magazzinoDocumentoBD
	 *            bd
	 * @param articolo
	 *            articolo
	 */
	public StatisticheArticoloTable(final IMagazzinoDocumentoBD magazzinoDocumentoBD, final Articolo articolo) {
		super("statisticheArticoloTable", new StatisticheArticoloTableModel(articolo.getNumeroDecimaliQta()));
		setTableType(TableType.HIERARCHICAL);

		this.magazzinoDocumentoBD = magazzinoDocumentoBD;

		setHierarchicalTableComponentFactory(new StatisticaArticoloHierarchilalComponent(magazzinoDocumentoBD, articolo));
	}

}
