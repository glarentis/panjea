package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

public class TabellaClienti extends TabellaDimensione {
	private static final long serialVersionUID = 4080060892975531397L;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public TabellaClienti() {
		super("Sedi", "vista", "tipo_anagrafica='C'", "clienti");
		addColumn("codice", int.class, "Cod Cliente");
		addColumn("denominazione", String.class, "Cliente");
		addColumn("descrizione_sede", String.class, "Sede Cliente");
		addColumn("nazione", String.class, "Nazione Cliente");
		addColumn("livelloAmministrativo1", String.class, "Regione Cliente");
		addColumn("livelloAmministrativo2", String.class, "Provincia Cliente");
		addColumn("livelloAmministrativo3", String.class, "Comune Cliente");
		addColumn("localita", String.class, "Località Cliente");
		addColumn("codiceZona", String.class, "Codice Zona Cliente");
		addColumn("descrizioneZona", String.class, "Desc. Zona Cleinte");
	}
}
