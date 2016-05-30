/**
 *
 */
package it.eurotn.panjea.contabilita.manager.ritenutaacconto;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.DatiRitenutaAccontoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.domain.ContributoEnasarco;
import it.eurotn.panjea.contabilita.domain.ContributoINPS;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoManager;
import it.eurotn.panjea.contabilita.util.CertificazioneRitenutaDTO;
import it.eurotn.panjea.contabilita.util.ParametriSituazioneRitenuteAcconto;
import it.eurotn.panjea.contabilita.util.SituazioneRitenuteAccontoDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.RitenutaAccontoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RitenutaAccontoManager")
public class RitenutaAccontoManagerBean implements RitenutaAccontoManager {

	private static Logger logger = Logger.getLogger(RitenutaAccontoManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	private AziendeManager aziendeManager;

	@Override
	public void cancellaCausaleRitenutaAcconto(CausaleRitenutaAcconto causaleRitenutaAcconto) {
		logger.debug("--> Enter cancellaCausaleRitenutaAcconto");

		try {
			panjeaDAO.delete(causaleRitenutaAcconto);
		} catch (Exception e) {
			logger.error("--> errore durante la cancellazione della causale ritenuta acconto", e);
			throw new RuntimeException("errore durante la cancellazione della causale ritenuta acconto", e);
		}

		logger.debug("--> Exit cancellaCausaleRitenutaAcconto");
	}

	@Override
	public void cancellaContributoPrevidenziale(ContributoPrevidenziale contributoPrevidenziale) {
		logger.debug("--> Enter cancellaContributoPrevidenziale");

		try {
			panjeaDAO.delete(contributoPrevidenziale);
		} catch (Exception e) {
			logger.error("--> errore durante la cancellazione del contributo previdenziale", e);
			throw new RuntimeException("errore durante la cancellazione del contributo previdenziale", e);
		}

		logger.debug("--> Exit cancellaContributoPrevidenziale");
	}

	@Override
	public void cancellaPrestazione(Prestazione prestazione) {
		logger.debug("--> Enter cancellaPrestazione");

		try {
			panjeaDAO.delete(prestazione);
		} catch (DAOException e) {
			logger.error("--> errore durante la cancellazione della prestazione", e);
			throw new RuntimeException("errore durante la cancellazione della prestazione", e);
		}

		logger.debug("--> Exit cancellaPrestazione");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CausaleRitenutaAcconto> caricaCausaliRitenuteAcconto(String codice) {
		logger.debug("--> Enter caricaCausaliRitenuteAcconto");

		StringBuilder sb = new StringBuilder("from CausaleRitenutaAcconto cra ");
		if (codice != null) {
			sb.append(" where cra.codice like ").append(PanjeaEJBUtil.addQuote(codice));
		}
		sb.append(" order by codice");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		List<CausaleRitenutaAcconto> causali = null;
		try {
			causali = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore in ricerca causali ritenute acconto", e);
			throw new RuntimeException("ricerca causali ritenute acconto", e);
		}
		logger.debug("--> Exit caricaCausaliRitenuteAcconto");
		return causali;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CertificazioneRitenutaDTO> caricaCertificazioneRitenute(int anno, Integer idEntita) {
		logger.debug("--> Enter caricaCertificazioneRitenute");

		// utilizzo una variabile per il calcolo del totale delle ritenute per evitare di dover fare altre query
		// aggiuntive
		StringBuilder sb = new StringBuilder(500);
		sb.append("select ent.id as idEntita,  ");
		sb.append("		 ac.id as idAreaContabile, ");
		sb.append("	  	 prest.descrizione as prestazione, ");
		sb.append("       doc.codice as numeroDocumento, ");
		sb.append("       doc.dataDocumento as dataDocumento, ");
		sb.append("       totaliIva.totLordo as totaleLordo, ");
		sb.append("       ac.imponibileNonSoggettoRitenuta as imponibileNonSoggetto, ");
		sb.append("       ac.imponibileSoggettoRitenuta as imponibileSoggetto, ");
		sb.append("       pag.dataPagamento as dataPagamento, ");
		sb.append("       ac.percentualeAliquota as percentualeAliquota, ");
		sb.append("       rataRitenuta.importoInValutaAzienda as importoRitenutaAcconto, ");
		sb.append("       coalesce(ac.speseRimborso,0) as importoRimborsoSpese, ");
		sb.append("       (select sum(ratPag.importoInValutaAzienda) from part_pagamenti pag1 inner join part_rate ratPag on pag1.id = ratPag.pagamentoRiferimento_id where pag1.rata_id = rata.id) as totaleImportoRitenute ");
		sb.append("from part_rate rata inner join part_area_partite ap on rata.areaRate_id = ap.id ");
		sb.append("				inner join docu_documenti doc on ap.documento_id = doc.id ");
		sb.append("				inner join cont_area_contabile ac on ac.documento_id = doc.id ");
		sb.append("				inner join anag_entita ent on ent.id = doc.entita_id ");
		sb.append("				inner join (select ai.documento_id as idDoc,sum(ri.importoInValutaAziendaImponibile)  as totLordo ");
		sb.append("        					  from cont_righe_iva ri inner join cont_aree_iva ai on ai.id = ri.areaIva_id ");
		sb.append("       				       group by ai.documento_id) as totaliIva on totaliIva.idDoc = doc.id ");
		sb.append("				inner join part_pagamenti pag on pag.rata_id = rata.id ");
		sb.append("				inner join part_rate rataRitenuta on rataRitenuta.areaRate_id = ap.id and pag.id = rataRitenuta.pagamentoRiferimento_id ");
		sb.append("				left join cont_prestazioni prest on prest.id = ac.prestazione_id ");
		sb.append("where year(pag.dataPagamento) = " + anno
				+ " and ac.causaleRitenutaPresente = true and rata.ritenutaAcconto = false ");
		if (idEntita != null) {
			sb.append(" and ent.id = " + idEntita);
		}

		sb.append(" order by ent.id,doc.dataDocumento,ac.codiceOrder,ac.id ");

		String[] alias = new String[] { "idEntita", "idAreaContabile", "prestazione", "numeroDocumento",
				"dataDocumento", "totaleLordo", "imponibileNonSoggetto", "imponibileSoggetto", "dataPagamento",
				"percentualeAliquota", "importoRitenutaAcconto", "importoRimborsoSpese", "totaleImportoRitenute" };
		Query query = panjeaDAO.prepareSQLQuery(sb.toString(), CertificazioneRitenutaDTO.class, Arrays.asList(alias));

		List<CertificazioneRitenutaDTO> certificazione;
		try {
			certificazione = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento della certificazione", e);
			throw new RuntimeException("errore durante il caricamento della certificazione", e);
		}

		AziendaLite azienda = aziendeManager.caricaAzienda(true);

		Map<Integer, Anagrafica> mapAnagrafiche = new HashMap<Integer, Anagrafica>();

		// ins erisco i dati delle entità
		for (CertificazioneRitenutaDTO certificazioneRitenutaDTO : certificazione) {
			Anagrafica anagrafica = mapAnagrafiche.get(certificazioneRitenutaDTO.getIdEntita());
			if (anagrafica == null) {
				sb = new StringBuilder(200);
				sb.append("select a ");
				sb.append("from Entita e inner join e.anagrafica a inner join fetch a.sedeAnagrafica sa ");
				sb.append("where e.id = ");
				sb.append(certificazioneRitenutaDTO.getIdEntita());
				try {
					query = panjeaDAO.prepareQuery(sb.toString());
					anagrafica = (Anagrafica) panjeaDAO.getSingleResult(query);
					mapAnagrafiche.put(certificazioneRitenutaDTO.getIdEntita(), anagrafica);
				} catch (Exception e) {
					logger.error("--> errore durante il caricamento dell'anagrafica.", e);
					throw new RuntimeException("errore durante il caricamento dell'anagrafica.", e);
				}
			}
			certificazioneRitenutaDTO.setAnagrafica(anagrafica);
			certificazioneRitenutaDTO.setAziendaLite(azienda);
		}

		logger.debug("--> Exit caricaCertificazioneRitenute");
		return certificazione;
	}

	@Override
	public List<ContributoPrevidenziale> caricaContributiEnasarco() {
		return caricaContributiPrevidenziali(ContributoEnasarco.class);
	}

	@Override
	public List<ContributoPrevidenziale> caricaContributiINPS() {
		return caricaContributiPrevidenziali(ContributoINPS.class);
	}

	@Override
	public List<ContributoPrevidenziale> caricaContributiPrevidenziali() {
		return caricaContributiPrevidenziali(ContributoPrevidenziale.class);
	}

	/**
	 * Carica tutti i contributi previdenziali.
	 *
	 * @param contributoClass
	 *            classe dei contributi da caricare
	 * @return controbuti caricati
	 */
	@SuppressWarnings("unchecked")
	private List<ContributoPrevidenziale> caricaContributiPrevidenziali(
			Class<? extends ContributoPrevidenziale> contributoClass) {
		logger.debug("--> Enter caricaContributiPrevidenziali");

		StringBuilder sb = new StringBuilder("from ");
		sb.append(contributoClass.getName());
		sb.append(" cp ");
		sb.append(" order by codice");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		List<ContributoPrevidenziale> contributi = null;
		try {
			contributi = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore in ricerca contributi previdenziali", e);
			throw new RuntimeException("errore in ricerca contributi previdenziali", e);
		}
		logger.debug("--> Exit caricaContributiPrevidenziali");
		return contributi;
	}

	@Override
	public DatiRitenutaAccontoEntita caricaDatiRitenutaAccontoAgente(Integer idAnagrafica) {
		logger.debug("--> Enter caricaDatiRitenutaAccontoAgente");

		StringBuilder sb = new StringBuilder();
		sb.append("select a.datiRitenutaAcconto from Agente a ");
		sb.append(" where a.anagrafica.id = :paramIdAnagrafica");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramIdAnagrafica", idAnagrafica);

		DatiRitenutaAccontoEntita datiRitenutaAccontoEntita = null;
		try {
			datiRitenutaAccontoEntita = (DatiRitenutaAccontoEntita) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			// l'agente non ha dati configurati
			datiRitenutaAccontoEntita = null;
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dei dati ritenuta acconto dell' agente con anagrafica "
					+ idAnagrafica, e);
			throw new RuntimeException(
					"errore durante il caricamento dei dati ritenuta acconto dell'agente con anagrafica "
							+ idAnagrafica, e);
		}
		return datiRitenutaAccontoEntita;
	}

	@Override
	public DatiRitenutaAccontoEntita caricaDatiRitenutaAccontoFornitore(Integer idFornitore) {
		logger.debug("--> Enter caricaDatiRitenutaAccontoEntita");

		StringBuilder sb = new StringBuilder();
		sb.append("select f.datiRitenutaAcconto from Fornitore f ");
		sb.append(" where f.id = :paramIdFornitore");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramIdFornitore", idFornitore);

		DatiRitenutaAccontoEntita datiRitenutaAccontoEntita = null;
		try {
			datiRitenutaAccontoEntita = (DatiRitenutaAccontoEntita) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			// il fornitore non ha dati configurati
			datiRitenutaAccontoEntita = null;
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dei dati ritenuta acconto del fornitore " + idFornitore, e);
			throw new RuntimeException("errore durante il caricamento dei dati ritenuta acconto del fornitore "
					+ idFornitore, e);
		}
		return datiRitenutaAccontoEntita;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Prestazione> caricaPrestazioni() {
		logger.debug("--> Enter caricaPrestazioni");

		Query query = panjeaDAO.prepareQuery("from Prestazione pr");
		List<Prestazione> prestazioni = null;
		try {
			prestazioni = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore in ricerca delle prestazioni", e);
			throw new RuntimeException("errore in ricerca delle prestazioni", e);
		}

		logger.debug("--> Exit caricaPrestazioni");
		return prestazioni;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SituazioneRitenuteAccontoDTO> caricaSituazioneRitenuteAccont(
			ParametriSituazioneRitenuteAcconto parametri) {

		String sqlQuery = SituazioneRateRitenuteQueryBuilder.getSQL(parametri);
		String[] alias = SituazioneRateRitenuteQueryBuilder.getAlias();

		Query query = panjeaDAO.prepareSQLQuery(sqlQuery, SituazioneRitenuteAccontoDTO.class, Arrays.asList(alias));

		List<SituazioneRitenuteAccontoDTO> situazione = new ArrayList<SituazioneRitenuteAccontoDTO>();
		try {
			situazione = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento della situazione ritenute d'acconto.", e);
			throw new RuntimeException("errore durante il caricamento della situazione ritenute d'acconto.", e);
		}

		return situazione;
	}

	@Override
	public CausaleRitenutaAcconto salvaCausaleRitenutaAcconto(CausaleRitenutaAcconto causaleRitenutaAcconto) {
		logger.debug("--> Enter salvaCausaleRitenutaAcconto");

		try {
			causaleRitenutaAcconto = panjeaDAO.save(causaleRitenutaAcconto);
		} catch (Exception e) {
			logger.error("--> errore durante il salvataggio della causale ritenuta acconto", e);
			throw new RuntimeException("errore durante il salvataggio della causale ritenuta acconto", e);
		}

		return causaleRitenutaAcconto;
	}

	@Override
	public ContributoPrevidenziale salvaContributoPrevidenziale(ContributoPrevidenziale contributoPrevidenziale) {
		logger.debug("--> Enter salvaContributoPrevidenziale");

		try {
			contributoPrevidenziale = panjeaDAO.save(contributoPrevidenziale);
		} catch (Exception e) {
			logger.error("--> errore durante il salvataggio del contributo previdenziale", e);
			throw new RuntimeException("errore durante il salvataggio del contributo previdenziale", e);
		}

		return contributoPrevidenziale;
	}

	@Override
	public Prestazione salvaPrestazione(Prestazione prestazione) {
		logger.debug("--> Enter salvaPrestazione");

		try {
			prestazione = panjeaDAO.save(prestazione);
		} catch (DAOException e) {
			logger.error("--> errore durante il salvataggio della prestazione", e);
			throw new RuntimeException("errore durante il salvataggio della prestazione", e);
		}

		return prestazione;
	}

}
