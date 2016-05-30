package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.ColonnaFunzione;
import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

public class TabellaTipiDocumento extends TabellaDimensione {
	private static final long serialVersionUID = 3174490469918278708L;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public TabellaTipiDocumento() {
		super("tipi_documento", "docu", "", "Tipi doc");
		addColumn("codice", String.class, "Cod. tipo doc.");
		addColumn("descrizione", String.class, "Desc. tipo doc.");
		ColonnaFunzione colonnaClasse = new ColonnaFunzione(
				"replace($aliasTabella$.classeTipoDocumento,'it.eurotn.panjea.anagrafica.classedocumento.impl.Classe',\"\")",
				String.class, this, "Classe");
		colonne.put(colonnaClasse.getKey(), colonnaClasse);
	}

}
