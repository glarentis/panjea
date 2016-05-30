package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

public class TabellaCategorieCommerciale extends TabellaDimensione {

	private static final long serialVersionUID = 5216055191913893113L;

	/**
	 * Costruttore.
	 */
	public TabellaCategorieCommerciale() {
		super("CategorieCommercialiArticolo", "vista", null, "Cat. comm. art.");
		setContentTableAlias("articoli");
		addColumn("codice", String.class, "Cat. comm.");
		addColumn("tipoLink", String.class, "Tipo cat. comm.");
	}

}
