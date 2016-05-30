package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class ProvenienzaPrezzoUltimoCostoAziendaQueryBuilder extends ProvenienzaPrezzoQueryBuilder {

	@Override
	public String getPrezzoSql(ParametriRicercaManutenzioneListino parametri) {
		StringBuffer sb = new StringBuffer();
		sb.append("update maga_riga_manutenzione_listino inner join ");
		sb.append("(select art.id as idArticolo, ");
		sb.append("	  coalesce((select rm.importoInValutaAziendaNetto ");
		sb.append("from maga_area_magazzino am inner join maga_righe_magazzino rm on rm.areaMagazzino_id = am.id ");
		sb.append("where rm.importoInValutaAziendaNetto <> 0 and ");
		sb.append(" am.aggiornaCostoUltimo = true and ");
		sb.append(" am.dataRegistrazione <= '" + DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd")
				+ "' and ");
		sb.append(" rm.articolo_id = art.id limit 1),0) as costo ");
		sb.append("from maga_articoli art ");
		sb.append("where 1=1 ");
		if (!parametri.isTutteCategorie() && !parametri.getCategorie().isEmpty()) {
			sb.append(" and art.categoria_id in (");
			sb.append(StringUtils.join(parametri.getCategorieSql(), ","));
			sb.append(") ");
		}
		if (parametri.getArticoli() != null && !parametri.getArticoli().isEmpty()) {
			sb.append("and art.id in (");
			sb.append(parametri.getArticoliId());
			sb.append(")");
		}
		sb.append(") artCosto on maga_riga_manutenzione_listino.articolo_id=artCosto.idArticolo ");
		sb.append(" set valoreOriginale=artCosto.costo ");
		sb.append("where maga_riga_manutenzione_listino.numero= ");
		sb.append(parametri.getNumeroInserimento());
		sb.append(" and userManutenzione='");
		sb.append(parametri.getUserManutenzione());
		sb.append("'");
		return sb.toString();
	}
}