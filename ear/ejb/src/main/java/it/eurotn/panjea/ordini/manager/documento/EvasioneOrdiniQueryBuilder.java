package it.eurotn.panjea.ordini.manager.documento;

import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaEvasione;

public final class EvasioneOrdiniQueryBuilder {

	/**
	 * Restituisce l'HQL per il caricamento delle righe da evadere o da associare ad una distinta di carico.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return HQL generato
	 */
	public static String getHQL(ParametriRicercaEvasione parametri) {

		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append("rigaTestata.id as idRigaTestata,");
		sb.append("rigaTestata.descrizione as descrizioneTestata, ");
		sb.append("riga.livello as livello, ");
		sb.append("areaOrdine.id as idAreaOrdine, ");
		sb.append("areaOrdine.version as versionAreaOrdine, ");
		sb.append("areaOrdine.dataRegistrazione as dataRegistrazione, ");
		sb.append("areaOrdine.trasportoCura as trasportoCura, ");
		sb.append("documento.codice as numeroDocumento, ");
		sb.append("documento.id as idDocumento, ");
		sb.append("entita.id as idEntita, ");
		sb.append("entita.codice as codiceEntita, ");
		sb.append("anagrafica.denominazione as descrizioneEntita, ");
		sb.append("tipoDocumento.id as idTipoDocumento, ");
		sb.append("tipoDocumento.version as versionTipoDocumento, ");
		sb.append("tipoDocumento.codice as codiceTipoDocumento, ");
		sb.append("tipoDocumento.descrizione as descrizioneTipoDocumento, ");
		sb.append("tipoDocumento.tipoEntita as tipoEntita, ");
		sb.append("riga.qta as qtaOrdinata, ");
		sb.append("riga.dataConsegna as dataConsegna, ");
		sb.append("riga.articolo.id as idArticolo, ");
		sb.append("riga.articolo.codice as codiceArticolo, ");
		sb.append("riga.descrizione as descrizioneRiga, ");
		sb.append("riga.id as idRigaArticolo, ");
		sb.append("riga.prezzoUnitario.importoInValuta as prezzoUnitario, ");
		sb.append("riga.prezzoUnitario.codiceValuta as codiceValuta, ");
		sb.append("riga.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("riga.variazione1 as variazione1, ");
		sb.append("riga.variazione2 as variazione2, ");
		sb.append("riga.variazione3 as variazione3, ");
		sb.append("riga.variazione4 as variazione4, ");
		sb.append("riga.tipoOmaggio as tipoOmaggio, ");
		sb.append("depositoOrigine.id as idDeposito, ");
		sb.append("depositoOrigine.version as versionDeposito, ");
		sb.append("depositoOrigine.codice as codiceDeposito, ");
		sb.append("depositoOrigine.descrizione as descrizioneDeposito, ");
		sb.append("sedeEntita.id as idSedeEntita, ");
		sb.append("articolo as articolo, ");
		sb.append("sede.descrizione as descrizioneSedeEntita, ");
		sb.append("sede.datiGeografici.livelloAmministrativo1.nome as livelloAmministrativo1, ");
		sb.append("sede.datiGeografici.livelloAmministrativo2.nome as livelloAmministrativo2, ");
		sb.append("sede.datiGeografici.livelloAmministrativo3.nome as livelloAmministrativo3, ");
		sb.append("coalesce(sum(rigacoll.qta*rigacoll.moltQtaOrdine),0) + coalesce(sum(righeDistintaCarico.qtaDaEvadere),0) as qtaEvasa ");
		sb.append("from RigaArticoloOrdine riga ");
		sb.append("inner join riga.areaOrdine areaOrdine ");
		sb.append("inner join areaOrdine.documento documento ");
		sb.append("inner join areaOrdine.tipoAreaOrdine tipoAreaOrdine ");
		sb.append("inner join documento.entita entita ");
		sb.append("inner join entita.anagrafica anagrafica ");
		sb.append("inner join documento.tipoDocumento tipoDocumento ");
		sb.append("inner join documento.sedeEntita sedeEntita ");
		sb.append("inner join sedeEntita.sede sede ");
		sb.append("inner join areaOrdine.depositoOrigine depositoOrigine ");
		sb.append("inner join riga.articolo articolo ");
		sb.append("left join sede.datiGeografici.livelloAmministrativo1 ");
		sb.append("left join sede.datiGeografici.livelloAmministrativo2 ");
		sb.append("left join sede.datiGeografici.livelloAmministrativo3 ");
		sb.append("left join riga.righeMagazzinoCollegate rigacoll ");
		sb.append("left join riga.areaOrdine.documento.entita ");
		sb.append("left join riga.righeDistintaCarico righeDistintaCarico ");
		sb.append("left join riga.rigaTestataCollegata rigaTestata ");
		sb.append("inner join sedeEntita.sedeMagazzino sedeMagazzino ");
		if (parametri.getTrasportoCuraMittente()) {
			sb.append(", TrasportoCura trasportoCura ");
		}
		sb.append("where riga.evasioneForzata = 0 and ");

		if (parametri.getDataRegistrazioneIniziale() != null) {
			sb.append(" areaOrdine.dataRegistrazione >= :dataRegistrazioneIniziale and ");
		}
		if (parametri.getDataRegistrazioneFinale() != null) {
			sb.append(" areaOrdine.dataRegistrazione <= :dataRegistrazioneFinale and ");
		}
		if (parametri.getDataCreazioneOrdini() != null) {
			sb.append(" areaOrdine.dataCreazioneTimeStamp = :dataCreazioneOrdini and ");
		}

		if (parametri.getNumeroDocumentoIniziale() != null) {
			sb.append(" documento.codice >= :numeroDocumentoIniziale and ");
		}

		if (!parametri.getNumeroDocumentoFinale().isEmpty()) {
			sb.append(" documento.codice.codice <= :numeroDocumentoFinale and ");
		}

		if (parametri.getDataConsegnaIniziale() != null) {
			sb.append(" riga.dataConsegna >= :dataConsegnaIniziale and ");
			sb.append(" riga.dataConsegna <= :dataConsegnaFinale and ");
		}

		if (!parametri.getTipiAreaOrdine().isEmpty()) {
			sb.append(" tipoAreaOrdine in (:tipiAreaOrdine) and ");
		}

		if (parametri.getTipoEntita() != null) {
			// tutti i tipi area ordine di entità CLIENTE
			sb.append(" tipoDocumento.tipoEntita = :tipoEntita and ");
		}

		if (parametri.getEntita() != null && parametri.getEntita().getId() != null) {
			sb.append(" documento.entita = :entita and ");
		}

		if (parametri.getSedeEntita() != null && parametri.getSedeEntita().getId() != null) {
			sb.append(" sedeEntita = :sedeEntita and ");
		}

		if (parametri.getDeposito() != null && parametri.getDeposito().getId() != null) {
			sb.append(" depositoOrigine = :deposito and ");
		}

		if (parametri.getArticolo() != null && parametri.getArticolo().getId() != null) {
			sb.append(" articolo = :articolo and ");
		}

		if (parametri.getAgente() != null) {
			sb.append(" areaOrdine.agente=:agente and ");
		}

		if (parametri.getAreeOrdine() != null && parametri.getAreeOrdine().size() > 0) {
			sb.append(" areaOrdine in (:areeOrdine) and ");
		}

		if (parametri.getTrasportoCuraMittente()) {
			sb.append(" trasportoCura.descrizione = areaOrdine.trasportoCura and trasportoCura.mittente=true and ");
		}
		sb.append(" areaOrdine.statoAreaOrdine = 1 and ");
		sb.append(" riga.bloccata = false ");
		sb.append(" group by riga ");
		sb.append(" having (coalesce(sum(rigacoll.qta*rigacoll.moltQtaOrdine),0) + coalesce(sum(righeDistintaCarico.qtaDaEvadere),0)) < riga.qta or riga.qta=0");
		sb.append(" order by riga.ordinamento");

		return sb.toString();

	}

	/**
	 * Costruttore.
	 */
	private EvasioneOrdiniQueryBuilder() {
		super();
	}
}
