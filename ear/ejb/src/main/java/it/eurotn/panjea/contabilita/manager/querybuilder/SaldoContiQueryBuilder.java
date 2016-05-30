package it.eurotn.panjea.contabilita.manager.querybuilder;

import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipiDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.querybuilder.SaldoContiQueryBuilderInterface;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.SaldoContiQueryBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SaldoContiQueryBuilder")
public class SaldoContiQueryBuilder implements SaldoContiQueryBuilderInterface {

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected TipiAreaContabileManager tipiAreaContabileManager;

	/**
	 * @return stringa contenente la group by per la query
	 */
	protected String buildGroupHql() {
		return " group by mastro, conto, sottoconto";
	}

	/**
	 * @param isRicercaCentroCosto
	 *            se la ricerca e' per centro di costo devo prendere l'importo sulla rigaCentroCosto
	 * @return stringa contenente la select per la query
	 */
	protected String buildSelectHql(boolean isRicercaCentroCosto) {
		StringBuffer queryHQL = new StringBuffer();
		queryHQL.append(" select  ");
		queryHQL.append(" mastro.codice as mastroCodice, ");
		queryHQL.append(" mastro.descrizione as mastroDescrizione, ");
		queryHQL.append(" mastro.id as mastroId, ");
		queryHQL.append(" conto.codice as contoCodice, ");
		queryHQL.append(" conto.descrizione as contoDescrizione, ");
		queryHQL.append(" conto.id as contoId, ");
		queryHQL.append(" sottoconto.codice as sottoContoCodice, ");
		queryHQL.append(" sottoconto.descrizione as sottoContoDescrizione, ");
		queryHQL.append(" sottoconto.id as sottoContoId, ");
		queryHQL.append(" conto.tipoConto as tipoConto, ");
		queryHQL.append(" conto.sottotipoConto as sottoTipoConto, ");
		if (isRicercaCentroCosto) {
			// queryHQL.append(" sum(case when rigaContabile.importoDare>0.0 then rigaCentroCosto.importo else 0.0 end), ");
			queryHQL.append(" sum(case when rigaContabile.contoInsert = it.eurotn.panjea.contabilita.domain.RigaContabile$EContoInsert.DARE then rigaCentroCosto.importo else 0.0 end) as importoDare, ");
			queryHQL.append(" sum(case when rigaContabile.contoInsert = it.eurotn.panjea.contabilita.domain.RigaContabile$EContoInsert.AVERE then rigaCentroCosto.importo else 0.0 end) as importoAvere ");
		} else {
			queryHQL.append(" sum(rigaContabile.importoDare) as importoDare, ");
			queryHQL.append(" sum(rigaContabile.importoAvere) as importoAvere ");
		}
		queryHQL.append(" from RigaContabile rigaContabile");
		queryHQL.append(" inner join rigaContabile.areaContabile areacontabile");
		queryHQL.append(" inner join areacontabile.documento documento");
		queryHQL.append(" inner join rigaContabile.conto sottoconto");
		queryHQL.append(" inner join sottoconto.conto conto");
		queryHQL.append(" inner join conto.mastro mastro");
		if (isRicercaCentroCosto) {
			queryHQL.append(" inner join rigaContabile.righeCentroCosto rigaCentroCosto");
		}
		return queryHQL.toString();
	}

	@Override
	public Query getQuery(SottoConto sottoConto, CentroCosto centroCosto, Date dataInizio, Date dataFine,
			Integer annoEsercizio, AziendaLite aziendaLite, List<StatoAreaContabile> listaStatiAree,
			boolean esclusiEconomici) throws ContabilitaException, TipoDocumentoBaseException {

		StringBuffer whereHQL = new StringBuffer();

		boolean isRicercaCentroCosto = centroCosto != null;

		// Recupero i tipidocumento per le aperture/chiusure dei vari conti
		TipiDocumentoBase tipiDocumentoBase = tipiAreaContabileManager.caricaTipiOperazione();
		List<TipoAreaContabile> tipiAreeContabiliChiusuraApertura = tipiDocumentoBase
				.getTipiDocumentiAperturaChiusura();

		whereHQL.append(" WHERE areaContabile.tipoAreaContabile not in (:paramTipoAreaContabileChiusura) ");

		Map<String, Object> valueParametri = new HashMap<String, Object>();
		if (sottoConto == null) {
			// filtro per azienda
			whereHQL.append(" and documento.codiceAzienda = :paramCodiceAzienda");
			valueParametri.put("paramCodiceAzienda", aziendaLite.getCodice());
		} else {
			// filtro per conto
			whereHQL.append("and sottoconto.id = :paramIdSottoConto ");
			valueParametri.put("paramIdSottoConto", sottoConto.getId());
		}

		if (isRicercaCentroCosto) {
			whereHQL.append(" and rigaCentroCosto.centroCosto.id=:paramIdCentroCosto ");
			valueParametri.put("paramIdCentroCosto", centroCosto.getId());
		}

		// Filtro per data
		if (dataInizio != null) {
			whereHQL.append(" and areaContabile.dataRegistrazione between :paramDaDataRegistrazione  and :paramADataRegistrazione ");
			valueParametri.put("paramDaDataRegistrazione", PanjeaEJBUtil.getDateTimeToZero(dataInizio));
			valueParametri.put("paramADataRegistrazione", PanjeaEJBUtil.getDateTimeToZero(dataFine));
		}

		valueParametri.put("paramTipoAreaContabileChiusura", tipiAreeContabiliChiusuraApertura);

		// filtro tipi documento
		if ((listaStatiAree != null) && (listaStatiAree.size() > 0)) {
			whereHQL.append(" and areaContabile.statoAreaContabile in (:paramStatoAreaContabile) ");
			valueParametri.put("paramStatoAreaContabile", listaStatiAree);
		}

		if (esclusiEconomici) {
			whereHQL.append(" and conto.tipoConto != 1 ");
		}

		if (annoEsercizio != 0) {
			whereHQL.append(" and areaContabile.annoMovimento = :paramAnnoMovimento");
			valueParametri.put("paramAnnoMovimento", annoEsercizio);
		}

		Query query = panjeaDAO.prepareQuery(buildSelectHql(isRicercaCentroCosto) + whereHQL + buildGroupHql());

		Set<String> set = valueParametri.keySet();
		for (String key : set) {
			Object value = valueParametri.get(key);
			if (value instanceof Date) {
				Date valueDate = (Date) value;
				query.setParameter(key, valueDate, TemporalType.DATE);
			} else {
				query.setParameter(key, valueParametri.get(key));
			}
		}
		return query;
	}
}
