package it.eurotn.panjea.intra.manager.sezionigenerator;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione4Intra;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;

public class Sezione4AcquistiBuilder extends SezioneRigheBuilder<RigaSezione4Intra> {

	private static Logger logger = Logger.getLogger(Sezione4AcquistiBuilder.class);

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
		sb.append("sum(rs.importo.importoInValuta) as importoInValuta, ");
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
		sb.append("and acOrigine.dataRegistrazione<=:dataIniziale ");
		sb.append("and ac.dataRegistrazione>=:dataIniziale ");
		sb.append("and ac.dataRegistrazione<=:dataFinale ");
		sb.append("group by anagrafica.partiteIVA,doc.codice.codice,doc.dataDocumento,nomenclatura,rs.modalitaErogazione,intra.modalitaIncasso,intra.paesePagamento ");
		Query query = panjeaDAO.prepareQuery(sb.toString(), RigaSezione4Intra.class, null);
		query.setParameter("dataIniziale", dichiarazioneIntra.getDataIniziale());
		query.setParameter("dataFinale", dichiarazioneIntra.getDataFinale());
		List<RigaSezioneIntra> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore ", e);
			throw new RuntimeException("errore nel recuperare le righeBene per la sezione 4", e);
		}
		for (RigaSezioneIntra rigaStornoSezioneIntra : result) {
			// Recupero una eventuale (teoricamente OBBLIGATORIA) riga che storno e la sottraggo
			if (righe.containsKey(rigaStornoSezioneIntra.checkSum())) {
				righe.get(rigaStornoSezioneIntra.checkSum()).aggiungi(rigaStornoSezioneIntra);
			}
		}
		return righe;
	}

	@Override
	protected Map<Long, RigaSezioneIntra> creaRighe(PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra) {
		// Recupero le righe relativi ai documenti intra del periodo.
		Map<Long, RigaSezioneIntra> righe = getRigheStorno(panjeaDAO, dichiarazioneIntra);

		// Recupero le righe relative agli storni intra del periodo e aggiorno le righe dei documenti
		righe = aggiornaConAddebiti(righe, panjeaDAO, dichiarazioneIntra);

		return righe;
	}

	/**
	 * 
	 * @param panjeaDAO
	 *            .
	 * @param dichiarazioneIntra
	 *            .
	 * @return righe con i costi aggiornati con gli storni.
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, RigaSezioneIntra> getRigheStorno(PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("nazione.codice as fornitoreStato, ");
		sb.append("anagrafica.partiteIVA as fornitorepiva, ");
		sb.append("sum(rs.importo.importoInValutaAzienda) * -1 as importo, ");
		sb.append("sum(rs.importo.importoInValuta) * -1 as importoInValuta, ");
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
		sb.append("and acOrigine.dataRegistrazione<=:dataIniziale ");
		sb.append("and ac.dataRegistrazione>=:dataIniziale ");
		sb.append("and ac.dataRegistrazione<=:dataFinale ");
		sb.append("group by anagrafica.partiteIVA,doc.codice.codice,doc.dataDocumento,nomenclatura,rs.modalitaErogazione,intra.modalitaIncasso,intra.paesePagamento ");
		Query query = panjeaDAO.prepareQuery(sb.toString(), RigaSezione4Intra.class, null);
		query.setParameter("dataIniziale", dichiarazioneIntra.getDataIniziale());
		query.setParameter("dataFinale", dichiarazioneIntra.getDataFinale());
		List<RigaSezioneIntra> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore ", e);
			throw new RuntimeException("errore nel recuperare le righeBene per la sezione 4", e);
		}
		Map<Long, RigaSezioneIntra> righe = new HashMap<Long, RigaSezioneIntra>();
		for (RigaSezioneIntra rigaSezioneIntra : result) {
			righe.put(rigaSezioneIntra.checkSum(), rigaSezioneIntra);
		}
		return righe;
	}
}