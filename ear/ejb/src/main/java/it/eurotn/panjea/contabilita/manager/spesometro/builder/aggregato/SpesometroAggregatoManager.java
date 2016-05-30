package it.eurotn.panjea.contabilita.manager.spesometro.builder.aggregato;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import it.eurotn.panjea.anagrafica.domain.TipologiaCodiceIvaSpesometro;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.SpesometroRecordBuilder.QuadroRecordC;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometroAggregato;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;

public class SpesometroAggregatoManager {

    /**
     * Costruttore.
     */
    public SpesometroAggregatoManager() {
    }

    /**
     * Aggrega i documenti per il formato dell'invio in forma aggregata.
     *
     * @param documentiSpesometro
     *            documenti da aggregare
     * @return documenti aggregati
     */
    public Map<QuadroRecordC, List<DocumentoSpesometroAggregato>> getDocumentiAggregati(
            List<DocumentoSpesometro> documentiSpesometro) {

        Map<QuadroRecordC, List<DocumentoSpesometroAggregato>> result = new HashMap<QuadroRecordC, List<DocumentoSpesometroAggregato>>();

        List<DocumentoSpesometro> documentiFA = new ArrayList<DocumentoSpesometro>();
        List<DocumentoSpesometro> documentiBL = new ArrayList<DocumentoSpesometro>();
        for (DocumentoSpesometro doc : documentiSpesometro) {
            QuadroRecordC quadroDoc = getQuadroRecordC(doc);

            switch (quadroDoc) {
            case FA:
                documentiFA.add(doc);
                break;
            case BL:
                documentiBL.add(doc);
                break;
            default:
                // quadro non ancora gestito
                break;
            }
        }

        result.put(QuadroRecordC.FA, raggruppaDocumentiFA(documentiFA));
        result.put(QuadroRecordC.BL, raggruppaDocumentiBL(documentiBL));

        return result;
    }

    /**
     * Calcola il numero di anagrafiche diverse presenti nella lista di documenti.
     *
     * @param documenti
     *            lista di documenti
     * @return numero di anagrafiche
     */
    public int getNumeroAnagraficheDocumentiBLAltro(List<DocumentoSpesometroAggregato> documenti) {

        Set<Integer> idAnagrafiche = new TreeSet<>();
        for (DocumentoSpesometroAggregato doc : documenti) {
            if (doc.getTipologiaCodiceIvaSpesometro() != TipologiaCodiceIvaSpesometro.SERVIZI) {
                idAnagrafiche.add(doc.getAnagrafica().getId());
            }
        }

        return idAnagrafiche.size();
    }

    /**
     * Calcola il numero di anagrafiche diverse presenti nella lista di documenti.
     *
     * @param documenti
     *            lista di documenti
     * @return numero di anagrafiche
     */
    public int getNumeroAnagraficheDocumentiBLServizi(List<DocumentoSpesometroAggregato> documenti) {

        Set<Integer> idAnagrafiche = new TreeSet<>();
        for (DocumentoSpesometroAggregato doc : documenti) {
            if (doc.getTipologiaCodiceIvaSpesometro() == TipologiaCodiceIvaSpesometro.SERVIZI) {
                idAnagrafiche.add(doc.getAnagrafica().getId());
            }
        }

        return idAnagrafiche.size();
    }

    /**
     * Numero di record totali creati per i documenti aggregati del quadro.
     *
     * @param documentiAggregati
     *            documenti aggregati
     * @param quadro
     *            quadro di cui avrere il numero record
     * @return numero record
     */
    public int getNumeroRecordQuadroC(Map<QuadroRecordC, List<DocumentoSpesometroAggregato>> documentiAggregati,
            QuadroRecordC quadro) {

        int numeroRecord = 0;

        switch (quadro) {
        case FA:
            // E' dato dal numero documenti FA diviso 3 poichè ogni record contiene la testata + 3 FA al massimo
            numeroRecord = new BigDecimal(documentiAggregati.get(QuadroRecordC.FA).size())
                    .divide(new BigDecimal(3), RoundingMode.CEILING).intValue();
            break;
        case BL:
            // il quadro BL prevede 1 documento per ogni record quindi è la somma dei documenti BL presenti
            numeroRecord = documentiAggregati.get(QuadroRecordC.BL).size();
            break;
        case SA:
            // per ora non viene gestito
            numeroRecord = 0;
            break;
        default:
            numeroRecord = 0;
            break;
        }

        return numeroRecord;
    }

    /**
     * @param doc
     *            documento spesometro
     * @return restituisce il quadro all'interno del quale il documento deve essere inserito nel record C
     */
    private QuadroRecordC getQuadroRecordC(DocumentoSpesometro doc) {

        QuadroRecordC result;

        // per ora il quadro SA non viene gestito
        if (doc.isNonResidente()) {
            result = QuadroRecordC.BL;
        } else {
            result = QuadroRecordC.FA;
        }

        return result;
    }

    /**
     * @return sql per caricare i documenti per lo spesometro aggregato
     */
    public String getSQLDocumenti() {

        StringBuilder sb = new StringBuilder(2000);
        sb.append(
                "select attivo,idEntita,riepilogativo,idAnagrafica,partitaIvaEntita,codiceFiscaleEntita,denominazioneEntita,legaleRappresentanteCodiceStato,codiceNazioneUICSede, ");
        sb.append(
                "  codiceNazioneSede,descrizioneLocalitaSede,indirizzoSede,personaFisicaNome,personaFisicaCognome,personaFisicaDataNascita,personaFisicaDescrizioneStato,personaFisicaCodiceStatoUIC, ");
        sb.append(
                "  personaFisicaCodiceStato,tipologiaIvaSpesomentro,documentiAggregati,sum(imponibile) as imponibile,sum(imposta) as imposta,notaCredito ");
        sb.append("from ( ");
        sb.append(getSQLDocumentiNoCointestati());
        sb.append(" union ");
        sb.append(getSQLDocumentiCointestati());
        sb.append(") as docu ");
        sb.append("group by idEntita,notaCredito,attivo,tipologiaIvaSpesomentro ");
        sb.append("order by idEntita,notaCredito,attivo ");

        return sb.toString();
    }

    /**
     * @return sql per caricare i documenti per lo spesometro aggregato
     */
    private String getSQLDocumentiCointestati() {

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("  tipoDoc.tipoEntita as attivo, ");
        sb.append("  ent.id as idEntita, ");
        sb.append("  ent.riepilogativo as riepilogativo, ");
        sb.append("  anag.id as idAnagrafica, ");
        sb.append("  anag.partita_iva as partitaIvaEntita, ");
        sb.append("  anag.codice_fiscale as codiceFiscaleEntita, ");
        sb.append("  anag.denominazione as denominazioneEntita, ");

        sb.append("  nazLR.codice as legaleRappresentanteCodiceStato, ");
        sb.append("  naz.codiceNazioneUIC as codiceNazioneUICSede, ");
        sb.append("  naz.codice as codiceNazioneSede, ");
        sb.append("  locAnag.descrizione as descrizioneLocalitaSede, ");
        sb.append("  sedeAnag.indirizzo as indirizzoSede, ");
        sb.append("  anag.personaFisica_nome as personaFisicaNome, ");
        sb.append("  anag.personaFisica_cognome as personaFisicaCognome, ");
        sb.append("  anag.personaFisica_dataNascita as personaFisicaDataNascita, ");
        sb.append("  nazPF.descrizione as personaFisicaDescrizioneStato, ");
        sb.append("  nazPF.codiceNazioneUIC as personaFisicaCodiceStatoUIC, ");
        sb.append("  nazPF.codice as personaFisicaCodiceStato, ");
        sb.append("  codIva.tipologiaSpesometro as tipologiaIvaSpesomentro, ");

        sb.append("  count(distinct doc.id) as documentiAggregati, ");
        sb.append("  abs(sum(rigaiva.importoInValutaAziendaImponibile)) as imponibile, ");
        sb.append("  abs(sum(rigaiva.importoInValutaAziendaImposta)) as imposta, ");
        sb.append("  tipoDoc.notaCreditoEnable as notaCredito ");
        sb.append("from cont_righe_iva rigaiva inner join cont_aree_iva areaiva on rigaiva.areaIva_id=areaiva.id ");
        sb.append("               inner join docu_documenti doc on doc.id = areaiva.documento_id ");
        sb.append("               inner join docu_tipi_documento tipoDoc on tipoDoc.id = doc.tipo_documento_id ");
        sb.append("               inner join anag_codici_iva codIva on codIva.id = rigaiva.codiceIva_id ");
        sb.append("               inner join cont_area_contabile ac on ac.documento_id = doc.id ");
        sb.append("               inner join cont_entita_cointestazione ec on ec.areaContabile_id = ac.id ");
        sb.append("               inner join anag_entita ent on ent.id = ec.entita_id ");
        sb.append("               inner join anag_anagrafica anag on anag.id = ent.anagrafica_id ");
        sb.append("               inner join anag_sedi_anagrafica sedeAnag on anag.sede_anagrafica_id = sedeAnag.id ");
        sb.append("               inner join geog_nazioni naz on naz.id = sedeAnag.nazione_id ");
        sb.append("               left join geog_nazioni nazPF on nazPF.id = anag.nazioneNascita_pf_id ");
        sb.append("               left join geog_localita locAnag on locAnag.id = sedeAnag.localita_id ");
        sb.append("               left join geog_nazioni nazLR on nazLR.id = anag.nazioneNascita_lr_id ");
        sb.append("where codIva.includiSpesometro = 1 and ");
        sb.append("     ent.escludiSpesometro = 0 and ");
        // prendo quelli che non sono intra e anche quelli non resitenti ( stato del legale IT e stato dell'entità
        // diverso da IT)
        sb.append("     (naz.intra = 0 or (nazLR.codice = 'IT' and naz.codice <> 'IT' )) and ");
        sb.append("     naz.blacklist = 0 and ");
        sb.append("     tipoDoc.tipoEntita <= 1 and ");
        sb.append("     year(ac.dataRegistrazione) = :paramAnno and ");
        sb.append("     ac.statoAreaContabile < 2 ");
        sb.append("group by ent.id,tipoDoc.notaCreditoEnable,tipoDoc.tipoEntita,codIva.tipologiaSpesometro ");
        // sb.append("order by ent.id,tipoDoc.notaCreditoEnable,tipoDoc.tipoEntita ");

        return sb.toString();
    }

    /**
     * @return sql per caricare i documenti per lo spesometro aggregato
     */
    private String getSQLDocumentiNoCointestati() {

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("  tipoDoc.tipoEntita as attivo, ");
        sb.append("  ent.id as idEntita, ");
        sb.append("  ent.riepilogativo as riepilogativo, ");
        sb.append("  anag.id as idAnagrafica, ");
        sb.append("  anag.partita_iva as partitaIvaEntita, ");
        sb.append("  anag.codice_fiscale as codiceFiscaleEntita, ");
        sb.append("  anag.denominazione as denominazioneEntita, ");

        sb.append("  nazLR.codice as legaleRappresentanteCodiceStato, ");
        sb.append("  naz.codiceNazioneUIC as codiceNazioneUICSede, ");
        sb.append("  naz.codice as codiceNazioneSede, ");
        sb.append("  locAnag.descrizione as descrizioneLocalitaSede, ");
        sb.append("  sedeAnag.indirizzo as indirizzoSede, ");
        sb.append("  anag.personaFisica_nome as personaFisicaNome, ");
        sb.append("  anag.personaFisica_cognome as personaFisicaCognome, ");
        sb.append("  anag.personaFisica_dataNascita as personaFisicaDataNascita, ");
        sb.append("  nazPF.descrizione as personaFisicaDescrizioneStato, ");
        sb.append("  nazPF.codiceNazioneUIC as personaFisicaCodiceStatoUIC, ");
        sb.append("  nazPF.codice as personaFisicaCodiceStato, ");
        sb.append("  codIva.tipologiaSpesometro as tipologiaIvaSpesomentro, ");

        sb.append("  count(distinct doc.id) as documentiAggregati, ");
        sb.append("  abs(sum(rigaiva.importoInValutaAziendaImponibile)) as imponibile, ");
        sb.append("  abs(sum(rigaiva.importoInValutaAziendaImposta)) as imposta, ");
        sb.append("  tipoDoc.notaCreditoEnable as notaCredito ");
        sb.append("from cont_righe_iva rigaiva inner join cont_aree_iva areaiva on rigaiva.areaIva_id=areaiva.id ");
        sb.append("               inner join docu_documenti doc on doc.id = areaiva.documento_id ");
        sb.append("               inner join docu_tipi_documento tipoDoc on tipoDoc.id = doc.tipo_documento_id ");
        sb.append("               inner join anag_codici_iva codIva on codIva.id = rigaiva.codiceIva_id ");
        sb.append("               inner join anag_entita ent on ent.id = doc.entita_id ");
        sb.append("               inner join anag_anagrafica anag on anag.id = ent.anagrafica_id ");
        sb.append("               inner join anag_sedi_anagrafica sedeAnag on anag.sede_anagrafica_id = sedeAnag.id ");
        sb.append("               inner join geog_nazioni naz on naz.id = sedeAnag.nazione_id ");
        sb.append("               inner join cont_area_contabile ac on ac.documento_id = doc.id ");
        sb.append("               left join geog_nazioni nazPF on nazPF.id = anag.nazioneNascita_pf_id ");
        sb.append("               left join geog_localita locAnag on locAnag.id = sedeAnag.localita_id ");
        sb.append("               left join geog_nazioni nazLR on nazLR.id = anag.nazioneNascita_lr_id ");
        sb.append("where codIva.includiSpesometro = 1 and ");
        sb.append("     ent.escludiSpesometro = 0 and ");
        // prendo quelli che non sono intra e anche quelli non resitenti ( stato del legale IT e stato dell'entità
        // diverso da IT)
        sb.append("     (naz.intra = 0 or (nazLR.codice = 'IT' and naz.codice <> 'IT' )) and ");
        sb.append("     naz.blacklist = 0 and ");
        sb.append("     tipoDoc.tipoEntita <= 1 and ");
        sb.append("     year(ac.dataRegistrazione) = :paramAnno and ");
        sb.append("     ac.statoAreaContabile < 2 ");
        sb.append("group by ent.id,tipoDoc.notaCreditoEnable,tipoDoc.tipoEntita,codIva.tipologiaSpesometro ");
        // sb.append("order by ent.id,tipoDoc.notaCreditoEnable,tipoDoc.tipoEntita ");

        return sb.toString();
    }

    /**
     * Raggruppa i documenti per il quadro BL. Il raggruppamento deve essere fatto prima in base alla tipologia del
     * codice iva facendo distinzione fra SERVIZI e altro e poi per anagrafica.
     *
     * @param documenti
     *            documenti da raggruppare
     * @return documenti raggruppati
     */
    private List<DocumentoSpesometroAggregato> raggruppaDocumentiBL(List<DocumentoSpesometro> documenti) {

        List<DocumentoSpesometro> documentiServizi = new ArrayList<DocumentoSpesometro>();
        List<DocumentoSpesometro> documentiAltro = new ArrayList<DocumentoSpesometro>();
        for (DocumentoSpesometro doc : documenti) {

            if (doc.getTipologiaCodiceIvaSpesometro() == TipologiaCodiceIvaSpesometro.SERVIZI && !doc.isAttivo()) {
                documentiServizi.add(doc);
            } else if (doc.getTipologiaCodiceIvaSpesometro() == TipologiaCodiceIvaSpesometro.MERCE) {
                documentiAltro.add(doc);
            }
        }

        List<DocumentoSpesometroAggregato> documentiAggregati = new ArrayList<>();
        documentiAggregati.addAll(raggruppaDocumentiPerAnagrafica(documentiAltro));
        documentiAggregati.addAll(raggruppaDocumentiPerAnagrafica(documentiServizi));

        return documentiAggregati;
    }

    /**
     * Raggruppa tutti i documenti per il quadro FA. Tutti i documenti devono essere raggruppati per anagrafica.
     *
     * @param documenti
     *            documenti da raggruppare
     * @return documenti raggruppati
     */
    private List<DocumentoSpesometroAggregato> raggruppaDocumentiFA(List<DocumentoSpesometro> documenti) {

        return raggruppaDocumentiPerAnagrafica(documenti);
    }

    /**
     * Raggruppa tutti i documenti per anagrafica.
     *
     * @param documenti
     *            documenti da raggruppare
     * @return documenti raggruppati
     */
    private List<DocumentoSpesometroAggregato> raggruppaDocumentiPerAnagrafica(List<DocumentoSpesometro> documenti) {

        Map<Integer, DocumentoSpesometroAggregato> docs = new HashMap<Integer, DocumentoSpesometroAggregato>();
        for (DocumentoSpesometro doc : documenti) {
            DocumentoSpesometroAggregato docMappa = docs.get(doc.getEntita().getAnagrafica().getId());
            if (docMappa == null) {
                docMappa = new DocumentoSpesometroAggregato();
            }
            docMappa.addDocumento(doc);
            docs.put(doc.getEntita().getAnagrafica().getId(), docMappa);
        }

        List<DocumentoSpesometroAggregato> result = new ArrayList<>();
        result.addAll(docs.values());
        return result;
    }

    /**
     * Setta i parametri, resultransformer e scalar alla query rendendola pronta per essere eseguita.
     *
     * @param query
     *            query
     * @param params
     *            parametri da applicare
     * @return query risultante
     */
    public SQLQuery setupQuery(SQLQuery query, ParametriCreazioneComPolivalente params) {

        query.setParameter("paramAnno", params.getAnnoRiferimento());
        query.setResultTransformer(Transformers.aliasToBean(DocumentoSpesometro.class));
        query.addScalar("attivo");
        query.addScalar("idEntita");
        query.addScalar("riepilogativo");
        query.addScalar("idAnagrafica");
        query.addScalar("partitaIvaEntita");
        query.addScalar("codiceFiscaleEntita");
        query.addScalar("denominazioneEntita");
        query.addScalar("legaleRappresentanteCodiceStato");
        query.addScalar("codiceNazioneUICSede");
        query.addScalar("codiceNazioneSede");
        query.addScalar("descrizioneLocalitaSede");
        query.addScalar("indirizzoSede");
        query.addScalar("personaFisicaNome");
        query.addScalar("personaFisicaCognome");
        query.addScalar("personaFisicaDataNascita");
        query.addScalar("personaFisicaDescrizioneStato");
        query.addScalar("personaFisicaCodiceStatoUIC");
        query.addScalar("personaFisicaCodiceStato");
        query.addScalar("tipologiaIvaSpesomentro");
        query.addScalar("documentiAggregati", Hibernate.INTEGER);
        query.addScalar("imponibile");
        query.addScalar("imposta");
        query.addScalar("notaCredito");

        return query;
    }
}
