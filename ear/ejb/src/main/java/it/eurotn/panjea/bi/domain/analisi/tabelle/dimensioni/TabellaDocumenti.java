package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

public class TabellaDocumenti extends TabellaDimensione {
	private static final long serialVersionUID = 3174490469918278708L;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public TabellaDocumenti() {
		super("documenti", "docu", "", "Documenti");
		addColumn("codice", String.class, "Numero doc.");
	}

}
