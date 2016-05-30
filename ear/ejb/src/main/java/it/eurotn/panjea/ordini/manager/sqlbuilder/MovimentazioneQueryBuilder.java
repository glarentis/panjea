package it.eurotn.panjea.ordini.manager.sqlbuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione.ESTATORIGA;

public final class MovimentazioneQueryBuilder {

	/**
	 * Metodo per caricare le righe di movimentazione del magazzino.
	 *
	 * @param articoloLite
	 *            articoloLite
	 * @param depositoLite
	 *            depositoLite
	 * @param entitaLite
	 *            entitaLite
	 * @param dataInizio
	 *            dataInizio
	 * @param dataFine
	 *            dataFine
	 * @param codiceAzienda
	 *            codiceAzienda
	 * @param useTipiAreaMagazzinoFilter
	 *            useTipiAreaMagazzinoFilter
	 * @param tipiAreaOrdine
	 *            tipi area ordine da filtrare
	 * @param noteRiga
	 *            noteRiga
	 * @param dataConsegnaIniziale
	 *            data iniziale di consegna
	 * @param dataConsegnaFinale
	 *            data finale di consegna
	 * @param statoriga
	 *            stato della riga
	 * @param agente
	 *            agente dell'area ordine
	 * @param righeOmaggio
	 *            true se cerco le righe omaggio.
	 * @param b
	 * @return String
	 */
	public static String getSqlMovimentazione(ArticoloLite articoloLite, DepositoLite depositoLite,
			EntitaLite entitaLite, Date dataInizio, Date dataFine, String codiceAzienda,
			boolean useTipiAreaMagazzinoFilter, Set<TipoAreaOrdine> tipiAreaOrdine, String noteRiga,
			Date dataConsegnaIniziale, Date dataConsegnaFinale, ESTATORIGA statoriga, AgenteLite agente,
			boolean righeOmaggio) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder sb = new StringBuilder();
		sb.append("select art.id as idArticolo, ");
		sb.append("	  concat(art.codice) as codiceArticolo, ");
		sb.append("	  concat(art.descrizioneLinguaAziendale) as descrizioneArticolo, ");
		sb.append("	  dep.id as idDeposito, ");
		sb.append("	  concat(dep.codice) as codiceDeposito, ");
		sb.append("	  concat(dep.descrizione) as descrizioneDeposito, ");
		sb.append("	  areaOrdine.id as areaOrdineId, ");
		sb.append("	  areaOrdine.dataRegistrazione as dataRegistrazione, ");
		sb.append("	  areaOrdine.dataOrdine as dataOrdine, ");
		sb.append("	  areaOrdine.modalitaRicezione as modalitaRicezione, ");
		sb.append("	  areaOrdine.numeroOrdine as numeroOrdineRicezione, ");
		sb.append("	  doc.dataDocumento as dataDocumento, ");
		sb.append("	  doc.codice as numeroDocumento, ");
		sb.append("	  tipoDoc.id as idTipoDocumento, ");
		sb.append("	  concat(tipoDoc.codice) as codiceTipoDocumento, ");
		sb.append("	  concat(tipoDoc.descrizione) as descrizioneTipoDocumento, ");
		sb.append("	  sedi.entita_id as idEntita, ");
		sb.append("	  sedi.codice as codiceEntita, ");
		sb.append("	  concat(sedi.denominazione) as descrizioneEntita, ");
		sb.append("	  concat(sedi.TIPO_ANAGRAFICA) as tipoEntita, ");
		sb.append("	  (select max(numeroDecimaliPrezzo) from maga_articoli) as numeroDecimaliPrezzo, ");
		sb.append("	  (select max(numeroDecimaliQta) from maga_articoli)  as numeroDecimaliQuantita, ");
		sb.append("	  riga.importoInValutaAzienda as prezzoUnitario, ");
		sb.append("	  riga.importoInValutaAziendaNetto as prezzoNetto, ");
		sb.append("	  riga.importoInValutaAziendaTotale as PrezzoTotale, ");
		sb.append("	  riga.variazione1 as variazione1, ");
		sb.append("	  riga.variazione2 as variazione2, ");
		sb.append("	  riga.variazione3 as variazione3, ");
		sb.append("	  riga.variazione4 as variazione4, ");
		sb.append("	  riga.qta as quantita, ");
		sb.append(
				"   ((select coalesce(sum(rigamaga.qta*rigamaga.moltQtaOrdine),0) from maga_righe_magazzino as rigamaga where rigamaga.rigaOrdineCollegata_Id = riga.id)) as quantitaEvasa, ");
		sb.append("	  riga.qtaMagazzino as quantitaMagazzino, ");
		sb.append("   riga.evasioneForzata as evasioneForzata,");
		sb.append("	  riga.noteRiga as noteRiga, ");
		sb.append("	  riga.tipoOmaggio as tipoOmaggio, ");
		sb.append("	  riga.dataConsegna as dataConsegna, ");
		sb.append("     riga.id as rigaOrdineId ");
		sb.append("from ordi_righe_ordine riga inner join maga_articoli art on riga.articolo_id = art.id ");
		sb.append("					   inner join ordi_area_ordine areaOrdine on riga.areaOrdine_id = areaOrdine.id ");
		sb.append("					   inner join docu_documenti doc on doc.id = areaOrdine.documento_id ");
		sb.append("					   inner join docu_tipi_documento tipoDoc on tipoDoc.id = doc.tipo_documento_id ");
		sb.append("					   left join anag_depositi dep on areaOrdine.depositoOrigine_id = dep.id ");
		sb.append("					   left  join dw_sedientita sedi on sedi.sede_entita_id = doc.sedeEntita_id ");
		sb.append("where doc.codiceAzienda ='" + codiceAzienda + "'");

		if (articoloLite != null && articoloLite.getId() != null) {
			sb.append(" and riga.articolo_id =" + articoloLite.getId());
		}
		if (dataInizio != null) {
			sb.append(" and areaOrdine.dataRegistrazione >='" + dateFormat.format(dataInizio) + "'");
		}
		if (dataFine != null) {
			sb.append(" and areaOrdine.dataRegistrazione <='" + dateFormat.format(dataFine) + "'");
		}
		if (depositoLite != null && depositoLite.getId() != null) {
			sb.append(" and dep.id =" + depositoLite.getId());
		}
		if (entitaLite != null && entitaLite.getId() != null) {
			sb.append(" and doc.entita_id =" + entitaLite.getId());
		}
		if (agente != null && agente.getId() != null) {
			sb.append(" and areaOrdine.agente_id =" + agente.getId());
		}
		if (noteRiga != null && noteRiga.trim().length() > 0) {
			sb.append(" and riga.noteRiga like '%" + noteRiga + "%'");
		}

		if (righeOmaggio) {
			sb.append(" and riga.tipoOmaggio>0 ");
		}

		if (tipiAreaOrdine != null) {
			if (tipiAreaOrdine.size() == 1) {
				TipoAreaOrdine tipoAreaMagazzino = tipiAreaOrdine.iterator().next();
				sb.append(" and tipoDoc.id =" + tipoAreaMagazzino.getTipoDocumento().getId());
			} else if (tipiAreaOrdine.size() > 1) {
				sb.append(" and tipoDoc.id in (");
				for (TipoAreaOrdine tipoAreaOrdine : tipiAreaOrdine) {
					sb.append(tipoAreaOrdine.getTipoDocumento().getId() + ",");
				}
				// cancello l'ultima virgola che e' in piu' e chiudo la
				// parentesi della IN
				sb.deleteCharAt(sb.length() - 1).append(") ");
			}
		}

		if (dataConsegnaIniziale != null) {
			sb.append(" and riga.dataConsegna >='" + dateFormat.format(dataConsegnaIniziale) + "'");
		}
		if (dataConsegnaFinale != null) {
			sb.append(" and riga.dataConsegna <='" + dateFormat.format(dataConsegnaFinale) + "'");
		}

		switch (statoriga) {
		case EVASA:
			sb.append(
					"  and (riga.evasioneForzata = true or ((select coalesce(sum(rigamaga.qta),0) from maga_righe_magazzino as rigamaga where rigamaga.rigaOrdineCollegata_Id = riga.id)) >= riga.qta) ");
			break;
		case NON_EVASA:
			sb.append(
					"   and (riga.evasioneForzata = false and ((select coalesce(sum(rigamaga.qta),0) from maga_righe_magazzino as rigamaga where rigamaga.rigaOrdineCollegata_Id = riga.id))  < riga.qta) ");
			break;
		default:
			break;
		}

		sb.append(" order by areaOrdine.dataRegistrazione asc ");
		return sb.toString();
	}

	/**
	 * Costruttore.
	 */
	private MovimentazioneQueryBuilder() {
		super();
	}
}
