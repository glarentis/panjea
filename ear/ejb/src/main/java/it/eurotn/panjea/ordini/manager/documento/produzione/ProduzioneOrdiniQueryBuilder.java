package it.eurotn.panjea.ordini.manager.documento.produzione;

import it.eurotn.panjea.ordini.util.ParametriRicercaProduzione;

public final class ProduzioneOrdiniQueryBuilder {

  /**
   * Costruttore.
   */
  private ProduzioneOrdiniQueryBuilder() {
    super();
  }

  /**
   * Restituisce l'HQL per il caricamento delle righe da evadere per la produzione.
   *
   * @param parametri
   *          parametri di ricerca
   * @return HQL generato
   */
  public static String getHQL(ParametriRicercaProduzione parametri) {

    StringBuffer sb = new StringBuffer();
    sb.append("select ");
    sb.append("rigaTestata.id as idRigaTestata, ");
    sb.append("rigaTestata.descrizione as descrizioneTestata, ");
    sb.append("riga.livello as livello, ");
    sb.append("areaOrdine.id as idAreaOrdine, ");
    sb.append("areaOrdine.version as versionAreaOrdine, ");
    sb.append("areaOrdine.dataRegistrazione as dataRegistrazione, ");
    sb.append("areaOrdine.trasportoCura as trasportoCura, ");
    sb.append("documento.codice as numeroDocumento, ");
    sb.append("documento.dataDocumento as dataDocumento, ");
    sb.append("documento.id as idDocumento, ");
    sb.append("tipoDocumento.id as idTipoDocumento, ");
    sb.append("tipoDocumento.version as versionTipoDocumento, ");
    sb.append("tipoDocumento.codice as codiceTipoDocumento, ");
    sb.append("tipoDocumento.descrizione as descrizioneTipoDocumento, ");
    sb.append("tipoDocumento.tipoEntita as tipoEntita, ");

    sb.append("rigaOrdColl.id as idRigaOrdineCollegata, ");
    sb.append("artRigaOrdColl.id as idArticoloRigaOrdineCollegata, ");
    sb.append("artRigaOrdColl.codice as codiceArticoloRigaOrdineCollegata, ");
    sb.append("rigaOrdColl.descrizione as descrizioneRigaOrdineCollegata, ");

    sb.append("areaOrdColl.id as idAreaOrdineCollegata, ");
    sb.append("areaOrdColl.dataRegistrazione as dataRegistrazioneOrdineCollegato, ");
    sb.append("docOrdColl.id as idDocumentoOrdineCollegato, ");
    sb.append("docOrdColl.dataDocumento as dataDocumentoOrdineCollegato, ");
    sb.append("docOrdColl.codice as numeroDocumentoOrdineCollegato, ");
    sb.append("tipoDocOrdColl.id as idTipoDocumentoOrdineCollegato, ");
    sb.append("tipoDocOrdColl.version as versionTipoDocumentoOrdineCollegato, ");
    sb.append("tipoDocOrdColl.codice as codiceTipoDocumentoOrdineCollegato, ");
    sb.append("tipoDocOrdColl.descrizione as descrizioneTipoDocumentoOrdineCollegato, ");
    sb.append("entitaDocOrdColl.id as idEntitaDocumentoOrdineCollegato, ");
    sb.append("entitaDocOrdColl.version as versionEntitaDocumentoOrdineCollegato, ");
    sb.append("entitaDocOrdColl.codice as codiceEntitaDocumentoOrdineCollegato, ");
    sb.append(
        "anagraficaDocOrdColl.denominazione as denominazioneEntitaDocumentoOrdineCollegato, ");

    sb.append(
        "coalesce(sum(rigacoll.qta*rigacoll.moltQtaOrdine),0) + coalesce(sum(righeDistintaCarico.qtaDaEvadere),0) as qtaEvasa, ");
    sb.append("riga.qta as qtaOrdinata, ");
    sb.append("riga.dataConsegna as dataConsegna, ");
    sb.append("riga.dataProduzione as dataProduzione, ");
    sb.append("riga.articolo.id as idArticolo, ");
    sb.append("riga.articolo.codice as codiceArticolo, ");
    sb.append("riga.descrizione as descrizioneRiga, ");
    sb.append("riga.id as idRigaArticolo, ");
    sb.append("riga.configurazioneDistinta.id as idConfigurazioneDistinta, ");
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
    sb.append("articolo as articolo ");

    sb.append("from RigaArticoloDistintaOrdine riga ");
    sb.append("inner join riga.areaOrdine areaOrdine ");
    sb.append("inner join areaOrdine.documento documento ");
    sb.append("inner join areaOrdine.tipoAreaOrdine tipoAreaOrdine ");
    sb.append("inner join documento.tipoDocumento tipoDocumento ");
    sb.append("inner join areaOrdine.depositoOrigine depositoOrigine ");
    sb.append("inner join riga.articolo articolo ");
    sb.append("left join riga.righeMagazzinoCollegate rigacoll ");
    sb.append("left join riga.righeDistintaCarico righeDistintaCarico ");
    sb.append("left join riga.rigaTestataCollegata rigaTestata ");

    sb.append("left join riga.righeOrdineCollegate rigaOrdColl ");
    sb.append("left join rigaOrdColl.articolo artRigaOrdColl ");
    sb.append("left join rigaOrdColl.areaOrdine areaOrdColl ");
    sb.append("left join areaOrdColl.documento docOrdColl ");
    sb.append("left join docOrdColl.tipoDocumento tipoDocOrdColl ");
    sb.append("left join docOrdColl.entita entitaDocOrdColl ");
    sb.append("left join entitaDocOrdColl.anagrafica anagraficaDocOrdColl ");

    sb.append("where riga.dataProduzione is not null ");
    sb.append("and tipoAreaOrdine.ordineProduzione=true ");
    sb.append("and riga.evasioneForzata = 0 ");

    if (parametri.getDataProduzioneIniziale() != null) {
      sb.append("and riga.dataProduzione >= :dataProduzioneIniziale ");
    }
    if (parametri.getDataProduzioneFinale() != null) {
      sb.append("and riga.dataProduzione <= :dataProduzioneFinale ");
    }

    sb.append("group by riga ");
    sb.append(
        " having (coalesce(sum(rigacoll.qta*rigacoll.moltQtaOrdine),0) + coalesce(sum(righeDistintaCarico.qtaDaEvadere),0)) < riga.qta or riga.qta=0");
    sb.append("order by riga.ordinamento ");

    return sb.toString();

  }
}
