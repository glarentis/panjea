package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

public class TabellaArticoli extends TabellaDimensione {
	private static final long serialVersionUID = 4903647028899097616L;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public TabellaArticoli() {
		super("articoli", "dw", null, "articoli");
		addColumn("tipoArticolo", Integer.class, "Tipo art.");
		addColumn("codice", String.class, "Articolo", false);
		addColumn("descrizioneLinguaAziendale", String.class, "Desc. art.");
		addColumn("descrizioneCategoria", String.class, "Desc. categoria");
		addColumn("codiceCategoria", String.class, "Categoria");
		addColumn("categoria_id", Integer.class, "Id Categoria");
	}
}
