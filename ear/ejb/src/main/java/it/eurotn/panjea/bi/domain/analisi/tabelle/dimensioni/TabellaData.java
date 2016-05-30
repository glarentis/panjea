package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

import java.util.Date;

public class TabellaData extends TabellaDimensione {
	private static final long serialVersionUID = 5430861492755275331L;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public TabellaData() {
		super("dimensionedata", "dw", null, "data");
		addColumn("ANNO", short.class, "Anno");
		addColumn("QUADRIMESTRE", short.class, "Trimestre");
		addColumn("MESE", short.class, "Mese");
		addColumn("SETTIMANA", short.class, "Settimana");
		addColumn("DATA_ID", Date.class, "Data reg.");
	}

	@Override
	public String getJoin(boolean alternativeJoin) {
		// la tabella della data viene sempre messa in inner join.
		return " inner join ";
	}
}
