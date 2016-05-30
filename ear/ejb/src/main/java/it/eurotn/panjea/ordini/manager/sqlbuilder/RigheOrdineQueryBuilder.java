package it.eurotn.panjea.ordini.manager.sqlbuilder;

public final class RigheOrdineQueryBuilder {

	/**
	 * Ritorna l'SQL per il caricamento delle righe ordine.
	 * 
	 * @param idAreaOrdine
	 *            id dell'area ordine di riferimento
	 * @param idEntita
	 *            id entita per il caricamento del codice entita relativo all'articolo
	 * @return SQL creato
	 */
	public static String getSQLRigheOrdine(Integer idAreaOrdine, Integer idEntita) {

		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append("righeOrd.TIPO_RIGA as tipoRiga, ");
		sb.append("righeOrd.id as id, ");
		sb.append("righeOrd.areaOrdine_id as idAreaOrdine, ");
		sb.append("art.id as idArticolo, ");
		sb.append("art.codice as codiceArticolo, ");
		sb.append("(select ");
		sb.append("		codiciarti3_.codice ");
		sb.append("		from ");
		sb.append("		maga_articoli articolo2_ ");
		sb.append("		left outer join ");
		sb.append("		maga_codici_articolo_entita codiciarti3_ ");
		sb.append("		on articolo2_.id=codiciarti3_.articolo_id ");
		sb.append("		where ");
		sb.append("		codiciarti3_.entita_id=" + idEntita);
		sb.append("		and art.id=articolo2_.id) as codiceArticoloEntita, ");
		sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
		sb.append("articoloPadre.id as idArticoloPadre, ");
		sb.append("articoloPadre.codice as codiceArticoloPadre, ");
		sb.append("articoloPadre.descrizioneLinguaAziendale as descrizioneArticoloPadre, ");
		sb.append("(select codiciarti3_.codice from maga_articoli articoloPadre2 ");
		sb.append("left outer join maga_codici_articolo_entita codiciarti3_ on articoloPadre2.id=codiciarti3_.articolo_id ");
		sb.append("where codiciarti3_.entita_id=" + idEntita);
		sb.append(" and articoloPadre.id=articoloPadre2.id) as codiceArticoloPadreEntita, ");
		sb.append("righeOrd.descrizione as descrizioneRiga, ");
		sb.append("righeOrd.codiceValuta as codiceValutaPrezzoUnitario, ");
		sb.append("righeOrd.importoInValuta as importoInValutaPrezzoUnitario, ");
		sb.append("righeOrd.importoInValutaAzienda as importoInValutaAziendaPrezzoUnitario, ");
		sb.append("righeOrd.tassoDiCambio as tassoDiCambioPrezzoUnitario, ");
		sb.append("righeOrd.qta as qtaRiga, ");
		sb.append("righeOrd.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("righeOrd.codiceValutaNetto as codiceValutaPrezzoNetto, ");
		sb.append("righeOrd.importoInValutaNetto as importoInValutaPrezzoNetto, ");
		sb.append("righeOrd.importoInValutaAziendaNetto as importoInValutaAziendaPrezzoNetto, ");
		sb.append("righeOrd.tassoDiCambioNetto as tassoDiCambioPrezzoNetto, ");
		sb.append("righeOrd.evasioneForzata as evasioneForzata, ");
		sb.append("righeOrd.variazione1 as variazione1, ");
		sb.append("righeOrd.variazione2 as variazione2, ");
		sb.append("righeOrd.variazione3 as variazione3, ");
		sb.append("righeOrd.variazione4 as variazione4, ");
		sb.append("righeOrd.dataConsegna as dataConsegna, ");
		sb.append("righeOrd.codiceTipoDocumentoCollegato as codiceTipoDocumentoCollegato, ");
		sb.append("righeOrd.areaPreventivoCollegata_id as idAreaPreventivoCollegata, ");
		sb.append("righeOrd.codiceValutaTotale as codiceValutaPrezzoTotale, ");
		sb.append("righeOrd.importoInValutaTotale as importoInValutaPrezzoTotale, ");
		sb.append("righeOrd.importoInValutaAziendaTotale as importoInValutaAziendaPrezzoTotale, ");
		sb.append("righeOrd.tassoDiCambioTotale as tassoDiCambioPrezzoTotale, ");
		sb.append("(select ");
		sb.append("		sum(righeMag.qta*righeMag.moltQtaOrdine) ");
		sb.append("		from ordi_righe_ordine righeOrd2 inner join maga_righe_magazzino righeMag on (righeOrd2.id = righeMag.rigaOrdineCollegata_Id) ");
		sb.append("		where ");
		sb.append("		righeOrd2.id=righeOrd.id) as qtaChiusa, ");
		sb.append("righeOrd.nota as rigaNota, ");
		sb.append("righeOrd.noteRiga as noteRiga, ");
		sb.append("righeOrd.rigaAutomatica as rigaAutomatica, ");
		sb.append("righeOrd.livello as livello ");
		sb.append("from ordi_righe_ordine righeOrd ");
		sb.append("left outer join maga_articoli art on righeOrd.articolo_id=art.id ");
		sb.append("left outer join maga_articoli articoloPadre on righeOrd.articoloDistinta_id=articoloPadre.id ");
		sb.append("where righeOrd.areaOrdine_id=" + idAreaOrdine.intValue());
		sb.append(" order by righeOrd.ordinamento ");

		return sb.toString();
	}

	/**
	 * Costruttore.
	 */
	private RigheOrdineQueryBuilder() {
		super();
	}
}
