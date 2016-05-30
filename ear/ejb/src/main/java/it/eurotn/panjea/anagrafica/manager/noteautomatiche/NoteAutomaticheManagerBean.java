/**
 *
 */
package it.eurotn.panjea.anagrafica.manager.noteautomatiche;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.anagrafica.manager.noteautomatiche.interfaces.NoteAutomaticheManager;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(mappedName = "Panjea.NoteAutomaticheManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.NoteAutomaticheManagerBean")
public class NoteAutomaticheManagerBean implements NoteAutomaticheManager {

	private static Logger logger = Logger.getLogger(NoteAutomaticheManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@Override
	public void cancellaNotaAutomatica(NotaAutomatica notaAutomatica) {
		logger.debug("--> Enter cancellaNotaAutomatica");

		try {
			panjeaDAO.delete(notaAutomatica);
		} catch (Exception e) {
			logger.error("--> errore durante la cancellazione della nota automatica " + notaAutomatica.getId(), e);
			throw new RuntimeException("errore durante la cancellazione della nota automatica "
					+ notaAutomatica.getId(), e);
		}

		logger.debug("--> Exit cancellaNotaAutomatica");
	}

	@Override
	public NotaAutomatica caricaNotaAutomatica(Integer idNotaAutomatica) {
		logger.debug("--> Enter caricaNotaAutomatica");
		NotaAutomatica notaAutomaticaCaricata;
		try {
			notaAutomaticaCaricata = panjeaDAO.load(NotaAutomatica.class, idNotaAutomatica);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento della nota automatica.", e);
			throw new RuntimeException("errore durante il caricamento della nota automatica.", e);
		}
		logger.debug("--> Exit caricaNotaAutomatica");
		return notaAutomaticaCaricata;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NotaAutomatica> caricaNoteAutomatiche(Date data, Documento documento) {
		logger.debug("--> Enter caricaNoteAutomatiche");

		List<NotaAutomatica> noteCaricate = new ArrayList<NotaAutomatica>();

		StringBuilder sb = new StringBuilder(300);
		sb.append(NoteAutomaticheQueryBuilder.getQuery());
		sb.append(NoteAutomaticheQueryBuilder.getJoinForArticoli());
		sb.append(" where ");
		sb.append(NoteAutomaticheQueryBuilder.getWhereForDocumento(documento));
		sb.append(NoteAutomaticheQueryBuilder.getWhereForArticoli(documento));
		sb.append(" and ");
		sb.append(NoteAutomaticheQueryBuilder.getWhereForDate(data));
		sb.append(NoteAutomaticheQueryBuilder.getOrderByQuery());

		Query query = panjeaDAO.prepareSQLQuery(sb.toString(), NotaAutomatica.class,
				NoteAutomaticheQueryBuilder.getQueryScalar());
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("nota", Hibernate.STRING);

		try {
			noteCaricate = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento delle note automatiche", e);
			throw new RuntimeException("errore durante il caricamento delle note automatiche", e);
		}

		logger.debug("--> Exit caricaNoteAutomatiche");
		return noteCaricate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NotaAutomatica> caricaNoteAutomatiche(Integer idEntita, Integer idSedeEntita) {
		logger.debug("--> Enter caricaNoteAutomatiche");

		List<NotaAutomatica> noteCaricate = new ArrayList<NotaAutomatica>();

		StringBuilder sb = new StringBuilder(300);
		sb.append(NoteAutomaticheQueryBuilder.getQuery());
		if (idSedeEntita != null) {
			sb.append("where na.sedeEntita_id =");
			sb.append(idSedeEntita);
		} else if (idEntita != null) {
			sb.append("where na.entita_id =");
			sb.append(idEntita);
		}
		sb.append(NoteAutomaticheQueryBuilder.getOrderByQuery());

		Query query = panjeaDAO.prepareSQLQuery(sb.toString(), NotaAutomatica.class,
				NoteAutomaticheQueryBuilder.getQueryScalar());
		((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar("nota", Hibernate.STRING);

		try {
			noteCaricate = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento delle note automatiche", e);
			throw new RuntimeException("errore durante il caricamento delle note automatiche", e);
		}

		logger.debug("--> Exit caricaNoteAutomatiche");
		return noteCaricate;
	}

	@Override
	public NotaAutomatica salvaNotaAutomatica(NotaAutomatica notaAutomatica) {
		logger.debug("--> Enter salvaNotaAutomatica");

		List<ArticoloLite> articoli = new ArrayList<ArticoloLite>();
		for (ArticoloLite articolo : notaAutomatica.getArticoli()) {
			articolo = panjeaDAO.loadLazy(ArticoloLite.class, articolo.getId());
			articoli.add(articolo);
		}
		notaAutomatica.getArticoli().clear();
		notaAutomatica.getArticoli().addAll(articoli);

		NotaAutomatica notaAutomaticaSalvata;
		try {
			notaAutomaticaSalvata = panjeaDAO.save(notaAutomatica);
		} catch (DAOException e) {
			logger.error("--> errore durante il salvataggio della nota automatica.", e);
			throw new RuntimeException("errore durante il salvataggio della nota automatica.", e);
		}

		logger.debug("--> Exit salvaNotaAutomatica");
		return notaAutomaticaSalvata;
	}
}
