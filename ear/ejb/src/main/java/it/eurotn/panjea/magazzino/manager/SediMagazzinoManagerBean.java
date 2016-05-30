package it.eurotn.panjea.magazzino.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.NoteAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.manager.interfaces.SediMagazzinoManager;
import it.eurotn.panjea.magazzino.service.exception.MagazzinoException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 *
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.SediMagazzinoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SediMagazzinoManager")
public class SediMagazzinoManagerBean implements SediMagazzinoManager {
	private static Logger logger = Logger.getLogger(SediMagazzinoManagerBean.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@Override
	@RolesAllowed("gestioneFatturazione")
	public void associaSediASedePerRifatturazione(List<SedeEntita> sediEntita,
			SedeMagazzinoLite sedePerRifatturazione) {
		logger.debug("--> Enter associaSediASedePerRifatturazione");

		for (SedeEntita sedeEntita : sediEntita) {
			SedeMagazzino sedeMagazzino = caricaSedeMagazzinoBySedeEntita(sedeEntita, false);
			sedeMagazzino.setSedePerRifatturazione(sedePerRifatturazione);

			salvaSedeMagazzino(sedeMagazzino);
		}

		logger.debug("--> Exit associaSediASedePerRifatturazione");
	}

	@Override
	public void associaSediMagazzinoPerRifatturazione(SedeMagazzinoLite sedeDiRifatturazione,
			List<SedeMagazzinoLite> sediDaAssociare) {
		logger.debug("--> Enter associaSediMagazzinoPerRifatturazione");

		for (SedeMagazzinoLite sedeMagazzino : sediDaAssociare) {

			SedeMagazzino sede = panjeaDAO.loadLazy(SedeMagazzino.class, sedeMagazzino.getId());
			sede.setSedePerRifatturazione(sedeDiRifatturazione);

			try {
				panjeaDAO.save(sede);
			} catch (Exception e) {
				logger.error("--> Errore durante l'associazione della sede per rifatturazione", e);
				throw new RuntimeException("Errore durante l'associazione della sede per rifatturazione", e);
			}
		}

		logger.debug("--> Exit associaSediMagazzinoPerRifatturazione");
	}

	@Override
	public void cancellaCategorieSediMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino) {
		logger.debug("--> Enter cancellaCategorieSediMagazzino");

		try {
			panjeaDAO.delete(categoriaSedeMagazzino);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione della categoria sede magazzino", e);
			throw new RuntimeException("Errore durante la cancellazione della categoria sede magazzino", e);
		}

		logger.debug("--> Exit cancellaCategorieSediMagazzino");
	}

	@Override
	public void cancellaSedeMagazzino(SedeEntita sedeEntita) {
		logger.debug("--> Enter cancellaSedeMagazzino");
		SedeMagazzino sedeMagazzino = caricaSedeMagazzinoBySedeEntita(sedeEntita, true);
		if (sedeMagazzino.getId() != null) {
			cancellaSedeMagazzino(sedeMagazzino);
		}

		logger.debug("--> Exit cancellaSedeMagazzino");
	}

	@Override
	public void cancellaSedeMagazzino(SedeMagazzino sedeMagazzino) {
		logger.debug("--> Enter cancellaSedeMagazzino");
		try {
			panjeaDAO.delete(sedeMagazzino);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione della sede magazzino", e);
			throw new RuntimeException("Errore durante la cancellazione della sede magazzino", e);
		}
		logger.debug("--> Exit caricaCategoriaSedeMagazzinoBySede");
	}

	@Override
	public CategoriaSedeMagazzino caricaCategoriaSedeMagazzinoBySede(SedeEntita sedeEntita) {
		logger.debug("--> Enter caricaCategoriaSedeMagazzinoBySede");

		CategoriaSedeMagazzino categoriaSedeMagazzino = null;
		Query query = panjeaDAO.prepareNamedQuery("SedeMagazzino.caricaCategoriaSedeMagazzino");
		query.setParameter("paramIdSede", sedeEntita.getId());
		try {
			categoriaSedeMagazzino = (CategoriaSedeMagazzino) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> categoria sede magazzino non trovata");
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaCategoriaSedeMagazzino", e);
			throw new MagazzinoException(e);
		}

		logger.debug("--> Exit caricaCategoriaSedeMagazzinoBySede");
		return categoriaSedeMagazzino;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaSedeMagazzino> caricaCategorieSediMagazzino(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaCategorieSediMagazzino");

		List<CategoriaSedeMagazzino> listCategorie = null;

		StringBuilder sb = new StringBuilder(
				"select c from CategoriaSedeMagazzino c where c.codiceAzienda = :paramCodiceAzienda ");
		if (valueSearch != null) {
			sb.append(" and ").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		sb.append(" order by ");
		sb.append(fieldSearch);
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramCodiceAzienda", getAzienda());
		try {
			listCategorie = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento delle categorie sedi magazzino. ", e);
			throw new MagazzinoException(e);
		}

		logger.debug("--> Exit caricaCategorieSediMagazzino");
		return listCategorie;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaSedeMagazzino> caricaCategorieSediMagazzinoSenzaContratto() {
		logger.debug("--> Enter caricaCategorieSediMagazzinoSenzaContratto");
		List<CategoriaSedeMagazzino> listCategorie = null;

		Query query = panjeaDAO.prepareNamedQuery("CategoriaSedeMagazzino.caricaCategoriaSenzaContratto");
		query.setParameter("paramCodiceAzienda", getAzienda());
		try {
			listCategorie = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento delle categorie sedi magazzino. ", e);
			throw new MagazzinoException(e);
		}

		logger.debug("--> Exit caricaCategorieSediMagazzinoSenzaContratto");
		return listCategorie;
	}

	@Override
	public NoteAreaMagazzino caricaNoteSede(SedeEntita sedeEntita) {
		NoteAreaMagazzino noteAreaMagazzino = new NoteAreaMagazzino();

		// le note entità e le note sede non sono ereditate, quindi carico
		// direttamente dalla sedeEntità.
		sedeEntita = panjeaDAO.loadLazy(SedeEntita.class, sedeEntita.getId());
		noteAreaMagazzino.setNoteSede(sedeEntita.getNote());
		noteAreaMagazzino.setNoteEntita(sedeEntita.getEntita().getNoteMagazzino());
		boolean bloccoEntita = sedeEntita.getEntita().getBloccoSede().isBlocco();
		noteAreaMagazzino.setBloccato(bloccoEntita || sedeEntita.getBloccoSede().isBlocco());
		noteAreaMagazzino.setNoteBlocco(bloccoEntita ? sedeEntita.getEntita().getBloccoSede().getNoteBlocco()
				: sedeEntita.getBloccoSede().getNoteBlocco());
		return noteAreaMagazzino;
	}

	@Override
	public SedeMagazzino caricaSedeMagazzinoBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali) {
		logger.debug("--> Enter caricaSedeMagazzinoBySedeEntita");

		SedeMagazzino sedeMagazzinoResult = null;
		try {
			sedeEntita = panjeaDAO.load(SedeEntita.class, sedeEntita.getId());
		} catch (Exception e1) {
			logger.error("-->errore nel caricare la sedeentita con id " + sedeEntita.getId(), e1);
			throw new RuntimeException(e1);
		}

		if (sedeEntita.isEreditaDatiCommerciali() && !ignoraEreditaDatiCommerciali) {
			sedeMagazzinoResult = caricaSedeMagazzinoPrincipale(sedeEntita.getEntita());
		} else {
			Query query = panjeaDAO.prepareNamedQuery("SedeMagazzino.caricaSediMagazzinoBySedeEntita");
			query.setParameter("paramSedeEntitaId", sedeEntita.getId());
			try {
				sedeMagazzinoResult = (SedeMagazzino) panjeaDAO.getSingleResult(query);
			} catch (ObjectNotFoundException e) {
				logger.debug("--> Nessuna sede magazzino per la sede entità. Restituisco una sede magazzino nuova. ");
				sedeMagazzinoResult = new SedeMagazzino();
				sedeMagazzinoResult.setSedeEntita(sedeEntita);
			} catch (DAOException e) {
				throw new MagazzinoException(e);
			}
		}
		logger.debug("--> Exit caricaSedeMagazzinoBySedeEntita");
		return sedeMagazzinoResult;
	}

	@Override
	public SedeMagazzino caricaSedeMagazzinoPrincipale(Entita entita) {
		logger.debug("--> Enter caricaSedeMagazzinoPrincipale");

		SedeMagazzino sedeMagazzino;
		Query query = panjeaDAO.prepareNamedQuery("SedeMagazzino.caricaSedeMagazzinoPrincipale");
		query.setParameter("paramEntitaId", entita.getId());
		try {
			sedeMagazzino = (SedeMagazzino) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> sede magazzino non trovata, restituisco oggetto vuoto ");
			sedeMagazzino = new SedeMagazzino();
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaSedeMagazzinoPrincipale", e);
			throw new MagazzinoException(e);
		}
		logger.debug("--> Exit caricaSedeMagazzinoPrincipale");
		return sedeMagazzino;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SedeMagazzino> caricaSediMagazzino(Map<String, Object> parametri, boolean textAsLike) {
		logger.debug("--> Enter caricaSediMagazzino");

		String operatoreTesto = " = ";
		String espressione = "";
		if (textAsLike) {
			operatoreTesto = " like ";
			espressione = "%";
		}
		List<SedeMagazzino> list = new ArrayList<SedeMagazzino>();
		StringBuffer hql = new StringBuffer("select sm from SedeMagazzino sm left join fetch sm.sedeEntita se ");
		hql.append("left join fetch se.entita e left join fetch se.sede sa left join fetch e.anagrafica ");
		hql.append("left join fetch se.sedeCollegata sc left join fetch sc.sede sac ");
		hql.append("where e.anagrafica.codiceAzienda = :paramCodiceAzienda  ");
		Map<String, Object> paramHql = new HashMap<String, Object>();
		paramHql.put("paramCodiceAzienda", getAzienda());

		if (parametri.containsKey("sedeEntita.ereditaDatiCommerciali")
				&& parametri.get("sedeEntita.ereditaDatiCommerciali") != null) {
			hql = hql.append(" and se.ereditaDatiCommerciali=:paramEreditaDatiCommerciali");
			paramHql.put("paramEreditaDatiCommerciali", parametri.get("sedeEntita.ereditaDatiCommerciali"));
		}

		if (parametri.containsKey("sedeEntita.entita.id")) {
			hql = hql.append(" and e.id = :paramEntitaId");
			paramHql.put("paramEntitaId", ((IDefProperty) parametri.get("sedeEntita.entita.id")).getId());
		}

		if (parametri.containsKey("sedeEntita.tipoSede") && parametri.get("sedeEntita.tipoSede") != null) {
			hql = hql.append(" and se.tipoSede.tipoSede<3 ");
		}

		if (parametri.containsKey("sedeEntita.sede.descrizione")
				&& parametri.get("sedeEntita.sede.descrizione") != null) {
			hql = hql.append(" and se.sede.descrizione ").append(operatoreTesto).append(" :paramDescrizione");
			paramHql.put("paramDescrizione", espressione + parametri.get("sedeEntita.sede.descrizione") + espressione);
		}
		Query query = panjeaDAO.prepareQuery(hql.toString());
		for (String key : paramHql.keySet()) {
			query.setParameter(key, paramHql.get(key));
		}
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaSediMagazzino", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaClientiLiteMagazzino");
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SedeMagazzinoLite> caricaSediMagazzinoByEntita(Entita entita) {
		logger.debug("--> Enter caricaSediMagazzinoByEntita");

		List<SedeMagazzinoLite> sediMagazzino = null;

		Query query = panjeaDAO.prepareNamedQuery("SedeMagazzinoLite.caricaSediMagazzinoByEntita");
		query.setParameter("paramEntitaId", entita.getId());
		try {
			sediMagazzino = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento delle sedi magazzino. ", e);
			throw new MagazzinoException(e);
		}
		logger.debug("--> Exit caricaSediMagazzinoByEntita");
		return sediMagazzino;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SedeMagazzinoLite> caricaSediMagazzinoDiRifatturazione() {
		logger.debug("--> Enter caricaSediMagazzinoDiRifatturazione");

		List<SedeMagazzinoLite> sediMagazzino = new ArrayList<SedeMagazzinoLite>();

		Query query = panjeaDAO.prepareNamedQuery("SedeMagazzino.caricaSediMagazzinoDiRifatturazione");
		query.setParameter("paramCodiceAzienda", getAzienda());
		try {
			sediMagazzino = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento delle sedi magazzino di rifatturazione. ", e);
			throw new MagazzinoException(e);
		}

		logger.debug("--> Exit caricaSediMagazzinoDiRifatturazione");
		return sediMagazzino;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SedeMagazzinoLite> caricaSediRifatturazioneAssociate() {
		logger.debug("--> Enter caricaSediRifatturazioneAssociate");

		List<SedeMagazzinoLite> sediMagazzino = null;

		Query query = panjeaDAO.prepareNamedQuery("SedeMagazzino.caricaSediRifatturazioneAssociate");
		query.setParameter("paramCodiceAzienda", getAzienda());
		try {
			sediMagazzino = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error(
					"--> Errore durante il caricamento delle sedi magazzino associate ad una sede di rifatturazione. ",
					e);
			throw new MagazzinoException(e);
		}

		logger.debug("--> Exit caricaSediRifatturazioneAssociate");
		return sediMagazzino;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SedeMagazzinoLite> caricaSediRifatturazioneNonAssociate(EntitaLite entita) {
		logger.debug("--> Enter caricaSediRifatturazioneNonAssociate");

		List<SedeMagazzinoLite> sediMagazzino = null;

		Query query = panjeaDAO.prepareNamedQuery("SedeMagazzino.caricaSediRifatturazioneNonAssociate");
		query.setParameter("paramEntita", entita);
		try {
			sediMagazzino = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error(
					"--> Errore durante il caricamento delle sedi magazzino non associate ad una sede di rifatturazione. ",
					e);
			throw new MagazzinoException(e);
		}

		logger.debug("--> Exit caricaSediRifatturazioneNonAssociate");
		return sediMagazzino;
	}

	/**
	 *
	 * @return codice azienda loggata
	 */
	private String getAzienda() {
		return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
	}

	@Override
	@RolesAllowed("gestioneFatturazione")
	public void rimuoviSedePerRifatturazione(SedeMagazzinoLite sedeMagazzino) {
		logger.debug("--> Enter rimuoviSedePerRifatturazione");

		SedeMagazzinoLite sede = panjeaDAO.loadLazy(SedeMagazzinoLite.class, sedeMagazzino.getId());
		sede.setSedePerRifatturazione(null);

		try {
			panjeaDAO.save(sede);
		} catch (Exception e) {
			logger.error("--> Errore durante la rimozione della sede per rifatturazione.", e);
			throw new RuntimeException("Errore durante la rimozione della sede per rifatturazione.", e);
		}

		logger.debug("--> Exit rimuoviSedePerRifatturazione");
	}

	@Override
	public CategoriaSedeMagazzino salvaCategoriaSedeMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino) {
		logger.debug("--> Enter salvaCategoriaSedeMagazzino");

		if (categoriaSedeMagazzino.getCodiceAzienda() == null) {
			categoriaSedeMagazzino.setCodiceAzienda(getAzienda());
		}

		CategoriaSedeMagazzino categoriaSedeMagazzinoSalvata = null;

		try {
			categoriaSedeMagazzinoSalvata = panjeaDAO.save(categoriaSedeMagazzino);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio della categoria sede magazzino", e);
			throw new RuntimeException("Errore durante il salvataggio della categoria sede magazzino", e);
		}
		logger.debug("--> Exit salvaCategoriaSedeMagazzino");
		return categoriaSedeMagazzinoSalvata;
	}

	@Override
	public SedeMagazzino salvaSedeMagazzino(SedeMagazzino sedeMagazzino) {
		logger.debug("--> Enter salvaSedeMagazzino");

		SedeMagazzino sedeMagazzinoSalvata = null;
		try {
			sedeMagazzinoSalvata = panjeaDAO.save(sedeMagazzino);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio della sede magazzino.", e);
			throw new RuntimeException("Errore durante il salvataggio della sede magazzino.", e);
		}

		logger.debug("--> Exit salvaSedeMagazzino");
		return sedeMagazzinoSalvata;
	}
}
