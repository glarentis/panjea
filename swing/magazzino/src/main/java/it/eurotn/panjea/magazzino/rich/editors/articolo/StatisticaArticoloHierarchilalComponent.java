package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.StatisticaArticolo;

import java.awt.Component;

import javax.swing.JPanel;

import com.jidesoft.grid.HierarchicalTable;
import com.jidesoft.grid.HierarchicalTableComponentFactory;

public class StatisticaArticoloHierarchilalComponent implements HierarchicalTableComponentFactory {

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;
	private Articolo articolo;

	/**
	 * Costruttore.
	 *
	 * @param magazzinoDocumentoBD
	 *            bd
	 * @param articolo
	 *            articolo
	 */
	public StatisticaArticoloHierarchilalComponent(final IMagazzinoDocumentoBD magazzinoDocumentoBD, Articolo articolo) {
		super();
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
		this.articolo = articolo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Component createChildComponent(HierarchicalTable arg0, Object values, int arg2) {

		StatisticaArticolo statisticaArticolo = (StatisticaArticolo) values;

		JPanel deposito = new StatisticaDepositoComponent(statisticaArticolo, articolo.getNumeroDecimaliPrezzo(),
				articolo.getNumeroDecimaliQta(), magazzinoDocumentoBD);

		return deposito;
	}

	@Override
	public void destroyChildComponent(HierarchicalTable arg0, Component component, int arg2) {

		JPanel tableChildComponent = (JPanel) component;

		tableChildComponent.removeAll();
		tableChildComponent = null;
	}
}
