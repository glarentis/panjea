package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;

public class ArticoloFiltro extends AbstractFiltroSorgente {

	private ParametriRicercaManutenzioneListino parametri;

	/**
	 * Costruttore.
	 *
	 * @param parametri
	 *            parametri
	 */
	public ArticoloFiltro(final ParametriRicercaManutenzioneListino parametri) {
		super();
		this.parametri = parametri;
	}

	@Override
	public String getJoin() {
		return "";
	}

	@Override
	public String getWhere() {
		return " articoli.id in ( " + parametri.getArticoliId() + ") ";
	}

}
