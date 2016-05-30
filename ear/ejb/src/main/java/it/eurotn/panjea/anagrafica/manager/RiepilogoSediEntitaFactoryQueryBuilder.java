/**
 *
 */
package it.eurotn.panjea.anagrafica.manager;

/**
 * @author leonardo
 */
public class RiepilogoSediEntitaFactoryQueryBuilder {

    /**
     * Costruttore.
     */
    public RiepilogoSediEntitaFactoryQueryBuilder() {
        super();
    }

    /**
     * Restituisce le left join per la query.
     *
     * @return le left join da aggiungere a seconda delle select che si vogliono presentare
     */
    protected String getHqlLeftJoins() {
        StringBuilder sb = new StringBuilder();
        sb.append(" left join sp.codicePagamento codPag ");
        sb.append(" left join sa.datiGeografici.nazione naz ");
        sb.append(" left join sa.datiGeografici.localita loc ");
        sb.append(" left join sa.datiGeografici.cap cap ");
        sb.append(" left join sa.datiGeografici.livelloAmministrativo1 l1 ");
        sb.append(" left join sa.datiGeografici.livelloAmministrativo2 l2 ");
        sb.append(" left join sa.datiGeografici.livelloAmministrativo3 l3 ");
        sb.append(" left join sa.datiGeografici.livelloAmministrativo4 l4 ");
        sb.append(" left join sm.causaleTrasporto causTrasp ");
        sb.append(" left join sm.trasportoCura as traspCura ");
        sb.append(" left join sm.aspettoEsteriore as aspEst ");
        sb.append(" left join sm.tipoPorto as tPorto ");
        sb.append(" left join sm.categoriaContabileSedeMagazzino catContSM ");
        sb.append(" left join sm.categoriaSedeMagazzino catSM ");
        sb.append(" left join sm.codiceIvaAlternativo codIva ");
        sb.append(" left join se.zonaGeografica zona ");
        sb.append(" left join sm.listino listino ");
        sb.append(" left join sm.listinoAlternativo listinoAlt ");
        sb.append(" left join se.agente agente ");
        sb.append(" left join agente.anagrafica anagAgente ");
        return sb.toString();
    }

    /**
     * Restiruisce la where opzionale per restringere la ricerca.
     *
     * @return la where da aggiungere in coda alla query
     */
    protected String getHqlOptionalWhere() {
        return "";
    }

    /**
     * Costruisce la query principale per recuperare il riepilogo sedi.
     *
     * @return la query hql completa
     */
    public String getHqlSedi() {
        StringBuilder sb = new StringBuilder();
        sb.append("select se.ereditaDatiCommerciali as ereditaDatiCommerciali, ");
        sb.append(getHqlSelectFields());
        sb.append("from SedeEntita se ");
        sb.append(" join se.sedeMagazzino sm  ");
        sb.append(" join se.entita ent ");
        sb.append(" join ent.anagrafica anag ");
        sb.append(" join se.sede sa ");
        sb.append(" left join se.sedePagamento sp ");
        sb.append(getHqlLeftJoins());
        sb.append(
                "where anag.codiceAzienda = :codiceAzienda and ent.abilitato = true and se.abilitato = true and se.ereditaDatiCommerciali = false ");
        sb.append(getHqlOptionalWhere());

        return sb.toString();
    }

    /**
     * Costruisce la query principale per recuperare il riepilogo sedi.
     *
     * @return la query hql completa
     */
    public String getHqlSediEreditarie() {
        StringBuilder sb = new StringBuilder();
        sb.append("select se.ereditaDatiCommerciali as ereditaDatiCommerciali, ");
        sb.append(getHqlSelectFields());
        sb.append(" from SedeEntita se, SedeEntita seprinc ");
        sb.append(" join seprinc.sedeMagazzino sm ");
        sb.append(" join se.entita ent ");
        sb.append(" join ent.anagrafica anag ");
        sb.append(" join se.sede sa ");
        sb.append(" left join seprinc.sedePagamento sp ");
        sb.append(getHqlLeftJoins());
        sb.append(
                "where anag.codiceAzienda = :codiceAzienda and ent.abilitato = true and se.abilitato = true and se.ereditaDatiCommerciali = true and ");
        sb.append("	se.entita = seprinc.entita and seprinc.tipoSede.sedePrincipale = true ");
        sb.append(getHqlOptionalWhere());

        return sb.toString();
    }

    /**
     * Seleziona tutti i campi da presentare per il repilogo sedi.
     *
     * @return l'hql con i campi della select
     */
    protected String getHqlSelectFields() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                " ent.id as idEntita,ent.codice as codiceEntita,anag.denominazione as descrizioneEntita,ent.class as tipoEntita, ");
        sb.append(
                "	se.id as idSedeEntita,se.codice as codiceSedeEntita,sa.descrizione as descrizioneSedeEntita,sa.indirizzo as indirizzoSedeEntita, ");
        sb.append("	codPag.descrizione as codicePagamento,codPag.codicePagamento as codicePagamentoCodice, ");
        sb.append(
                "	causTrasp.descrizione as causaleTrasporto,traspCura.descrizione as trasportoCura, aspEst.descrizione as aspettoEsteriore,tPorto.descrizione as tipoPorto, ");
        sb.append(
                "	catContSM.codice as categoriaContabileSedeMagazzino, catSM.descrizione as categoriaSedeMagazzino, ");
        sb.append(
                "	sm.tipologiaGenerazioneDocumentoFatturazione as tipologiaGenerazioneDocumentoFatturazione, sm.tipologiaCodiceIvaAlternativo as tipologiaCodiceIvaAlternativo, ");
        sb.append("	concat(codIva.codice,' - ',codIva.descrizioneInterna) as codiceIvaAlternativo, ");
        sb.append(
                "	sm.raggruppamentoBolle as raggruppamentoBolle,sm.calcoloSpese as calcoloSpese,sm.stampaPrezzo as stampaPrezzo, ");
        sb.append("	se.tipoSede.descrizione as tipoSede,se.codiceValuta as codiceValuta,se.lingua as lingua, ");
        sb.append(
                "	concat(zona.codice,' - ',zona.descrizione) as zonaGeografica, se.bloccoSede.blocco as bloccoSede, ");
        sb.append(" concat(listino.codice,' - ',listino.descrizione) as listino, ");
        sb.append(" concat(listinoAlt.codice,' - ',listinoAlt.descrizione) as listinoAlternativo, ");
        sb.append(" concat(anagAgente.denominazione,' - ',agente.codice) as agente, ");
        sb.append(" listino.id as idListino, ");
        sb.append(" listinoAlt.id as idListinoAlternativo, ");
        sb.append("naz.codice as nazione, ");
        sb.append("loc.descrizione as localita, ");
        sb.append("cap.descrizione as cap, ");
        sb.append("l1.nome as lvl1, ");
        sb.append("l2.sigla as lvl2, ");
        sb.append("l3.nome as lvl3, ");
        sb.append("l4.nome as lvl4, ");
        sb.append("sm.dichiarazioneIntento.dataScadenza as dataScadenza, ");
        sb.append("sa.indirizzoMail as indirizzoMail ");

        return sb.toString();
    }

}
