package it.eurotn.panjea.contabilita.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.contabilita.domain.NoteAreaContabile;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaAnagraficaManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

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
 * Manager per classi anagrafiche delle contabilita roles: visualizzaRegistriIva, cancellaRegistriIva,
 * modificaRegistriIva
 *
 * @author adriano
 * @version 1.0, 31/ago/07
 */
@Stateless(name = "Panjea.ContabilitaAnagraficaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ContabilitaAnagraficaManager")
public class ContabilitaAnagraficaManagerBean implements ContabilitaAnagraficaManager {

	private static Logger logger = Logger.getLogger(ContabilitaAnagraficaManagerBean.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@Override
	public void cancellaRegistroIva(RegistroIva registroIva) {
		logger.debug("--> Enter cancellaRegistroIva");
		try {
			panjeaDAO.delete(registroIva);
		} catch (Exception e) {
			logger.error("--> errore cancellaRegistroIva", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaRegistroIva");

	}

	@Override
	public NoteAreaContabile caricaNoteSede(SedeEntita sedeEntita) {
		NoteAreaContabile noteAreaContabile = new NoteAreaContabile();

		// le note entità e le note sede non sono ereditate, quindi carico
		// direttamente dalla sedeEntità.
		sedeEntita = panjeaDAO.loadLazy(SedeEntita.class, sedeEntita.getId());
		noteAreaContabile.setNoteSede(sedeEntita.getNote());
		noteAreaContabile.setNoteEntita(sedeEntita.getEntita().getNoteContabilita());
		return noteAreaContabile;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroIva> caricaRegistriIva(String fieldSearch, String valueSearch) throws ContabilitaException {
		logger.debug("--> Enter caricaRegistriIva");
		StringBuilder sb = new StringBuilder("from RegistroIva ri where ri.codiceAzienda = :paramCodiceAzienda");
		if (valueSearch != null) {
			sb.append(" and ri.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		sb.append(" order by ").append(fieldSearch);
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramCodiceAzienda", getAzienda());
		List<RegistroIva> registriIva = null;
		try {
			registriIva = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore in ricerca registri Iva", e);
			throw new ContabilitaException("ricerca registri Iva ", e);
		}
		logger.debug("--> Exit caricaRegistriIva");
		return registriIva;
	}

	@Override
	public RegistroIva caricaRegistroIva(Integer id) throws ContabilitaException {
		logger.debug("--> Enter caricaRegistroIva");
		try {
			RegistroIva registroIva = panjeaDAO.load(RegistroIva.class, id);
			logger.debug("--> Exit caricaRegistroIva");
			return registroIva;
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore caricaRegistroIva", e);
			throw new ContabilitaException("Impossibile restituire RegistroIva identificata da " + id);
		}
	}

	@Override
	public RegistroIva caricaRegistroIva(Integer numero, TipoRegistro tipoRegistro) throws ObjectNotFoundException {
		logger.debug("--> Enter caricaRegistriIva");
		Query query = panjeaDAO.prepareNamedQuery("RegistroIva.caricaByNumeroETipoRegistro");
		query.setParameter("paramCodiceAzienda", getAzienda());
		query.setParameter("paramTipoRegistro", tipoRegistro);
		query.setParameter("paramNumero", numero);

		RegistroIva registroIva = null;
		try {
			registroIva = (RegistroIva) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			// devo catturare e rilanciare la objectnotfound dato che essa estende da DAOException
			throw e;
		} catch (DAOException e) {
			logger.error("--> Errore nella ricerca registro Iva", e);
			throw new RuntimeException("--> Errore nella ricerca registro Iva", e);
		}
		logger.debug("--> Exit caricaRegistriIva");
		return registroIva;
	}

	/**
	 * @return azienda associata all'utente loggato
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@Override
	public RegistroIva salvaRegistroIva(RegistroIva registroIva) {
		logger.debug("--> Exit salvaRegistroIva");
		if (registroIva.isNew()) {
			registroIva.setCodiceAzienda(getAzienda());
		}
		try {
			RegistroIva registroIvaSalvato = panjeaDAO.save(registroIva);
			logger.debug("--> Exit salvaRegistroIva");
			return registroIvaSalvato;
		} catch (Exception e) {
			logger.error("--> errore impossibile eseguire il salvataggio di RegistroIva", e);
			throw new RuntimeException(e);
		}
	}

}
