/**
 *
 */
package it.eurotn.panjea.anagrafica.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.manager.interfaces.StatisticheAnagraficaManager;
import it.eurotn.panjea.anagrafica.util.StatisticaEntitaDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.StatisticheAnagraficaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.StatisticheAnagraficaManager")
public class StatisticheAnagraficaManagerBean implements StatisticheAnagraficaManager {

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@Override
	public List<StatisticaEntitaDTO> caricaStatisticheEntita() {

		List<StatisticaEntitaDTO> statistiche = new ArrayList<StatisticaEntitaDTO>();

		int annoCorrente = Calendar.getInstance().get(Calendar.YEAR);

		StringBuilder sb = new StringBuilder(2000);
		sb.append("select idEntita as idEntita, ");
		sb.append("sum(annoPrec) as importoFatturatoAnnoPrecedente,sum(annoCorrente) as importoFatturatoAnnoCorrente, ");
		sb.append("sum(importoRateAperte) as importoRateAperte,sum(importoPagatoAnnoCorrente) as importoRatePagateAnnoCorrente, ");
		sb.append("sum(ordinatoAnnoPrec) as importoOrdinatoAnnoPrecedente,sum(ordinatoAnnoCorrente) as importoOrdinatoAnnoCorrente,sum(importoOrdinatoDaEvadere) as importoOrdinatoDaEvadere,sum(numeroOrdiniDaEvadere) as numeroOrdiniDaEvadere ");
		sb.append("from ( ");
		// dati fatturato
		sb.append(getSQLFatturato(annoCorrente));
		sb.append(" union all ");
		// dati rate
		sb.append(getSQLRate());
		sb.append(" union all ");
		// pagamenti
		sb.append(getSQLPagato(annoCorrente));
		sb.append(" union all ");
		// ordinato
		sb.append(getSQLOrdinato(annoCorrente));
		sb.append(" union all ");
		// ordini aperti
		sb.append(getSQLOrdiniDaEvadere(annoCorrente));
		sb.append(") t ");
		sb.append("group by idEntita");

		String[] alias = new String[] { "idEntita", "importoFatturatoAnnoCorrente", "importoFatturatoAnnoPrecedente",
				"importoRateAperte", "importoRatePagateAnnoCorrente", "importoOrdinatoAnnoCorrente",
				"importoOrdinatoAnnoPrecedente", "importoOrdinatoDaEvadere", "numeroOrdiniDaEvadere" };

		Query query = panjeaDAO.prepareSQLQuery(sb.toString(), StatisticaEntitaDTO.class, Arrays.asList(alias));

		try {
			statistiche = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return statistiche;
	}

	/**
	 * recupera {@link JecPrincipal} dal {@link SessionContext}.
	 *
	 * @return {@link JecPrincipal}
	 */
	private JecPrincipal getJecPrincipal() {
		return (JecPrincipal) context.getCallerPrincipal();
	}

	private String getSQLFatturato(int annoCorrente) {
		StringBuilder sb = new StringBuilder(500);
		sb.append("select sede.entita_id as idEntita, ");
		sb.append("	  sum(if(mov.annoMovimento=" + String.valueOf(annoCorrente - 1)
				+ ",mov.importoFatturatoScarico,0)) as annoPrec, ");
		sb.append("	  sum(if(mov.annoMovimento=" + String.valueOf(annoCorrente)
				+ ",mov.importoFatturatoScarico,0)) as annoCorrente, ");
		sb.append("	  0 as importoRateAperte, ");
		sb.append("	  0 as importoPagatoAnnoCorrente, ");
		sb.append("	  0 as ordinatoAnnoPrec, ");
		sb.append("	  0 as ordinatoAnnoCorrente, ");
		sb.append("	  0 as importoOrdinatoDaEvadere, ");
		sb.append("	  0 as numeroOrdiniDaEvadere ");
		sb.append("from dw_movimentimagazzino mov inner join anag_sedi_entita sede on mov.sedeEntita_id = sede.id ");
		sb.append("where mov.annoMovimento = 2014 or mov.annoMovimento = 2013 and mov.codiceAzienda = "
				+ PanjeaEJBUtil.addQuote(getJecPrincipal().getCodiceAzienda()));
		sb.append(" group by sede.entita_id ");
		return sb.toString();
	}

	private String getSQLOrdinato(int annoCorrente) {
		StringBuilder sb = new StringBuilder(500);
		sb.append("select doc.entita_id as idEntita, ");
		sb.append("	  0 as annoPrec, ");
		sb.append("	  0 as annoCorrente, ");
		sb.append("	  0 as importoRateAperte, ");
		sb.append("	  0 as importoPagatoAnnoCorrente, ");
		sb.append("	  sum(if(ao.annoMovimento=" + String.valueOf(annoCorrente - 1)
				+ ",rigaOrd.importoInValutaAziendaTotale,0)) as ordinatoAnnoPrec, ");
		sb.append("	  sum(if(ao.annoMovimento=" + String.valueOf(annoCorrente)
				+ ",rigaOrd.importoInValutaAziendaTotale,0)) as ordinatoAnnoCorrente, ");
		sb.append("	  0 as importoOrdinatoDaEvadere, ");
		sb.append("	  0 as numeroOrdiniDaEvadere ");
		sb.append("from ordi_righe_ordine rigaOrd inner join ordi_area_ordine ao on ao.id=rigaOrd.areaOrdine_id ");
		sb.append("						 inner join docu_documenti doc on doc.id=ao.documento_id ");
		sb.append("where doc.entita_id is not null and doc.codiceAzienda = "
				+ PanjeaEJBUtil.addQuote(getJecPrincipal().getCodiceAzienda()));
		sb.append(" group by doc.entita_id ");
		return sb.toString();
	}

	private String getSQLOrdiniDaEvadere(int annoCorrente) {
		StringBuilder sb = new StringBuilder(500);
		sb.append("select doc.entita_id as idEntita, ");
		sb.append("	  0 as annoPrec, ");
		sb.append("	  0 as annoCorrente, ");
		sb.append("	  0 as importoRateAperte, ");
		sb.append("	  0 as importoPagatoAnnoCorrente, ");
		sb.append("	  0 as ordinatoAnnoPrec, ");
		sb.append("	  0 as ordinatoAnnoCorrente, ");
		sb.append("	  sum(rigaOrd.importoInValutaAziendaTotale) as importoOrdinatoDaEvadere, ");
		sb.append("	  count(DISTINCT ao.id) as numeroOrdiniDaEvadere ");
		sb.append("from ordi_righe_ordine rigaOrd inner join ordi_area_ordine ao on ao.id=rigaOrd.areaOrdine_id ");
		sb.append("						 inner join docu_documenti doc on doc.id=ao.documento_id ");
		sb.append("						 left join maga_righe_magazzino rigaMag on rigaMag.rigaOrdineCollegata_Id=rigaOrd.id ");
		sb.append("where doc.entita_id is not null and rigaMag.id is null and ao.annoMovimento = "
				+ String.valueOf(annoCorrente) + " and doc.codiceAzienda = "
				+ PanjeaEJBUtil.addQuote(getJecPrincipal().getCodiceAzienda()));
		sb.append(" group by doc.entita_id ");
		return sb.toString();
	}

	private String getSQLPagato(int annoCorrente) {
		StringBuilder sb = new StringBuilder(500);
		sb.append("select doc.entita_id as idEntita, ");
		sb.append("	  0 as annoPrec, ");
		sb.append("	  0 as annoCorrente, ");
		sb.append("	  0 as importoRateAperte, ");
		sb.append("	  sum(pag.importoInValutaAzienda + pag.importoInValutaAziendaForzato) as importoPagatoAnnoCorrente, ");
		sb.append("	  0 as ordinatoAnnoPrec, ");
		sb.append("	  0 as ordinatoAnnoCorrente, ");
		sb.append("	  0 as importoOrdinatoDaEvadere, ");
		sb.append("	  0 as numeroOrdiniDaEvadere ");
		sb.append("from part_pagamenti pag inner join part_rate rata on rata.id = pag.rata_id ");
		sb.append("				    inner join part_area_partite ap on ap.id=rata.areaRate_id ");
		sb.append("				    inner join docu_documenti doc on doc.id=ap.documento_id ");
		sb.append("where doc.entita_id is not null and year(pag.dataPagamento) = " + String.valueOf(annoCorrente)
				+ " and doc.codiceAzienda = " + PanjeaEJBUtil.addQuote(getJecPrincipal().getCodiceAzienda()));
		sb.append("group by doc.entita_id ");
		return sb.toString();
	}

	private String getSQLRate() {
		StringBuilder sb = new StringBuilder(500);
		sb.append("select doc.entita_id as idEntita, ");
		sb.append("	  0 as annoPrec, ");
		sb.append("	  0 as annoCorrente, ");
		sb.append("	  sum(rate.importoInValutaAzienda) - (( select sum(ifnull((p.importoInValutaAzienda + p.importoInValutaAziendaForzato),0)) ");
		sb.append("									from part_rate r left join part_pagamenti p on p.rata_id=r.id ");
		sb.append("												  inner join part_area_partite ap on ap.id=r.areaRate_id ");
		sb.append("												  inner join docu_documenti docp on docp.id=ap.documento_id ");
		sb.append("									where docp.entita_id=doc.entita_id)- sum(if(rate.dataScadenza is null,rate.importoInValutaAzienda,0)-if (rate.rataRiemessa_id is not null,rate.importoInValutaAzienda,0))) as importoRateAperte, ");
		sb.append("	  0 as importoPagatoAnnoCorrente, ");
		sb.append("	  0 as ordinatoAnnoPrec, ");
		sb.append("	  0 as ordinatoAnnoCorrente, ");
		sb.append("	  0 as importoOrdinatoDaEvadere, ");
		sb.append("	  0 as numeroOrdiniDaEvadere ");
		sb.append("from part_rate rate inner join part_area_partite ap on ap.id=rate.areaRate_id ");
		sb.append("				inner join docu_documenti doc on doc.id=ap.documento_id ");
		sb.append("where doc.entita_id is not null and doc.codiceAzienda = "
				+ PanjeaEJBUtil.addQuote(getJecPrincipal().getCodiceAzienda()));
		sb.append(" group by doc.entita_id ");
		return sb.toString();
	}

}
