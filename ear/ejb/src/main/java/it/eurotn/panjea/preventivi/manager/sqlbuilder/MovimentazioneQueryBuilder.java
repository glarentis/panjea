package it.eurotn.panjea.preventivi.manager.sqlbuilder;

import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.util.PanjeaEJBUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

public final class MovimentazioneQueryBuilder {

	/**
	 * Applica tutti gli addScalar alla query sql.
	 *
	 * @param query
	 *            query sql
	 */
	public static void addQueryScalar(SQLQuery query) {
		query.addScalar("idRiga");
		query.addScalar("idArticolo");
		query.addScalar("codiceArticolo");
		query.addScalar("descrizioneArticolo");
		query.addScalar("areaPreventivoId");
		query.addScalar("dataRegistrazione");
		query.addScalar("dataConsegna");
		query.addScalar("dataDocumento");
		query.addScalar("numeroDocumento", Hibernate.STRING);
		query.addScalar("idTipoDocumento");
		query.addScalar("codiceTipoDocumento");
		query.addScalar("descrizioneTipoDocumento");
		query.addScalar("idEntita");
		query.addScalar("codiceEntita");
		query.addScalar("descrizioneEntita");
		query.addScalar("tipoEntita");
		query.addScalar("descrizioneRiga");
		query.addScalar("numeroDecimaliPrezzo", Hibernate.INTEGER);
		query.addScalar("numeroDecimaliQuantita", Hibernate.INTEGER);
		query.addScalar("prezzoUnitario");
		query.addScalar("prezzoNetto");
		query.addScalar("PrezzoTotale");
		query.addScalar("variazione1");
		query.addScalar("variazione2");
		query.addScalar("variazione3");
		query.addScalar("variazione4");
		query.addScalar("quantita");
		query.addScalar("quantitaEvasa");
		query.addScalar("noteRiga");
		Properties params = new Properties();
		params.put("enumClass", "it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio");
		params.put("type", "12");
		query.addScalar("tipoOmaggio", Hibernate.custom(org.hibernate.type.EnumType.class, params));
	}

	/**
	 * Metodo per caricare le righe di movimentazione del magazzino.
	 *
	 * @param parametri
	 *            parametri ricerca
	 * @param codiceAzienda
	 *            codiceAzienda
	 * @return String
	 */
	public static String getSqlMovimentazione(ParametriRicercaMovimentazione parametri, String codiceAzienda) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder sb = new StringBuilder();
		sb.append("select art.id as idArticolo, ");
		sb.append("			  riga.id as idRiga, ");
		sb.append("			  concat(art.codice) as codiceArticolo, ");
		sb.append("			  concat(art.descrizioneLinguaAziendale) as descrizioneArticolo, ");
		sb.append("			  areaPreventivo.id as areaPreventivoId, ");
		sb.append("			  areaPreventivo.dataRegistrazione as dataRegistrazione, ");
		sb.append("			  areaPreventivo.dataConsegna as dataConsegna, ");
		sb.append("			  doc.dataDocumento as dataDocumento, ");
		sb.append("			  doc.codice as numeroDocumento, ");
		sb.append("			  tipoDoc.id as idTipoDocumento, ");
		sb.append("			  concat(tipoDoc.codice) as codiceTipoDocumento, ");
		sb.append("			  concat(tipoDoc.descrizione) as descrizioneTipoDocumento, ");
		sb.append("			  sedi.entita_id as idEntita, ");
		sb.append("			  sedi.codice as codiceEntita, ");
		sb.append("			  concat(sedi.denominazione) as descrizioneEntita, ");
		sb.append("			  concat(sedi.TIPO_ANAGRAFICA) as tipoEntita, ");
		sb.append("			  riga.descrizione as descrizioneRiga, ");
		sb.append("			  riga.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("			  riga.numeroDecimaliQta as numeroDecimaliQuantita, ");
		sb.append("			  riga.importoInValutaAzienda as prezzoUnitario, ");
		sb.append("			  riga.importoInValutaAziendaNetto as prezzoNetto, ");
		sb.append("			  riga.importoInValutaAziendaTotale as PrezzoTotale, ");
		sb.append("			  riga.variazione1 as variazione1, ");
		sb.append("			  riga.variazione2 as variazione2, ");
		sb.append("			  riga.variazione3 as variazione3, ");
		sb.append("			  riga.variazione4 as variazione4, ");
		sb.append("			  riga.qta as quantita, ");
		sb.append("		   (select coalesce(sum(rigaord.qta),0) from ordi_righe_ordine as rigaord where rigaord.rigaPreventivoCollegata_Id = riga.id) as quantitaEvasa, ");
		sb.append("			  riga.qtaMagazzino as quantitaMagazzino, ");
		sb.append("		   	riga.noteRiga as noteRiga, ");
		sb.append("			  riga.tipoOmaggio as tipoOmaggio ");
		sb.append("		from prev_righe_preventivo riga inner join maga_articoli art on riga.articolo_id = art.id ");
		sb.append("							   inner join prev_area_preventivi areaPreventivo on riga.areaPreventivo_id = areaPreventivo.id ");
		sb.append("							   inner join docu_documenti doc on doc.id = areaPreventivo.documento_id ");
		sb.append("							   inner join docu_tipi_documento tipoDoc on tipoDoc.id = doc.tipo_documento_id ");
		sb.append("							   inner join dw_sedientita sedi on sedi.sede_entita_id = doc.sedeEntita_id ");
		sb.append("where doc.codiceAzienda ='" + codiceAzienda + "'");

		if (parametri.getArticoloLite() != null && parametri.getArticoloLite().getId() != null) {
			sb.append(" and riga.articolo_id =" + parametri.getArticoloLite().getId());
		}

		Date dataRegIniziale = PanjeaEJBUtil.getDateTimeToZero(parametri.getDataRegistrazione().getDataIniziale());
		if (dataRegIniziale != null) {
			sb.append(" and areaPreventivo.dataRegistrazione >='" + dateFormat.format(dataRegIniziale) + "'");
		}

		Date dataRegFinale = PanjeaEJBUtil.getDateTimeToZero(parametri.getDataRegistrazione().getDataFinale());
		if (dataRegFinale != null) {
			sb.append(" and areaPreventivo.dataRegistrazione <='" + dateFormat.format(dataRegFinale) + "'");
		}

		if (parametri.getEntitaLite() != null && parametri.getEntitaLite().getId() != null) {
			sb.append(" and doc.entita_id =" + parametri.getEntitaLite().getId());
		}

		Set<TipoAreaPreventivo> tipiAreePreventivo = parametri.getTipiAreaPreventivo();
		if (tipiAreePreventivo != null) {
			if (tipiAreePreventivo.size() == 1) {
				TipoAreaPreventivo tipoAreaPreventivo = tipiAreePreventivo.iterator().next();
				sb.append(" and tipoDoc.id =" + tipoAreaPreventivo.getTipoDocumento().getId());
			} else if (tipiAreePreventivo.size() > 1) {
				sb.append(" and tipoDoc.id in (");
				for (TipoAreaPreventivo tipoAreaPreventivo : tipiAreePreventivo) {
					sb.append(tipoAreaPreventivo.getTipoDocumento().getId() + ",");
				}
				// cancello l'ultima virgola che e' in piu' e chiudo la
				// parentesi della IN
				sb.deleteCharAt(sb.length() - 1).append(") ");
			}
		}

		Date dataConsIniziale = PanjeaEJBUtil.getDateTimeToZero(parametri.getDataConsegna().getDataIniziale());
		if (dataConsIniziale != null) {
			sb.append(" and riga.dataConsegna >='" + dateFormat.format(dataConsIniziale) + "'");
		}
		Date dataConsFinale = PanjeaEJBUtil.getDateTimeToZero(parametri.getDataConsegna().getDataFinale());
		if (dataConsFinale != null) {
			sb.append(" and riga.dataConsegna <='" + dateFormat.format(dataConsFinale) + "'");
		}

		switch (parametri.getStatoRiga()) {
		case NON_PROCESSATE:
			sb.append(" and (select coalesce(sum(rigaord.qta),0) from ordi_righe_ordine as rigaord where rigaord.rigaPreventivoCollegata_Id = riga.id) = 0 ");
			break;
		case PROCESSATE:
			sb.append(" and (select coalesce(sum(rigaord.qta),0) from ordi_righe_ordine as rigaord where rigaord.rigaPreventivoCollegata_Id = riga.id) > 0 ");
			break;
		default:
			break;
		}

		return sb.toString();
	}

	/**
	 * Costruttore.
	 */
	private MovimentazioneQueryBuilder() {
		super();
	}
}
