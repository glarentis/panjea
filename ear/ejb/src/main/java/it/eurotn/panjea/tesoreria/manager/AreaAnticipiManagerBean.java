package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaAnticipo;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.RigaAnticipo;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAnticipiManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAnticipoContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaDistintaBancariaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.panjea.tesoreria.util.SituazioneRigaAnticipo;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AreaAnticipiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaAnticipiManager")
public class AreaAnticipiManagerBean implements AreaAnticipiManager {

	private static Logger logger = Logger.getLogger(AreaAnticipiManagerBean.class.getName());

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
	@IgnoreDependency
	protected AreaTesoreriaManager areaTesoreriaManager;

	@EJB
	protected AreaAnticipoContabilitaManager areaAnticipoContabilitaManager;

	@EJB
	@IgnoreDependency
	protected AreaDistintaBancariaManager areaDistintaBancariaManager;

	@Override
	public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate) {
		logger.debug("--> Enter cancellaAreaTesoreria");

		try {
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

		AreaAnticipo result = null;
		Query query = panjeaDAO.prepareNamedQuery("AreaAnticipo.carica");
		query.setParameter("paramIdAreaAnticipo", idAreaTesoreria);
		try {
			result = (AreaAnticipo) panjeaDAO.getSingleResult(query);
			result.getAnticipi();
		} catch (ObjectNotFoundException e) {
			logger.error("--> area anticipo non trovata id " + idAreaTesoreria);
			throw e;
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dell'area anticipo", e);
			throw new RuntimeException("Errore durante il caricamento dell'area anticipo", e);
		}

		logger.debug("--> Exit caricaAreaTesoreria");
		return result;
	}

	@Override
	public BigDecimal caricaImportoAnticipato(AreaEffetti areaEffetti, RapportoBancarioAzienda rapportoBancarioAzienda,
			Date dataValuta) {
		logger.debug("--> Enter caricaImportoAnticipato");

		BigDecimal importoAnticipato = BigDecimal.ZERO;
		Query query = panjeaDAO.prepareNamedQuery("RigaAnticipo.caricaImportoAnticipi");
		query.setParameter("paramDataValuta", dataValuta);
		query.setParameter("paramRapportoBancario", rapportoBancarioAzienda);
		query.setParameter("paramAreaEffetti", areaEffetti);
		try {
			importoAnticipato = (BigDecimal) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dell'importo anticipato", e);
			throw new RuntimeException("Errore durante il caricamento dell'importo anticipato", e);
		}
		if (importoAnticipato == null) {
			importoAnticipato = BigDecimal.ZERO;
		}

		logger.debug("--> Exit caricaImportoAnticipato");
		return importoAnticipato;
	}

	@Override
	public AreaAnticipo creaAreaAnticipo(List<SituazioneRigaAnticipo> situazioneRigaAnticipo) {
		logger.debug("--> Enter creaAreaAnticipo");

		// carico il tipo documento base
		TipoDocumentoBasePartite tipoDocumentoBase;
		try {
			tipoDocumentoBase = tipiAreaPartitaManager
					.caricaTipoDocumentoBase(TipoDocumentoBasePartite.TipoOperazione.ANTICIPO);
		} catch (Exception e1) {
			logger.error("--> Errore, tipo documento base per area anticipo non trovato", e1);
			throw new RuntimeException("Errore, tipo documento base per area anticiponon trovato", e1);
		}

		Importo importoDoc = null;
		BigDecimal totDoc = BigDecimal.ZERO;
		Date dataDoc = null;
		for (SituazioneRigaAnticipo situazione : situazioneRigaAnticipo) {
			if (importoDoc == null) {
				dataDoc = situazione.getRigaAnticipo().getDataValuta();
				importoDoc = situazione.getRigaAnticipo().getAreaAnticipo().getDocumento().getTotale().clone();
			}
			totDoc = totDoc.add(situazione.getRigaAnticipo().getImportoAnticipato());
		}
		importoDoc.setImportoInValuta(totDoc);
		importoDoc.calcolaImportoValutaAzienda(2);

		Documento doc = new Documento();
		doc.setCodiceAzienda(getJecPrincipal().getCodiceAzienda());
		doc.setDataDocumento(dataDoc);
		doc.setTipoDocumento(tipoDocumentoBase.getTipoAreaPartita().getTipoDocumento());
		doc.setEntita(null);
		doc.setRapportoBancarioAzienda(situazioneRigaAnticipo.get(0).getRigaAnticipo().getAreaAnticipo().getDocumento()
				.getRapportoBancarioAzienda());
		doc.setTotale(importoDoc);
		doc.setContrattoSpesometro(null);
		Documento docSalvato;
		try {
			docSalvato = documentiManager.salvaDocumento(doc);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del documento.", e);
			throw new RuntimeException("Errore durante il salvataggio del documento.", e);
		}
		// area partite
		AreaAnticipo areaAnticipo = new AreaAnticipo();
		areaAnticipo.setDocumento(docSalvato);
		areaAnticipo.setTipoAreaPartita(tipoDocumentoBase.getTipoAreaPartita());
		areaAnticipo.setCodicePagamento(null);
		areaAnticipo = (AreaAnticipo) areaTesoreriaManager.salvaAreaTesoreria(areaAnticipo);

		for (SituazioneRigaAnticipo situazione : situazioneRigaAnticipo) {
			RigaAnticipo rigaAnticipoArea = situazione.getRigaAnticipo();
			rigaAnticipoArea.setAreaAnticipo(areaAnticipo);
			// rigaAnticipoArea.setAreaEffetti(situazione.getSituazioneEffetti().get(0).getAreaEffetto());
			try {
				panjeaDAO.saveWithoutFlush(rigaAnticipoArea);
			} catch (Exception e) {
				logger.error("-->errore durante il salvataggio della riga anticipo", e);
				throw new RuntimeException("errore durante il salvataggio della riga  anticipo", e);
			}
		}

		areaAnticipoContabilitaManager.creaAreaContabileAnticipo(areaAnticipo);

		logger.debug("--> Exit creaAreaAnticipo");
		return areaAnticipo;
	}

	@Override
	public List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria) {
		logger.debug("--> Enter getAreeEmissioneEffetti");

		AreaAnticipo areaAnticipo = null;
		try {
			areaAnticipo = panjeaDAO.load(AreaAnticipo.class, areaTesoreria.getId());
		} catch (Exception e) {
			logger.error("-->errore durante il caricamento dell'area anticipo con id " + areaTesoreria.getId(), e);
			throw new RuntimeException("errore durante il caricamento dell'area anticipo con id "
					+ areaTesoreria.getId(), e);
		}

		// carico le distinte di riferimento
		Set<AreaEffetti> areeDistinte = new java.util.TreeSet<AreaEffetti>();
		for (RigaAnticipo rigaAnticipo : areaAnticipo.getAnticipi()) {

			areeDistinte.add(rigaAnticipo.getAreaEffetti());
		}

		// per ogni distinta carico la usa area emissione effetti
		List<AreaEffetti> areeEmissioni = new ArrayList<AreaEffetti>();
		for (AreaEffetti areaEffetti : areeDistinte) {
			areeEmissioni.addAll(areaDistintaBancariaManager.getAreeEmissioneEffetti(areaEffetti));
		}

		logger.debug("--> Exit getAreeEmissioneEffetti");
		return areeEmissioni;
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
