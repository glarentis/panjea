package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaAccredito;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccreditoContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccreditoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.panjea.tesoreria.service.exception.DataRichiestaDopoIncassoException;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GroupingList;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AreaAccreditoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaAccreditoManager")
public class AreaAccreditoManagerBean implements AreaAccreditoManager {

	private static Logger logger = Logger.getLogger(AreaAccreditoManagerBean.class.getName());

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	@IgnoreDependency
	protected TipiAreaPartitaManager tipiAreaPartitaManager;

	@EJB
	@IgnoreDependency
	protected DocumentiManager documentiManager;

	@EJB
	@IgnoreDependency
	protected AreaTesoreriaManager areaTesoreriaManager;

	@EJB
	protected AreaAccreditoContabilitaManager areaAccreditoContabilitaManager;

	@EJB
	protected AreaContabileManager areaContabileManager;

	@EJB
	protected AreaContabileCancellaManager areaContabileCancellaManager;

	@Resource
	protected SessionContext context;

	@Override
	public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate) {
		logger.debug("--> Enter cancellaAreaTesoreria");

		try {
			AreaAccredito areaAccredito = panjeaDAO.load(AreaAccredito.class, areaTesoreria.getId());

			if (deleteAreeCollegate) {
				// cancello l'area contabile se esiste
				AreaContabileLite areaContabileLite = areaContabileManager
						.caricaAreaContabileLiteByDocumento(areaAccredito.getDocumento());
				if (areaContabileLite != null) {
					areaContabileCancellaManager.cancellaAreaContabile(areaAccredito.getDocumento(), true);
				}
			}

			// annullo la data valuta sugli effetti
			StringBuffer updatePagamentiSQL = new StringBuffer();
			updatePagamentiSQL
					.append("update part_pagamenti pag inner join part_effetti eff on (eff.id = pag.effetto_id) set pag.dataPagamento = null");
			updatePagamentiSQL.append(" where eff.areaAccredito_id = " + areaAccredito.getId());

			SQLQuery updatePagamentiQuery = ((Session) panjeaDAO.getEntityManager().getDelegate())
					.createSQLQuery(updatePagamentiSQL.toString());
			updatePagamentiQuery.executeUpdate();

			// libero gli effetti dall'area accredito
			for (Effetto effetto : areaAccredito.getEffetti()) {
				effetto.setAreaAccredito(null);
				panjeaDAO.saveWithoutFlush(effetto);
			}

			// cancello l'area distinta
			panjeaDAO.delete(areaAccredito);

			if (deleteAreeCollegate) {
				// cancello il documento
				documentiManager.cancellaDocumento(areaAccredito.getDocumento());
			}
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione dell'area distinta.", e);
			throw new RuntimeException("Errore durante la cancellazione dell'area distinta.", e);
		}

		logger.debug("--> Exit cancellaAreaTesoreria");
	}

	@Override
	public AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException {
		logger.debug("--> Enter caricaAreaTesoreria");

		AreaAccredito result = null;
		Query query = panjeaDAO.prepareNamedQuery("AreaAccredito.carica");
		query.setParameter("paramIdAreaAccredito", idAreaTesoreria);
		try {
			result = (AreaAccredito) panjeaDAO.getSingleResult(query);
			result.getEffetti();
		} catch (ObjectNotFoundException e) {
			logger.error("--> area accredito non trovata id " + idAreaTesoreria);
			throw e;
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dell'area accredito", e);
			throw new RuntimeException("Errore durante il caricamento dell'area accredito", e);
		}

		logger.debug("--> Exit caricaAreaTesoreria");
		return result;
	}

	/**
	 * Crea un documento di accredito per la lista di effetti.
	 * 
	 * @param listaGruppoEffetti
	 *            la lista di effetti che soddisfano le stesse condizioni (ad es stesso rapporto bancario e valuta o
	 *            effetti la cui area non prevede la parte contabile)
	 * @param tipoDocumentoBase
	 *            il TipoDocumentoBasePartite
	 * @param isDopoIncasso
	 *            definisce se l'area del guppo di effetti possiede una parte contabile o meno
	 * @param dataDopoIncasso
	 *            data scelta dall'utente per l'incasso
	 * @return AreaAccredito
	 */
	private AreaAccredito creaAccreditoPerGruppoEffetti(List<Effetto> listaGruppoEffetti,
			TipoDocumentoBasePartite tipoDocumentoBase, boolean isDopoIncasso, Date dataDopoIncasso) {
		Importo totaleDoc = null;
		Date dataDoc = null;
		for (Effetto effettoTmp : listaGruppoEffetti) {
			if (totaleDoc == null) {
				dataDoc = effettoTmp.getDataValuta();
				totaleDoc = effettoTmp.getImporto().clone();
			} else {
				totaleDoc = totaleDoc.add(effettoTmp.getImporto(), 2);
			}
		}
		// quando arrivo qui so già che l'utente ha impostato la data dopo incasso per il documento
		if (isDopoIncasso) {
			dataDoc = dataDopoIncasso;
		}

		RapportoBancarioAzienda rapportoBancarioAzienda = listaGruppoEffetti.get(0).getAreaEffetti().getDocumento()
				.getRapportoBancarioAzienda();

		// crea area accredito
		AreaAccredito areaAccredito = creaAreaAccredito(dataDoc, tipoDocumentoBase, rapportoBancarioAzienda, totaleDoc);

		for (Effetto effettoArea : listaGruppoEffetti) {
			effettoArea.setAreaAccredito(areaAccredito);
			try {
				panjeaDAO.saveWithoutFlush(effettoArea);
			} catch (Exception e) {
				logger.error("-->errore durante il salvataggio dell'effetto per l'area accredito", e);
				throw new RuntimeException("errore durante il salvataggio dell'effetto per l'area accredito", e);
			}
		}

		// Eseguo un flush della sessione per scrivere sul database le modifiche
		// altrimenti l'update della data di pagamento non funziona perchè è in join
		// con l'area Efffetti che vengono salvato con saveWithoutFlush
		panjeaDAO.getEntityManager().flush();
		// Imposto la data pagamento sui pagamenti
		StringBuffer updatePagamentiSQL = new StringBuffer();
		updatePagamentiSQL
				.append("update part_pagamenti inner join part_effetti eff on (eff.id = part_pagamenti.effetto_id) set part_pagamenti.dataPagamento = eff.dataValuta");
		updatePagamentiSQL.append(" where eff.areaAccredito_id = " + areaAccredito.getId());

		Query updatePagamentiQuery = panjeaDAO.getEntityManager().createNativeQuery(updatePagamentiSQL.toString());
		updatePagamentiQuery.executeUpdate();

		areaAccreditoContabilitaManager.creaAreaContabileAccredito(areaAccredito, listaGruppoEffetti, isDopoIncasso);

		return areaAccredito;
	}

	/**
	 * Crea l'area accredito secondo i parametri richiesti.
	 * 
	 * @param dataDocumento
	 *            la data del documento
	 * @param tipoDocumentoBase
	 *            il TipoDocumentoBasePartite
	 * @param rapportoBancarioAzienda
	 *            il rapportoBancarioAzienda
	 * @param totaleDoc
	 *            l'importo che è il totale documento
	 * @return AreaAccredito salvata
	 */
	private AreaAccredito creaAreaAccredito(Date dataDocumento, TipoDocumentoBasePartite tipoDocumentoBase,
			RapportoBancarioAzienda rapportoBancarioAzienda, Importo totaleDoc) {
		Documento doc = new Documento();
		doc.setCodiceAzienda(getJecPrincipal().getCodiceAzienda());
		doc.setDataDocumento(dataDocumento);
		doc.setTipoDocumento(tipoDocumentoBase.getTipoAreaPartita().getTipoDocumento());
		doc.setEntita(null);
		doc.setRapportoBancarioAzienda(rapportoBancarioAzienda);
		doc.setTotale(totaleDoc);
		doc.setContrattoSpesometro(null);
		Documento docSalvato;
		try {
			docSalvato = documentiManager.salvaDocumento(doc);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del documento.", e);
			throw new RuntimeException("Errore durante il salvataggio del documento.", e);
		}
		// area partite
		AreaAccredito areaAccredito = new AreaAccredito();
		areaAccredito.setDocumento(docSalvato);
		areaAccredito.setTipoAreaPartita(tipoDocumentoBase.getTipoAreaPartita());
		areaAccredito.setCodicePagamento(null);
		areaAccredito = (AreaAccredito) areaTesoreriaManager.salvaAreaTesoreria(areaAccredito);

		return areaAccredito;
	}

	@Override
	public List<AreaAccredito> creaAreeAccredito(List<Effetto> effetti, Date dataDopoIncasso)
			throws DataRichiestaDopoIncassoException {
		logger.debug("--> Enter creaAreeAcrredito");

		List<AreaAccredito> areeAccreditoCreate = new ArrayList<AreaAccredito>();

		// carico il tipo documento base
		TipoDocumentoBasePartite tipoDocumentoBase;
		try {
			tipoDocumentoBase = tipiAreaPartitaManager
					.caricaTipoDocumentoBase(TipoDocumentoBasePartite.TipoOperazione.VALUTA_PAGAMENTI);
		} catch (Exception e1) {
			logger.error("--> Errore, tipo documento base per area accredito non trovato", e1);
			throw new RuntimeException("Errore, tipo documento base per area accredito non trovato", e1);
		}

		List<Effetto> effettiLoad = new ArrayList<Effetto>();
		List<Effetto> effettiPostData = new ArrayList<Effetto>();
		for (Effetto effetto : effetti) {
			try {
				Effetto effettoLoaded = panjeaDAO.load(Effetto.class, effetto.getId());

				// verifico se dopo incasso, in tal caso l'utente deve impostare la data per il documento,
				// altrimenti sollevo una eccezione
				boolean isDopoIncasso = areaAccreditoContabilitaManager.isDopoIncasso(effettoLoaded);
				if (isDopoIncasso) {
					if (dataDopoIncasso == null) {
						throw new DataRichiestaDopoIncassoException();
					}
					effettiPostData.add(effettoLoaded);
				} else {
					effettiLoad.add(effettoLoaded);
				}
			} catch (DataRichiestaDopoIncassoException e) {
				logger.error("-->errore durante il caricamento dell'effetto", e);
				throw e;
			} catch (Exception e) {
				logger.error("-->errore durante il caricamento dell'effetto", e);
				throw new RuntimeException("errore durante il caricamento dell'effetto", e);
			}
		}

		// raggruppo per rapporto bancario e data valuta
		Comparator<Effetto> effettoComparator = new Comparator<Effetto>() {

			@Override
			public int compare(Effetto o1, Effetto o2) {

				RapportoBancarioAzienda rBancario1 = o1.getAreaEffetti().getDocumento().getRapportoBancarioAzienda();
				RapportoBancarioAzienda rBancario2 = o2.getAreaEffetti().getDocumento().getRapportoBancarioAzienda();
				if (rBancario1.compareTo(rBancario2) != 0) {
					return rBancario1.compareTo(rBancario2);
				} else {
					return o1.getDataValuta().compareTo(o2.getDataValuta());
				}
			}
		};
		EventList<Effetto> eventList = new BasicEventList<Effetto>();
		eventList.addAll(effettiLoad);
		GroupingList<Effetto> effettiRaggruppati = new GroupingList<Effetto>(eventList, effettoComparator);

		// creo gli accrediti per gli effetti in condizione standard
		for (List<Effetto> effettiTmp : effettiRaggruppati) {
			AreaAccredito accredito = creaAccreditoPerGruppoEffetti(effettiTmp, tipoDocumentoBase, false, null);
			areeAccreditoCreate.add(accredito);
		}

		// creo un documento unico per gli effetti per i quali non ho l'area contabile
		if (effettiPostData.size() > 0) {
			AreaAccredito accreditoPost = creaAccreditoPerGruppoEffetti(effettiPostData, tipoDocumentoBase, true,
					dataDopoIncasso);
			areeAccreditoCreate.add(accreditoPost);
		}

		return areeAccreditoCreate;
	}

	@Override
	public List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria) {
		logger.debug("--> Enter getAreeEmissioneEffetti");

		AreaAccredito areaAccredito = null;
		try {
			areaAccredito = panjeaDAO.load(AreaAccredito.class, areaTesoreria.getId());
		} catch (Exception e) {
			logger.error("-->errore durante il caricamento dell'area accredito con id = " + areaTesoreria.getId(), e);
			throw new RuntimeException("errore durante il caricamento dell'area accredito con id = "
					+ areaTesoreria.getId(), e);
		}

		Set<AreaEffetti> aree = new java.util.TreeSet<AreaEffetti>();
		for (Effetto effetto : areaAccredito.getEffetti()) {
			aree.add(effetto.getAreaEffetti());
		}

		List<AreaEffetti> lilstAree = new ArrayList<AreaEffetti>();
		lilstAree.addAll(aree);

		logger.debug("--> Exit getAreeEmissioneEffetti");
		return lilstAree;
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

}
