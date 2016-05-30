package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

public class TabellaVettore extends TabellaDimensione {
	private static final long serialVersionUID = 5074141032262710881L;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public TabellaVettore() {
		super("Sedi", "vista", "tipo_anagrafica='V'", "vettori");
		addColumn("codice", int.class, "Cod. vett.");
		addColumn("denominazione", String.class, "Vettore");
		addColumn("descrizione_sede", String.class, "Sede vett.");
		addColumn("nazione", String.class, "Naz. vett.");
		addColumn("livelloAmministrativo1", String.class, "Regione vett.");
		addColumn("livelloAmministrativo2", String.class, "Provincia  vett.");
		addColumn("livelloAmministrativo3", String.class, "Comune  vett.");
		addColumn("localita", String.class, "Localita  vett.");
		addColumn("codiceZona", String.class, "Codice Zona vett.");
		addColumn("descrizioneZona", String.class, "Desc. Zona vett.");
	}
}
