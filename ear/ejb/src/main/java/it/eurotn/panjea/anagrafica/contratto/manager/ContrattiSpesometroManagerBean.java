/**
 * 
 */
package it.eurotn.panjea.anagrafica.contratto.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.contratto.manager.interfaces.ContrattiSpesometroManager;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.util.ParametriRicercaDocumento;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author leonardo
 * 
 */
@Stateless(name = "Panjea.ContrattiSpesometroManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ContrattiSpesometroManager")
public class ContrattiSpesometroManagerBean implements ContrattiSpesometroManager {

	private static Logger logger = Logger.getLogger(ContrattiSpesometroManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext context;

	@EJB
	protected DocumentiManager documentiManager;

	@Override
	public Documento aggiungiContrattoADocumento(ContrattoSpesometro contratto, Documento documento) {
		Documento documentoCaricato = caricaDocumento(documento);
		documentoCaricato.setContrattoSpesometro(contratto);
		Documento documentoSalvato = documentiManager.salvaDocumentoNoCheck(documentoCaricato);
		return documentoSalvato;
	}

	@Override
	public void cancellaContratto(ContrattoSpesometro contratto) {
		logger.debug("--> Enter cancellaContratto");
		try {
			panjeaDAO.delete(contratto);
		} catch (Exception e) {
			logger.error("--> Errore nel cancellare il contratto " + contratto);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaContratto");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContrattoSpesometro> caricaContratti(EntitaLite entita) {
		logger.debug("--> Enter caricaContratti");
		List<ContrattoSpesometro> list = new ArrayList<ContrattoSpesometro>();
		Query query = null;
		// carica tutti i contratti o i contratti dell'entita' scelta
		if (entita == null) {
			query = panjeaDAO.prepareNamedQuery("ContrattoSpesometro.caricaContratti");
		} else {
			query = panjeaDAO.prepareNamedQuery("ContrattoSpesometro.caricaContrattiEntita");
			query.setParameter("paramIdEntita", entita.getId());
		}
		query.setParameter("paramCodiceAzienda", getAzienda());

		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento dei contratti per spesometro", e);
			throw new RuntimeException("Errore durante il caricamento dei contratti per spesometro", e);
		}
		logger.debug("--> Exit caricaContratti");
		return list;
	}

	@Override
	public ContrattoSpesometro caricaContratto(Integer idContratto) {
		return caricaContratto(idContratto, false);
	}

	@Override
	public ContrattoSpesometro caricaContratto(Integer idContratto, boolean loadCollection) {
		logger.debug("--> Enter caricaContratto");
		ContrattoSpesometro contrattoCaricato = null;
		try {
			contrattoCaricato = panjeaDAO.load(ContrattoSpesometro.class, idContratto);
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore ObjectNotFoundException in caricaContratto", e);
			throw new RuntimeException(e);
		}
		if (loadCollection) {
			// inizializza le collection lazy
			contrattoCaricato.getDocumenti().size();
		}
		logger.debug("--> Exit caricaContratto");
		return contrattoCaricato;
	}

	@Override
	public List<Documento> caricaDocumentiContratto(Integer idContratto) {
		logger.debug("--> Enter caricaDocumentiContratto");

		ParametriRicercaDocumento parametriRicercaDocumento = new ParametriRicercaDocumento();
		parametriRicercaDocumento.setIdContratto(idContratto);
		List<Documento> documenti = documentiManager.ricercaDocumenti(parametriRicercaDocumento);

		logger.debug("--> Exit caricaDocumentiContratto " + documenti.size());
		return documenti;
	}

	/**
	 * Carica il documento dall'id.
	 * 
	 * @param documento
	 *            il documento da caricare
	 * @return Documento
	 */
	private Documento caricaDocumento(Documento documento) {
		Documento documentoCaricato = documentiManager.caricaDocumento(documento.getId());
		return documentoCaricato;
	}

	/**
	 * ritorna la descrizione della azienda corrente.
	 * 
	 * @return descrizione della azienda corrente
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@Override
	public void rimuovContrattoDaDocumento(Documento documento) {
		Documento documentoCaricato = caricaDocumento(documento);
		documentoCaricato.setContrattoSpesometro(null);
		documentiManager.salvaDocumentoNoCheck(documentoCaricato);
	}

	@Override
	public ContrattoSpesometro salvaContratto(ContrattoSpesometro contratto) {
		logger.debug("--> Enter salvaContratto");

		// se si sta salvando una nuova unità di misura setto l'azienda
		if (contratto.isNew()) {
			contratto.setCodiceAzienda(getAzienda());
		}

		try {
			contratto = panjeaDAO.save(contratto);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del contratto " + contratto, e);
			throw new RuntimeException("Errore durante il salvataggio del contratto " + contratto, e);
		}

		logger.debug("--> Exit salvaContratto");
		return contratto;
	}

}
