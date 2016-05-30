package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;

public class ProvenienzaPrezzoListinoQueryBuilder extends ProvenienzaPrezzoQueryBuilder {

	@Override
	public String getPrezzoSql(ParametriRicercaManutenzioneListino parametri) {
		StringBuffer sb = new StringBuffer();
		sb.append("update maga_riga_manutenzione_listino ");
		sb.append("inner join maga_righe_listini rl on maga_riga_manutenzione_listino.articolo_id=rl.articolo_id ");
		sb.append("inner join maga_versioni_listino vl on vl.id=:versioneListino ");
		sb.append("inner join maga_listini l on l.id=vl.listino_id ");
		sb.append("inner join maga_scaglioni_listini sl on sl.rigaListino_id=rl.id ");
		sb.append("set maga_riga_manutenzione_listino.valoreOriginale=sl.prezzo ");
		sb.append("where rl.versioneListino_id=:versioneListino ");
		sb.append("and sl.quantita = maga_riga_manutenzione_listino.quantita ");
		sb.append("and maga_riga_manutenzione_listino.numero=:numeroInserimento ");
		sb.append("and userManutenzione=:userManutenzione ");
		return sb.toString();
	}
}