package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;

import java.util.ArrayList;
import java.util.Collection;

public class DecimaliQueryBuilder {
	/**
	 * 
	 * @return sql di aggiornamento decimali basati sull'anagrafica articolo
	 */
	private String getDecimaliArticoloSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("update maga_riga_manutenzione_listino ");
		sb.append("inner join maga_articoli on maga_riga_manutenzione_listino.articolo_id=maga_articoli.id set maga_riga_manutenzione_listino.numeroDecimali=maga_articoli.numeroDecimaliPrezzo, ");
		sb.append("maga_riga_manutenzione_listino.numeroDecimaliOriginale=maga_articoli.numeroDecimaliPrezzo, ");
		sb.append("maga_riga_manutenzione_listino.provenienzaDecimali=4 ");
		sb.append(" where maga_riga_manutenzione_listino.numero=:numeroInserimento ");
		sb.append("and maga_riga_manutenzione_listino.numeroDecimaliOriginale is null ");
		sb.append(" and userManutenzione=:userManutenzione ");
		return sb.toString();
	}

	/**
	 * 
	 * @return sql di aggiornamento decimali basati sulle righe del listino di provenienza.
	 */
	private String getDecimaliRigheListinoProvenienzaSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("update maga_riga_manutenzione_listino inner join maga_righe_listini ");
		sb.append("on maga_riga_manutenzione_listino.articolo_id=maga_righe_listini.articolo_id ");
		sb.append("set ");
		sb.append("maga_riga_manutenzione_listino.numeroDecimali= maga_righe_listini.numeroDecimaliPrezzo, ");
		sb.append("maga_riga_manutenzione_listino.numeroDecimaliOriginale= maga_righe_listini.numeroDecimaliPrezzo, ");
		sb.append("maga_riga_manutenzione_listino.provenienzaDecimali=3 ");
		sb.append("where maga_righe_listini.versioneListino_id=:versioneListino ");
		sb.append("and maga_riga_manutenzione_listino.numeroDecimaliOriginale is null ");
		sb.append(" and maga_riga_manutenzione_listino.numero=:numeroInserimento");
		sb.append(" and userManutenzione=:userManutenzione ");
		return sb.toString();
	}

	/**
	 * @param parametri
	 *            parametri per la creazione del listino
	 * @return sql per aggiornare i decimali nel carrello di manutenzione listino
	 */
	public Collection<String> getSql(ParametriRicercaManutenzioneListino parametri) {
		Collection<String> result = new ArrayList<String>();

		if (parametri.getVersioneListino() != null) {
			result.add(getDecimaliRigheListinoProvenienzaSql());
		}
		result.add(getDecimaliArticoloSql());
		return result;
	}
}
