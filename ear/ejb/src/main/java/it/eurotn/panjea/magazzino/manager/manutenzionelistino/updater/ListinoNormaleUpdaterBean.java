/**
 *
 */
package it.eurotn.panjea.magazzino.manager.manutenzionelistino.updater;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListinoStorico;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoManager;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 *
 */
@Stateless(mappedName = "Panjea.ListinoNormaleUpdaterBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ListinoNormaleUpdaterBean")
public class ListinoNormaleUpdaterBean extends ListinoManutenzioneAbstractUpdaterBean {

	private static Logger logger = Logger.getLogger(ListinoNormaleUpdaterBean.class);

	@EJB
	private ListinoManager listinoManager;

	@SuppressWarnings("unchecked")
	@Override
	public void aggiornaRigheListinoDaManutenzione(VersioneListino versioneListino) {
		StringBuilder buffy = new StringBuilder();
		buffy.append("select ");
		buffy.append("l.id, ");
		buffy.append("m.numeroDecimali, ");
		buffy.append("m.valore, ");
		buffy.append("m.descrizione ");
		buffy.append("from maga_riga_manutenzione_listino as m ");
		buffy.append("inner join maga_righe_listini as l on m.articolo_id=l.articolo_id and l.versioneListino_id=");
		buffy.append(versioneListino.getId());
		buffy.append(" ");
		buffy.append("inner join maga_articoli as art on m.articolo_id=art.id ");
		buffy.append("where m.userManutenzione='");
		buffy.append(getPrincipal().getName());
		buffy.append("'");

		QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager().createNativeQuery(
				buffy.toString());
		SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
		List<Object[]> results = null;
		try {
			results = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in aggiornaListinoDaManutenzione", e);
			throw new RuntimeException(e);
		}

		for (Object[] objs : results) {
			try {
				RigaListino rigaListino = panjeaDAO.load(RigaListino.class, ((Integer) objs[0]));
				rigaListino.setVersioneListino(versioneListino);
				rigaListino.setNumeroDecimaliPrezzo((Integer) objs[1]);
				rigaListino.getScaglioni().iterator().next().setPrezzo((BigDecimal) objs[2]);
				rigaListino.getScaglioni().iterator().next().setNota((String) objs[3]);

				ScaglioneListinoStorico storico = new ScaglioneListinoStorico();
				storico.setNumeroDecimaliPrezzo((Integer) objs[1]);
				storico.setPrezzo((BigDecimal) objs[2]);
				storico.setNote((String) objs[3]);
				storico.setArticolo(rigaListino.getArticolo());
				storico.setListino(rigaListino.getVersioneListino().getListino());
				storico.setNumeroVersione(rigaListino.getVersioneListino().getCodice());
				listinoManager.salvaScaglioneListinoStorico(storico);

				panjeaDAO.saveWithoutFlush(rigaListino);
			} catch (DAOException e) {
				logger.error("--> Errore nel salvare la rigaListino in aggiornaListinoDaManutenzione", e);
				throw new RuntimeException("Errore nel salvare la rigaListino in aggiornaListinoDaManutenzione", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void aggiungiRigheListinoMancantiDaManutenzione(VersioneListino versioneListino) {
		StringBuilder buffy = new StringBuilder();
		buffy.append("select ");
		buffy.append("l.id as id, ");
		buffy.append("l.version as version, ");
		buffy.append("m.numeroDecimali as numeroDecimaliPrezzo, ");
		buffy.append("m.valore as prezzo, ");
		buffy.append("m.descrizione as note, ");
		buffy.append("art.id as idArticolo, ");
		buffy.append("art.version as versionArticolo ");
		buffy.append("from maga_riga_manutenzione_listino as m ");
		buffy.append("left join maga_righe_listini as l on m.articolo_id=l.articolo_id and l.versioneListino_id=");
		buffy.append(versioneListino.getId());
		buffy.append(" ");
		buffy.append("inner join maga_articoli as art on m.articolo_id=art.id ");
		buffy.append("where l.id is null and m.userManutenzione='");
		buffy.append(getPrincipal().getName());
		buffy.append("'");

		org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
				.createNativeQuery(buffy.toString());
		SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
		sqlQuery.setResultTransformer(Transformers.aliasToBean(RigaListino.class));
		List<RigaListino> righeListino = null;
		try {
			sqlQuery.addScalar("id");
			sqlQuery.addScalar("version");
			sqlQuery.addScalar("numeroDecimaliPrezzo");
			sqlQuery.addScalar("prezzo");
			sqlQuery.addScalar("note");
			sqlQuery.addScalar("idArticolo");
			sqlQuery.addScalar("versionArticolo");
			righeListino = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in aggiornaListinoDaManutenzione", e);
			throw new RuntimeException(e);
		}

		for (RigaListino rigaListino : righeListino) {
			rigaListino.setVersioneListino(versioneListino);

			ScaglioneListinoStorico storico = new ScaglioneListinoStorico();
			storico.setNumeroDecimaliPrezzo(rigaListino.getNumeroDecimaliPrezzo());
			storico.setPrezzo(rigaListino.getScaglioni().iterator().next().getPrezzo());
			storico.setNote(rigaListino.getScaglioni().iterator().next().getNota());
			storico.setArticolo(rigaListino.getArticolo());
			storico.setListino(versioneListino.getListino());
			storico.setNumeroVersione(versioneListino.getCodice());
			listinoManager.salvaScaglioneListinoStorico(storico);

			try {
				panjeaDAO.saveWithoutFlush(rigaListino);
			} catch (DAOException e) {
				logger.error("--> Errore nel salvare la rigaListino in aggiornaListinoDaManutenzione", e);
				throw new RuntimeException("Errore nel salvare la rigaListino in aggiornaListinoDaManutenzione", e);
			}
		}
	}

}
