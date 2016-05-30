package it.eurotn.panjea.ordini.util.rigaordine.builder.domain;

import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaArticoloComponente;
import it.eurotn.panjea.ordini.domain.RigaArticoloDistinta;
import it.eurotn.panjea.ordini.domain.RigaNota;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.RigaTestata;

import java.util.HashMap;
import java.util.Map;

public class RigaOrdineFactoryBuilder {
	private static Map<String, RigaOrdineBuilder> builder = new HashMap<String, RigaOrdineBuilder>();

	static {
		builder.put(RigaNota.class.getName(), new RigaOrdineBuilder());
		builder.put(RigaTestata.class.getName(), new RigaOrdineBuilder());
		builder.put(RigaArticolo.class.getName(), new RigaOrdineBuilder());
		builder.put(RigaArticoloComponente.class.getName(), new RigaOrdineBuilder());
		builder.put(RigaArticoloDistinta.class.getName(), new RigaOrdineBuilder());
	}

	/**
	 * 
	 * @param rigaOrdineResult
	 *            tiopRiga per il quale ritornare il builder
	 * @return builder per creare il tipo riga richiesto
	 */
	public RigaOrdineBuilder getBuilder(RigaOrdine rigaOrdineResult) {
		return builder.get(rigaOrdineResult.getClass().getName());
	}
}
