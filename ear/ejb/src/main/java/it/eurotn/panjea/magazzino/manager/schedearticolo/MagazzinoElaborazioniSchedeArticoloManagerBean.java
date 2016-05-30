/**
 *
 */
package it.eurotn.panjea.magazzino.manager.schedearticolo;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces.MagazzinoElaborazioniSchedeArticoloManager;
import it.eurotn.panjea.magazzino.util.ElaborazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaElaborazioni;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.MagazzinoElaborazioniSchedeArticoloManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MagazzinoElaborazioniSchedeArticoloManager")
public class MagazzinoElaborazioniSchedeArticoloManagerBean implements MagazzinoElaborazioniSchedeArticoloManager {

	private static Logger logger = Logger.getLogger(MagazzinoElaborazioniSchedeArticoloManagerBean.class);

	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	@SuppressWarnings("unchecked")
	@Override
	public List<ElaborazioneSchedaArticoloDTO> caricaElaborazioniSchedeArticolo(ParametriRicercaElaborazioni parametri) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct noteSchede.note as nota,art.id as idArticolo,art.codice as codiceArticolo,art.descrizioneLinguaAziendale as descrizioneArticolo, schede.anno as anno, schede.mese as mese,schede.stato as stato ");
		sb.append("from maga_schede_articolo_note noteSchede  inner join maga_schede_articolo schede on noteSchede.id = schede.id ");
		sb.append("					    		inner join maga_articoli art on art.id = schede.articolo_id ");
		sb.append("where schede.anno = :anno and schede.mese = :mese and schede.codiceAzienda = :codiceAzienda ");
		if (!parametri.getNota().isEmpty()) {
			sb.append(" and noteSchede.note = :note ");
		}
		sb.append("order by noteSchede.note ");

		QueryImpl query = (QueryImpl) panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
		SQLQuery sqlQuery = ((SQLQuery) query.getHibernateQuery());
		sqlQuery.setResultTransformer(Transformers.aliasToBean((ElaborazioneSchedaArticoloDTO.class)));
		sqlQuery.addScalar("nota");
		sqlQuery.addScalar("idArticolo");
		sqlQuery.addScalar("codiceArticolo");
		sqlQuery.addScalar("descrizioneArticolo");
		sqlQuery.addScalar("anno");
		sqlQuery.addScalar("mese");
		sqlQuery.addScalar("stato", Hibernate.INTEGER);

		query.setParameter("anno", parametri.getAnno());
		query.setParameter("mese", parametri.getMese());
		query.setParameter("codiceAzienda", getAzienda());
		if (!parametri.getNota().isEmpty()) {
			query.setParameter("note", parametri.getNota());
		}

		List<ElaborazioneSchedaArticoloDTO> elaborazioni = new ArrayList<ElaborazioneSchedaArticoloDTO>();
		try {
			elaborazioni = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento delle elaborazioni.", e);
			throw new RuntimeException("errore durante il caricamento delle elaborazioni.", e);
		}

		return elaborazioni;
	}

	@Override
	public int caricaNumeroSchedeArticoloInCodaDiElaborazione() {
		Long messageCount = new Long(0);
		try {
			ObjectName codaMgr = new ObjectName(
					"org.hornetq:module=JMS,type=Queue,name=\"Panjea.MagazzinoSchedeArticoloManager\"");
			MBeanServer server = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);

			messageCount = (Long) server.getAttribute(codaMgr, "MessageCount");
		} catch (Exception e) {
			logger.error("---> Errore nel flush della cache di JBoss", e);
			throw new RuntimeException(e);
		}
		return messageCount.intValue();
	}

	/**
	 *
	 * @return codice Azienda loggata
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@Override
	@RolesAllowed("gestioneSchedeArticolo")
	public void modificaDescrizioneElaborazione(String descrizioneOld, String descrizioneNew) {

		// controllo che non ci sia già la descrizione
		StringBuilder sb = new StringBuilder();
		sb.append("select count(id) from maga_schede_articolo_note where note = '");
		sb.append(descrizioneNew);
		sb.append("'");

		Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());

		Number numeroDescrizioni = ((Number) query.getSingleResult());
		if (numeroDescrizioni.intValue() > 0) {
			throw new GenericException("Esiste già una elaborazione con descrizione: " + descrizioneNew);
		}

		sb = new StringBuilder();
		sb.append("update maga_schede_articolo_note set note = '");
		sb.append(descrizioneNew);
		sb.append("' where note = '");
		sb.append(descrizioneOld);
		sb.append("'");

		query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());

		try {
			panjeaDAO.executeQuery(query);
		} catch (Exception e) {
			logger.error("--> errore durante la modifica del testo dell'elaborazione " + descrizioneOld, e);
			throw new RuntimeException("errore durante la modifica del testo dell'elaborazione " + descrizioneOld, e);
		}
	}

}
