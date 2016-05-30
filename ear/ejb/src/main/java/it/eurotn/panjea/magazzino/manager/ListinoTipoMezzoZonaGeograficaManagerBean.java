package it.eurotn.panjea.magazzino.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ListinoTipoMezzoZonaGeografica;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoTipoMezzoZonaGeograficaManager;
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
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.ListinoTipoMezzoZonaGeograficaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ListinoTipoMezzoZonaGeograficaManager")
public class ListinoTipoMezzoZonaGeograficaManagerBean implements ListinoTipoMezzoZonaGeograficaManager {

	private static Logger logger = Logger.getLogger(ListinoTipoMezzoZonaGeograficaManagerBean.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext sessionContext;

	@Override
	public void cancellaListinoTipoMezzoZonaGeografica(ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica) {
		logger.debug("--> Enter cancellaListinoTipoMezzoZonaGeografica");
		try {
			panjeaDAO.delete(listinoTipoMezzoZonaGeografica);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione del listino tipo mezzo zona geografica", e);
			throw new RuntimeException("Errore durante la cancellazione del listino tipo mezzo zona geografica", e);
		}
		logger.debug("--> Exit cancellaListinoTipoMezzoZonaGeografica");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ListinoTipoMezzoZonaGeografica> caricaListiniTipoMezzoZonaGeografica() {
		logger.debug("--> Enter caricaListiniTipoMezzoZonaGeografica");

		Query query = panjeaDAO.prepareNamedQuery("ListinoTipoMezzoZonaGeografica.caricaAll");
		query.setParameter("paramCodiceAzienda", getAzienda());

		List<ListinoTipoMezzoZonaGeografica> list = new ArrayList<ListinoTipoMezzoZonaGeografica>();

		try {
			list = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dei listini", e);
			throw new RuntimeException("Errore durante il caricamento dei listini", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit caricaListiniTipoMezzoZonaGeografica " + list.size());
		}
		return list;
	}

	@Override
	public ListinoTipoMezzoZonaGeografica caricaListinoTipoMezzoZonaGeografica(TipoMezzoTrasporto tipoMezzoTrasporto,
			ZonaGeografica zonaGeografica) {
		logger.debug("--> Enter caricaListinoTipoMezzoZonaGeografica");

		Query query = panjeaDAO.prepareNamedQuery("ListinoTipoMezzoZonaGeografica.caricaByTipoMezzoEZona");
		query.setParameter("paramCodiceAzienda", getAzienda());
		query.setParameter("paramTipoMezzoId", tipoMezzoTrasporto.getId());
		query.setParameter("paramZonaGeograficaId", zonaGeografica.getId());

		ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica = null;

		try {
			listinoTipoMezzoZonaGeografica = (ListinoTipoMezzoZonaGeografica) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> Nessun ListinoTipoMezzoZonaGeografica trovato, restituisco null");
			return null;
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento del ListinoTipoMezzoZonaGeografica", e);
			throw new RuntimeException("Errore durante il caricamento del ListinoTipoMezzoZonaGeografica", e);
		}

		logger.debug("--> Exit caricaListinoTipoMezzoZonaGeografica");
		return listinoTipoMezzoZonaGeografica;
	}

	/**
	 * @return Restituisce dal context il codice dell'azienda corrente
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

	@Override
	public ListinoTipoMezzoZonaGeografica salvaListinoTipoMezzoZonaGeografica(
			ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica) {
		logger.debug("--> Enter salvaListinoTipoMezzoZonaGeografica");

		if (listinoTipoMezzoZonaGeografica.getCodiceAzienda() == null) {
			listinoTipoMezzoZonaGeografica.setCodiceAzienda(getAzienda());
		}

		try {
			listinoTipoMezzoZonaGeografica = panjeaDAO.save(listinoTipoMezzoZonaGeografica);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del listino tipo mezzo zona geografica", e);
			throw new RuntimeException("Errore durante il salvataggio del listino tipo mezzo zona geografica", e);
		}

		logger.debug("--> Exit salvaListinoTipoMezzoZonaGeografica");
		return listinoTipoMezzoZonaGeografica;
	}

}
