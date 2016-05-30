package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

public class TabellaFornitori extends TabellaDimensione {
	private static final long serialVersionUID = 6433820267723641733L;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public TabellaFornitori() {
		super("Sedi", "vista", "tipo_anagrafica='F'", "fornitori");
		addColumn("codice", int.class, "Cod Forn.");
		addColumn("denominazione", String.class, "Fornitore");
		addColumn("descrizione_sede", String.class, "Sede Forn.");
		addColumn("nazione", String.class, "NazioneForn.");
		addColumn("livelloAmministrativo1", String.class, "Regione Forn.");
		addColumn("livelloAmministrativo2", String.class, "Provincia Forn.");
		addColumn("livelloAmministrativo3", String.class, "Comune Forn.");
		addColumn("localita", String.class, "Località Forn.");
		addColumn("codiceZona", String.class, "Codice Zona Forn.");
		addColumn("descrizioneZona", String.class, "Desc. Zona Forn.");
	}
}
