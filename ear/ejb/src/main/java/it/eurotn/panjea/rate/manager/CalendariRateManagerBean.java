package it.eurotn.panjea.rate.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.RigaCalendarioRate;
import it.eurotn.panjea.rate.manager.interfaces.CalendariRateManager;
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

@Stateless(name = "Panjea.CalendariRateManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CalendariRateManager")
public class CalendariRateManagerBean implements CalendariRateManager {

	private static Logger logger = Logger.getLogger(CalendariRateManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void cancellaCalendarioRate(CalendarioRate calendarioRate) {
		logger.debug("--> Enter cancellaCalendarioRate");

		Query query = panjeaDAO
				.prepareQuery("delete from RigaCalendarioRate r where r.calendarioRate.id = :paramIdCalendario");
		query.setParameter("paramIdCalendario", calendarioRate.getId());
		try {
			panjeaDAO.executeQuery(query);
			panjeaDAO.delete(calendarioRate);
		} catch (Exception e) {
			logger.error("--> errore durante la cancellazione del calendario rate.", e);
			throw new RuntimeException("errore durante la cancellazione del calendario rate.", e);
		}

		logger.debug("--> Exit cancellaCalendarioRate");
	}

	@Override
	public void cancellaRigaCalendarioRate(RigaCalendarioRate rigaCalendarioRate) {
		logger.debug("--> Enter cancellaRigaCalendarioRate");

		try {
			panjeaDAO.delete(rigaCalendarioRate);
		} catch (DAOException e) {
			logger.error("--> errore durante la cancellazione della riga calendario rate.", e);
			throw new RuntimeException("errore durante la cancellazione della riga calendario rate.", e);
		}

		logger.debug("--> Exit cancellaRigaCalendarioRate");
	}

	@Override
	public CalendarioRate caricaCalendarioRate(CalendarioRate calendarioRate, boolean loadLazy) {
		logger.debug("--> Enter caricaCalendarioRate");

		CalendarioRate calendarioCaricato = null;

		try {
			calendarioCaricato = panjeaDAO.load(CalendarioRate.class, calendarioRate.getId());
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento del calendario rate.", e);
			throw new RuntimeException("errore durante il caricamento del calendario rate.", e);
		}

		if (loadLazy) {
			calendarioCaricato.getCategorieRate().size();
			calendarioCaricato.getClienti().size();
		}

		logger.debug("--> Exit enclosing_method");
		return calendarioCaricato;
	}

	@Override
	public CalendarioRate caricaCalendarioRateAziendale(CategoriaRata categoriaRata) {
		logger.debug("--> Enter caricaCalendarioRateAziendale");

		CalendarioRate calendario = null;

		Query query = panjeaDAO.prepareNamedQuery("CalendarioRate.caricaByAziendaECategoriaRata");
		query.setMaxResults(1);
		List<CategoriaRata> categorieRate = new ArrayList<CategoriaRata>();
		categorieRate.add(categoriaRata);
		query.setParameter("parameCategoriaRata", categorieRate);

		try {
			calendario = (CalendarioRate) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> Non esiste nessun calendario quindi restituisco null.");
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dei calendari rate.", e);
			throw new RuntimeException("errore durante il caricamento dei calendari rate.", e);
		}

		logger.debug("--> Exit caricaCalendarioRateAziendale");
		return calendario;
	}

	@Override
	public CalendarioRate caricaCalendarioRateCliente(ClienteLite clienteLite, CategoriaRata categoriaRata) {
		logger.debug("--> Enter caricaCalendarioRateCliente");

		CalendarioRate calendario = null;

		Query query = panjeaDAO.prepareNamedQuery("CalendarioRate.caricaByClienteECategoriaRata");
		query.setMaxResults(1);
		List<ClienteLite> clienti = new ArrayList<ClienteLite>();
		clienti.add(clienteLite);
		query.setParameter("paramCliente", clienti);
		List<CategoriaRata> categorieRate = new ArrayList<CategoriaRata>();
		categorieRate.add(categoriaRata);
		query.setParameter("parameCategoriaRata", categorieRate);

		try {
			calendario = (CalendarioRate) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> Non esiste nessun calendario quindi restituisco null.");
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dei calendari rate.", e);
			throw new RuntimeException("errore durante il caricamento dei calendari rate.", e);
		}

		logger.debug("--> Exit caricaCalendarioRateCliente");
		return calendario;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CalendarioRate> caricaCalendariRate(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaCalendariRate");

		List<CalendarioRate> calendari = new ArrayList<CalendarioRate>();

		StringBuilder sb = new StringBuilder("select c from CalendarioRate c ");
		if (valueSearch != null) {
			sb.append(" where ").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		sb.append(" order by ");
		sb.append(fieldSearch);
		Query query = panjeaDAO.prepareQuery(sb.toString());
		try {
			calendari = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dei calendari rate.", e);
			throw new RuntimeException("errore durante il caricamento dei calendari rate.", e);
		}

		logger.debug("--> Exit caricaCalendariRate");
		return calendari;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CalendarioRate> caricaCalendariRateAzienda() {
		logger.debug("--> Enter caricaCalendariRateAzienda");

		List<CalendarioRate> calendari = new ArrayList<CalendarioRate>();

		Query query = panjeaDAO.prepareNamedQuery("CalendarioRate.caricaAziendali");

		try {
			calendari = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dei calendari aziendali.", e);
			throw new RuntimeException("errore durante il caricamento dei calendari aziendali.", e);
		}

		logger.debug("--> Exit caricaClientiNonDisponibiliPerCalendario");
		return calendari;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CalendarioRate> caricaCalendariRateCliente(ClienteLite clienteLite) {
		logger.debug("--> Enter caricaCalendariRateCliente");

		List<CalendarioRate> calendari = new ArrayList<CalendarioRate>();

		Query query = panjeaDAO.prepareNamedQuery("CalendarioRate.caricaByCliente");
		query.setParameter("paramCliente", clienteLite);

		try {
			calendari = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dei calendari cliente.", e);
			throw new RuntimeException("errore durante il caricamento dei calendari cliente.", e);
		}

		logger.debug("--> Exit caricaClientiNonDisponibiliPerCalendario");
		return calendari;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClienteLite> caricaClientiNonDisponibiliPerCalendario(List<CategoriaRata> categorieRateDaScludere) {
		logger.debug("--> Enter caricaClientiNonDisponibiliPerCalendario");

		List<ClienteLite> clienti = new ArrayList<ClienteLite>();

		if (categorieRateDaScludere != null && !categorieRateDaScludere.isEmpty()) {
			Query query = panjeaDAO
					.prepareQuery("select distinct cal.clienti from CalendarioRate cal inner join cal.categorieRate cat where cat in (:paramCategorieRate)");
			query.setParameter("paramCategorieRate", categorieRateDaScludere);

			try {
				clienti = panjeaDAO.getResultList(query);
			} catch (Exception e) {
				logger.error("--> errore durante il caricamento dei clienti.", e);
				throw new RuntimeException("errore durante il caricamento dei clienti.", e);
			}
		}
		logger.debug("--> Exit caricaClientiNonDisponibiliPerCalendario");
		return clienti;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaCalendarioRate> caricaRigheCalendarioRate(CalendarioRate calendarioRate) {
		logger.debug("--> Enter caricaRigheCalendarioRate");

		List<RigaCalendarioRate> righeCalendario = new ArrayList<RigaCalendarioRate>();

		Query query = panjeaDAO.prepareNamedQuery("RigaCalendarioRate.caricaByCalendario");
		query.setParameter("paramIdCalendario", calendarioRate.getId());
		try {
			righeCalendario = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento delle righe calendario.", e);
			throw new RuntimeException("errore durante il caricamento delle righe calendario.", e);
		}

		logger.debug("--> Exit caricaRigheCalendarioRate");
		return righeCalendario;
	}

	@Override
	public CalendarioRate salvaCalendarioRate(CalendarioRate calendarioRate) {
		logger.debug("--> Enter salvaCalendarioRate");

		CalendarioRate calendarioRateSalvato = null;

		try {
			calendarioRateSalvato = panjeaDAO.save(calendarioRate);
			calendarioRateSalvato.getCategorieRate().size();
			calendarioRateSalvato.getClienti().size();
		} catch (Exception e) {
			logger.error("--> errore durante il salvataggio del calendario.", e);
			throw new RuntimeException("errore durante il salvataggio del calendario.", e);
		}

		logger.debug("--> Exit salvaCalendarioRate");
		return calendarioRateSalvato;
	}

	@Override
	public RigaCalendarioRate salvaRigaCalendarioRate(RigaCalendarioRate rigaCalendarioRate) {
		logger.debug("--> Enter salvaRigaCalendarioRate");

		RigaCalendarioRate rigaCalendarioRateSalvata = null;
		try {
			rigaCalendarioRateSalvata = panjeaDAO.save(rigaCalendarioRate);
		} catch (Exception e) {
			logger.error("--> errore durante il salvataggio della riga calendario.", e);
			throw new RuntimeException("errore durante il salvataggio della riga calendario.", e);
		}

		logger.debug("--> Exit salvaRigaCalendarioRate");
		return rigaCalendarioRateSalvata;
	}

}
