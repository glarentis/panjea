package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

/**
 *
 *
 * @author giangi
 * @version 1.0, 08/mag/2013
 *
 */
public class TabellaSediEntita extends TabellaDimensione {
	private static final long serialVersionUID = 379852144260325677L;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public TabellaSediEntita() {
		super("Sedi", "vista", null, "entità");
		addColumn("denominazione", String.class, "Entità");
		addColumn("codice", int.class, "Cod. entita");
		addColumn("tipo_anagrafica", String.class, "Tipo Ent.");
		addColumn("descrizione_sede", String.class, "Sede Ent.");
		addColumn("nazione", String.class, "Nazione  Ent.");
		addColumn("livelloAmministrativo1", String.class, "Regione  Ent.");
		addColumn("livelloAmministrativo2", String.class, "Provincia  Ent.");
		addColumn("livelloAmministrativo3", String.class, "Comune  Ent.");
		addColumn("localita", String.class, "Località  Ent.");
		addColumn("codiceZona", String.class, "Codice Zona Ent.");
		addColumn("descrizioneZona", String.class, "Desc. Zona Ent.");
		addColumn("categoriaCommerciale", String.class, "Cat. comm. Ent.");
	}
}
