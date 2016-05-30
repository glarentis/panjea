package it.eurotn.panjea.intra.manager.sezionigenerator;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione1Intra;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;

public class Sezione1AcquistiBuilder extends SezioneRigheBuilder<RigaSezione1Intra> {

	private static Logger logger = Logger.getLogger(Sezione1AcquistiBuilder.class);

	/**
	 * Costruttore.
	 */
	public Sezione1AcquistiBuilder() {
		super();
	}

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
		sb.append("select nazione.codice as fornitoreStato, ");
		sb.append("anagrafica.partiteIVA as fornitorepiva, ");
		sb.append("sum(rb.importo.importoInValutaAzienda) as importo, ");
		sb.append("sum(rb.importo.importoInValuta) as importoInValuta, ");
		sb.append("intra.naturaTransazione as naturaTransazione, ");
		sb.append("servizi as nomenclatura, ");
		sb.append("sum(rb.massa) as massaCalcolata, ");
		sb.append("rb.um as um, ");
		sb.append("sum(rb.importo.importoInValutaAzienda) as valoreStatisticoEuro , ");
		sb.append("intra.gruppoCondizioneConsegna as gruppoCondizioneConsegna, ");
		sb.append("intra.modalitaTrasporto as modalitaTrasporto, ");
		sb.append("intra.paese as paese, ");
		sb.append("intra.provincia as provincia, ");
		sb.append("rb.paeseOrigineArticolo as paeseOrigineArticolo ");
		sb.append("from RigaBeneIntra rb, AreaContabile ac,AreaContabile acOrigine ");
		sb.append("join rb.nomenclatura servizi ");
		sb.append("join rb.areaIntra intra ");
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
		sb.append("group by anagrafica.partiteIVA,intra.naturaTransazione, ");
		sb.append("servizi,rb.um,intra.gruppoCondizioneConsegna,intra.modalitaTrasporto,intra.paese,intra.provincia");
		Query query = panjeaDAO.prepareQuery(sb.toString(), RigaSezione1Intra.class, null);
		query.setParameter("dataIniziale", dichiarazioneIntra.getDataIniziale());
		query.setParameter("dataFinale", dichiarazioneIntra.getDataFinale());
		List<RigaSezioneIntra> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore ", e);
			throw new RuntimeException("errore nel recuperare le righeBene per la sezione 1", e);
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
		sb.append("select nazione.codice as fornitoreStato, ");
		sb.append("anagrafica.partiteIVA as fornitorepiva, ");
		sb.append("sum(rb.importo.importoInValutaAzienda)  as importo, ");
		sb.append("sum(rb.importo.importoInValuta)  as importoInValuta, ");
		sb.append("intra.naturaTransazione as naturaTransazione, ");
		sb.append("servizi as nomenclatura, ");
		sb.append("sum(rb.massa) as massaCalcolata, ");
		sb.append("rb.um as um, ");
		sb.append("sum(rb.importo.importoInValutaAzienda) as valoreStatisticoEuro , ");
		sb.append("intra.gruppoCondizioneConsegna as gruppoCondizioneConsegna, ");
		sb.append("intra.modalitaTrasporto as modalitaTrasporto, ");
		sb.append("intra.paese as paese, ");
		sb.append("intra.provincia as provincia, ");
		sb.append("rb.paeseOrigineArticolo as paeseOrigineArticolo ");
		sb.append("from RigaBeneIntra rb, AreaContabile ac,AreaContabile acOrigine ");
		sb.append("join rb.nomenclatura servizi ");
		sb.append("join rb.areaIntra intra ");
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
		sb.append("group by anagrafica.partiteIVA,intra.naturaTransazione, ");
		sb.append("servizi,rb.um,intra.gruppoCondizioneConsegna,intra.modalitaTrasporto,intra.paese,intra.provincia");
		Query query = panjeaDAO.prepareQuery(sb.toString(), RigaSezione1Intra.class, null);
		query.setParameter("dataIniziale", dichiarazioneIntra.getDataIniziale());
		query.setParameter("dataFinale", dichiarazioneIntra.getDataFinale());
		List<RigaSezioneIntra> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore ", e);
			throw new RuntimeException("errore nel recuperare le righeBene per la sezione 1", e);
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
	protected RigaSezioneIntra calcolaRigaSezione(RigaSezioneIntra rigaSezioneIntra) {
		// per la sezione 1 posso avere l'unità supplementare impostata
		if (rigaSezioneIntra instanceof RigaSezione1Intra) {
			RigaSezione1Intra rigaSezione1 = (RigaSezione1Intra) rigaSezioneIntra;
			rigaSezione1 = calcolaRigaSezione1(rigaSezione1);
			rigaSezione1.setValoreStatisticoEuro(calcolaValoreStatistico(rigaSezione1.getImporto(), rigaSezione1
					.getDichiarazione().getPercValoreStatistico()));
			rigaSezioneIntra = rigaSezione1;
		}
		return super.calcolaRigaSezione(rigaSezioneIntra);
	}

	@Override
	protected Map<Long, RigaSezioneIntra> creaRighe(PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra) {
		// Recupero le righe relativi ai documenti intra del periodo.
		Map<Long, RigaSezioneIntra> righe = getRigheBene(panjeaDAO, dichiarazioneIntra);

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
	private Map<Long, RigaSezioneIntra> getRigheBene(PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra) {
		StringBuilder sb = new StringBuilder();
		sb.append("select nazione.codice as fornitoreStato, ");
		sb.append("anagrafica.partiteIVA as fornitorepiva, ");
		sb.append("sum(rb.importo.importoInValutaAzienda) as importo, ");
		sb.append("sum(rb.importo.importoInValuta) as importoInValuta, ");
		sb.append("intra.naturaTransazione as naturaTransazione, ");
		sb.append("servizi as nomenclatura, ");
		sb.append("sum(rb.massa) as massaCalcolata, ");
		sb.append("rb.um as um, ");
		sb.append("sum(rb.importo.importoInValutaAzienda) as valoreStatisticoEuro , ");
		sb.append("intra.gruppoCondizioneConsegna as gruppoCondizioneConsegna, ");
		sb.append("intra.modalitaTrasporto as modalitaTrasporto, ");
		sb.append("intra.paese as paese, ");
		sb.append("intra.provincia as provincia, ");
		sb.append("rb.paeseOrigineArticolo as paeseOrigineArticolo ");
		sb.append("from RigaBeneIntra rb, AreaContabile ac ");
		sb.append("join rb.nomenclatura servizi ");
		sb.append("join rb.areaIntra intra ");
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
		sb.append("group by anagrafica.partiteIVA,intra.naturaTransazione, ");
		sb.append("servizi,rb.um,intra.gruppoCondizioneConsegna,intra.modalitaTrasporto,intra.paese,intra.provincia");
		Query query = panjeaDAO.prepareQuery(sb.toString(), RigaSezione1Intra.class, null);
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
