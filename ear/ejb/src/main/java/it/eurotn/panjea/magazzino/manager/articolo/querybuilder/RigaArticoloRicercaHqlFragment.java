package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;

public interface RigaArticoloRicercaHqlFragment {

	/**
	 * Crea la stringa hql a seconda dei parametri ricervuti.
	 * 
	 * @param parametriRicercaArticolo
	 *            parametriRicercaArticolo
	 * @return hql fragment
	 */
	String createHql(ParametriRicercaArticolo parametriRicercaArticolo);

}
