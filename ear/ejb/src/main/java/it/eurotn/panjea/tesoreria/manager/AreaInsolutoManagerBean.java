package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaInsoluti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaInsolutoContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaInsolutoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AreaInsolutoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaInsolutoManager")
public class AreaInsolutoManagerBean implements AreaInsolutoManager {

	private static Logger logger = Logger.getLogger(AreaInsolutoManagerBean.class.getName());

	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext context;

	@EJB
	@IgnoreDependency
	protected TipiAreaPartitaManager tipiAreaPartitaManager;

	@EJB
	@IgnoreDependency
	protected DocumentiManager documentiManager;

	@EJB
	protected AreaInsolutoContabilitaManager areaInsolutoContabilitaManager;

	@EJB
	@IgnoreDependency
	protected AreaTesoreriaManager areaTesoreriaManager;

	@EJB
	protected AreaContabileCancellaManager areaContabileCancellaManager;

	@EJB
	protected AreaContabileManager areaContabileManager;

	@Override
	public void annullaInsoluto(Effetto effetto) {
		logger.debug("--> Enter annullaInsoluto");

		logger.debug("--> Exit annullaInsoluto");

	}

	@Override
	public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate) {
		logger.debug("--> Enter cancellaAreaTesoreria");

		try {
			if (deleteAreeCollegate) {
				// cancello l'area contabile se esiste
				AreaContabileLite areaContabileLite = areaContabileManager
						.caricaAreaContabileLiteByDocumento(areaTesoreria.getDocumento());
				if (areaContabileLite != null) {
					areaContabileCancellaManager.cancellaAreaContabile(areaTesoreria.getDocumento(), true);
				}
			}

			AreaInsoluti areaInsoluti = (AreaInsoluti) caricaAreaTesoreria(areaTesoreria.getId());
			for (Effetto effetto : areaInsoluti.getEffetti()) {
				effetto.setAreaInsoluti(null);
				effetto.setSpeseInsoluto(BigDecimal.ZERO);
				panjeaDAO.saveWithoutFlush(effetto);

				for (Pagamento pagamento : effetto.getPagamenti()) {
					pagamento.setInsoluto(false);

					Rata rata = pagamento.getRata();
					rata.setDataScadenza(effetto.getDataScadenza());

					panjeaDAO.saveWithoutFlush(pagamento);
					panjeaDAO.saveWithoutFlush(rata);
				}
			}

			panjeaDAO.delete(areaTesoreria);

			if (deleteAreeCollegate) {
				// cancello il documento
				documentiManager.cancellaDocumento(areaTesoreria.getDocumento());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit cancellaAreaTesoreria");
	}

	@Override
	public AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException {
		logger.debug("--> Enter caricaAreaTesoreria");

		AreaInsoluti result = null;
		Query query = panjeaDAO.prepareNamedQuery("AreaInsoluti.carica");
		query.setParameter("paramIdAreaInsoluti", idAreaTesoreria);
		try {
			result = (AreaInsoluti) panjeaDAO.getSingleResult(query);
			result.getEffetti();
		} catch (ObjectNotFoundException e) {
			logger.error("--> area insoluti non trovata id " + idAreaTesoreria);
			throw e;
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dell'area insoluti", e);
			throw new RuntimeException("Errore durante il caricamento dell'area insoluti", e);
		}

		logger.debug("--> Exit caricaAreaTesoreria");
		return result;
	}

	@Override
	public AreaInsoluti creaAreaInsoluti(Date dataDocumento, String nDocumento, BigDecimal speseInsoluto,
			Set<Effetto> effetti) {
		logger.debug("--> Enter creaAreaInsoluti");

		// carico il tipo documento base
		TipoDocumentoBasePartite tipoDocumentoBase;
		try {
			tipoDocumentoBase = tipiAreaPartitaManager
					.caricaTipoDocumentoBase(TipoDocumentoBasePartite.TipoOperazione.INSOLUTO);
		} catch (Exception e1) {
			logger.error("--> Errore, tipo documento base per area accredito non trovato", e1);
			throw new RuntimeException("Errore, tipo documento base per area accredito non trovato", e1);
		}

		Set<Effetto> effettiLoad = new TreeSet<Effetto>();
		Importo totaleDoc = null;
		for (Effetto effetto : effetti) {
			try {
				effetto = panjeaDAO.load(Effetto.class, effetto.getId());
			} catch (Exception e1) {
				logger.error("-->errore durante il caricamento dell'effetto", e1);
				throw new RuntimeException("errore durante il caricamento dell'effetto", e1);
			}

			if (totaleDoc == null) {
				totaleDoc = effetto.getImporto().clone();
			} else {
				totaleDoc = totaleDoc.add(effetto.getImporto(), 2);
			}

			for (Pagamento pagamento : effetto.getPagamenti()) {
				pagamento.setInsoluto(true);

				Rata rata = pagamento.getRata();
				rata.setDataScadenza(null);

				try {
					panjeaDAO.saveWithoutFlush(pagamento);
					panjeaDAO.saveWithoutFlush(rata);
				} catch (DAOException e) {
					logger.error("--> errore durante il salvataggio di pagamento o rata", e);
					throw new RuntimeException("errore durante il salvataggio di pagamento o rata", e);
				}
			}

			effettiLoad.add(effetto);
		}
		// aggiungo le spese di incasso
		if (speseInsoluto != null && speseInsoluto.compareTo(BigDecimal.ZERO) != 0) {
			BigDecimal totale = totaleDoc.getImportoInValutaAzienda();
			totale = totale.add(speseInsoluto);
			totaleDoc.setImportoInValuta(totale);
			totaleDoc.calcolaImportoValutaAzienda(2);
		}

		Documento doc = new Documento();
		doc.setCodiceAzienda(getJecPrincipal().getCodiceAzienda());
		doc.setDataDocumento(dataDocumento);
		doc.getCodice().setCodice(nDocumento);
		doc.setTipoDocumento(tipoDocumentoBase.getTipoAreaPartita().getTipoDocumento());
		doc.setEntita(null);
		doc.setRapportoBancarioAzienda(effetti.iterator().next().getAreaEffetti().getDocumento()
				.getRapportoBancarioAzienda());
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
		AreaInsoluti areaInsoluti = new AreaInsoluti();
		areaInsoluti.setDocumento(docSalvato);
		areaInsoluti.setTipoAreaPartita(tipoDocumentoBase.getTipoAreaPartita());
		areaInsoluti.setCodicePagamento(null);
		BigDecimal spesaEffetto = BigDecimal.ZERO;
		if (speseInsoluto != null && speseInsoluto.compareTo(BigDecimal.ZERO) != 0) {
			areaInsoluti.setSpeseIncasso(speseInsoluto);
			spesaEffetto = speseInsoluto.divide(new BigDecimal(effettiLoad.size()));
		}
		areaInsoluti = (AreaInsoluti) areaTesoreriaManager.salvaAreaTesoreria(areaInsoluti);

		areaInsolutoContabilitaManager.creaAreaContabileInsoluto(areaInsoluti, effettiLoad);

		for (Effetto effettoArea : effettiLoad) {
			effettoArea.setAreaInsoluti(areaInsoluti);
			effettoArea.setSpeseInsoluto(spesaEffetto);
			try {
				panjeaDAO.saveWithoutFlush(effettoArea);
			} catch (Exception e) {
				logger.error("-->errore durante il salvataggio dell'effetto per l'area insoluti", e);
				throw new RuntimeException("errore durante il salvataggio dell'effetto per l'area insoluti", e);
			}
		}
		areaInsoluti.setEffetti(effettiLoad);
		logger.debug("--> Exit creaAreaInsoluti");
		return areaInsoluti;
	}

	@Override
	public List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria) {
		logger.debug("--> Enter getAreeEmissioneEffetti");

		AreaInsoluti areaInsoluti = null;
		try {
			areaInsoluti = panjeaDAO.load(AreaInsoluti.class, areaTesoreria.getId());
		} catch (Exception e) {
			logger.error("-->errore durante il caricamento dell'area insoluto con id = " + areaTesoreria.getId(), e);
			throw new RuntimeException("errore durante il caricamento dell'area insoluto con id = "
					+ areaTesoreria.getId(), e);
		}

		Set<AreaEffetti> aree = new java.util.TreeSet<AreaEffetti>();
		for (Effetto effetto : areaInsoluti.getEffetti()) {
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
