/**
 * 
 */
package it.eurotn.panjea.offerte.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.offerte.domain.AreaOfferta;
import it.eurotn.panjea.offerte.domain.RigaOfferta;
import it.eurotn.panjea.offerte.manager.interfaces.AreaOffertaRicercaManager;
import it.eurotn.panjea.offerte.util.ParametriRicercaOfferte;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author Leonardo
 * 
 */
@Stateless(name = "Panjea.AreaOffertaRicercaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaOffertaRicercaManager")
public class AreaOffertaRicercaManagerBean implements AreaOffertaRicercaManager {

	private static Logger logger = Logger.getLogger(AreaOffertaManagerBean.class);

	@Resource
	private SessionContext sessionContext;

	@EJB
	private PanjeaDAO panjeaDAO;

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaOfferta> ricercaOfferte(ParametriRicercaOfferte parametriRicercaOfferte) {
		logger.debug("--> Enter ricercaOfferte");
		Map<String, Object> valueParametri = new TreeMap<String, Object>();

		StringBuffer queryHQL = new StringBuffer(" select r from RigaOfferta r join fetch r.areaOfferta");

		// codice azienda
		StringBuffer whereHQL = new StringBuffer(" where r.areaOfferta.documento.codiceAzienda = :paramCodiceAzienda  ");
		JecPrincipal jecPrincipal = (JecPrincipal) sessionContext.getCallerPrincipal();
		// condizioni obbligatorie
		valueParametri.put("paramCodiceAzienda", jecPrincipal.getCodiceAzienda());

		// filtro data documento
		if (parametriRicercaOfferte.getDaDataDocumento() != null) {
			whereHQL.append(" and (r.areaOfferta.documento.dataDocumento >= :paramDaDataDocumento) ");
			valueParametri.put("paramDaDataDocumento",
					PanjeaEJBUtil.getDateTimeToZero(parametriRicercaOfferte.getDaDataDocumento()));
		}
		if (parametriRicercaOfferte.getADataDocumento() != null) {
			whereHQL.append(" and (r.areaOfferta.documento.dataDocumento <= :paramADataDocumento) ");
			valueParametri.put("paramADataDocumento",
					PanjeaEJBUtil.getDateTimeToZero(parametriRicercaOfferte.getADataDocumento()));
		}
		// filtro numero documento
		if (parametriRicercaOfferte.getDaNumeroDocumento() != null) {
			whereHQL.append(" and (r.areaOfferta.documento.codice >= :paramDaNumeroDocumento) ");
			valueParametri.put("paramDaNumeroDocumento", parametriRicercaOfferte.getDaNumeroDocumento());
		}
		if (parametriRicercaOfferte.getANumeroDocumento() != null) {
			whereHQL.append(" and (r.areaOfferta.documento.codice <= :paramANumeroDocumento) ");
			valueParametri.put("paramANumeroDocumento", parametriRicercaOfferte.getANumeroDocumento());
		}

		// riga accettata
		if (parametriRicercaOfferte.getAccettata() != null && parametriRicercaOfferte.getAccettata().size() == 1) {
			whereHQL.append(" and (r.accettata = :paramAccettata) ");
			valueParametri.put("paramAccettata", parametriRicercaOfferte.getAccettata().get(0));
		}

		// articolo
		if (parametriRicercaOfferte.getArticoloLite() != null
				&& parametriRicercaOfferte.getArticoloLite().getId() != null) {
			whereHQL.append(" and (r.articolo.id = :paramIdArticolo) ");
			valueParametri.put("paramIdArticolo", parametriRicercaOfferte.getArticoloLite().getId());
		}
		// filtro entita
		if (parametriRicercaOfferte.getEntita() != null && parametriRicercaOfferte.getEntita().getId() != null) {
			whereHQL.append(" and (r.areaOfferta.sedeEntita.entita.id = :paramIdEntita) ");
			valueParametri.put("paramIdEntita", parametriRicercaOfferte.getEntita().getId());
		}
		// filtro utente
		if (parametriRicercaOfferte.getUtente() != null && parametriRicercaOfferte.getUtente().getId() != null) {
			whereHQL.append(" and (r.areaOfferta.utente.id = :paramIdUtente) ");
			valueParametri.put("paramIdUtente", parametriRicercaOfferte.getUtente().getId());
		}
		// filtro tipi documento
		if (parametriRicercaOfferte.getTipoAreaOfferta() != null
				&& parametriRicercaOfferte.getTipoAreaOfferta().getId() != null) {
			whereHQL.append(" and r.areaOfferta.documento.tipoDocumento.id = :paramIdTipoDocumento ");
			valueParametri.put("paramIdTipoDocumento", parametriRicercaOfferte.getTipoAreaOfferta().getTipoDocumento());
		}
		whereHQL.append(" order by r.areaOfferta.id ");
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

		List<RigaOfferta> listResult = new ArrayList<RigaOfferta>();
		try {
			List<RigaOfferta> righeOfferta = panjeaDAO.getResultList(query);
			if (righeOfferta == null) {
				righeOfferta = new ArrayList<RigaOfferta>();
			}

			// ricerco le aree contabili senza righe
			List<RigaOfferta> areeSenzaRighe = ricercaOfferteSenzaRighe(parametriRicercaOfferte);
			if (areeSenzaRighe == null) {
				areeSenzaRighe = new ArrayList<RigaOfferta>();
			}
			listResult.addAll(areeSenzaRighe);
			listResult.addAll(righeOfferta);
		} catch (Exception e) {
			logger.error("--> errore, impossibile eseguire l'interrogazione di ricercaControlloDocumenti ", e);
			throw new RuntimeException("Impossibile eseguire l'interrogazione per Controllo Documenti ", e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit ricercaOfferte " + listResult.size());
		}
		return listResult;
	}

	/**
	 * Ricerca tutte le aree offerte senza righe.
	 * 
	 * @param parametriRicercaOfferte
	 *            parametri di ricerca
	 * @return aree trovate
	 * @throws DAOException
	 *             eccezione generica
	 */
	private List<RigaOfferta> ricercaOfferteSenzaRighe(ParametriRicercaOfferte parametriRicercaOfferte)
			throws DAOException {
		logger.debug("--> Enter ricercaOfferte");
		Map<String, Object> valueParametri = new TreeMap<String, Object>();

		StringBuffer queryHQL = new StringBuffer(" select a from RigaOfferta r right join r.areaOfferta a ");

		// codice azienda
		StringBuffer whereHQL = new StringBuffer(
				" where r.id is null and a.documento.codiceAzienda = :paramCodiceAzienda  ");
		JecPrincipal jecPrincipal = (JecPrincipal) sessionContext.getCallerPrincipal();
		// condizioni obbligatorie
		valueParametri.put("paramCodiceAzienda", jecPrincipal.getCodiceAzienda());

		// filtro data documento
		if (parametriRicercaOfferte.getDaDataDocumento() != null) {
			whereHQL.append(" and (a.documento.dataDocumento >= :paramDaDataDocumento) ");
			valueParametri.put("paramDaDataDocumento",
					PanjeaEJBUtil.getDateTimeToZero(parametriRicercaOfferte.getDaDataDocumento()));
		}
		if (parametriRicercaOfferte.getADataDocumento() != null) {
			whereHQL.append(" and (a.documento.dataDocumento <= :paramADataDocumento) ");
			valueParametri.put("paramADataDocumento",
					PanjeaEJBUtil.getDateTimeToZero(parametriRicercaOfferte.getADataDocumento()));
		}
		// filtro numero documento
		if (parametriRicercaOfferte.getDaNumeroDocumento() != null) {
			whereHQL.append(" and (a.documento.codice >= :paramDaNumeroDocumento) ");
			valueParametri.put("paramDaNumeroDocumento", parametriRicercaOfferte.getDaNumeroDocumento());
		}
		if (parametriRicercaOfferte.getANumeroDocumento() != null) {
			whereHQL.append(" and (a.documento.codice <= :paramANumeroDocumento) ");
			valueParametri.put("paramANumeroDocumento", parametriRicercaOfferte.getANumeroDocumento());
		}
		// filtro entita
		if (parametriRicercaOfferte.getEntita() != null && parametriRicercaOfferte.getEntita().getId() != null) {
			whereHQL.append(" and (a.sedeEntita.entita.id = :paramIdEntita) ");
			valueParametri.put("paramIdEntita", parametriRicercaOfferte.getEntita().getId());
		}
		// filtro utente
		if (parametriRicercaOfferte.getUtente() != null && parametriRicercaOfferte.getUtente().getId() != null) {
			whereHQL.append(" and (a.utente.id = :paramIdUtente) ");
			valueParametri.put("paramIdUtente", parametriRicercaOfferte.getUtente().getId());
		}
		// filtro tipi documento
		if (parametriRicercaOfferte.getTipoAreaOfferta() != null
				&& parametriRicercaOfferte.getTipoAreaOfferta().getId() != null) {
			whereHQL.append(" and a.documento.tipoDocumento.id = :paramIdTipoDocumento ");
			valueParametri.put("paramIdTipoDocumento", parametriRicercaOfferte.getTipoAreaOfferta().getTipoDocumento());
		}
		whereHQL.append(" order by a.documento.dataDocumento ");

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
		List<RigaOfferta> righeFake = new ArrayList<RigaOfferta>();
		@SuppressWarnings("unchecked")
		List<AreaOfferta> areaOfferta = panjeaDAO.getResultList(query);
		for (AreaOfferta area : areaOfferta) {
			RigaOfferta rigaFake = new RigaOfferta();
			rigaFake.setId(-1);
			rigaFake.setAreaOfferta(area);
			righeFake.add(rigaFake);
		}

		return righeFake;
	}

}
