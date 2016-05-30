package it.eurotn.panjea.ordini.manager.documento.evasione;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficaTabelleManager;
import it.eurotn.panjea.anagrafica.manager.noteautomatiche.interfaces.NoteAutomaticheManager;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.ordini.manager.documento.evasione.interfaces.DocumentoEvasioneManager;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.rate.manager.interfaces.RateGenerator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 */
@Stateless(mappedName = "Panjea.DocumentoEvasioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DocumentoEvasioneManager")
public class DocumentoEvasioneManagerBean implements DocumentoEvasioneManager {

	private static Logger logger = Logger.getLogger(DocumentoEvasioneManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private AreaMagazzinoManager areaMagazzinoManager;

	@EJB
	private AreaRateManager areaRateManager;

	@EJB
	private RateGenerator rateGenerator;

	@EJB
	private AnagraficaTabelleManager anagraficaTabelleManager;

	@EJB
	private RigaMagazzinoManager rigaMagazzinoManager;

	@EJB
	private NoteAutomaticheManager noteAutomaticheManager;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void aggiornaDistinteCaricoEvase(Set<Integer> righeOrdineDaforzare, String uuid) {
		// // Aggiorno il flag chiusuraForzata alle righe ordine che devono essere forzate
		if (righeOrdineDaforzare.size() > 0) {
			StringBuffer hqlUpdateForzate = new StringBuffer();
			hqlUpdateForzate
					.append("update RigaOrdine ro set ro.evasioneForzata=true where ro.id in (:righeOrdineDaForzare)");
			Query queryUpdate = panjeaDAO.prepareQuery(hqlUpdateForzate.toString());
			queryUpdate.setParameter("righeOrdineDaForzare", righeOrdineDaforzare);
			try {
				panjeaDAO.executeQuery(queryUpdate);
			} catch (DAOException e) {
				throw new RuntimeException(e);
			}

			hqlUpdateForzate = new StringBuffer();
			hqlUpdateForzate
					.append("delete from RigaDistintaCarico rdc where rdc.rigaArticolo.id in (:righeOrdineDaForzare)");
			queryUpdate = panjeaDAO.prepareQuery(hqlUpdateForzate.toString());
			queryUpdate.setParameter("righeOrdineDaForzare", righeOrdineDaforzare);
			try {
				panjeaDAO.executeQuery(queryUpdate);
			} catch (DAOException e) {
				throw new RuntimeException(e);
			}

			StringBuffer sqlUpdateForzate = new StringBuffer();
			sqlUpdateForzate.append("update ordi_righe_ordine as ro ");
			sqlUpdateForzate.append("inner join ordi_righe_ordine as ro2 on ro.rigaDistintaCollegata_id ");
			sqlUpdateForzate.append("set ro.evasioneForzata=1 ");
			sqlUpdateForzate.append("where ro2.id in (:righeOrdineDaForzare)");
			SQLQuery queryUpdateForzaComponenti = panjeaDAO.prepareNativeQuery(sqlUpdateForzate.toString());
			queryUpdateForzaComponenti.setParameter("righeOrdineDaForzare", righeOrdineDaforzare);
			queryUpdateForzaComponenti.executeUpdate();
		}

		// cancello le righe distinte carico evase
		StringBuffer hqlDelete = new StringBuffer();
		hqlDelete.append("delete from RigaDistintaCarico rdc where rdc.rigaArticolo.id in ( ");
		hqlDelete.append(" select rm.rigaOrdineCollegata from RigaMagazzino rm ");
		hqlDelete.append(" where rm.areaMagazzino.uUIDContabilizzazione=:uuid) ");
		Query queryDelete = panjeaDAO.prepareQuery(hqlDelete.toString());
		queryDelete.setParameter("uuid", uuid);
		try {
			panjeaDAO.executeQuery(queryDelete);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}

		// cancello tutte le distinte che non hanno più righe
		hqlDelete = new StringBuffer();
		hqlDelete.append("delete from DistintaCarico d where d.righeEvasione.size = 0");
		queryDelete = panjeaDAO.prepareQuery(hqlDelete.toString());
		try {
			panjeaDAO.executeQuery(queryDelete);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Se sono state configurate delle note automatiche per il documento di destinazione, viene creata una riga nota
	 * automatice.
	 *
	 * @param areaMagazzino
	 *            area magazzino di riferimento
	 */
	private void creaRigaNotaAutomatica(AreaMagazzino areaMagazzino) {

		List<NotaAutomatica> noteAutomatiche = noteAutomaticheManager.caricaNoteAutomatiche(
				areaMagazzino.getDataRegistrazione(), areaMagazzino.getDocumento());

		if (!noteAutomatiche.isEmpty()) {
			List<NotaAnagrafica> noteAnagrafiche = anagraficaTabelleManager.caricaNoteAnagrafica();

			StringBuilder sb = new StringBuilder();
			for (NotaAutomatica nota : noteAutomatiche) {

				if (!sb.toString().isEmpty()) {
					sb.append("<BR>");
				}
				sb.append(nota.getNotaElaborata(noteAnagrafiche));
			}

			rigaMagazzinoManager.creaRigaNoteAutomatica(areaMagazzino, sb.toString());
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public AreaMagazzino salvaDocumentoEvasione(AreaMagazzinoFullDTO areaMagazzinoFullDTO) {
		logger.debug("--> Enter salvaDocumentoEvasione");

		// salvo l'area magazzino
		AreaMagazzino areaMagazzino = null;
		try {
			areaMagazzino = areaMagazzinoManager.salvaAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino(), true);
		} catch (RuntimeException e) {
			logger.error("-->errore durante il salvataggio dell'area magazzino", e);
			throw e;
		} catch (Exception e) {
			logger.error("-->errore durante il salvataggio dell'area magazzino", e);
			throw new RuntimeException(e);
		}

		List<RigaMagazzino> righeMagazzinoSalvate = new ArrayList<RigaMagazzino>();
		// salvo le righe magazzino
		for (RigaMagazzino rigaMagazzino : areaMagazzinoFullDTO.getRigheMagazzino()) {
			rigaMagazzino.setAreaMagazzino(areaMagazzino);
			try {
				RigaMagazzino rigaMagazzinoSalvata = rigaMagazzinoManager.getDao(rigaMagazzino).salvaRigaMagazzino(
						rigaMagazzino);
				righeMagazzinoSalvate.add(rigaMagazzinoSalvata);
				// panjeaDAO.saveWithoutFlush(rigaMagazzino);
			} catch (Exception e) {
				logger.error(
						"-->errore nel salvare la riga magazzino collegata alla riga ordine "
								+ rigaMagazzino.getRigaOrdineCollegata(), e);
				throw new RuntimeException(e);
			}
		}
		// per la valida righe magazzino, nel caso di intra abilitato, prendo le righe dall'area magazzino
		areaMagazzino.setRigheMagazzino(righeMagazzinoSalvate);

		// creo la riga nota automentica se configurata
		creaRigaNotaAutomatica(areaMagazzino);

		// salvo l'area rate se presente
		AreaRate areaRate = areaMagazzinoFullDTO.getAreaRate();
		if (areaRate != null) {
			areaRate.setDocumento(areaMagazzino.getDocumento());
			areaRate = areaRateManager.salvaAreaRate(areaRate);
		}

		try {
			areaMagazzino = areaMagazzinoManager.validaRigheMagazzino(areaMagazzino, areaRate, false, false);
		} catch (Exception e) {
			logger.error("-->errore durante la validazione delle righe magazzino del documento", e);
			throw new RuntimeException("errore durante la validazione delle righe magazzino del documento", e);
		}

		// genero le rate dell'areaRate dopo la validazione dell'areaMagazzino dato che in essa
		// vengono generati gli importi, le righe iva e tutte le informazioni necessarie
		if (areaRate != null && areaRate.getTipoAreaPartita().getTipoOperazione() == TipoOperazione.GENERA) {
			areaRate = rateGenerator.generaRate(areaMagazzino, areaRate);
		}

		logger.debug("--> Exit salvaDocumentoEvasione");
		panjeaDAO.getEntityManager().flush();
		return areaMagazzino;
	}
}
