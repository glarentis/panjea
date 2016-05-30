/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.manutenzionelistino.updater;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneComparator;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.util.ScaglioneListinoDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(mappedName = "Panjea.ListinoScaglioniUpdaterBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ListinoScaglioniUpdaterBean")
public class ListinoScaglioniUpdaterBean extends ListinoManutenzioneAbstractUpdaterBean {

	private static Logger logger = Logger.getLogger(ListinoScaglioniUpdaterBean.class);

	@Override
	public void aggiornaRigheListinoDaManutenzione(VersioneListino versioneListino) {

		// cancello gli scaglioni presenti sulla versione di destinazione ma non nella manutenzione listino
		cancellaScaglioniNonPresentiInManutenzione(versioneListino);

		// aggiorno il valore degli scaglioni che esistono
		aggiornaScaglioniEsistenti(versioneListino);
	}

	@SuppressWarnings("unchecked")
	private void aggiornaScaglioniEsistenti(VersioneListino versioneListino) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("l.id as idRigaListino, ");
		sb.append("l.version as versionRigaListino, ");
		sb.append("m.numeroDecimali as numeroDecimaliPrezzo, ");
		sb.append("m.quantita as quantita, ");
		sb.append("m.valore as prezzo, ");
		sb.append("m.descrizione as note, ");
		sb.append("art.id as idArticolo, ");
		sb.append("art.version as versionArticolo ");
		sb.append("from maga_riga_manutenzione_listino as m ");
		sb.append("inner join maga_righe_listini as l on m.articolo_id=l.articolo_id and l.versioneListino_id=");
		sb.append(versioneListino.getId());
		sb.append(" inner join maga_scaglioni_listini sl on sl.rigaListino_id=l.id and sl.quantita=m.quantita ");
		sb.append("inner join maga_articoli as art on m.articolo_id=art.id ");
		sb.append("where m.userManutenzione='");
		sb.append(getPrincipal().getName());
		sb.append("'");

		org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
				.createNativeQuery(sb.toString());
		SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
		sqlQuery.setResultTransformer(Transformers.aliasToBean(ScaglioneListinoDTO.class));
		List<ScaglioneListinoDTO> scaglioniListinoDTO = null;
		try {
			sqlQuery.addScalar("idRigaListino");
			sqlQuery.addScalar("versionRigaListino");
			sqlQuery.addScalar("numeroDecimaliPrezzo");
			sqlQuery.addScalar("quantita");
			sqlQuery.addScalar("prezzo");
			sqlQuery.addScalar("note");
			sqlQuery.addScalar("idArticolo");
			sqlQuery.addScalar("versionArticolo");
			scaglioniListinoDTO = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in aggiornaListinoDaManutenzione", e);
			throw new RuntimeException(e);
		}

		// raggruppo gli scaglioni per riga listino
		Map<RigaListino, List<ScaglioneListinoDTO>> mapScaglioni = new HashMap<RigaListino, List<ScaglioneListinoDTO>>();
		for (ScaglioneListinoDTO scaglioneListinoDTO : scaglioniListinoDTO) {
			List<ScaglioneListinoDTO> scaglioniArticolo = mapScaglioni.get(scaglioneListinoDTO.getRigaListino());
			if (scaglioniArticolo == null) {
				scaglioniArticolo = new ArrayList<ScaglioneListinoDTO>();
			}
			scaglioniArticolo.add(scaglioneListinoDTO);
			mapScaglioni.put(scaglioneListinoDTO.getRigaListino(), scaglioniArticolo);
		}

		for (Entry<RigaListino, List<ScaglioneListinoDTO>> entry : mapScaglioni.entrySet()) {
			try {
				RigaListino rigaListino = panjeaDAO.load(RigaListino.class, entry.getKey().getId());
				rigaListino.setNumeroDecimaliPrezzo(entry.getKey().getNumeroDecimaliPrezzo());

				// aggiorno il prezzo e nota degli scaglioni
				for (ScaglioneListinoDTO scaglioneListinoDTO : entry.getValue()) {
					for (ScaglioneListino scaglioneRiga : rigaListino.getScaglioni()) {
						if (scaglioneRiga.getQuantita().compareTo(
								scaglioneListinoDTO.getScaglioneListino().getQuantita()) == 0) {
							scaglioneRiga.setPrezzo(scaglioneListinoDTO.getScaglioneListino().getPrezzo());
							scaglioneRiga.setNota(scaglioneListinoDTO.getScaglioneListino().getNota());
							break;
						}
					}
				}

				panjeaDAO.saveWithoutFlush(rigaListino);
			} catch (DAOException e) {
				logger.error("--> Errore nel salvare la rigaListino in aggiornaListinoDaManutenzione", e);
				throw new RuntimeException("Errore nel salvare la rigaListino in aggiornaListinoDaManutenzione", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void aggiungiArticoliMancanti(VersioneListino versioneListino) {
		StringBuilder buffy = new StringBuilder();
		buffy.append("select ");
		buffy.append("l.id as idRigaListino, ");
		buffy.append("l.version as versionRigaListino, ");
		buffy.append("m.numeroDecimali as numeroDecimaliPrezzo, ");
		buffy.append("m.quantita as quantita, ");
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
		sqlQuery.setResultTransformer(Transformers.aliasToBean(ScaglioneListinoDTO.class));
		List<ScaglioneListinoDTO> scaglioniListinoDTO = null;
		try {
			sqlQuery.addScalar("idRigaListino");
			sqlQuery.addScalar("versionRigaListino");
			sqlQuery.addScalar("numeroDecimaliPrezzo");
			sqlQuery.addScalar("quantita");
			sqlQuery.addScalar("prezzo");
			sqlQuery.addScalar("note");
			sqlQuery.addScalar("idArticolo");
			sqlQuery.addScalar("versionArticolo");
			scaglioniListinoDTO = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in aggiornaListinoDaManutenzione", e);
			throw new RuntimeException(e);
		}

		// raggruppo gli scaglioni per articolo e creo la riga listino da salvare
		Map<ArticoloLite, List<ScaglioneListinoDTO>> mapScaglioni = new HashMap<ArticoloLite, List<ScaglioneListinoDTO>>();
		for (ScaglioneListinoDTO scaglioneListinoDTO : scaglioniListinoDTO) {
			List<ScaglioneListinoDTO> scaglioniArticolo = mapScaglioni.get(scaglioneListinoDTO.getArticolo());
			if (scaglioniArticolo == null) {
				scaglioniArticolo = new ArrayList<ScaglioneListinoDTO>();
			}
			scaglioniArticolo.add(scaglioneListinoDTO);
			mapScaglioni.put(scaglioneListinoDTO.getArticolo(), scaglioniArticolo);
		}

		for (Entry<ArticoloLite, List<ScaglioneListinoDTO>> entry : mapScaglioni.entrySet()) {
			RigaListino rigaListino = entry.getValue().get(0).getRigaListino();
			rigaListino.setVersioneListino(versioneListino);

			Set<ScaglioneListino> scaglioni = new TreeSet<ScaglioneListino>(new ScaglioneComparator());
			for (ScaglioneListinoDTO scaglioneListinoDTO : entry.getValue()) {
				ScaglioneListino scaglioneListino = scaglioneListinoDTO.getScaglioneListino();
				scaglioneListino.setRigaListino(rigaListino);
				scaglioni.add(scaglioneListino);
			}
			rigaListino.setScaglioni(scaglioni);

			try {
				panjeaDAO.saveWithoutFlush(rigaListino);
			} catch (DAOException e) {
				logger.error("--> Errore nel salvare la rigaListino in aggiornaListinoDaManutenzione", e);
				throw new RuntimeException("Errore nel salvare la rigaListino in aggiornaListinoDaManutenzione", e);
			}
		}
	}

	@Override
	public void aggiungiRigheListinoMancantiDaManutenzione(VersioneListino versioneListino) {

		// aggiungo gli scaglioni che mancano alle righe listino esistenti
		aggiungiScaglioniMancanti(versioneListino);

		// aggiungo tutte le righe listino ( e suoi scaglioni ) degli articoli che non ci sono
		aggiungiArticoliMancanti(versioneListino);
	}

	@SuppressWarnings("unchecked")
	private void aggiungiScaglioniMancanti(VersioneListino versioneListino) {
		StringBuilder buffy = new StringBuilder();
		buffy.append("select ");
		buffy.append("l.id as idRigaListino, ");
		buffy.append("l.version as versionRigaListino, ");
		buffy.append("m.numeroDecimali as numeroDecimaliPrezzo, ");
		buffy.append("m.quantita as quantita, ");
		buffy.append("m.valore as prezzo, ");
		buffy.append("m.descrizione as note, ");
		buffy.append("art.id as idArticolo, ");
		buffy.append("art.version as versionArticolo ");
		buffy.append("from maga_riga_manutenzione_listino as m ");
		buffy.append("inner join maga_righe_listini as l on m.articolo_id=l.articolo_id and l.versioneListino_id=");
		buffy.append(versioneListino.getId());
		buffy.append(" left join maga_scaglioni_listini sl on sl.rigaListino_id=l.id and sl.quantita=m.quantita ");
		buffy.append("inner join maga_articoli as art on m.articolo_id=art.id ");
		buffy.append("where sl.id is null and m.userManutenzione='");
		buffy.append(getPrincipal().getName());
		buffy.append("'");

		org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
				.createNativeQuery(buffy.toString());
		SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
		sqlQuery.setResultTransformer(Transformers.aliasToBean(ScaglioneListinoDTO.class));
		List<ScaglioneListinoDTO> scaglioniListinoDTO = null;
		try {
			sqlQuery.addScalar("idRigaListino");
			sqlQuery.addScalar("versionRigaListino");
			sqlQuery.addScalar("numeroDecimaliPrezzo");
			sqlQuery.addScalar("quantita");
			sqlQuery.addScalar("prezzo");
			sqlQuery.addScalar("note");
			sqlQuery.addScalar("idArticolo");
			sqlQuery.addScalar("versionArticolo");
			scaglioniListinoDTO = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in aggiornaListinoDaManutenzione", e);
			throw new RuntimeException(e);
		}

		// raggruppo gli scaglioni per riga listino
		Map<RigaListino, List<ScaglioneListinoDTO>> mapScaglioni = new HashMap<RigaListino, List<ScaglioneListinoDTO>>();
		for (ScaglioneListinoDTO scaglioneListinoDTO : scaglioniListinoDTO) {
			List<ScaglioneListinoDTO> scaglioniArticolo = mapScaglioni.get(scaglioneListinoDTO.getRigaListino());
			if (scaglioniArticolo == null) {
				scaglioniArticolo = new ArrayList<ScaglioneListinoDTO>();
			}
			scaglioniArticolo.add(scaglioneListinoDTO);
			mapScaglioni.put(scaglioneListinoDTO.getRigaListino(), scaglioniArticolo);
		}

		for (Entry<RigaListino, List<ScaglioneListinoDTO>> entry : mapScaglioni.entrySet()) {
			// carico la riga listino esistente
			RigaListino rigaListino;
			try {
				rigaListino = panjeaDAO.load(RigaListino.class, entry.getKey().getId());
			} catch (ObjectNotFoundException e1) {
				logger.error("--> errore durante il caricamento della riga listino", e1);
				throw new RuntimeException("errore durante il caricamento della riga listino", e1);
			}

			// aggiungo gli ascaglioni che mancano
			for (ScaglioneListinoDTO scaglioneListinoDTO : entry.getValue()) {
				ScaglioneListino scaglioneListino = scaglioneListinoDTO.getScaglioneListino();
				scaglioneListino.setRigaListino(rigaListino);
				rigaListino.getScaglioni().add(scaglioneListino);
			}

			// aggiorno il numero decimali del prezzo proveniente dalla manutenzione
			rigaListino.setNumeroDecimaliPrezzo(entry.getKey().getNumeroDecimaliPrezzo());

			try {
				panjeaDAO.saveWithoutFlush(rigaListino);
			} catch (DAOException e) {
				logger.error("--> Errore nel salvare la rigaListino in aggiornaListinoDaManutenzione", e);
				throw new RuntimeException("Errore nel salvare la rigaListino in aggiornaListinoDaManutenzione", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void cancellaScaglioniNonPresentiInManutenzione(VersioneListino versioneListino) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("l.id as idRigaListino, ");
		sb.append("l.version as versionRigaListino, ");
		sb.append("l.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("sl.quantita as quantita, ");
		sb.append("sl.prezzo as prezzo, ");
		sb.append("sl.id as idScaglioneListino, ");
		sb.append("'' as note, ");
		sb.append("art.id as idArticolo, ");
		sb.append("art.version as versionArticolo ");
		sb.append("from maga_righe_listini as l ");
		sb.append("inner join maga_scaglioni_listini sl on sl.rigaListino_id=l.id ");
		sb.append("left join maga_riga_manutenzione_listino as m on m.articolo_id=l.articolo_id and sl.quantita=m.quantita and m.userManutenzione='");
		sb.append(getPrincipal().getName());
		sb.append("' ");
		sb.append("inner join maga_articoli as art on l.articolo_id=art.id ");
		sb.append("where l.versioneListino_id=");
		sb.append(versioneListino.getId());
		sb.append(" and m.id is null");
		sb.append(" and l.articolo_id in (select distinct rml2.articolo_id ");
		sb.append(" from maga_riga_manutenzione_listino rml2) ");

		org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
				.createNativeQuery(sb.toString());
		SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
		sqlQuery.setResultTransformer(Transformers.aliasToBean(ScaglioneListinoDTO.class));
		List<ScaglioneListinoDTO> scaglioniListinoDTO = null;
		try {
			sqlQuery.addScalar("idRigaListino");
			sqlQuery.addScalar("versionRigaListino");
			sqlQuery.addScalar("numeroDecimaliPrezzo");
			sqlQuery.addScalar("quantita");
			sqlQuery.addScalar("prezzo");
			sqlQuery.addScalar("idScaglioneListino");
			sqlQuery.addScalar("note");
			sqlQuery.addScalar("idArticolo");
			sqlQuery.addScalar("versionArticolo");
			scaglioniListinoDTO = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in aggiornaListinoDaManutenzione", e);
			throw new RuntimeException(e);
		}

		// raggruppo gli scaglioni per riga listino
		Map<RigaListino, List<ScaglioneListinoDTO>> mapScaglioni = new HashMap<RigaListino, List<ScaglioneListinoDTO>>();
		for (ScaglioneListinoDTO scaglioneListinoDTO : scaglioniListinoDTO) {
			List<ScaglioneListinoDTO> scaglioniArticolo = mapScaglioni.get(scaglioneListinoDTO.getRigaListino());
			if (scaglioniArticolo == null) {
				scaglioniArticolo = new ArrayList<ScaglioneListinoDTO>();
			}
			scaglioniArticolo.add(scaglioneListinoDTO);
			mapScaglioni.put(scaglioneListinoDTO.getRigaListino(), scaglioniArticolo);
		}

		for (Entry<RigaListino, List<ScaglioneListinoDTO>> entry : mapScaglioni.entrySet()) {
			try {
				RigaListino rigaListino = panjeaDAO.load(RigaListino.class, entry.getKey().getId());

				// recupero gli scaglioni che devo cancellare
				Set<ScaglioneListino> scaglioniDaCancellare = new TreeSet<ScaglioneListino>(new ScaglioneComparator());
				for (ScaglioneListinoDTO scaglioneListinoDTO : entry.getValue()) {
					for (ScaglioneListino scaglioneRiga : rigaListino.getScaglioni()) {
						if (scaglioneRiga.getQuantita().compareTo(
								scaglioneListinoDTO.getScaglioneListino().getQuantita()) == 0) {
							scaglioniDaCancellare.add(scaglioneListinoDTO.getScaglioneListino());
							break;
						}
					}
				}

				// tolgo gli scaglioni dalla riga e la salvo
				rigaListino.getScaglioni().removeAll(scaglioniDaCancellare);
				panjeaDAO.saveWithoutFlush(rigaListino);
			} catch (DAOException e) {
				logger.error("--> Errore nel salvare la rigaListino in aggiornaListinoDaManutenzione", e);
				throw new RuntimeException("Errore nel salvare la rigaListino in aggiornaListinoDaManutenzione", e);
			}
		}
	}
}
