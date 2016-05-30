package it.eurotn.panjea.intra.manager.sezionigenerator;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione2Intra;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.intra.domain.TipoPeriodo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;

public class Sezione2CessioniBuilder extends SezioneRigheBuilder<RigaSezione2Intra> {

	private static Logger logger = Logger.getLogger(Sezione2CessioniBuilder.class);

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
	private Map<Long, RigaSezioneIntra> aggiornaConRigheAddebito(Map<Long, RigaSezioneIntra> righe,
			PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra) {
		StringBuilder sb = new StringBuilder();
		sb.append("select nazione.codice as fornitoreStato, ");
		sb.append("anagrafica.partiteIVA as fornitorepiva, ");
		sb.append("MONTH(acOrigine.dataRegistrazione) as mese, ");
		sb.append("( ((MONTH(acOrigine.dataRegistrazione)-1)/3)+1 ) as trimestre,");
		sb.append("YEAR(acOrigine.dataRegistrazione) as anno, ");
		sb.append("sum(rb.importo.importoInValutaAzienda) as importo, ");
		sb.append("intra.naturaTransazione as naturaTransazione, ");
		sb.append("servizi as nomenclatura, ");
		sb.append("sum(rb.importo.importoInValutaAzienda) as valoreStatisticoEuro  ");
		sb.append("from RigaBeneIntra rb, AreaContabile ac,AreaContabile acOrigine ");
		sb.append("join rb.areaIntra intra ");
		sb.append("join intra.documento doc ");
		sb.append("join rb.nomenclatura servizi ");
		sb.append("join doc.tipoDocumento td ");
		sb.append("join doc.documentiOrigine link ");
		sb.append("join link.documentoDestinazione docOrigine ");
		sb.append("join doc.entita ent ");
		sb.append("join doc.sedeEntita sede ");
		sb.append("join sede.sede sedeAnagrafica ");
		sb.append("join sedeAnagrafica.datiGeografici.nazione nazione ");
		sb.append("join ent.anagrafica anagrafica ");
		sb.append("where td.tipoEntita=0 ");
		sb.append("and acOrigine.documento=docOrigine ");
		sb.append("and acOrigine.dataRegistrazione<:dataIniziale ");
		sb.append("and ac.documento=doc ");
		sb.append("and ac.dataRegistrazione>=:dataIniziale ");
		sb.append("and ac.dataRegistrazione<=:dataFinale ");
		sb.append("and td.notaAddebitoEnable=1 ");
		sb.append("and link.tipoCollegamento=1 ");
		if (dichiarazioneIntra.getTipoPeriodo().equals(TipoPeriodo.M)) {
			sb.append("group by MONTH(acOrigine.dataRegistrazione),");
		} else {
			sb.append("group by ( ((MONTH(acOrigine.dataRegistrazione)-1)/3)+1 ),");
		}
		sb.append("anagrafica.partiteIVA,intra.naturaTransazione,servizi,rb.um,intra.gruppoCondizioneConsegna,intra.modalitaTrasporto ");
		Query query = panjeaDAO.prepareQuery(sb.toString(), RigaSezione2Intra.class, null);
		query.setParameter("dataIniziale", dichiarazioneIntra.getDataIniziale());
		query.setParameter("dataFinale", dichiarazioneIntra.getDataFinale());
		List<RigaSezioneIntra> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore ", e);
			throw new RuntimeException("errore nel recuperare le righeBene per la sezione 1", e);
		}
		for (RigaSezioneIntra rigaSezioneIntra : result) {
			if (righe.containsKey(rigaSezioneIntra.checkSum())) {
				righe.get(rigaSezioneIntra.checkSum()).aggiungi(rigaSezioneIntra);
			} else {
				righe.put(rigaSezioneIntra.checkSum(), rigaSezioneIntra);
			}
		}
		return righe;
	}

	@Override
	protected RigaSezioneIntra calcolaRigaSezione(RigaSezioneIntra rigaSezioneIntra) {

		if (rigaSezioneIntra instanceof RigaSezione2Intra) {
			RigaSezione2Intra rigaSezione2 = (RigaSezione2Intra) rigaSezioneIntra;
			rigaSezione2.setValoreStatisticoEuro(calcolaValoreStatistico(rigaSezione2.getImporto(), rigaSezione2
					.getDichiarazione().getPercValoreStatistico()));
			rigaSezioneIntra = rigaSezione2;
		}

		return super.calcolaRigaSezione(rigaSezioneIntra);
	}

	@Override
	protected Map<Long, RigaSezioneIntra> creaRighe(PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra) {
		Map<Long, RigaSezioneIntra> righe = getRigheStorno(panjeaDAO, dichiarazioneIntra);
		righe = aggiornaConRigheAddebito(righe, panjeaDAO, dichiarazioneIntra);

		return righe;
	}

	/**
	 * 
	 * @param panjeaDAO
	 *            .
	 * @param dichiarazioneIntra
	 *            .
	 * @return .
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, RigaSezioneIntra> getRigheStorno(PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra) {
		StringBuilder sb = new StringBuilder();
		sb.append("select nazione.codice as fornitoreStato, ");
		sb.append("anagrafica.partiteIVA as fornitorepiva, ");
		sb.append("MONTH(acOrigine.dataRegistrazione) as mese, ");
		sb.append("( ((MONTH(acOrigine.dataRegistrazione)-1)/3)+1 ) as trimestre,");
		sb.append("YEAR(acOrigine.dataRegistrazione) as anno, ");
		sb.append("sum(rb.importo.importoInValutaAzienda) *-1 as importo, ");
		sb.append("intra.naturaTransazione as naturaTransazione, ");
		sb.append("servizi as nomenclatura, ");
		sb.append("sum(rb.importo.importoInValutaAzienda) *-1 as valoreStatisticoEuro  ");
		sb.append("from RigaBeneIntra rb, AreaContabile ac,AreaContabile acOrigine ");
		sb.append("join rb.areaIntra intra ");
		sb.append("join intra.documento doc ");
		sb.append("join rb.nomenclatura servizi ");
		sb.append("join doc.tipoDocumento td ");
		sb.append("join doc.documentiOrigine link ");
		sb.append("join link.documentoDestinazione docOrigine ");
		sb.append("join doc.entita ent ");
		sb.append("join doc.sedeEntita sede ");
		sb.append("join sede.sede sedeAnagrafica ");
		sb.append("join sedeAnagrafica.datiGeografici.nazione nazione ");
		sb.append("join ent.anagrafica anagrafica ");
		sb.append("where td.tipoEntita=0 ");
		sb.append("and acOrigine.documento=docOrigine ");
		sb.append("and acOrigine.dataRegistrazione<:dataIniziale ");
		sb.append("and ac.documento=doc ");
		sb.append("and ac.dataRegistrazione>=:dataIniziale ");
		sb.append("and ac.dataRegistrazione<=:dataFinale ");
		sb.append("and td.notaCreditoEnable=1 ");
		sb.append("and link.tipoCollegamento=1 ");
		if (dichiarazioneIntra.getTipoPeriodo().equals(TipoPeriodo.M)) {
			sb.append("group by MONTH(acOrigine.dataRegistrazione),");
		} else {
			sb.append("group by ( ((MONTH(acOrigine.dataRegistrazione)-1)/3)+1 ),");
		}
		sb.append("anagrafica.partiteIVA,intra.naturaTransazione,servizi,rb.um,intra.gruppoCondizioneConsegna,intra.modalitaTrasporto ");
		Query query = panjeaDAO.prepareQuery(sb.toString(), RigaSezione2Intra.class, null);
		query.setParameter("dataIniziale", dichiarazioneIntra.getDataIniziale());
		query.setParameter("dataFinale", dichiarazioneIntra.getDataFinale());
		List<RigaSezioneIntra> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore ", e);
			throw new RuntimeException("errore nel recuperare le righeBene per la sezione 1", e);
		}
		Map<Long, RigaSezioneIntra> righe = new HashMap<Long, RigaSezioneIntra>();
		for (RigaSezioneIntra rigaSezioneIntra : result) {
			righe.put(rigaSezioneIntra.checkSum(), rigaSezioneIntra);
		}
		return righe;
	}
}
