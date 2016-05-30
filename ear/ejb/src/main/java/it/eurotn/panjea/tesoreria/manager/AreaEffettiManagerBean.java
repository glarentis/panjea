package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaEffettiContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaEffettiManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaPagamentiManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.panjea.tesoreria.manager.sqlbuilder.RicercaEffettiQueryBuilder;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaEffetti;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AreaEffettiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaEffettiManager")
public class AreaEffettiManagerBean implements AreaEffettiManager {
	private static Logger logger = Logger.getLogger(AreaEffettiManagerBean.class.getName());

	@Resource
	protected SessionContext context;

	@EJB
	private AreaContabileCancellaManager areaContabileCancellaManager;

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected DocumentiManager documentiManager;

	@EJB
	protected AreaTesoreriaManager areaTesoreriaManager;

	@EJB
	protected AreaPagamentiManager areaPagamentiManager;

	@EJB
	protected AreaEffettiContabilitaManager areaEffettiContabilitaManager;

	@Override
	public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate) {
		logger.debug("--> Enter cancellaAreaTesoreria");

		try {
			if (deleteAreeCollegate) {
				AreaContabileLite areaContabileLite = areaContabileManager
						.caricaAreaContabileLiteByDocumento(areaTesoreria.getDocumento());
				if (areaContabileLite != null) {
					areaContabileCancellaManager.cancellaAreaContabile(areaTesoreria.getDocumento(), true);
				}
			}
			panjeaDAO.delete(areaTesoreria);
			if (deleteAreeCollegate) {
				documentiManager.cancellaDocumento(areaTesoreria.getDocumento());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit cancellaAreaTesoreria");
	}

	@Override
	public void cancellaEffetto(Effetto effetto) {
		logger.debug("--> Enter cancellaEffetto");
		try {
			// Cancello l'effetto
			StringBuffer sb = new StringBuffer();
			sb.append("delete from part_pagamenti where effetto_id = " + effetto.getId());
			javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
			try {
				panjeaDAO.executeQuery(query);
			} catch (DAOException e) {
				throw new RuntimeException(e);
			}

			sb = new StringBuffer();
			sb.append("delete from part_effetti where id = " + effetto.getId());
			query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
			try {
				panjeaDAO.executeQuery(query);
			} catch (DAOException e) {
				throw new RuntimeException(e);
			}

			// se non ho altri effetti cancello l'area tesoreria e quelle collegate, altrimenti ritotalizzo il documento
			// e
			// rifaccio l'area contabile
			if (effetto.getAreaEffetti().getEffetti().size() > 1) {
				AreaEffetti areaEffetti = (AreaEffetti) areaTesoreriaManager.caricaAreaTesoreria(effetto
						.getAreaEffetti());

				// Ritotalizzo
				Importo totaleDocumento = areaEffetti.getEffetti().iterator().next().getImporto().clone();
				totaleDocumento.setImportoInValuta(BigDecimal.ZERO);
				totaleDocumento.calcolaImportoValutaAzienda(2);

				for (Effetto effettoArea : areaEffetti.getEffetti()) {
					totaleDocumento = totaleDocumento.add(effettoArea.getImporto(), 2);
				}
				areaEffetti.getDocumento().setTotale(totaleDocumento);
				areaEffetti = panjeaDAO.save(areaEffetti);

				// Ricreo l'area contabile
				areaEffettiContabilitaManager.creaAreaContabileEffetti(areaEffetti, null);
			} else {
				areaTesoreriaManager.cancellaAreaTesoreria(effetto.getAreaEffetti(), true);
			}

		} catch (Exception e) {
			logger.error("-->errore nel cancellare l'effetto con id " + effetto.getId(), e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaEffetto");

	}

	@Override
	public AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException {
		logger.debug("--> Enter caricaAreaTesoreria");

		AreaEffetti result = null;
		StringBuilder sb = new StringBuilder(500);
		sb.append("select distinct ae ");
		sb.append("from AreaEffetti ae ");
		sb.append("inner join fetch ae.effetti effs ");
		sb.append("inner join fetch effs.pagamenti pags ");
		sb.append("inner join fetch pags.rata rata ");
		// sb.append("inner join fetch rata.areaRate ar ");
		// sb.append("inner join fetch ar.documento doc ");
		// sb.append("inner join fetch doc.entita ent ");
		// sb.append("inner join fetch ent.anagrafica anag ");
		// sb.append("inner join fetch anag.sedeAnagrafica sa ");
		sb.append("where ae.id = :paramIdAreaEffetti ");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramIdAreaEffetti", idAreaTesoreria);
		try {
			result = (AreaEffetti) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.error("--> area effetti non trovata id " + idAreaTesoreria);
			throw e;
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dell'area effetti", e);
			throw new RuntimeException("Errore durante il caricamento dell'area effetti", e);
		}

		logger.debug("--> Exit caricaAreaTesoreria");
		return result;
	}

	@Override
	public Long caricaNumeroAreeCollegate(AreaEffetti areaEffetti) {
		logger.debug("--> Enter caricaNumeroAreeCollegate");

		Long numeroAree = new Long(0);

		Query query = panjeaDAO.prepareNamedQuery("Effetto.caricaNumeroAreeCollegate");
		query.setParameter("paramIdAreaEffetti", areaEffetti.getId());

		try {
			numeroAree = (Long) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento delle aree collegate", e);
			throw new RuntimeException("Errore durante il caricamento delle aree collegate", e);
		}

		logger.debug("--> Exit caricaNumeroAreeCollegate");
		return numeroAree;
	}

	@Override
	public AreaEffetti creaAreaEffetti(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			List<Pagamento> pagamenti) throws DocumentoDuplicateException {
		logger.debug("--> Enter creaAreaEffetti");

		AreaEffetti areaEffetti = new AreaEffetti();

		Map<EntitaLite, List<Pagamento>> pagamentiRaggruppatiPerEntita = raggruppaPagamentiPerEntita(
				parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoDocumento().getTipoEntita(), pagamenti);
		areaEffetti = generaAreaEffetti(parametriCreazioneAreaChiusure, pagamentiRaggruppatiPerEntita);

		// genero l'area contabile dall'area effetti
		areaEffettiContabilitaManager.creaAreaContabileEffetti(areaEffetti, parametriCreazioneAreaChiusure);

		logger.debug("--> Exit creaAreaEffetti");
		return areaEffetti;
	}

	/**
	 * Genera l' {@link AreaEffetti}.
	 * 
	 * @param parametriCreazioneAreaChiusure
	 *            parametri di creazione
	 * @param pagamentiRaggruppatiPerEntita
	 *            pagamenti
	 * @return area effetti creata
	 * @throws DocumentoDuplicateException
	 *             DocumentoDuplicateException
	 */
	private AreaEffetti generaAreaEffetti(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			Map<EntitaLite, List<Pagamento>> pagamentiRaggruppatiPerEntita) throws DocumentoDuplicateException {
		logger.debug("--> Enter generaAreaEffetti");
		// documento di pagamento e' sempre relativo ad un rapporto bancario
		if (parametriCreazioneAreaChiusure.getRapportoBancarioAzienda() == null
				|| (parametriCreazioneAreaChiusure.getRapportoBancarioAzienda() != null && parametriCreazioneAreaChiusure
						.getRapportoBancarioAzienda().isNew())) {
			throw new IllegalArgumentException("rapportoBancarioAzienda cannot be null!");
		}

		// CALCOLO IL TOTALE DEL DOCUMENTO
		Importo totaleDocumento = new Importo();
		// Estraggo il primo pagamento per la prima entità per utilizzare i dati di codiceValuta e tassoDi cambio che
		// sono ugulali per tutti i pagamenti
		Pagamento pagamentoRiferimento = pagamentiRaggruppatiPerEntita.values().iterator().next().get(0);
		totaleDocumento.setCodiceValuta(pagamentoRiferimento.getImporto().getCodiceValuta());
		totaleDocumento.setTassoDiCambio(pagamentoRiferimento.getImporto().getTassoDiCambio());

		for (List<Pagamento> arrays : pagamentiRaggruppatiPerEntita.values()) {
			for (Pagamento pagamento : arrays) {
				// Sommo l'importo in valuta
				totaleDocumento.setImportoInValuta(totaleDocumento.getImportoInValuta().add(
						pagamento.getImporto().getImportoInValuta()));
				// All'importo sommo anche l'importo forzato
				totaleDocumento.setImportoInValuta(totaleDocumento.getImportoInValuta().add(
						pagamento.getImportoForzato().getImportoInValuta()));
				// Sommo l'importo in valuta azienda
				totaleDocumento.setImportoInValutaAzienda(totaleDocumento.getImportoInValutaAzienda().add(
						pagamento.getImporto().getImportoInValutaAzienda()));
				// All'importo sommo anche l'importo in valuta forzato
				totaleDocumento.setImportoInValutaAzienda(totaleDocumento.getImportoInValutaAzienda().add(
						pagamento.getImportoForzato().getImportoInValutaAzienda()));
			}
		}

		// documento
		Documento doc = new Documento();
		doc.setCodiceAzienda(getJecPrincipal().getCodiceAzienda());
		doc.setDataDocumento(parametriCreazioneAreaChiusure.getDataDocumento());
		doc.setTipoDocumento(parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoDocumento());
		doc.setEntita(null);
		doc.setSedeEntita(null);
		doc.setRapportoBancarioAzienda(parametriCreazioneAreaChiusure.getRapportoBancarioAzienda());
		doc.setTotale(totaleDocumento);
		doc.setContrattoSpesometro(null);
		Documento docSalvato;
		docSalvato = documentiManager.salvaDocumento(doc);

		// area effetti
		AreaEffetti areaEffetti = new AreaEffetti();
		areaEffetti.setDocumento(docSalvato);
		areaEffetti.setTipoAreaPartita(parametriCreazioneAreaChiusure.getTipoAreaPartita());
		areaEffetti.setCodicePagamento(null);
		areaEffetti.setSpeseIncasso(parametriCreazioneAreaChiusure.getSpeseIncasso());
		AreaEffetti areaEffettiSalvata;
		areaEffettiSalvata = (AreaEffetti) areaTesoreriaManager.salvaAreaTesoreria(areaEffetti);
		List<Effetto> listaEffetti = new ArrayList<Effetto>();

		for (EntitaLite entita : pagamentiRaggruppatiPerEntita.keySet()) {
			// per ogni entita....
			List<Pagamento> pags = pagamentiRaggruppatiPerEntita.get(entita);

			if (parametriCreazioneAreaChiusure.isGeneraSingoloEffetto()) {
				// Genere un solo effetto per tutti i pagamenti
				Effetto effetto = new Effetto();
				effetto.setDataScadenza(parametriCreazioneAreaChiusure.getDataEffetto());
				effetto.setAreaEffetti(areaEffettiSalvata);
				Effetto effettoSalvato = salvaEffetto(effetto);
				listaEffetti.add(effettoSalvato);

				for (Pagamento pagamento : pags) {
					pagamento.setAreaChiusure(areaEffettiSalvata);
					pagamento.setEffetto(effettoSalvato);
					areaPagamentiManager.salvaPagamento(pagamento);
				}
			} else {
				// raggruppo i pagamenti per data scadenza della rata
				Map<Date, List<Pagamento>> pagsRaggruppatiPerData = raggruppaPerData(pags);
				for (List<Pagamento> pagamentiMapValues : pagsRaggruppatiPerData.values()) {
					// per ogni data.....
					if (verificaRagguppamentoCliente(entita)) {
						// raggruppo in un effetto i pagamenti
						Effetto effetto = new Effetto();
						effetto.setDataScadenza(pagamentiMapValues.get(0).getRata().getDataScadenza());
						effetto.setAreaEffetti(areaEffettiSalvata);
						Effetto effettoSalvato = salvaEffetto(effetto);
						listaEffetti.add(effettoSalvato);

						for (Pagamento pagamento : pagamentiMapValues) {
							pagamento.setAreaChiusure(areaEffettiSalvata);
							pagamento.setEffetto(effettoSalvato);
							areaPagamentiManager.salvaPagamento(pagamento);
						}
					} else {
						// per ogni pagamento un effetto
						for (Pagamento pagamento : pagamentiMapValues) {
							// salvo prima l'effetto perche' il pagamento ha
							// l'effetto_id ed e' quindi quello che conta
							// salvare per avere la relazione pagamento-effetto
							Effetto effetto = new Effetto();
							effetto.setDataScadenza(pagamentiMapValues.get(0).getRata().getDataScadenza());
							effetto.setAreaEffetti(areaEffettiSalvata);
							Effetto effettoSalvato = salvaEffetto(effetto);

							pagamento.setEffetto(effettoSalvato);
							pagamento.setAreaChiusure(areaEffettiSalvata);
							Pagamento pagamentoSalvato = areaPagamentiManager.salvaPagamento(pagamento);
							if (logger.isDebugEnabled() && pagamentoSalvato.getEffetto() != null) {
								logger.debug("--> associato l'effetto " + pagamentoSalvato.getEffetto().getId()
										+ " al pagamento " + pagamentoSalvato.getId());
							}
							listaEffetti.add(effettoSalvato);
						}
					}
				}
			}
		}
		logger.debug("--> Exit creaAreaPartitaIncassiConDistinta");
		return areaEffettiSalvata;
	}

	@Override
	public List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria) {
		logger.debug("--> Enter getAreeEmissioneEffetti");

		ArrayList<AreaEffetti> list = new ArrayList<AreaEffetti>();
		// per il momento se mi arriva un area effetti sono sicuro che è l'area emissione effetti
		list.add((AreaEffetti) areaTesoreria);

		logger.debug("--> Exit getAreeEmissioneEffetti");
		return list;
	}

	/**
	 * recupera {@link JecPrincipal} dal {@link SessionContext}.
	 * 
	 * @return utente loggato
	 */
	private JecPrincipal getJecPrincipal() {
		logger.debug("--> Enter getJecPrincipal");
		return (JecPrincipal) context.getCallerPrincipal();
	}

	/**
	 * Metodo di utilita' che serve a raggruppare i pagamenti per ogni entita.
	 * 
	 * @param pagamenti
	 *            pagamenti da raggruppare
	 * @param tipoEntita
	 *            tipo entità
	 * @return pagamenti raggruppati
	 */
	private Map<EntitaLite, List<Pagamento>> raggruppaPagamentiPerEntita(TipoEntita tipoEntita,
			List<Pagamento> pagamenti) {
		logger.debug("--> Enter raggruppaPagamenti");
		Map<EntitaLite, List<Pagamento>> raggruppamenti = new HashMap<EntitaLite, List<Pagamento>>();
		for (Pagamento pagamento : pagamenti) {
			if (pagamento.getRata().getAreaRate().getDocumento().getTipoDocumento().getTipoEntita()
					.equals(TipoEntita.CLIENTE)
					|| pagamento.getRata().getAreaRate().getDocumento().getTipoDocumento().getTipoEntita()
							.equals(TipoEntita.FORNITORE)) {
				// Cliente o Fornitore
				EntitaLite entita = pagamento.getRata().getAreaRate().getDocumento().getEntita();
				List<Pagamento> pagamentiTmp = new ArrayList<Pagamento>();
				if (raggruppamenti.containsKey(entita)) {
					pagamentiTmp = raggruppamenti.get(entita);
				}
				pagamentiTmp.add(pagamento);
				raggruppamenti.put(entita, pagamentiTmp);
			}
		}
		logger.debug("--> Exit raggruppaPagamenti");
		return raggruppamenti;
	}

	/**
	 * Raggruppa i pagamenti per data scadenza rata.
	 * 
	 * @param pags
	 *            pagamenti da raggruppare
	 * @return mappa dei pagamenti raggruppati
	 */

	private Map<Date, List<Pagamento>> raggruppaPerData(List<Pagamento> pags) {
		Map<Date, List<Pagamento>> raggruppa = new HashMap<Date, List<Pagamento>>();
		for (Pagamento pagamento : pags) {
			List<Pagamento> pagamenti = new ArrayList<Pagamento>();
			if (raggruppa.containsKey(pagamento.getRata().getDataScadenza())) {
				pagamenti = raggruppa.get(pagamento.getRata().getDataScadenza());
			}
			pagamenti.add(pagamento);
			raggruppa.put(pagamento.getRata().getDataScadenza(), pagamenti);
		}
		return raggruppa;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SituazioneEffetto> ricercaEffetti(ParametriRicercaEffetti parametriRicercaEffetti) {
		logger.debug("--> Enter ricercaEffetti");
		List<SituazioneEffetto> effetti = new ArrayList<SituazioneEffetto>();

		String sqlQuery = RicercaEffettiQueryBuilder.getSQLRicercaEffetti(getJecPrincipal().getCodiceAzienda(),
				parametriRicercaEffetti.getNumeroDocumento(), parametriRicercaEffetti.getRapportoBancarioAzienda(),
				parametriRicercaEffetti.getDaDataValuta(), parametriRicercaEffetti.getADataValuta(),
				parametriRicercaEffetti.getEntita(), parametriRicercaEffetti.getStatiEffetto(),
				parametriRicercaEffetti.getDaImporto(), parametriRicercaEffetti.getAImporto(),
				parametriRicercaEffetti.isEscludiEffettiAccreditati());

		javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sqlQuery);
		((QueryImpl) query).getHibernateQuery().setResultTransformer(
				Transformers.aliasToBean((SituazioneEffetto.class)));

		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("idEffetto");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("versionEffetto");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("dataValuta");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("idRapportoBancario");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("descrizioneRapportoBancario");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("speseInsolutoRapportoBancario");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("codiceTipoDocumento");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("descrizioneTipoDocumento");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("tipoEntitaTipoDocumento");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("idDocumento");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("numeroDocumento");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("dataDocumento");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("codiceTipoDocumentoDistinta");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("descrizioneTipoDocumentoDistinta");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("tipoEntitaTipoDocumentoDistinta");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("tipoEntitaTipoDocumento");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("idDocumentoDistinta");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("numeroDocumentoDistinta");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("dataDocumentoDistinta");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("idEntitaRata");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("codiceEntitaRata");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("denominazioneEntitaRata");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("tipoEntitaRata");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("importo");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("codiceValuta");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("dataPagamento");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("insoluto");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("dataScadenza");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("idAreaEffetto");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("versionAreaEffetto");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("versionRapportoBancario");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("idAreaDistinta");
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("versionAreaDistinta");

		try {
			effetti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento degli effetti.", e);
			throw new RuntimeException("Errore durante il caricamento degli effetti.", e);
		}

		logger.debug("--> Exit caricaListaEffettiPerAccredito con num effetti" + effetti.size());
		return effetti;
	}

	@Override
	public Effetto salvaEffetto(Effetto effetto) {
		logger.debug("--> Enter salvaEffetto");
		Effetto effettoSalvato;
		try {
			// Assegno la data scadenza
			effettoSalvato = panjeaDAO.save(effetto);
		} catch (Exception e) {
			logger.error("--> Errore in salvaEffetto", e);
			throw new RuntimeException(e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit salvaEffetto " + effettoSalvato.getId());
		}
		return effettoSalvato;
	}

	/**
	 * 
	 * @param entita
	 *            entita del documento
	 * @return true se devo raggruppare le rate con la stessa scandenza in un unico effetto
	 */
	private boolean verificaRagguppamentoCliente(EntitaLite entita) {
		return entita.isRaggruppaEffetti();
	}

}
