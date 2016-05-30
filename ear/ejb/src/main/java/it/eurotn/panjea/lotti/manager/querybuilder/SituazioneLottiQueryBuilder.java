package it.eurotn.panjea.lotti.manager.querybuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.lotti.manager.LottiManagerBean.RimanenzaLotto;

public final class SituazioneLottiQueryBuilder {

	/**
	 * Costruttore.
	 *
	 */
	private SituazioneLottiQueryBuilder() {
		super();
	}

	/**
	 * @return alias
	 */
	public static List<String> getAlias() {
		List<String> alias = new ArrayList<>();
		alias.add("lottoId");
		alias.add("lottoVersione");
		alias.add("lottoCodice");
		alias.add("lottoScadenza");
		alias.add("lottoPriorita");
		alias.add("lottoTipo");
		alias.add("depositoId");
		alias.add("depositoCodice");
		alias.add("depositoDescrizione");
		alias.add("articoloId");
		alias.add("articoloVersion");
		alias.add("articoloAbilitato");
		alias.add("articoloBarCode");
		alias.add("articoloCodice");
		alias.add("articoloDescrizione");
		alias.add("articoloNumDecPrezzo");
		alias.add("articoloNumDecQta");
		alias.add("categoriaId");
		alias.add("categoriaCodice");
		alias.add("categoriaDescrizione");
		alias.add("quantitaCarico");
		alias.add("quantitaScarico");
		alias.add("rimanenza");
		return alias;
	}

	private static String getSelectSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("lotto.id as lottoId, ");
		sb.append("lotto.version as lottoVersione, ");
		sb.append("lotto.codice as lottoCodice, ");
		sb.append("lotto.dataScadenza as lottoScadenza, ");
		sb.append("lotto.priorita as lottoPriorita, ");
		sb.append("lotto.TIPO_LOTTO as lottoTipo, ");
		sb.append("dep.id as depositoId, ");
		sb.append("dep.codice as depositoCodice, ");
		sb.append("dep.descrizione as depositoDescrizione, ");
		sb.append("lotto.articolo_id AS articoloId ");

		return sb.toString();
	}

	/**
	 * Restituisce la query per il caricamento della situazione lotti.
	 *
	 * @param filtraDeposito
	 *            aggiunge il filtro per il deposito
	 * @param filtraArticolo
	 *            aggiunge il filtro per l'articolo
	 * @param filtraCategorie
	 *            aggiunge il filtro per le categorie articolo
	 * @param data
	 *            data di riferimento
	 * @param rimanenzaLotto
	 *            rimanenza lotto
	 * @param dataScadenza
	 *            data scadenza
	 * @param codiceLotto
	 *            codice lotto
	 * @param dataInizioRicercaScadenza
	 *            data iniziale basata sulla data scadenza lotto
	 *
	 * @return hql
	 */
	public static String getSituazioneLottiArticolo(boolean filtraDeposito, boolean filtraArticolo,
			boolean filtraCategorie, Date data, RimanenzaLotto rimanenzaLotto, Date dataScadenza, String codiceLotto,
			Date dataInizioRicercaScadenza) {
		StringBuilder sb = new StringBuilder();
		sb.append("select lottoId, ");
		sb.append("lottoVersione, ");
		sb.append("lottoCodice, ");
		sb.append("lottoScadenza, ");
		sb.append("lottoPriorita, ");
		sb.append("lottoTipo, ");
		sb.append("depositoId, ");
		sb.append("depositoCodice, ");
		sb.append("depositoDescrizione, ");
		sb.append("articoloId, ");
		sb.append("art.version as articoloVersion, ");
		sb.append("art.abilitato as articoloAbilitato, ");
		sb.append("art.barCode as articoloBarCode, ");
		sb.append("art.codice as articoloCodice, ");
		sb.append("art.descrizioneLinguaAziendale as articoloDescrizione, ");
		sb.append("art.numeroDecimaliPrezzo as articoloNumDecPrezzo, ");
		sb.append("art.numeroDecimaliQta as articoloNumDecQta, ");
		sb.append("cat.id as categoriaId, ");
		sb.append("cat.codice as categoriaCodice, ");
		sb.append("cat.descrizioneLinguaAziendale as categoriaDescrizione, ");
		sb.append("			 sum(quantitaCarico) as quantitaCarico, ");
		sb.append("			 sum(quantitaScarico) as quantitaScarico, ");
		sb.append("			 round(sum(quantitaCarico)+sum(quantitaScarico),art.numeroDecimaliPrezzo) as rimanenza ");
		sb.append("from (");
		sb.append(getSQLforCarichi(filtraDeposito, filtraArticolo, filtraCategorie, data, rimanenzaLotto, dataScadenza,
				codiceLotto, dataInizioRicercaScadenza));
		sb.append("union ");
		sb.append(getSQLforScarichi(filtraDeposito, filtraArticolo, filtraCategorie, data, rimanenzaLotto, dataScadenza,
				codiceLotto, dataInizioRicercaScadenza));
		sb.append(" order by null ) as statistiche inner join maga_articoli art on statistiche.articoloId = art.id ");
		sb.append(
				"																					    inner join maga_categorie cat on art.categoria_id = cat.id ");
		sb.append("group by lottoId,depositoId ");
		switch (rimanenzaLotto) {
		case NEGATIVA:
			sb.append("having rimanenza < 0 ");
			break;
		case POSITIVA:
			sb.append("having rimanenza > 0 ");
			break;
		case ZERO:
			sb.append("having rimanenza = 0 ");
			break;
		default:
			// non faccio niente, prendo tutte le rimanenze
			break;
		}
		sb.append("order by lottoId,depositoId ");

		return sb.toString();
	}

	private static String getSQLforCarichi(boolean filtraDeposito, boolean filtraArticolo, boolean filtraCategorie,
			Date data, RimanenzaLotto rimanenzaLotto, Date dataScadenza, String codiceLotto,
			Date dataInizioRicercaScadenza) {
		StringBuilder sb = new StringBuilder(2000);
		sb.append("select ");
		sb.append(
				"round(sum(coalesce(rigaLotto.quantita, 0)*if(tipoDoc.notaCreditoEnable, -1, 1)*case when tam.tipoMovimento=1 ");
		sb.append(
				"																		then 1 when tam.tipoMovimento=4 ");
		sb.append(
				"																		then 1 when tam.tipoMovimento=2 ");
		sb.append(
				"																		then -1 when tam.tipoMovimento=3 ");
		sb.append(
				"																		then -1 when tam.tipoMovimento=7 ");
		sb.append(
				"																		then 1 end),art.numeroDecimaliPrezzo) as quantitaCarico, ");
		sb.append("0 as quantitaScarico, ");
		sb.append(getSelectSQL());
		sb.append(
				"from maga_righe_lotti rigaLotto inner join maga_righe_magazzino rm on rigaLotto.rigaArticolo_id=rm.id ");
		sb.append("					inner join maga_lotti lotto on lotto.id=rigaLotto.lotto_id ");
		sb.append("				  inner join maga_area_magazzino am on rm.areaMagazzino_id=am.id ");
		sb.append("				  inner join maga_tipi_area_magazzino tam on am.tipoAreaMagazzino_id=tam.id ");
		sb.append("				  inner join docu_tipi_documento tipoDoc on tam.tipoDocumento_id=tipoDoc.id ");
		sb.append("				  inner join anag_depositi dep on am.depositoOrigine_id=dep.id ");
		sb.append("INNER JOIN maga_articoli art ON lotto.articolo_id = art.id ");
		// filtro le categorie qui invece che sulla join in fondo per prestazioni
		if (filtraCategorie) {
			sb.append("INNER JOIN maga_categorie cat ON art.categoria_id = cat.id and cat.id in (:categorie) ");
		}
		sb.append("where (tam.tipoMovimento<7 or (tam.tipoMovimento=7 and rm.TIPO_RIGA<>'C')) ");
		sb.append(getWhereCarichiScarichi(filtraDeposito, filtraArticolo, data, dataScadenza, codiceLotto,
				dataInizioRicercaScadenza));
		sb.append("group by lotto.id, dep.id ");
		return sb.toString();
	}

	private static String getSQLforScarichi(boolean filtraDeposito, boolean filtraArticolo, boolean filtraCategorie,
			Date data, RimanenzaLotto rimanenzaLotto, Date dataScadenza, String codiceLotto,
			Date dataInizioRicercaScadenza) {
		StringBuilder sb = new StringBuilder(2000);
		sb.append("select ");
		sb.append("0 as quantitaCarico, ");
		sb.append(
				"round(sum(coalesce(rigaLotto.quantita, 0)*case when tam.tipoMovimento=3 then 1 when tam.tipoMovimento=7 then -1 end),art.numeroDecimaliPrezzo) as quantitaScarico, ");
		sb.append(getSelectSQL());
		sb.append(
				"from maga_righe_lotti rigaLotto inner join maga_righe_magazzino rm on rigaLotto.rigaArticolo_id=rm.id ");
		sb.append(
				"																inner join maga_lotti lotto on lotto.id=rigaLotto.lotto_id ");
		sb.append(
				"																inner join maga_area_magazzino am on rm.areaMagazzino_id=am.id");
		sb.append(
				"																inner join maga_tipi_area_magazzino tam on am.tipoAreaMagazzino_id=tam.id ");
		sb.append(
				"																inner join docu_documenti tipoDoc on am.documento_id=tipoDoc.id ");
		sb.append(
				"																inner join anag_depositi dep on am.depositoDestinazione_id=dep.id ");
		sb.append("INNER JOIN maga_articoli art ON lotto.articolo_id = art.id ");
		// filtro le categorie qui invece che sulla join in fondo per prestazioni
		if (filtraCategorie) {
			sb.append("INNER JOIN maga_categorie cat ON art.categoria_id = cat.id and cat.id in (:categorie) ");
		}
		sb.append("where (tam.tipoMovimento=3 or (tam.tipoMovimento=7 and rm.TIPO_RIGA='C')) ");
		sb.append(getWhereCarichiScarichi(filtraDeposito, filtraArticolo, data, dataScadenza, codiceLotto,
				dataInizioRicercaScadenza));
		sb.append("group by lotto.id, dep.id ");
		return sb.toString();
	}

	private static String getWhereCarichiScarichi(boolean filtraDeposito, boolean filtraArticolo, Date data,
			Date dataScadenza, String codiceLotto, Date dataInizioRicercaScadenza) {
		StringBuilder sb = new StringBuilder(1000);
		if (dataInizioRicercaScadenza != null) {
			sb.append(" and lotto.dataScadenza >= :paramDataInizioDataScadenza ");
		}
		if (filtraArticolo) {
			sb.append(" and lotto.articolo_id = :articolo ");
		}
		if (filtraDeposito) {
			sb.append(" and dep.id = :deposito ");
		}
		if (data != null) {
			sb.append(" and am.dataRegistrazione <= :paramData ");
		}
		if (dataScadenza != null) {
			sb.append(" and lotto.dataScadenza = :paramDataScadenza ");
		}
		if (!StringUtils.isBlank(codiceLotto)) {
			sb.append(" and lotto.codice like :paramCodiceLotto ");
		}

		return sb.toString();

	}

}
