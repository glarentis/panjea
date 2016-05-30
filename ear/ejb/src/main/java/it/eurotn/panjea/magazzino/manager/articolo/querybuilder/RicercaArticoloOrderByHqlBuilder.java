package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;

public class RicercaArticoloOrderByHqlBuilder implements RigaArticoloRicercaHqlFragment {

	@Override
	public String createHql(ParametriRicercaArticolo parametriRicercaArticolo) {
		StringBuilder sb = new StringBuilder();

		if (parametriRicercaArticolo.isRicercaBarCodePresente()) {
			sb.append("order by a.barCode ");
		} else if (parametriRicercaArticolo.isRicercaCodiceInternoPresente()) {
			sb.append("order by a.codiceInterno ");
		} else if (parametriRicercaArticolo.getCodiceEntita() != null) {
			sb.append("order by codEnt.codice ");
		} else if (parametriRicercaArticolo.isRicercaCodiceOrDescrizione()
				&& parametriRicercaArticolo.getCodice() != null && parametriRicercaArticolo.getDescrizione() != null) {
			sb.append("order by a.codice ");
		} else {
			if (parametriRicercaArticolo.getCodice() != null) {
				sb.append("order by a.codice ");
			}
			if (parametriRicercaArticolo.getDescrizione() != null) {
				sb.append(getOrderByDescrizioneLinguaFragment(parametriRicercaArticolo));
			}
		}
		return sb.toString();
	}

	/**
	 * Restituisce la stringa hql che permette l'ordinamento dei dati a seconda che la lingua dell'utente sia uguale a
	 * quella aziendale o meno.
	 * 
	 * @param parametriRicercaArticolo
	 *            i parametri per effettuare il controllo
	 * @return la stringa hql con l'ordinamento associato alla lingua dell'utente
	 */
	private String getOrderByDescrizioneLinguaFragment(ParametriRicercaArticolo parametriRicercaArticolo) {
		String orderByString = null;
		if (parametriRicercaArticolo.isLinguaAzienda()) {
			orderByString = new String("order by a.descrizioneLinguaAziendale ");
		} else {
			orderByString = new String("order by dl.descrizione ");
		}
		return orderByString;
	}

}
