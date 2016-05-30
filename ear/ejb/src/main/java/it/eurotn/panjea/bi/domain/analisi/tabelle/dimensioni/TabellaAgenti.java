package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

/**
 *
 *
 * @author giangi
 * @version 1.0, 08/mag/2013
 *
 */
public class TabellaAgenti extends TabellaDimensione {
	private static final long serialVersionUID = -4366654373582462345L;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public TabellaAgenti() {
		super("Sedi", "vista", "tipo_anagrafica='A'", "agenti");
		addColumn("codice", int.class, "Cod Agente");
		addColumn("denominazione", String.class, "Agente");
		addColumn("nazione", String.class, "Nazione Agente");
		addColumn("livelloAmministrativo1", String.class, "Regione Agente");
		addColumn("livelloAmministrativo2", String.class, "Provincia Agente");
		addColumn("livelloAmministrativo3", String.class, "Comune Agente");
		addColumn("localita", String.class, "Localita Agente");
		addColumn("capoarea_denominazione", String.class, "Capo area");
		addColumn("codiceZona", String.class, "Codice Zona Agente");
		addColumn("descrizioneZona", String.class, "Desc. Zona Agente");
	}
}
