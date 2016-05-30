package it.eurotn.panjea.magazzino.manager.sqlbuilder;

import java.util.Date;
import java.util.List;

public class DatawarehouseDettaglioQueryBuilder {
	/**
	 * 
	 * @param fieldsAnalisi
	 *            la lista contiene un array con index:0=nome colonna e index:1=valore
	 * @return stringa sql per il dettaglio dei movimenti nel datawarehouse
	 */
	public String getSql(List<Object[]> fieldsAnalisi) {
		boolean includeData = false;
		boolean includeDep = false;

		String andString = " AND ";
		StringBuilder whereSb = new StringBuilder(" WHERE ");
		StringBuilder sb = new StringBuilder(
				"select CONCAT_WS(' - ',mov.tipoDocumentoCodice,mov.tipoDocumentoDescrizione),mov.dataDocumento,mov.numeroDocumento,mov.dataRegistrazione,");
		sb.append("CONCAT_WS(' - ',art.codice,art.descrizioneLinguaAziendale),CONCAT_WS(' - ',CAST(sedi.codice AS CHAR(11)),sedi.denominazione), ");
		sb.append("(mov.qtaCarico + mov.qtaScarico + mov.qtaCaricoAltro + mov.qtaScaricoAltro) as qta, ");
		sb.append("(mov.qtaFatturatoCarico + mov.qtaFatturatoScarico) as qtaFatturato, ");
		sb.append("(mov.importoCarico + mov.importoScarico + mov.importoCaricoAltro + mov.importoScaricoAltro) as importo, ");
		sb.append("(mov.importoFatturatoCarico + mov.importoFatturatoScarico) as importoFatturato, ");
		sb.append("mov.areaMagazzino_id ");
		sb.append(" from dw_movimentimagazzino mov inner join dw_articoli art on art.id=mov.articolo_id left join dw_sedientita sedi on sedi.sede_entita_id=mov.sedeEntita_id ");
		for (Object[] field : fieldsAnalisi) {
			if (!includeDep && ((String) field[0]).startsWith("dep")) {
				sb.append("inner join dw_depositi dep on dep.id=mov.deposito_id ");
				includeDep = true;
			}
			if (!includeData && ((String) field[0]).startsWith("data")) {
				sb.append("inner join dw_dimensionedata data on data.DATA_ID=mov.dataRegistrazione ");
				includeData = true;
			}
			whereSb.append(field[0]);
			whereSb.append(" = ");
			if (field[1] instanceof String || field[1] instanceof Date) {
				whereSb.append("'");
				whereSb.append(field[1]);
				whereSb.append("'");
			} else {
				whereSb.append(field[1]);
			}
			whereSb.append(andString);
		}
		whereSb.delete(whereSb.length() - andString.length(), whereSb.length());
		sb.append(whereSb);
		return sb.toString();
	}
}
