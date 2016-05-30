package it.eurotn.panjea.beniammortizzabili2.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.beniammortizzabili.exception.BeniAmmortizzabiliException;
import it.eurotn.panjea.beniammortizzabili2.domain.Gruppo;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.panjea.beniammortizzabili2.domain.TipologiaEliminazione;
import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.TabelleBeniAmmortizzabiliManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 *
 * @author adriano
 * @version 1.0, 03/ott/07
 *
 */
@Stateless(name = "Panjea.TabelleBeniAmmortizzabiliManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.TabelleBeniAmmortizzabiliManager")
public class TabelleBeniAmmortizzabiliManagerBean implements TabelleBeniAmmortizzabiliManager {

	private static Logger logger = Logger.getLogger(TabelleBeniAmmortizzabiliManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected AziendeManager aziendeManager;

	@Resource
	protected SessionContext sessionContext;

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#cancellaGruppo(it.eurotn .panjea.beniammortizzabili2.domain.Gruppo)
	 */
	@Override
	public void cancellaGruppo(Gruppo gruppo) {
		logger.debug("--> Enter cancellaGruppo");
		try {
			panjeaDAO.delete(gruppo);
		} catch (VincoloException e) {
			logger.error("--> errore, impossibile cancellare Gruppo ", e);
			throw new RuntimeException("Errore, impossibile cancellare Gruppo: VincoloException ", e);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile cancellare TipologiaEliminazione ", e);
			throw new RuntimeException("Errore, impossibile cancellare Gruppo: DAOException ", e);
		}
		logger.debug("--> Exit cancellaGruppo");
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#cancellaSottoSpecie( it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie)
	 */
	@Override
	public void cancellaSottoSpecie(SottoSpecie sottoSpecie) {
		logger.debug("--> Enter cancellaSottoSpecie");
		try {
			Specie specie = sottoSpecie.getSpecie();
			specie.getSottoSpecie().remove(sottoSpecie);
			panjeaDAO.save(specie);
			panjeaDAO.delete(sottoSpecie);
			logger.error("--> Cancellata la sottospecie " + sottoSpecie.getId());
		} catch (DAOException e) {
			logger.error("--> Errore durante la cancellazione della sottospecie: " + sottoSpecie, e);
			throw new RuntimeException("Errore durante la cancellazione della sottospecie: " + sottoSpecie, e);
		}
		logger.debug("--> Exit cancellaSottoSpecie");

	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#cancellaSpecie(it.eurotn .panjea.beniammortizzabili2.domain.Specie)
	 */
	@Override
	public void cancellaSpecie(Specie specie) {
		logger.debug("--> Enter cancellaSpecie");

		try {
			Gruppo gruppo = specie.getGruppo();
			gruppo.getSpecie().remove(specie);
			panjeaDAO.save(gruppo);
			panjeaDAO.delete(specie);
		} catch (DAOException e) {
			logger.error("--> Errore durante la cancellazione della specie " + specie.getId());
			throw new RuntimeException("Errore durante la cancellazione della specie " + specie.getId(), e);
		}
		logger.debug("--> Exit cancellaSpecie");

	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.TabelleBeniAmmortizzabiliManager #
	 * cancellaTipologiaEliminazione(it.eurotn.panjea.beniammortizzabili2.domain .TipologiaEliminazione)
	 */
	@Override
	public void cancellaTipologiaEliminazione(TipologiaEliminazione tipologiaEliminazione) {
		logger.debug("--> Enter cancellaTipologiaEliminazione");
		try {
			panjeaDAO.delete(tipologiaEliminazione);
		} catch (VincoloException e) {
			logger.error("--> errore, impossibile cancellare TipologiaEliminazione ", e);
			throw new RuntimeException("Errore, impossibile cancellare TipologiaEliminazione: VincoloException ", e);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile cancellare TipologiaEliminazione ", e);
			throw new RuntimeException("Errore, impossibile cancellare TipologiaEliminazione: DAOException ", e);
		}
		logger.debug("--> Exit cancellaTipologiaEliminazione");
	}

	@Override
	public void cancellaUbicazione(Ubicazione ubicazione) {
		logger.debug("--> Enter cancellaUbicazione");
		try {
			panjeaDAO.delete(ubicazione);
		} catch (VincoloException e) {
			logger.error("--> errore, impossibile cancellare Ubicazione ", e);
			throw new RuntimeException("Errore, impossibile cancellare Ubicazione: VincoloException", e);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile cancellare Ubicazione ", e);
			throw new RuntimeException("Errore, impossibile cancellare Ubicazione: DAOException", e);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Gruppo> caricaGruppi(String fieldSearch, String valueSearch) throws BeniAmmortizzabiliException {
		logger.debug("--> Enter caricaGruppi");

		List<Gruppo> gruppi = new ArrayList<Gruppo>();

		StringBuilder sb = new StringBuilder("select g from Gruppo g ");
		if (valueSearch != null) {
			sb.append(" where ").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		sb.append(" order by ");
		sb.append(fieldSearch);
		Query query = panjeaDAO.prepareQuery(sb.toString());
		try {
			gruppi = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante in caricamento dei gruppi.", e);
			throw new BeniAmmortizzabiliException("Errore durante in caricamento dei gruppi.", e);
		}

		logger.debug("--> Exit caricaGruppi");
		return gruppi;
	}

	@Override
	public Gruppo caricaGruppoAzienda() {
		logger.debug("--> Enter caricaGruppoAzienda");

		String codiceAzienda = getAzienda();
		logger.debug("--> Azienda del JECPrincipal: " + codiceAzienda);

		Gruppo gruppo = null;

		Query query = panjeaDAO.prepareNamedQuery("Gruppo.caricaByAzienda");
		query.setParameter("paramCodiceAzienda", codiceAzienda);
		try {
			gruppo = (Gruppo) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> gruppo per azienda non trovato  ");
			return null;
		} catch (DAOException e) {
			logger.error("--> errore, impossibile caricare Gruppo per Azienda ", e);
			throw new RuntimeException("--> impossibile caricare Gruppo per Azienda", e);
		}

		logger.debug("--> Exit caricaGruppoAzienda");
		return gruppo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#caricaGruppo(java.lang .String)
	 */
	@Override
	public Gruppo caricaGruppoByCodice(String codiceGruppo) {
		logger.debug("--> Enter caricaGruppo " + codiceGruppo);

		String codiceAzienda = getAzienda();
		logger.debug("--> Azienda del JECPrincipal: " + codiceAzienda);

		Gruppo gruppo = null;

		Query query = panjeaDAO.prepareNamedQuery("Gruppo.caricaByCodice");
		query.setParameter("paramCodiceAzienda", codiceAzienda);
		query.setParameter("paramCodice", codiceGruppo);

		try {
			gruppo = (Gruppo) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> gruppo per azienda con codice " + codiceGruppo + " non trovato ");
			return null;
		} catch (DAOException e) {
			logger.error("--> errore, impossibile caricare Gruppo per Azienda e codice " + codiceGruppo, e);
			throw new RuntimeException("--> impossibile caricare Gruppo per Azienda", e);
		}

		logger.debug("--> Exit caricaGruppoAzienda");
		return gruppo;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#caricaSottoSpecie(java .lang.Integer)
	 */
	@Override
	public SottoSpecie caricaSottoSpecie(Integer id) throws BeniAmmortizzabiliException {
		logger.debug("--> Enter caricaSottoSpecie");

		if (id == null) {
			logger.error("--> Impossibile caricare la SottoSpecie. ID nullo.");
			throw new BeniAmmortizzabiliException("Impossibile caricare la SottoSpecie. ID nullo.");
		}

		SottoSpecie sottoSpecie;
		try {
			sottoSpecie = panjeaDAO.load(SottoSpecie.class, id);
			logger.debug("--> Caricata la SottoSpecie " + sottoSpecie.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> Errore durante il caricamento della sottoSpecie " + id, e);
			throw new BeniAmmortizzabiliException("Errore durante il caricamento della sottoSpecie " + id, e);
		}

		logger.debug("--> Exit caricaSottoSpecie");
		return sottoSpecie;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces. TabelleBeniAmmortizzabiliManager#caricaSottoSpecie()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SottoSpecie> caricaSottoSpecie(String fieldSearch, String valueSearch)
			throws BeniAmmortizzabiliException {
		logger.debug("--> Enter caricaSottoSpecie");

		List<SottoSpecie> listSottoSpecie = new ArrayList<SottoSpecie>();

		StringBuilder sb = new StringBuilder(
				" select sp from SottoSpecie sp inner join sp.specie s inner join s.gruppo g inner join g.aziende a where a.codice = :paramCodiceAzienda ");
		if (valueSearch != null) {
			String[] codici = valueSearch.split("\\.");
			if (codici != null) {
				if (codici.length == 1) {
					sb.append(" and s.codice like ").append(PanjeaEJBUtil.addQuote(codici[0]));
				} else if (codici.length == 2) {
					System.err.println(valueSearch);
					sb.append(" and s.codice like ").append(PanjeaEJBUtil.addQuote(codici[0]));
					sb.append(" and sp.codice like ").append(PanjeaEJBUtil.addQuote("%" + codici[1].replace("0", "")));
				}
			}
		}
		sb.append(" order by  sp.codice,sp.codice ");

		try {
			Query query = panjeaDAO.prepareQuery(sb.toString());
			query.setParameter("paramCodiceAzienda", getAzienda());

			listSottoSpecie = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore, immpossibile caricare la lista di SottoSpecie", e);
			throw new BeniAmmortizzabiliException("impossibile caricare la lista di SottoSpecie", e);
		}
		logger.debug("--> Exit caricaSottoSpecie");
		return listSottoSpecie;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#caricaSpecieAzienda()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Specie> caricaSpeci(String fieldSearch, String valueSearch) throws BeniAmmortizzabiliException {
		logger.debug("--> Enter caricaSpeci");
		List<Specie> specieAzienda;

		StringBuilder sb = new StringBuilder("select sp from Specie sp ");
		if (valueSearch != null) {
			sb.append(" where ").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		sb.append(" order by ");
		sb.append(fieldSearch);

		Query query = panjeaDAO.prepareQuery(sb.toString());
		try {
			specieAzienda = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile caricare la lista di Specie", e);
			throw new BeniAmmortizzabiliException("impossibile caricare la lista di Specie", e);
		}
		logger.debug("--> Exit caricaSpeci");
		return specieAzienda;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#caricaSpecie(it.eurotn .panjea.beniammortizzabili2.domain.Gruppo)
	 */
	@Override
	public List<Specie> caricaSpecie(Gruppo gruppo) throws BeniAmmortizzabiliException {
		logger.debug("--> Enter caricaSpecie");

		if (gruppo.isNew()) {
			logger.error("--> Impossibile caricare la specie. ID gruppo nullo.");
			throw new BeniAmmortizzabiliException("Impossibile caricare la specie. ID gruppo nullo.");
		}

		Set<Specie> listSpecie = new HashSet<Specie>();
		List<Specie> list = new ArrayList<Specie>();
		Gruppo gruppo2;
		try {
			gruppo2 = panjeaDAO.load(Gruppo.class, gruppo.getId());
			listSpecie = gruppo2.getSpecie();
			for (Specie specie : listSpecie) {
				list.add(specie);
			}
			logger.debug("--> Caricate " + listSpecie.size() + " specie");
		} catch (ObjectNotFoundException e) {
			logger.error("--> " + e.getMessage(), e);
			throw new BeniAmmortizzabiliException("impossibile recuperare lista di specie per gruppo ", e);
		}

		logger.debug("--> Exit caricaSpecie");
		return list;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#caricaSpecie(java.lang .Integer)
	 */
	@Override
	public Specie caricaSpecie(Integer id) throws BeniAmmortizzabiliException {
		logger.debug("--> Enter caricaSpecie");

		if (id == null) {
			logger.error("--> Impossibile caricare la specie. ID nullo.");
			throw new BeniAmmortizzabiliException("Impossibile caricare la specie. ID nullo.");
		}

		Specie specie = null;
		try {
			specie = panjeaDAO.load(Specie.class, id);
			logger.debug("--> Caricata la Specie " + specie.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> " + e.getMessage(), e);
			throw new BeniAmmortizzabiliException("impossibile trovare Specie ", e);
		}

		logger.debug("--> Exit caricaSpecie");
		return specie;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#caricaTipologiaEliminazione (java.lang.Integer)
	 */
	@Override
	public TipologiaEliminazione caricaTipologiaEliminazione(Integer id) throws BeniAmmortizzabiliException {
		logger.debug("--> Enter caricaTipologiaEliminazione");

		if (id == null) {
			logger.error("--> Impossibile caricare la tipologia di eliminazione. ID nullo.");
			throw new BeniAmmortizzabiliException("Impossibile caricare la tipologia di eliminazione. ID nullo.");
		}

		TipologiaEliminazione tipologiaEliminazione = null;
		try {
			tipologiaEliminazione = panjeaDAO.load(TipologiaEliminazione.class, id);
			logger.debug("--> Caricata la TipologiaEliminazione " + tipologiaEliminazione.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> " + e.getMessage(), e);
			throw new BeniAmmortizzabiliException("tipologia eliminazione non trovata ", e);
		}

		logger.debug("--> Exit caricaTipologiaEliminazione");
		return tipologiaEliminazione;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#caricaTipologieEliminazione ()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipologiaEliminazione> caricaTipologieEliminazione(String codice) {
		logger.debug("--> Enter caricaTipologieEliminazione");

		List<TipologiaEliminazione> tipologieCaricate = new ArrayList<TipologiaEliminazione>();

		StringBuilder sb = new StringBuilder("select tm from TipologiaEliminazione tm ");
		if (codice != null) {
			sb.append(" where tm.codice like ").append(PanjeaEJBUtil.addQuote(codice));
		}
		sb.append(" order by codice");
		Query query = panjeaDAO.prepareQuery(sb.toString());

		try {
			tipologieCaricate = panjeaDAO.getResultList(query);
			logger.debug("--> Caricate " + tipologieCaricate.size() + " tipologieEliminazione.");
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento delle tipologieEliminazione.", e);
			throw new RuntimeException("Errore durante il caricamento delle tipologieEliminazione.");
		}
		logger.debug("--> Exit caricaTipologieEliminazione");
		return tipologieCaricate;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#caricaUbicazione(java .lang.Integer)
	 */
	@Override
	public Ubicazione caricaUbicazione(Integer id) throws BeniAmmortizzabiliException {
		logger.debug("--> Enter caricaUbicazione");

		if (id == null) {
			logger.error("--> Impossibile caricare l'ubicazione. ID nullo.");
			throw new BeniAmmortizzabiliException("Impossibile caricare l'ubicazione. ID nullo.");
		}

		Ubicazione ubicazione = null;
		try {
			ubicazione = panjeaDAO.load(Ubicazione.class, id);
			logger.debug("--> Caricata l'ubicazione: " + ubicazione.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> " + e.getMessage(), e);
			throw new BeniAmmortizzabiliException("ubicazione non trovata ", e);
		}

		logger.debug("--> Exit caricaUbicazione");
		return ubicazione;
	}

	@Override
	public Ubicazione caricaUbicazioneByCodice(String codiceUbiazione) {
		logger.debug("--> Enter caricaUbicazioneByCodice " + codiceUbiazione);

		String codiceAzienda = getAzienda();
		logger.debug("--> Azienda del JECPrincipal: " + codiceAzienda);

		Ubicazione ubicazione = null;

		Query query = panjeaDAO.prepareNamedQuery("Ubicazione.caricaByCodice");
		query.setParameter("paramCodiceAzienda", codiceAzienda);
		query.setParameter("paramCodice", codiceUbiazione);

		try {
			ubicazione = (Ubicazione) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> gruppo per azienda con codice " + codiceUbiazione + " non trovato");
			return null;
		} catch (DAOException e) {
			logger.error("--> errore, impossibile caricare Ubicazione per Azienda e codice " + codiceUbiazione, e);
			throw new RuntimeException("--> impossibile caricare Ubicazione per Azienda e codice " + codiceUbiazione, e);
		}

		logger.debug("--> Exit caricaUbicazioneByCodice");
		return ubicazione;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces. TabelleBeniAmmortizzabiliManager#caricaUbicazioni()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Ubicazione> caricaUbicazioni(String codice) {
		logger.debug("--> Enter caricaUbicazioni");

		List<Ubicazione> ubicazioni;

		StringBuilder sb = new StringBuilder("select u from Ubicazione u where u.codiceAzienda = :paramCodiceAzienda ");
		if (codice != null) {
			sb.append(" and u.codice like ").append(PanjeaEJBUtil.addQuote(codice));
		}
		sb.append(" order by codice");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		try {
			query.setParameter("paramCodiceAzienda", getAzienda());
			ubicazioni = panjeaDAO.getResultList(query);
			logger.debug("--> Caricate " + ubicazioni.size() + " ubicazioni.");
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento delle ubicazioni.", e);
			throw new RuntimeException("Errore durante il caricamento delle ubicazioni.");
		}
		logger.debug("--> Exit caricaUbicazioni");
		return ubicazioni;
	}

	/**
	 * Dal session context recupera l'utente callerPrincipal e da qui restituisce l'azineda.
	 *
	 * @return L'azienda dell'utente corrente
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#salvaGruppo(it.eurotn .panjea.beniammortizzabili2.domain.Gruppo)
	 */
	@Override
	public Gruppo salvaGruppo(Gruppo gruppo) {
		logger.debug("--> Enter salvaGruppo");
		Gruppo gruppo2 = null;
		try {
			gruppo2 = panjeaDAO.save(gruppo);
			logger.debug("--> Salvato il gruppo " + gruppo.getId());
		} catch (Exception e) {
			logger.error("--> errore, impossibile salvare gruppo", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaGruppo");
		return gruppo2;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#salvaSottoSpecie(it. eurotn.panjea.beniammortizzabili2.domain.SottoSpecie)
	 */
	@Override
	public SottoSpecie salvaSottoSpecie(SottoSpecie sottoSpecie) {
		logger.debug("--> Enter salvaSottoSpecie");

		// salvo la sottospecie
		SottoSpecie sottoSpecieSalvata = null;
		try {
			sottoSpecieSalvata = panjeaDAO.save(sottoSpecie);
			logger.debug("--> Salvata la sottoSpecie: " + sottoSpecie.getId());
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio della sottoSpecie.", e);
			throw new RuntimeException("Errore durante il salvataggio della sottoSpecie.", e);
		}

		logger.debug("--> Exit salvaSottoSpecie");
		return sottoSpecieSalvata;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#salvaSpecie(it.eurotn .panjea.beniammortizzabili2.domain.Specie)
	 */
	@Override
	public Specie salvaSpecie(Specie specie) {
		logger.debug("--> Enter salvaSpecie");
		Specie specie2 = null;
		try {
			specie2 = panjeaDAO.save(specie);
		} catch (Exception e) {
			logger.error("--> errore, impossibile salvare la Specie ", e);
			throw new RuntimeException("impossibile salvare la Specie", e);
		}
		logger.debug("--> Exit salvaSpecie");
		return specie2;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#salvaTipologiaEliminazione
	 * (it.eurotn.panjea.beniammortizzabili2.domain.TipologiaEliminazione)
	 */
	@Override
	public TipologiaEliminazione salvaTipologiaEliminazione(TipologiaEliminazione tipologiaEliminazione) {
		logger.debug("--> Enter salvaTipologiaEliminazione");
		TipologiaEliminazione tipologiaEliminazione2 = null;
		try {
			tipologiaEliminazione2 = panjeaDAO.save(tipologiaEliminazione);
			logger.debug("--> Salvata la TipologiaEliminazione " + tipologiaEliminazione.getId());
		} catch (Exception e) {
			logger.error("--> errore, impossibile salvare tipologia eliminazione ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaTipologiaEliminazione");
		return tipologiaEliminazione2;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * TabelleBeniAmmortizzabiliManager#salvaUbicazione(it.eurotn .panjea.beniammortizzabili2.domain.Ubicazione)
	 */
	@Override
	public Ubicazione salvaUbicazione(Ubicazione ubicazione) {
		logger.debug("--> Enter salvaUbicazione");

		// setto il codice azienda dell'ubicazione recuperandolo dal caller
		// principal
		if (ubicazione.getCodiceAzienda() == null) {
			ubicazione.setCodiceAzienda(getAzienda());
		}

		Ubicazione ubicazione2;
		try {
			ubicazione2 = panjeaDAO.save(ubicazione);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio dell'ubicazione ", e);
			throw new RuntimeException("Errore durante il salvataggio dell'ubicazione ", e);
		}

		logger.debug("--> Exit salvaUbicazione");
		return ubicazione2;
	}

	@Override
	public Gruppo settaGruppoAzienda(Gruppo gruppo) {
		logger.debug("--> Enter settaGruppoAzienda");

		AziendaLite aziendaLite = null;
		try {
			aziendaLite = aziendeManager.caricaAzienda(getAzienda());
		} catch (AnagraficaServiceException e) {
			// non rilancio perchè non deve mai accadere
			logger.error("-->errore in caricaAzienda", e);
		}

		// carico l'attuale gruppo dell'azienda e rimuovo l'associazione
		Gruppo gruppoAttuale = caricaGruppoAzienda();

		if (gruppoAttuale != null && !gruppoAttuale.getId().equals(gruppo.getId())) {
			if (gruppoAttuale != null) {
				gruppoAttuale.getAziende().remove(aziendaLite);
				salvaGruppo(gruppoAttuale);
			}

			// setto l'azienda al nuovo gruppo e lo salvo
			gruppo.addToAzienda(aziendaLite);

			gruppo = salvaGruppo(gruppo);

			return gruppo;
		}

		logger.debug("--> Exit settaGruppoAzienda");
		return null;
	}

}
