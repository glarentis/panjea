package it.eurotn.panjea.intra.manager.sezionigenerator;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione3Intra;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;

public class Sezione3AcquistiBuilder extends SezioneRigheBuilder<RigaSezione3Intra> {

	private static Logger logger = Logger.getLogger(Sezione3AcquistiBuilder.class);

	/**
	 * 
	 * @param righe
	 *            .
	 * @param panjeaDAO
	 *            .
	 * @param dichiarazioneIntra
	 *            .
	 * @return .
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, RigaSezioneIntra> aggiornaConAddebiti(Map<Long, RigaSezioneIntra> righe, PanjeaDAO panjeaDAO,
			DichiarazioneIntra dichiarazioneIntra) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("nazione.codice as fornitoreStato, ");
		sb.append("anagrafica.partiteIVA as fornitorepiva, ");
		sb.append("sum(rs.importo.importoInValutaAzienda) as importo, ");
		sb.append("docOrigine.codice as numeroFattura, ");
		sb.append("docOrigine.dataDocumento as dataFattura, ");
		sb.append("nomenclatura as servizio, ");
		sb.append("rs.modalitaErogazione as modalitaErogazione, ");
		sb.append("intra.modalitaIncasso as modalitaIncasso, ");
		sb.append("intra.paesePagamento as paesePagamento ");
		sb.append("from RigaServizioIntra rs, AreaContabile ac,AreaContabile acOrigine ");
		sb.append("join rs.servizio nomenclatura ");
		sb.append("join rs.areaIntra intra ");
		sb.append("join intra.documento doc ");
		sb.append("join doc.tipoDocumento td ");
		sb.append("join doc.entita ent ");
		sb.append("join doc.documentiOrigine link ");
		sb.append("join link.documentoDestinazione docOrigine ");
		sb.append("join doc.entita ent ");
		sb.append("join doc.sedeEntita sede ");
		sb.append("join sede.sede sedeAnagrafica ");
		sb.append("join sedeAnagrafica.datiGeografici.nazione nazione ");
		sb.append("join ent.anagrafica anagrafica ");
		sb.append("where td.tipoEntita=1 ");
		sb.append("and acOrigine.documento=docOrigine ");
		sb.append("and ac.documento=doc ");
		sb.append("and td.notaAddebitoEnable=1 ");
		sb.append("and link.tipoCollegamento=1 ");
		sb.append("and acOrigine.dataRegistrazione>=:dataIniziale ");
		sb.append("and acOrigine.dataRegistrazione<=:dataFinale ");
		sb.append("and ac.dataRegistrazione>=:dataIniziale ");
		sb.append("and ac.dataRegistrazione<=:dataFinale ");
		sb.append("group by anagrafica.partiteIVA,doc.codice.codice,doc.dataDocumento,nomenclatura,rs.modalitaErogazione,intra.modalitaIncasso,intra.paesePagamento ");
		Query query = panjeaDAO.prepareQuery(sb.toString(), RigaSezione3Intra.class, null);
		query.setParameter("dataIniziale", dichiarazioneIntra.getDataIniziale());
		query.setParameter("dataFinale", dichiarazioneIntra.getDataFinale());
		List<RigaSezioneIntra> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore ", e);
			throw new RuntimeException("errore nel recuperare le righeBene per la sezione 3", e);
		}
		for (RigaSezioneIntra rigaStornoSezioneIntra : result) {
			// Recupero una eventuale (teoricamente OBBLIGATORIA) riga che storno e la sottraggo
			if (righe.containsKey(rigaStornoSezioneIntra.checkSum())) {
				righe.get(rigaStornoSezioneIntra.checkSum()).aggiungi(rigaStornoSezioneIntra);
			}
		}
		return righe;
	}

	/**
	 * 
	 * @param righe
	 *            .
	 * @param panjeaDAO
	 *            .
	 * @param dichiarazioneIntra
	 *            .
	 * @return righe con i costi aggiornati con gli storni.
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, RigaSezioneIntra> aggiornaConStorni(Map<Long, RigaSezioneIntra> righe, PanjeaDAO panjeaDAO,
			DichiarazioneIntra dichiarazioneIntra) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("nazione.codice as fornitoreStato, ");
		sb.append("anagrafica.partiteIVA as fornitorepiva, ");
		sb.append("sum(rs.importo.importoInValutaAzienda) as importo, ");
		sb.append("docOrigine.codice as numeroFattura, ");
		sb.append("docOrigine.dataDocumento as dataFattura, ");
		sb.append("nomenclatura as servizio, ");
		sb.append("rs.modalitaErogazione as modalitaErogazione, ");
		sb.append("intra.modalitaIncasso as modalitaIncasso, ");
		sb.append("intra.paesePagamento as paesePagamento ");
		sb.append("from RigaServizioIntra rs, AreaContabile ac,AreaContabile acOrigine ");
		sb.append("join rs.servizio nomenclatura ");
		sb.append("join rs.areaIntra intra ");
		sb.append("join intra.documento doc ");
		sb.append("join doc.tipoDocumento td ");
		sb.append("join doc.documentiOrigine link ");
		sb.append("join link.documentoDestinazione docOrigine ");
		sb.append("join doc.entita ent ");
		sb.append("join doc.sedeEntita sede ");
		sb.append("join sede.sede sedeAnagrafica ");
		sb.append("join sedeAnagrafica.datiGeografici.nazione nazione ");
		sb.append("join ent.anagrafica anagrafica ");
		sb.append("where td.tipoEntita=1 ");
		sb.append("and acOrigine.documento=docOrigine ");
		sb.append("and ac.documento=doc ");
		sb.append("and td.notaCreditoEnable=1 ");
		sb.append("and link.tipoCollegamento=1 ");
		sb.append("and acOrigine.dataRegistrazione>=:dataIniziale ");
		sb.append("and acOrigine.dataRegistrazione<=:dataFinale ");
		sb.append("and ac.dataRegistrazione>=:dataIniziale ");
		sb.append("and ac.dataRegistrazione<=:dataFinale ");
		sb.append("group by anagrafica.partiteIVA,doc.codice.codice,doc.dataDocumento,nomenclatura,rs.modalitaErogazione,intra.modalitaIncasso,intra.paesePagamento ");
		Query query = panjeaDAO.prepareQuery(sb.toString(), RigaSezione3Intra.class, null);
		query.setParameter("dataIniziale", dichiarazioneIntra.getDataIniziale());
		query.setParameter("dataFinale", dichiarazioneIntra.getDataFinale());
		List<RigaSezioneIntra> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore ", e);
			throw new RuntimeException("errore nel recuperare le righeBene per la sezione 3", e);
		}
		for (RigaSezioneIntra rigaStornoSezioneIntra : result) {
			if (righe.containsKey(rigaStornoSezioneIntra.checkSum())) {
				righe.get(rigaStornoSezioneIntra.checkSum()).sottrai(rigaStornoSezioneIntra);
			} else {
				// Inserisco lo storno, non sottraendolo da un valore già presente devo girare i segni
				rigaStornoSezioneIntra.negateImporti();
				righe.put(rigaStornoSezioneIntra.checkSum(), rigaStornoSezioneIntra);
			}
		}
		return righe;
	}

	@Override
	protected Map<Long, RigaSezioneIntra> creaRighe(PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra) {
		// Recupero le righe relativi ai documenti intra del periodo.
		Map<Long, RigaSezioneIntra> righe = getRighe(panjeaDAO, dichiarazioneIntra);

		// Recupero le righe relative agli storni intra del periodo e aggiorno le righe dei documenti
		righe = aggiornaConStorni(righe, panjeaDAO, dichiarazioneIntra);
		righe = aggiornaConAddebiti(righe, panjeaDAO, dichiarazioneIntra);

		return righe;
	}

	/**
	 * @param panjeaDAO
	 *            dao panjea
	 * @param dichiarazioneIntra
	 *            dichiarazione intra da associare
	 * @return lista di righe intra di quel periodo (ESCLUSI NOTEACCREDITO E ADDEBITO)
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, RigaSezioneIntra> getRighe(PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("nazione.codice as fornitoreStato, ");
		sb.append("anagrafica.partiteIVA as fornitorepiva, ");
		sb.append("sum(rs.importo.importoInValutaAzienda) as importo, ");
		sb.append("doc.codice as numeroFattura, ");
		sb.append("doc.dataDocumento as dataFattura, ");
		sb.append("nomenclatura as servizio, ");
		sb.append("rs.modalitaErogazione as modalitaErogazione, ");
		sb.append("intra.modalitaIncasso as modalitaIncasso, ");
		sb.append("intra.paesePagamento as paesePagamento ");
		sb.append("from RigaServizioIntra rs, AreaContabile ac ");
		sb.append("join rs.servizio nomenclatura ");
		sb.append("join rs.areaIntra intra ");
		sb.append("join intra.documento doc ");
		sb.append("join doc.tipoDocumento td ");
		sb.append("join doc.entita ent ");
		sb.append("join doc.sedeEntita sede ");
		sb.append("join sede.sede sedeAnagrafica ");
		sb.append("join sedeAnagrafica.datiGeografici.nazione nazione ");
		sb.append("join ent.anagrafica anagrafica ");
		sb.append("where td.tipoEntita=1 ");
		sb.append("and ac.documento=doc ");
		sb.append("and td.notaCreditoEnable=0 ");
		sb.append("and td.notaAddebitoEnable=0 ");
		sb.append("and ac.dataRegistrazione>=:dataIniziale ");
		sb.append("and ac.dataRegistrazione<=:dataFinale ");
		sb.append("group by anagrafica.partiteIVA,doc.codice.codice,doc.dataDocumento,nomenclatura,rs.modalitaErogazione,intra.modalitaIncasso,intra.paesePagamento ");
		Query query = panjeaDAO.prepareQuery(sb.toString(), RigaSezione3Intra.class, null);
		query.setParameter("dataIniziale", dichiarazioneIntra.getDataIniziale());
		query.setParameter("dataFinale", dichiarazioneIntra.getDataFinale());
		List<RigaSezioneIntra> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore ", e);
			throw new RuntimeException("errore nel recuperare le righeBene per la sezione 3", e);
		}
		Map<Long, RigaSezioneIntra> righe = new HashMap<Long, RigaSezioneIntra>();
		for (RigaSezioneIntra rigaSezioneIntra : result) {
			righe.put(rigaSezioneIntra.checkSum(), rigaSezioneIntra);
		}
		return righe;
	}
}