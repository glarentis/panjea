package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.AreaAccredito;
import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAnticipo;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaBonifico;
import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaInsoluti;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccontoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccreditoAssegnoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccreditoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAnticipiManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAssegnoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaBonificoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaDistintaBancariaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaEffettiManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaInsolutoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaPagamentiManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.IAreaTesoreriaDAO;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AreaTesorieriaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaTesorieriaManager")
public class AreaTesoreriaManagerBean implements AreaTesoreriaManager {
	private static Logger logger = Logger.getLogger(AreaTesoreriaManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@EJB
	private AreaAccreditoManager areaAccreditoManager;

	@EJB
	private AreaAnticipiManager areaAnticipiManager;

	@EJB
	@IgnoreDependency
	private AreaDistintaBancariaManager areaDistintaBancariaManager;

	@EJB
	@IgnoreDependency
	private AreaBonificoManager areaBonificoManager;

	@EJB
	@IgnoreDependency
	private AreaEffettiManager areaEffettiManager;

	@EJB
	@IgnoreDependency
	private AreaInsolutoManager areaInsolutoManager;

	@EJB
	@IgnoreDependency
	private AreaPagamentiManager areaPagamentiManager;

	@EJB
	@IgnoreDependency
	private AreaAccontoManager areaAccontoManager;

	@EJB
	@IgnoreDependency
	private AreaAssegnoManager areaAssegnoManager;

	@EJB
	@IgnoreDependency
	private AreaAccreditoAssegnoManager areaAccreditoAssegnoManager;

	@Override
	public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate) {

		IAreaTesoreriaDAO areaTesoreriaDAO = getAreaTesoreriaDAO(areaTesoreria);

		if (areaTesoreriaDAO != null) {
			areaTesoreriaDAO.cancellaAreaTesoreria(areaTesoreria, deleteAreeCollegate);
		}
	}

	@Override
	public void cancellaAreaTesoreria(Documento documento, boolean deleteAreeCollegate) {
		AreaTesoreria areaTesoreria = caricaAreaTesoreria(documento);
		if (areaTesoreria.getId() != null) {
			cancellaAreaTesoreria(areaTesoreria, deleteAreeCollegate);
		}
	}

	@Override
	public void cancellaAreeTesorerie(List<AreaTesoreria> listAree, boolean deleteAreeCollegate) {

		// Creo una mappa di tutte le aree con chiave classe area e valore lista di aree perchè devono
		// essere cancellate nel seguente ordine:
		// area accredito
		// area distinta
		// area effetti
		Map<String, List<AreaTesoreria>> mapAree = new java.util.HashMap<String, List<AreaTesoreria>>();

		for (AreaTesoreria areaTesoreria : listAree) {

			List<AreaTesoreria> areeTesorerie = new ArrayList<AreaTesoreria>();
			String tipoArea = "";
			if (areaTesoreria instanceof AreaAccredito) {
				tipoArea = AreaAccredito.class.getName();
			} else if (areaTesoreria instanceof AreaDistintaBancaria) {
				tipoArea = AreaDistintaBancaria.class.getName();
			} else if (areaTesoreria instanceof AreaEffetti) {
				tipoArea = AreaEffetti.class.getName();
			} else if (areaTesoreria instanceof AreaPagamenti) {
				tipoArea = AreaPagamenti.class.getName();
			}

			if (mapAree.containsKey(tipoArea)) {
				areeTesorerie = mapAree.get(tipoArea);
			}
			areeTesorerie.add(areaTesoreria);
			mapAree.put(tipoArea, areeTesorerie);
		}

		// cancello le aree nell'ordine corretto
		if (mapAree.containsKey(AreaAccredito.class.getName())) {
			for (AreaTesoreria areaTesoreria : mapAree.get(AreaAccredito.class.getName())) {
				cancellaAreaTesoreria(areaTesoreria, deleteAreeCollegate);
			}
		} else if (mapAree.containsKey(AreaDistintaBancaria.class.getName())) {
			for (AreaTesoreria areaTesoreria : mapAree.get(AreaDistintaBancaria.class.getName())) {
				cancellaAreaTesoreria(areaTesoreria, deleteAreeCollegate);
			}
		} else if (mapAree.containsKey(AreaEffetti.class.getName())) {
			for (AreaTesoreria areaTesoreria : mapAree.get(AreaEffetti.class.getName())) {
				cancellaAreaTesoreria(areaTesoreria, deleteAreeCollegate);
			}
		} else if (mapAree.containsKey(AreaPagamenti.class.getName())) {
			for (AreaTesoreria areaTesoreria : mapAree.get(AreaPagamenti.class.getName())) {
				cancellaAreaTesoreria(areaTesoreria, deleteAreeCollegate);
			}
		}
	}

	@Override
	public AreaTesoreria caricaAreaTesoreria(AreaTesoreria areaTesoreria) throws ObjectNotFoundException {
		AreaTesoreria result = null;

		IAreaTesoreriaDAO areaTesoreriaDAO = getAreaTesoreriaDAO(areaTesoreria);

		if (areaTesoreriaDAO != null) {
			result = areaTesoreriaDAO.caricaAreaTesoreria(areaTesoreria.getId());
		}

		return result;
	}

	@Override
	public AreaTesoreria caricaAreaTesoreria(Documento documento) {
		logger.debug("--> Enter caricaAreaTesoreria");
		Query query = panjeaDAO.prepareNamedQuery("AreaTesoreria.caricaByDocumento");
		query.setParameter("paramIdDocumento", documento.getId());
		AreaTesoreria areaTesoreria = null;
		try {
			// carico con una query l'area tesoreria del documento passato come parametro e
			// successivamente carico l'area tesoreria con il metodo caricaAreaTasoreria perche'
			// devono essere inizializzate le collezioni ( pagament per area pagamenti, effetti su area effetti, ecc...)
			((Session) panjeaDAO.getEntityManager().getDelegate()).clear();
			areaTesoreria = (AreaTesoreria) panjeaDAO.getSingleResult(query);
			areaTesoreria = caricaAreaTesoreria(areaTesoreria);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> AreaTesoreria non trovata per il documento " + documento.getId()
					+ " restituisco una istanza vuota !");
			areaTesoreria = new AreaTesoreria();
		} catch (Exception e) {
			logger.debug("--> Errore in caricaAreaTesoreria ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaAreaTesoreria");
		return areaTesoreria;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaTesoreria> caricaAreeCollegate(AreaTesoreria areaTesoreria) {
		logger.debug("--> Enter caricaAreeCollegate");

		Set<AreaTesoreria> areeCollegate = new TreeSet<AreaTesoreria>();

		if (areaTesoreria instanceof AreaEffetti) {
			AreaEffetti areaEffetti = null;
			try {
				areaEffetti = (AreaEffetti) caricaAreaTesoreria(areaTesoreria); // panjeaDAO.load(areaTesoreria.getClass(),
																				// areaEffetti.getId());
			} catch (Exception e) {
				logger.error("--> errore durante il caricamento dell'area effetti con id " + areaTesoreria.getId(), e);
				throw new RuntimeException("errore durante il caricamento dell'area effetti con id "
						+ areaEffetti.getId(), e);
			}

			// cerco subito eventuali aree accredito o insoluto sugli effetti dell'area
			for (Effetto effetto : areaEffetti.getEffetti()) {
				if (effetto.getAreaAccredito() != null) {
					areeCollegate.add(effetto.getAreaAccredito());
				}

				if (effetto.getAreaInsoluti() != null) {
					areeCollegate.add(effetto.getAreaInsoluti());
				}
			}

			// cerco se esiste una distinta collegata all'area effetti
			Query query = panjeaDAO.prepareNamedQuery("AreaDistintaBancaria.caricaByAreaCollegata");
			query.setParameter("paramIdAreaEffettiCollegata", areaEffetti.getId());

			AreaDistintaBancaria areaDistinta = null;
			try {
				areaDistinta = (AreaDistintaBancaria) panjeaDAO.getSingleResult(query);
			} catch (DAOException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("--> nessuna distinta bancaria collegata all'area effetti.");
				}
			} catch (Exception e) {
				logger.error("-->errore durante il caricamento della distinta bancaria collegata all'area effetti", e);
				throw new RuntimeException(
						"errore durante il caricamento della distinta bancaria collegata all'area effetti", e);
			}
			if (areaDistinta != null) {
				areeCollegate.add(areaDistinta);

				// cerco se esistono aree anticipi collegate alla distinta bancaria
				query = panjeaDAO.prepareNamedQuery("AreaAnticipo.caricaByAreaDistintaBancaria");
				query.setParameter("paramIdAreaDistinta", areaEffetti.getId());

				List<AreaEffetti> areeAnticipo = null;
				try {
					areeAnticipo = panjeaDAO.getResultList(query);
				} catch (Exception e) {
					logger.error(
							"-->errore durante il caricamento delle aree anticipo per la distinta "
									+ areaDistinta.getId(), e);
					throw new RuntimeException("errore durante il caricamento delle aree anticipo per la distinta "
							+ areaDistinta.getId(), e);
				}
				if (areeAnticipo != null) {
					areeCollegate.addAll(areeAnticipo);
				}
			}
		}

		List<AreaTesoreria> listAree = new ArrayList<AreaTesoreria>();
		listAree.addAll(areeCollegate);
		logger.debug("--> Exit caricaAreeCollegate");
		return listAree;
	}

	@Override
	public Long caricaNumeroAreeCollegate(AreaEffetti areaEffetti) {
		logger.debug("--> Enter caricaNumeroAreeCollegate");

		Long numeroAreeCollegate = new Long(0);

		numeroAreeCollegate = numeroAreeCollegate + areaEffettiManager.caricaNumeroAreeCollegate(areaEffetti);

		logger.debug("--> Exit caricaNumeroAreeCollegate");
		return numeroAreeCollegate;
	}

	@Override
	public AreaTesoreria checkAreaTesoreria(Documento documento) {
		logger.debug("--> Enter checkAreaTesoreria");
		Query query = panjeaDAO.prepareNamedQuery("AreaTesoreria.checkByDocumento");
		query.setParameter("paramIdDocumento", documento.getId());
		AreaTesoreria areaTesoreria = null;
		try {
			areaTesoreria = (AreaTesoreria) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			return areaTesoreria;
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit checkAreaTesoreria");
		return areaTesoreria;
	}

	/**
	 * 
	 * @param areaTesoreria
	 *            {@link AreaTesoreria} per la quale recuperare il DAO
	 * @return Dao per gestire una classe che eredita da {@link AreaTesoreria}
	 */
	private IAreaTesoreriaDAO getAreaTesoreriaDAO(AreaTesoreria areaTesoreria) {

		IAreaTesoreriaDAO areaTesoreriaDAO = null;

		if (areaTesoreria instanceof AreaAssegno) {
			areaTesoreriaDAO = areaAssegnoManager;
		} else if (areaTesoreria instanceof AreaAccreditoAssegno) {
			areaTesoreriaDAO = areaAccreditoAssegnoManager;
		} else if (areaTesoreria instanceof AreaBonifico) {
			areaTesoreriaDAO = areaBonificoManager;
		} else if (areaTesoreria instanceof AreaPagamenti) {
			areaTesoreriaDAO = areaPagamentiManager;
		} else if (areaTesoreria instanceof AreaAcconto) {
			areaTesoreriaDAO = areaAccontoManager;
		} else if (areaTesoreria instanceof AreaAnticipo) {
			areaTesoreriaDAO = areaAnticipiManager;
		} else if (areaTesoreria instanceof AreaInsoluti) {
			areaTesoreriaDAO = areaInsolutoManager;
		} else if (areaTesoreria instanceof AreaAccredito) {
			areaTesoreriaDAO = areaAccreditoManager;
		} else if (areaTesoreria instanceof AreaDistintaBancaria) {
			areaTesoreriaDAO = areaDistintaBancariaManager;
		} else if (areaTesoreria instanceof AreaEffetti) {
			areaTesoreriaDAO = areaEffettiManager;
		} else {
			throw new IllegalArgumentException("class area tesoreria " + areaTesoreria != null ? areaTesoreria
					.getClass().getName() : " NULL");
		}

		return areaTesoreriaDAO;
	}

	@Override
	public List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria) {

		IAreaTesoreriaDAO areaTesoreriaDAO = getAreaTesoreriaDAO(areaTesoreria);

		return areaTesoreriaDAO.getAreeEmissioneEffetti(areaTesoreria);
	}

	/**
	 * recupera {@link JecPrincipal} dal {@link SessionContext}.
	 * 
	 * @return jecPrincipal
	 */
	private JecPrincipal getJecPrincipal() {
		logger.debug("--> Enter getJecPrincipal");
		return (JecPrincipal) context.getCallerPrincipal();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaTesoreria> ricercaAreeTesorerie(ParametriRicercaAreeTesoreria parametriRicercaAreeTesoreria) {
		logger.debug("--> Enter ricercaAreeTesorerie");
		Map<String, Object> valueParametri = new TreeMap<String, Object>();
		StringBuffer queryHQL = new StringBuffer("select at from AreaTesoreria at ");
		queryHQL.append("inner join fetch at.documento doc left join fetch doc.entita ent left join fetch ent.anagrafica ana ");
		StringBuffer whereHQL = new StringBuffer(
				" where doc.codiceAzienda=:paramCodiceAzienda and at.class != it.eurotn.panjea.tesoreria.domain.AreaAcconto ");
		JecPrincipal jecPrincipal = getJecPrincipal();
		// condizioni obbligatorie
		valueParametri.put("paramCodiceAzienda", jecPrincipal.getCodiceAzienda());

		// condizioni variabili da parametri
		// Filtro da data documento
		if (parametriRicercaAreeTesoreria.getPeriodo().getDataIniziale() != null) {
			whereHQL.append(" and (doc.dataDocumento>=:paramDaDataDocumento ) ");
			valueParametri.put("paramDaDataDocumento",
					PanjeaEJBUtil.getDateTimeToZero(parametriRicercaAreeTesoreria.getPeriodo().getDataIniziale()));
		}
		// Filtro a data documento
		if (parametriRicercaAreeTesoreria.getPeriodo().getDataFinale() != null) {
			whereHQL.append(" and (doc.dataDocumento<=:paramADataDocumento ) ");
			valueParametri.put("paramADataDocumento",
					PanjeaEJBUtil.getDateTimeToZero(parametriRicercaAreeTesoreria.getPeriodo().getDataFinale()));
		}

		// Filtro da numero documento
		if (parametriRicercaAreeTesoreria.getDaNumeroDocumento() != null) {
			whereHQL.append(" and (doc.codice>=:paramDaNumeroDocumento ) ");
			valueParametri.put("paramDaNumeroDocumento", parametriRicercaAreeTesoreria.getDaNumeroDocumento());
		}
		// Filtro a numero documento
		if (parametriRicercaAreeTesoreria.getANumeroDocumento() != null) {
			whereHQL.append(" and (doc.codice<=:paramANumeroDocumento ) ");
			valueParametri.put("paramANumeroDocumento", parametriRicercaAreeTesoreria.getANumeroDocumento());
		}

		// Filtro tipoAreaPartita
		if (parametriRicercaAreeTesoreria.getTipoAreaPartita() != null
				&& parametriRicercaAreeTesoreria.getTipoAreaPartita().getId() != null) {
			whereHQL.append(" and (at.tipoAreaPartita.id=:paramIdTipoAreaPartita) ");
			valueParametri.put("paramIdTipoAreaPartita", parametriRicercaAreeTesoreria.getTipoAreaPartita().getId());
		}

		// Filtro rapporto bacario azienda
		if (parametriRicercaAreeTesoreria.getRapportoBancarioAzienda() != null
				&& parametriRicercaAreeTesoreria.getRapportoBancarioAzienda().getId() != null) {
			whereHQL.append(" and (doc.rapportoBancarioAzienda.id=:paramIdRapportoBancario) ");
			valueParametri.put("paramIdRapportoBancario", parametriRicercaAreeTesoreria.getRapportoBancarioAzienda()
					.getId());
		}
		whereHQL.append(" order by doc.dataDocumento ");
		Query query = panjeaDAO.prepareQuery(queryHQL.toString() + whereHQL.toString());
		Set<String> set = valueParametri.keySet();
		for (String key : set) {
			Object value = valueParametri.get(key);
			if (value instanceof Date) {
				Date valueDate = (Date) value;
				query.setParameter(key, valueDate, TemporalType.DATE);
			} else {
				query.setParameter(key, valueParametri.get(key));
			}
		}

		List<AreaTesoreria> areeTesoreria;
		try {
			areeTesoreria = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante la ricerca delle aree tesoreria ", e);
			throw new RuntimeException("Errore durante la ricerca delle aree tesoreria ", e);
		}

		return areeTesoreria;
	}

	@Override
	public AreaTesoreria salvaAreaTesoreria(AreaTesoreria areaTesoreria) {
		logger.debug("--> Enter salvaAreaTesoreria");

		AreaTesoreria areaTesoreriaSalvata = null;

		try {
			areaTesoreriaSalvata = panjeaDAO.save(areaTesoreria);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio dell'area tesoreria", e);
			throw new RuntimeException("Errore durante il salvataggio dell'area tesoreria", e);
		}

		logger.debug("--> Exit salvaAreaTesoreria");
		return areaTesoreriaSalvata;
	}
}
