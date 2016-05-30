package it.eurotn.panjea.anagrafica.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.domain.Carica;
import it.eurotn.panjea.anagrafica.domain.FormaGiuridica;
import it.eurotn.panjea.anagrafica.domain.Mansione;
import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita.TipoSede;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficheTipologieManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.TipoSedeEntitaNonTrovataException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AnagraficheTipologieManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AnagraficheTipologieManager")
public class AnagraficheTipologieManagerBean implements AnagraficheTipologieManager {

	private static Logger logger = Logger.getLogger(AnagraficheTipologieManagerBean.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	protected PanjeaDAO panjeaDAO;

	@Override
	public void cancellaCarica(Carica carica) throws AnagraficaServiceException {
		logger.debug("--> Enter cancellaCarica");
		if (carica.isNew()) {
			logger.error("--> Impossibile eliminare la carica. ID nullo ");
			throw new AnagraficaServiceException("Impossibile eliminare la carica. ID nullo.");
		}
		try {
			panjeaDAO.delete(carica);
		} catch (VincoloException e) {
			logger.error("--> impossibile eliminare la carica con id " + carica.getId(), e);
			throw new RuntimeException(e);
		} catch (DAOException e) {
			logger.error("--> impossibile eliminare la carica con id " + carica.getId(), e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaCarica");

	}

	@Override
	public void cancellaFormaGiuridica(FormaGiuridica formaGiuridica) throws AnagraficaServiceException {
		logger.debug("--> Enter cancellaFormaGiuridica");
		if (formaGiuridica.isNew()) {
			logger.error("--> Impossibile eliminare una Forma Giuridica con id nullo.");
			throw new AnagraficaServiceException("Impossibile eliminare una Forma Giuridica con id nullo.");
		}
		try {
			panjeaDAO.delete(formaGiuridica);
			logger.debug("--> Eliminata la forma giuridica " + formaGiuridica.getId());
		} catch (DAOException e) {
			throw new RuntimeException();
		}

		logger.debug("--> Exit cancellaFormaGiuridica");
	}

	@Override
	public void cancellaMansione(Mansione mansione) throws AnagraficaServiceException {
		logger.debug("--> Enter cancellaMansione");

		if (mansione.isNew()) {
			logger.error("--> Impossibile cancellare la mansione. L'id � nullo.");
			throw new AnagraficaServiceException("Impossibile cancellare la mansione. L'id � nullo.");
		}

		try {
			panjeaDAO.delete(mansione);
			logger.debug("--> Cancellata la mansione " + mansione.getId());
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaMansione");

	}

	@Override
	public void cancellaTipoDeposito(TipoDeposito tipoDeposito) throws AnagraficaServiceException {
		if (tipoDeposito.isNew()) {
			logger.error("--> Impossibile eliminare il tipoDeposito. ID nullo.");
			throw new AnagraficaServiceException("Impossibile eliminare il tipoDeposito. ID nullo.");
		}

		try {
			panjeaDAO.delete(tipoDeposito);
			logger.debug("--> Eliminato il tipoDeposito " + tipoDeposito.getId());
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void cancellaTipoSedeEntita(TipoSedeEntita tipoSedeEntita) throws AnagraficaServiceException {
		if (tipoSedeEntita.isNew()) {
			logger.error("--> Impossibile eliminare il tipoSedeEntita. ID nullo.");
			throw new AnagraficaServiceException("Impossibile eliminare il tipoSedeEntita. ID nullo.");
		}

		try {
			panjeaDAO.delete(tipoSedeEntita);
			logger.debug("--> Eliminato il tipoSedeEntita " + tipoSedeEntita.getId());
		} catch (DAOException e) {
			logger.error("--> Impossibile eliminare il tipoSedeEntitaVO con id = " + tipoSedeEntita.getId(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Carica> caricaCariche(String fieldSearch, String valueSearch) throws AnagraficaServiceException {
		logger.debug("--> Enter caricaCariche");
		List<Carica> list = new ArrayList<Carica>();
		StringBuilder sb = new StringBuilder("select c from Carica c ");
		if (valueSearch != null) {
			sb.append(" where ").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		sb.append(" order by ");
		sb.append(fieldSearch);
		Query query = panjeaDAO.prepareQuery(sb.toString());
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile ottenere la lista di cariche ", e);
			throw new AnagraficaServiceException(e);
		}
		logger.debug("--> Exit caricaCariche");
		return list;
	}

	@Override
	public FormaGiuridica caricaFormaGiuridica(Integer idFormaGiuridica) throws AnagraficaServiceException,
			ObjectNotFoundException {
		logger.debug("--> Enter caricaFormaGiuridica");

		if (idFormaGiuridica == null) {
			logger.error("--> Impossibile caricare la forma giuridica, id nullo.");
			throw new AnagraficaServiceException("Impossibile caricare la forma giuridica, id nullo.");
		}

		FormaGiuridica formaGiuridica = null;
		try {
			formaGiuridica = panjeaDAO.load(FormaGiuridica.class, idFormaGiuridica);
		} catch (ObjectNotFoundException e) {
			logger.error("--> Errore durante il caricamento della forma giuridica. Id = " + idFormaGiuridica);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit caricaFormaGiuridica");
		return formaGiuridica;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FormaGiuridica> caricaFormeGiuridiche(String fieldSearch, String valueSearch)
			throws AnagraficaServiceException {
		logger.debug("--> Enter caricaFormeGiuridiche");
		List<FormaGiuridica> formeGiuridiche = null;

		StringBuilder sb = new StringBuilder("select fg from FormaGiuridica fg ");
		if (valueSearch != null) {
			sb.append(" where ").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		sb.append(" order by ");
		sb.append(fieldSearch);
		Query query = panjeaDAO.prepareQuery(sb.toString());

		try {
			formeGiuridiche = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore nel caricare le forme giuridiche", e);
			throw new AnagraficaServiceException("Errore nel caricare le forme giuridiche", e);
		}
		logger.debug("--> Exit caricaFormeGiuridiche");
		return formeGiuridiche;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Mansione> caricaMansioni(String descrizione)
			throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException {
		logger.debug("--> Enter caricaMansioni");
		List<Mansione> mansioni = new ArrayList<Mansione>();
		StringBuilder sb = new StringBuilder("select m from Mansione m ");
		if (descrizione != null) {
			sb.append(" where m.descrizione like ").append(PanjeaEJBUtil.addQuote(descrizione));
		}
		sb.append(" order by descrizione");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		try {
			mansioni = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile rcuperare la lista di mansioni ", e);
			throw new AnagraficaServiceException(e);
		}
		logger.debug("--> Exit caricaMansioni");
		return mansioni;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDeposito> caricaTipiDepositi() throws AnagraficaServiceException {
		logger.debug("--> Enter caricaTipiDepositi");
		Query query = panjeaDAO.prepareNamedQuery("TipoDeposito.caricaAll");
		List<TipoDeposito> list;
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore impossibile restituire la lista di TipiDepositi ", e);
			throw new AnagraficaServiceException(e);
		}
		logger.debug("--> Exit caricaTipiDepositi");
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoSedeEntita> caricaTipiSede(String codice) throws AnagraficaServiceException {
		logger.debug("--> Enter caricaTipiSede");
		if (codice == null) {
			codice = "%";
		}
		Query query = panjeaDAO.prepareNamedQuery("TipoSedeEntita.caricaTipiSedeByCodice");
		query.setParameter("codice", codice);
		List<TipoSedeEntita> list;
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore impossibile restituire la lista di TipoSede ", e);
			throw new AnagraficaServiceException(e);
		}
		logger.debug("--> Exit caricaTipiSede");
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoSedeEntita> caricaTipiSedeSecondari() throws AnagraficaServiceException {
		logger.debug("--> Enter caricaTipiSedeSecondari");
		Query query = panjeaDAO.prepareNamedQuery("TipoSedeEntita.caricaTipiSedeSecondarie");
		List<TipoSedeEntita> list;
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile restituire la lista di TipoSede secondarie ", e);
			throw new AnagraficaServiceException(e);
		}
		logger.debug("--> Exit caricaTipiSedeSecondari");
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TipoSedeEntita caricaTipoSedeEntitaByTipoSede(TipoSede tipoSede) throws TipoSedeEntitaNonTrovataException {
		logger.debug("--> Enter caricaTipoSedeEntitaPerSedeGenerica");
		Query query = panjeaDAO.prepareNamedQuery("TipoSedeEntita.caricaTipoSedeByTipoSede");
		query.setParameter("paramTipoSede", tipoSede);
		List<TipoSedeEntita> tipiSedeEntita;
		try {
			tipiSedeEntita = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore, impossibile recuperare TipoSedeEntita Generica", e);
			throw new RuntimeException(e);
		}
		if (tipiSedeEntita.size() == 0) {
			TipoSedeEntitaNonTrovataException tipoSedeEntitaNonTrovataException = new TipoSedeEntitaNonTrovataException();
			tipoSedeEntitaNonTrovataException.setTipoSede(tipoSede);
			throw tipoSedeEntitaNonTrovataException;
		}
		TipoSedeEntita tipoSedeEntita = tipiSedeEntita.get(0);
		logger.debug("--> Exit caricaTipoSedeEntitaPerSedeGenerica");
		return tipoSedeEntita;
	}

	@Override
	public TipoSedeEntita caricaTipoSedeEntitaPrincipale() throws AnagraficaServiceException {
		logger.debug("--> Enter caricaTipoSedeEntitaPrincipale");
		Query query = panjeaDAO.prepareNamedQuery("TipoSedeEntita.caricaTipoSedePrincipale");
		TipoSedeEntita tipoSedeEntita;
		try {
			tipoSedeEntita = (TipoSedeEntita) panjeaDAO.getSingleResult(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipoSedeEntitaPrincipale");
		return tipoSedeEntita;
	}

	@Override
	public Carica salvaCarica(Carica carica) {
		logger.debug("--> Enter salvaCarica");
		Carica caricaSave;
		try {
			caricaSave = panjeaDAO.save(carica);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaCarica");
		return caricaSave;

	}

	@Override
	public FormaGiuridica salvaFormaGiuridica(FormaGiuridica formaGiuridica) {
		logger.debug("--> Enter salvaFormaGiuridica");

		FormaGiuridica formaGiuridicaSave = null;
		try {
			formaGiuridicaSave = panjeaDAO.save(formaGiuridica);
		} catch (DAOException e1) {
			logger.error("--> Errore durante il salvataggio della forma giuridica.", e1);
			throw new RuntimeException(e1);
		}

		logger.debug("--> Exit salvaFormaGiuridica");
		return formaGiuridicaSave;
	}

	@Override
	public Mansione salvaMansione(Mansione mansione) {
		logger.debug("--> Enter salvaMansione");
		Mansione mansioneSave = null;
		try {
			mansioneSave = panjeaDAO.save(mansione);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaMansione");
		return mansioneSave;
	}

	@Override
	public TipoDeposito salvaTipoDeposito(TipoDeposito tipoDeposito) throws AnagraficaServiceException {
		logger.debug("--> Enter salvaTipoDeposito");
		TipoDeposito tipoDepositoSave = null;
		try {
			tipoDepositoSave = panjeaDAO.save(tipoDeposito);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaTipoDeposito");
		return tipoDepositoSave;
	}

	@Override
	public TipoSedeEntita salvaTipoSedeEntita(TipoSedeEntita tipoSedeEntita) throws AnagraficaServiceException {
		logger.debug("--> Enter salvaTipoSedeEntita");
		TipoSedeEntita tipoSedeEntitaSave = null;
		try {
			tipoSedeEntitaSave = panjeaDAO.save(tipoSedeEntita);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaTipoSedeEntita");
		return tipoSedeEntitaSave;
	}
}
