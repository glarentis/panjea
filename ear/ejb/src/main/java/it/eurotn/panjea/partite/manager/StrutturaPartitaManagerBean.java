package it.eurotn.panjea.partite.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrategiaCalcoloPartita;
import it.eurotn.panjea.partite.domain.StrategiaCalcoloPartitaConti;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.domain.StrutturaPartitaLite;
import it.eurotn.panjea.partite.manager.interfaces.StrutturaPartitaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
@Stateless(name = "Panjea.StrutturaPartitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.StrutturaPartitaManager")
public class StrutturaPartitaManagerBean implements StrutturaPartitaManager {

	private static Logger logger = Logger.getLogger(StrutturaPartitaManagerBean.class);

	@Resource
	private SessionContext context;

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	protected PanjeaDAO panjeaDAO;

	@Override
	public void cancellaCategoriaRata(CategoriaRata categoriaRata) {
		logger.debug("--> Enter cancellaCategoriaRata");
		try {
			panjeaDAO.delete(categoriaRata);
		} catch (Exception e) {
			logger.error("--> Errore delete categoria rata");
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaCategoriaRata");
	}

	@Override
	public void cancellaRigaStrutturaPartite(RigaStrutturaPartite rigaStrutturaPartite) {
		logger.debug("--> Enter cancellaRigaStrutturaPartite");
		try {
			panjeaDAO.delete(rigaStrutturaPartite);
		} catch (Exception e) {
			logger.error("--> Errore delete riga struttura partita");
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaRigaStrutturaPartite");
	}

	/**
	 * Cancella la lista di righe delle struttura partita.
	 * 
	 * @param strutturaPartita
	 *            la struttura di cui cancellare le righe
	 */
	private void cancellaRigheStrutturaPartita(StrutturaPartita strutturaPartita) {
		logger.debug("--> Enter cancellaRigheStrutturaPartita");
		String hql = "delete from RigaStrutturaPartite r where r.strutturaPartita.id=:paramIdStrutturaPartita";
		Query query = panjeaDAO.prepareQuery(hql);
		query.setParameter("paramIdStrutturaPartita", strutturaPartita.getId());
		query.executeUpdate();
		logger.debug("--> Exit cancellaRigheStrutturaPartita");
	}

	@Override
	public void cancellaStrutturaPartita(StrutturaPartita strutturaPartite) {
		logger.debug("--> Enter cancellaStrutturaPartite");
		try {
			panjeaDAO.delete(strutturaPartite);
		} catch (Exception e) {
			logger.error("--> Errore delete struttura partite");
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit cancellaStrutturaPartite");
	}

	@Override
	public CategoriaRata caricaCategoriaRata(Integer idCategoriaRata) {
		logger.debug("--> Enter caricaCategoriaRata");
		CategoriaRata categoriaRata = null;
		try {
			categoriaRata = panjeaDAO.load(CategoriaRata.class, idCategoriaRata);
		} catch (ObjectNotFoundException e) {
			logger.error("--> Errore nel caricamento Categoria Rata");
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaCategoriaRata");
		return categoriaRata;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CategoriaRata> caricaCategorieRata(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaCategorieRata");

		StringBuilder sb = new StringBuilder("from CategoriaRata cr where cr.codiceAzienda = :paramCodiceAzienda ");
		if (valueSearch != null) {
			sb.append(" and ").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		sb.append(" order by ");
		sb.append(fieldSearch);

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramCodiceAzienda", getAzienda());
		List<CategoriaRata> list = query.getResultList();
		logger.debug("--> Exit caricaCategorieRata");
		return list;
	}

	@Override
	public StrutturaPartita caricaStrutturaPartita(Integer idStruttura) {
		logger.debug("--> Enter caricaStrutturaPartita");
		StrutturaPartita strutturaPartita = null;
		try {
			strutturaPartita = panjeaDAO.load(StrutturaPartita.class, idStruttura);
			strutturaPartita.getRigheStrutturaPartita().size();
			// Se la strategia è di tipo conti allora inizializzo la lista dei
			// conti
			// che è lazy
			// prima strategia

			if (strutturaPartita.getPrimaStrategiaCalcoloPartita() != null
					&& StrategiaCalcoloPartita.CONTI.equals(strutturaPartita.getPrimaStrategiaCalcoloPartita()
							.getCodiceStrategia())) {
				((StrategiaCalcoloPartitaConti) (strutturaPartita.getPrimaStrategiaCalcoloPartita())).getContiPartita()
						.size();
			}
			// seconda strategia
			if (strutturaPartita.getSecondaStrategiaCalcoloPartita() != null
					&& StrategiaCalcoloPartita.CONTI.equals(strutturaPartita.getSecondaStrategiaCalcoloPartita()
							.getCodiceStrategia())) {
				((StrategiaCalcoloPartitaConti) (strutturaPartita.getSecondaStrategiaCalcoloPartita()))
						.getContiPartita().size();
			}

		} catch (Exception e) {
			logger.error("--> Errore in caricamento StrutturaPartite con tipo ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaStrutturaPartita");
		return strutturaPartita;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StrutturaPartitaLite> caricaStrutturePartita() {
		logger.debug("--> Enter caricaStrutturePartita");
		Query query = panjeaDAO.prepareNamedQuery("StrutturaPartitaLite.caricaAll");
		query.setParameter("paramCodiceAzienda", getAzienda());
		List<StrutturaPartitaLite> strutturePartita = query.getResultList();
		return strutturePartita;
	}

	@Override
	public List<RigaStrutturaPartite> creaRigheStrutturaPartite(StrutturaPartita strutturaPartita, int numeroRate,
			int intervallo) {
		logger.debug("--> Enter creaRateStrutturaPartitecon n rate e intervallo " + numeroRate + " " + intervallo);
		cancellaRigheStrutturaPartita(strutturaPartita);
		List<RigaStrutturaPartite> righe = new ArrayList<RigaStrutturaPartite>();
		int progIntervallo = intervallo;
		BigDecimal cento = Importo.HUNDRED;
		BigDecimal numeroR = new BigDecimal(numeroRate);
		// MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
		BigDecimal percentuale = cento.divide(numeroR, 4, RoundingMode.HALF_UP);
		BigDecimal progressivo = BigDecimal.ZERO;
		for (int i = 0; i < numeroRate; i++) {
			if (i == numeroRate - 1) {
				// sull'ultimo sottraggo il progresivo da 100
				percentuale = cento.subtract(progressivo);
			} else {
				progressivo = progressivo.add(percentuale);
			}
			RigaStrutturaPartite rigaStrutturaPartite = new RigaStrutturaPartite();
			rigaStrutturaPartite.setNumeroRata(i + 1);
			rigaStrutturaPartite.setIntervallo(progIntervallo);
			rigaStrutturaPartite.setPrimaPercentuale(percentuale);
			rigaStrutturaPartite.setSecondaPercentuale(BigDecimal.ZERO);
			rigaStrutturaPartite.setStrutturaPartita(strutturaPartita);

			RigaStrutturaPartite rigaSalvata = salvaRigaStrutturaPartite(rigaStrutturaPartite);

			righe.add(rigaSalvata);
			logger.debug("Aggiunto partita" + i);
			progIntervallo += intervallo;
		}
		logger.debug("--> Exit creaRateStrutturaPartite");
		return righe;

	}

	/**
	 * @return the azienda to get
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@Override
	public CategoriaRata salvaCategoriaRata(CategoriaRata categoriaRata) {
		logger.debug("--> Enter salvaCategoriaRata");
		CategoriaRata categoriaRataReturn;
		try {
			if (categoriaRata.isNew()) {
				categoriaRata.setCodiceAzienda(getAzienda());
			}
			categoriaRataReturn = panjeaDAO.save(categoriaRata);
		} catch (Exception e) {
			logger.error("--> Errore nel salvataggio di categira rata");
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaCategoriaRata");
		return categoriaRataReturn;
	}

	@Override
	public RigaStrutturaPartite salvaRigaStrutturaPartite(RigaStrutturaPartite rigaStrutturaPartite) {
		logger.debug("--> Enter salvaCategoriaRata");
		RigaStrutturaPartite rigaStrutturaPartita;
		try {
			rigaStrutturaPartita = panjeaDAO.save(rigaStrutturaPartite);
		} catch (Exception e) {
			logger.error("--> Errore nel salvataggio di categira rata");
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaCategoriaRata");
		return rigaStrutturaPartita;
	}

	@Override
	public StrutturaPartita salvaStrutturaPartita(StrutturaPartita strutturaPartita) {
		logger.debug("--> Enter salvaStrutturaPartite");
		StrutturaPartita strutturaPartitaReturn;
		try {
			if (strutturaPartita.isNew()) {
				strutturaPartita.setCodiceAzienda(getAzienda());
			}

			// Carico la struttura per tenere traccia se è cambiata la
			// strategia.
			// la delete-orphan non funziona con la one-to-one allora la devo
			// fare a mano
			// :NB:Non mi preoccupo di fare una select in più perchè la
			// struttura partita viene salvata una alla volta
			StrategiaCalcoloPartita primaStrategiaOld = null;
			StrategiaCalcoloPartita secondaStrategiaOld = null;
			// Se è nuova non mi serve verificare un eventuale cambio di
			// strategia
			if (strutturaPartita.getId() != null) {
				StrutturaPartita strutturaPartitaOld = panjeaDAO.load(StrutturaPartita.class, strutturaPartita.getId());
				primaStrategiaOld = strutturaPartitaOld.getPrimaStrategiaCalcoloPartita();
				secondaStrategiaOld = strutturaPartitaOld.getSecondaStrategiaCalcoloPartita();
			}

			strutturaPartitaReturn = panjeaDAO.save(strutturaPartita);

			// Se le strategie sono diverse, in caso avessi cambiato da Formula
			// a Conto, devo cancellarle altrimenti
			// rimarrebbero orfane

			if (primaStrategiaOld != null && primaStrategiaOld.getId() != null
					&& !primaStrategiaOld.equals(strutturaPartitaReturn.getPrimaStrategiaCalcoloPartita())) {
				logger.debug("--> Cancello la strategia orfana perchè non più utilizzata da prima strategia:"
						+ primaStrategiaOld);
				try {
					panjeaDAO.delete(primaStrategiaOld);
				} catch (RuntimeException e) {
					logger.error("--> Errore nel cancellare la strategia orfana", e);
					throw new RuntimeException(e);
				}
			}

			if (secondaStrategiaOld != null && secondaStrategiaOld.getId() != null
					&& !secondaStrategiaOld.equals(strutturaPartitaReturn.getSecondaStrategiaCalcoloPartita())) {
				logger.debug("--> Cancello la strategia orfana perchè non più utilizzata da prima strategia:"
						+ secondaStrategiaOld);
				try {
					panjeaDAO.delete(secondaStrategiaOld);
				} catch (RuntimeException e) {
					logger.error("--> Errore nel cancellare la strategia orfana", e);
					throw new RuntimeException(e);
				}
			}

		} catch (Exception e) {
			logger.error("--> Errore nel salvataggio struttura partite e rige struttura", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaStrutturaPartite");
		return strutturaPartitaReturn;
	}

}
