package it.eurotn.panjea.contabilita.manager.spesometro.builder.analitico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.SpesometroRecordBuilder.QuadroD;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

public class SpesometroAnaliticoManager {

    private static final Logger LOGGER = Logger.getLogger(SpesometroAnaliticoManager.class);

    private PanjeaDAO panjeaDAO;

    private Map<QuadroD, List<DocumentoSpesometro>> documenti;

    private ParametriCreazioneComPolivalente params;

    /**
     * Costruttore.
     *
     * @param panjeaDAO
     *            panjeaDAO
     * @param params
     *            parametri per la crazione dello spesometro
     */
    public SpesometroAnaliticoManager(final PanjeaDAO panjeaDAO, final ParametriCreazioneComPolivalente params) {
        super();
        this.panjeaDAO = panjeaDAO;
        this.params = params;
    }

    /**
     * Carica tutti i documenti per lo spesometro analitico.
     */
    public void caricaDocumenti() {
        documenti = new HashMap<>();
        documenti.put(QuadroD.FE, caricaOperazioni(true, false));
        documenti.put(QuadroD.FR, caricaOperazioni(false, false));
        documenti.put(QuadroD.NE, caricaOperazioni(true, true));
        documenti.put(QuadroD.NR, caricaOperazioni(false, true));
        documenti.put(QuadroD.FN, caricaOperazioniNonResidenti(true, false));
        documenti.put(QuadroD.SE, caricaOperazioniNonResidenti(false, true));
    }

    /**
     * Carica tutte le operazioni.
     *
     * @param attive
     *            <code>true</code> operazioni attive, <code>false</code> operazioni passive
     * @param noteCredito
     *            <code>true</code> carica solo note accredito, <code>false</code> esclude le note di accredito
     * @return operazioni caricate
     */
    @SuppressWarnings("unchecked")
    private List<DocumentoSpesometro> caricaOperazioni(boolean attive, boolean noteCredito) {
        QueryImpl query = (QueryImpl) panjeaDAO.getEntityManager().createNativeQuery(getSQLDocumenti(attive));
        SQLQuery sqlQuery = (SQLQuery) query.getHibernateQuery();
        sqlQuery = setupQuery(sqlQuery, noteCredito);

        QueryImpl queryCointestati = (QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(getSQLDocumentiCointestati(attive));
        SQLQuery sqlQueryCointestati = (SQLQuery) queryCointestati.getHibernateQuery();
        sqlQueryCointestati = setupQuery(sqlQueryCointestati, noteCredito);

        List<DocumentoSpesometro> results = new ArrayList<DocumentoSpesometro>();
        List<DocumentoSpesometro> resultsCointestati = new ArrayList<DocumentoSpesometro>();
        try {
            results = panjeaDAO.getResultList(query);
            resultsCointestati = panjeaDAO.getResultList(queryCointestati);

            if (!CollectionUtils.isEmpty(resultsCointestati)) {
                results.addAll(resultsCointestati);
            }
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare le fatture attive dello spesometro", e);
            throw new RuntimeException(e);
        }

        return results;
    }

    /**
     * Carica tutte le operazioni legate ai non residenti.
     *
     * @param attive
     *            <code>true</code> operazioni attive, <code>false</code> operazioni passive
     * @param servizi
     *            <code>true</code> carica solo righe iva con codice iva di tipo servizi, <code>false</code> esclude i
     *            codici iva di tipo servizi
     * @return operazioni caricate
     */
    @SuppressWarnings("unchecked")
    private List<DocumentoSpesometro> caricaOperazioniNonResidenti(boolean attive, boolean servizi) {
        QueryImpl query = (QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(getSQLDocumentiNonResidenti(attive, servizi));
        SQLQuery sqlQuery = (SQLQuery) query.getHibernateQuery();
        sqlQuery = setupQueryNonResidenti(sqlQuery);

        QueryImpl queryCointestati = (QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(getSQLDocumentiNonResidentiCointestato(attive, servizi));
        SQLQuery sqlQueryCointestati = (SQLQuery) queryCointestati.getHibernateQuery();
        sqlQueryCointestati = setupQueryNonResidenti(sqlQueryCointestati);

        List<DocumentoSpesometro> results = new ArrayList<DocumentoSpesometro>();
        List<DocumentoSpesometro> resultsCointestati = new ArrayList<DocumentoSpesometro>();
        try {
            results = panjeaDAO.getResultList(query);
            resultsCointestati = panjeaDAO.getResultList(queryCointestati);

            if (!CollectionUtils.isEmpty(resultsCointestati)) {
                results.addAll(resultsCointestati);
            }
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare le fatture attive dello spesometro", e);
            throw new RuntimeException(e);
        }

        return results;
    }

    /**
     * @return restituisce la lista di tutti i documenti caricati per lo spesometro.
     */
    public List<DocumentoSpesometro> getAllDocumenti() {

        List<DocumentoSpesometro> result = new ArrayList<>();
        for (Entry<QuadroD, List<DocumentoSpesometro>> documentiQuadro : documenti.entrySet()) {
            result.addAll(documentiQuadro.getValue());
        }

        return result;
    }

    /**
     * Restituisce i documenti raggruppati per modulo secondo lo schema del record D. Ogni modulo comprende 6 FE - 6 FR
     * - 10 NE - 10 NR - 3 FN - 3 SE.
     *
     * @return documenti raggruppati per modulo
     */
    public Map<Integer, List<DocumentoSpesometro>> getDocumentiByModuloRecordD() {

        Map<Integer, List<DocumentoSpesometro>> mapDocumenti = new HashMap<Integer, List<DocumentoSpesometro>>();
        splitDocument(mapDocumenti, getDocumentoQuadro(QuadroD.FE), 6);
        splitDocument(mapDocumenti, getDocumentoQuadro(QuadroD.FR), 6);
        splitDocument(mapDocumenti, getDocumentoQuadro(QuadroD.NE), 10);
        splitDocument(mapDocumenti, getDocumentoQuadro(QuadroD.NR), 10);
        splitDocument(mapDocumenti, getDocumentoQuadro(QuadroD.FN), 3);
        splitDocument(mapDocumenti, getDocumentoQuadro(QuadroD.SE), 3);

        return mapDocumenti;
    }

    /**
     * Restituisce tutti i documenti del quadro specificato.
     *
     * @param modulo
     *            modulo
     * @return documenti
     */
    public List<DocumentoSpesometro> getDocumentoQuadro(QuadroD modulo) {

        List<DocumentoSpesometro> documentiQuadro = documenti.get(modulo);

        if (documentiQuadro == null) {
            documentiQuadro = new ArrayList<>();
        }

        return documentiQuadro;
    }

    /**
     * Restituisce il numero dei documenti presenti del quadro specificato.
     *
     * @param quadro
     *            quadro
     * @param riepilogativi
     *            <code>true</code> per i soli documenti di entità riepilogative, <code>false</code> altrimenti
     * @return numero documenti
     */
    public int getNumeroDocumenti(QuadroD quadro, boolean riepilogativi) {

        int numeroDoc = 0;
        for (DocumentoSpesometro doc : getDocumentoQuadro(quadro)) {
            if (doc.getEntita().isRiepilogativo() == riepilogativi) {
                numeroDoc++;
            }
        }

        return numeroDoc;
    }

    /**
     * @param attive
     *            <code>true</code> per caricare solo i documenti attivi, <code>false</code> altrimenti
     * @return sql per caricare i documenti per lo spesometro analitico
     */
    private String getSQLDocumenti(boolean attive) {

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("  tipoDoc.tipoEntita as attivo, ");
        sb.append("  doc.id as idDocumento, ");
        sb.append("  ac.dataRegistrazione as dataRegistrazione, ");
        sb.append("  ent.id as idEntita, ");
        sb.append("  ent.codice as codiceEntita, ");
        sb.append("  anag.denominazione as denominazioneEntita, ");
        sb.append("  ent.riepilogativo as riepilogativo, ");
        sb.append("  anag.id as idAnagrafica, ");
        sb.append("  anag.partita_iva as partitaIvaEntita, ");
        sb.append("  anag.codice_fiscale as codiceFiscaleEntita, ");
        sb.append("  abs(sum(rigaiva.importoInValutaAziendaImponibile)) as imponibile, ");
        sb.append("  abs(sum(rigaiva.importoInValutaAziendaImposta)) as imposta, ");
        sb.append("  doc.codice as numero, ");
        sb.append("  doc.codiceOrder as numeroOrder, ");
        sb.append("  tipoDoc.id as idTipoDocumento, ");
        sb.append("  tipoDoc.codice as codiceTipoDocumento, ");
        sb.append("  tipoDoc.descrizione as descrizioneTipoDocumento, ");
        sb.append("  tipoDoc.notaCreditoEnable as notaCredito, ");
        sb.append("  if(tac.gestioneIva = 2,TRUE,FALSE) as reverseCharge ");
        sb.append("from cont_righe_iva rigaiva inner join cont_aree_iva areaiva on rigaiva.areaIva_id=areaiva.id ");
        sb.append("             inner join docu_documenti doc on doc.id = areaiva.documento_id ");
        sb.append("             inner join docu_tipi_documento tipoDoc on tipoDoc.id = doc.tipo_documento_id ");
        sb.append("             inner join anag_codici_iva codIva on codIva.id = rigaiva.codiceIva_id ");
        sb.append("             inner join anag_entita ent on ent.id = doc.entita_id ");
        sb.append("             inner join anag_anagrafica anag on anag.id = ent.anagrafica_id ");
        sb.append("             inner join anag_sedi_anagrafica sedeAnag on anag.sede_anagrafica_id = sedeAnag.id ");
        sb.append("             inner join geog_nazioni naz on naz.id = sedeAnag.nazione_id ");
        sb.append("             inner join cont_area_contabile ac on ac.documento_id = doc.id ");
        sb.append("             inner join cont_tipi_area_contabile tac on ac.tipoAreaContabile_id = tac.id ");
        sb.append("               left join geog_nazioni nazLR on nazLR.id = anag.nazioneNascita_lr_id ");
        sb.append("where codIva.includiSpesometro = 1 and ");
        sb.append("   ent.escludiSpesometro = 0 and ");
        sb.append("   (naz.intra = 0 && NOT(nazLR.codice = 'IT' and naz.codice <> 'IT')) and ");
        sb.append("   naz.blacklist = 0 and ");
        sb.append("   anag.nazioneNascita_lr_id is null and ");
        sb.append("     tipoDoc.notaCreditoEnable = :paramNotaCreditoEnable and ");
        if (attive) {
            sb.append("      tipoDoc.tipoEntita = 0 and ");
        } else {
            sb.append("      tipoDoc.tipoEntita = 1 and ");
        }
        sb.append("   year(ac.dataRegistrazione) = :paramAnno and ");
        sb.append("   ac.statoAreaContabile < 2 ");
        sb.append("group by doc.id ");

        return sb.toString();
    }

    /**
     * @param attive
     *            <code>true</code> per caricare solo i documenti attivi, <code>false</code> altrimenti
     * @return sql per caricare i documenti per lo spesometro analitico
     */
    private String getSQLDocumentiCointestati(boolean attive) {

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("  tipoDoc.tipoEntita as attivo, ");
        sb.append("  doc.id as idDocumento, ");
        sb.append("  ac.dataRegistrazione as dataRegistrazione, ");
        sb.append("  ent.id as idEntita, ");
        sb.append("  ent.codice as codiceEntita, ");
        sb.append("  anag.denominazione as denominazioneEntita, ");
        sb.append("  ent.riepilogativo as riepilogativo, ");
        sb.append("  anag.id as idAnagrafica, ");
        sb.append("  anag.partita_iva as partitaIvaEntita, ");
        sb.append("  anag.codice_fiscale as codiceFiscaleEntita, ");
        sb.append("  abs(sum(rigaiva.importoInValutaAziendaImponibile)) as imponibile, ");
        sb.append("  abs(sum(rigaiva.importoInValutaAziendaImposta)) as imposta, ");
        sb.append("  doc.codice as numero, ");
        sb.append("  doc.codiceOrder as numeroOrder, ");
        sb.append("  tipoDoc.id as idTipoDocumento, ");
        sb.append("  tipoDoc.codice as codiceTipoDocumento, ");
        sb.append("  tipoDoc.descrizione as descrizioneTipoDocumento, ");
        sb.append("  tipoDoc.notaCreditoEnable as notaCredito, ");
        sb.append("  if(tac.gestioneIva = 2,TRUE,FALSE) as reverseCharge ");
        sb.append("from cont_righe_iva rigaiva inner join cont_aree_iva areaiva on rigaiva.areaIva_id=areaiva.id ");
        sb.append("             inner join docu_documenti doc on doc.id = areaiva.documento_id ");
        sb.append("             inner join docu_tipi_documento tipoDoc on tipoDoc.id = doc.tipo_documento_id ");
        sb.append("             inner join anag_codici_iva codIva on codIva.id = rigaiva.codiceIva_id ");
        sb.append("             inner join cont_area_contabile ac on ac.documento_id = doc.id ");
        sb.append("             inner join cont_entita_cointestazione ec on ec.areaContabile_id = ac.id ");
        sb.append("             inner join anag_entita ent on ent.id = ec.entita_id ");
        sb.append("             inner join anag_anagrafica anag on anag.id = ent.anagrafica_id ");
        sb.append("             inner join anag_sedi_anagrafica sedeAnag on anag.sede_anagrafica_id = sedeAnag.id ");
        sb.append("             inner join geog_nazioni naz on naz.id = sedeAnag.nazione_id ");
        sb.append("             inner join cont_tipi_area_contabile tac on ac.tipoAreaContabile_id = tac.id ");
        sb.append("               left join geog_nazioni nazLR on nazLR.id = anag.nazioneNascita_lr_id ");
        sb.append("where codIva.includiSpesometro = 1 and ");
        sb.append("   ent.escludiSpesometro = 0 and ");
        sb.append("   (naz.intra = 0 && NOT(nazLR.codice = 'IT' and naz.codice <> 'IT')) and ");
        sb.append("   naz.blacklist = 0 and ");
        sb.append("   anag.nazioneNascita_lr_id is null and ");
        sb.append("     tipoDoc.notaCreditoEnable = :paramNotaCreditoEnable and ");
        if (attive) {
            sb.append("      tipoDoc.tipoEntita = 0 and ");
        } else {
            sb.append("      tipoDoc.tipoEntita = 1 and ");
        }
        sb.append("   year(ac.dataRegistrazione) = :paramAnno and ");
        sb.append("   ac.statoAreaContabile < 2 ");
        sb.append("group by doc.id,ec.entita_id ");

        return sb.toString();
    }

    /**
     * @param attive
     *            <code>true</code> per caricare solo i documenti attivi, <code>false</code> altrimenti
     * @return sql per caricare i documenti per lo spesometro analitico
     */
    private String getSQLDocumentiNonResidenti(boolean attive, boolean servizi) {

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("  tipoDoc.tipoEntita as attivo, ");
        sb.append("  doc.id as idDocumento, ");
        sb.append("  ac.dataRegistrazione as dataRegistrazione, ");
        sb.append("  ent.id as idEntita, ");
        sb.append("  ent.codice as codiceEntita, ");
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

        sb.append("  ent.riepilogativo as riepilogativo, ");
        sb.append("  anag.id as idAnagrafica, ");
        sb.append("  anag.partita_iva as partitaIvaEntita, ");
        sb.append("  anag.codice_fiscale as codiceFiscaleEntita, ");
        sb.append("  abs(sum(rigaiva.importoInValutaAziendaImponibile)) as imponibile, ");
        sb.append("  abs(sum(rigaiva.importoInValutaAziendaImposta)) as imposta, ");
        sb.append("  doc.codice as numero, ");
        sb.append("  doc.codiceOrder as numeroOrder, ");
        sb.append("  tipoDoc.id as idTipoDocumento, ");
        sb.append("  tipoDoc.codice as codiceTipoDocumento, ");
        sb.append("  tipoDoc.descrizione as descrizioneTipoDocumento, ");
        sb.append("  tipoDoc.notaCreditoEnable as notaCredito, ");
        sb.append("  if(tac.gestioneIva = 2,TRUE,FALSE) as reverseCharge ");
        sb.append("from cont_righe_iva rigaiva inner join cont_aree_iva areaiva on rigaiva.areaIva_id=areaiva.id ");
        sb.append("             inner join docu_documenti doc on doc.id = areaiva.documento_id ");
        sb.append("             inner join docu_tipi_documento tipoDoc on tipoDoc.id = doc.tipo_documento_id ");
        sb.append("             inner join anag_codici_iva codIva on codIva.id = rigaiva.codiceIva_id ");
        sb.append("             inner join anag_entita ent on ent.id = doc.entita_id ");
        sb.append("             inner join anag_anagrafica anag on anag.id = ent.anagrafica_id ");
        sb.append("             inner join anag_sedi_anagrafica sedeAnag on anag.sede_anagrafica_id = sedeAnag.id ");
        sb.append("             inner join geog_nazioni naz on naz.id = sedeAnag.nazione_id ");
        sb.append("             inner join cont_area_contabile ac on ac.documento_id = doc.id ");
        sb.append("             inner join cont_tipi_area_contabile tac on ac.tipoAreaContabile_id = tac.id ");
        sb.append("               left join geog_nazioni nazPF on nazPF.id = anag.nazioneNascita_pf_id ");
        sb.append("               left join geog_localita locAnag on locAnag.id = sedeAnag.localita_id ");
        sb.append("               left join geog_nazioni nazLR on nazLR.id = anag.nazioneNascita_lr_id ");
        sb.append("where codIva.includiSpesometro = 1 and ");
        sb.append("   ent.escludiSpesometro = 0 and ");
        sb.append("   nazLR.codice = 'IT' and naz.codice <> 'IT' and ");
        sb.append("   naz.blacklist = 0 and ");
        // sb.append(" tipoDoc.notaCreditoEnable = :paramNotaCreditoEnable and ");
        if (servizi) {
            sb.append(" codIva.tipologiaSpesometro = 1 and ");
        } else {
            sb.append(" codIva.tipologiaSpesometro <> 1 and ");
        }
        if (attive) {
            sb.append("      tipoDoc.tipoEntita = 0 and ");
        } else {
            sb.append("      tipoDoc.tipoEntita = 1 and ");
        }
        sb.append("   year(ac.dataRegistrazione) = :paramAnno and ");
        sb.append("   ac.statoAreaContabile < 2 ");
        sb.append("group by doc.id ");

        return sb.toString();
    }

    /**
     * @param attive
     *            <code>true</code> per caricare solo i documenti attivi, <code>false</code> altrimenti
     * @return sql per caricare i documenti per lo spesometro analitico
     */
    private String getSQLDocumentiNonResidentiCointestato(boolean attive, boolean servizi) {

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("  tipoDoc.tipoEntita as attivo, ");
        sb.append("  doc.id as idDocumento, ");
        sb.append("  ac.dataRegistrazione as dataRegistrazione, ");
        sb.append("  ent.id as idEntita, ");
        sb.append("  ent.codice as codiceEntita, ");
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

        sb.append("  ent.riepilogativo as riepilogativo, ");
        sb.append("  anag.id as idAnagrafica, ");
        sb.append("  anag.partita_iva as partitaIvaEntita, ");
        sb.append("  anag.codice_fiscale as codiceFiscaleEntita, ");
        sb.append("  abs(sum(rigaiva.importoInValutaAziendaImponibile)) as imponibile, ");
        sb.append("  abs(sum(rigaiva.importoInValutaAziendaImposta)) as imposta, ");
        sb.append("  doc.codice as numero, ");
        sb.append("  doc.codiceOrder as numeroOrder, ");
        sb.append("  tipoDoc.id as idTipoDocumento, ");
        sb.append("  tipoDoc.codice as codiceTipoDocumento, ");
        sb.append("  tipoDoc.descrizione as descrizioneTipoDocumento, ");
        sb.append("  tipoDoc.notaCreditoEnable as notaCredito, ");
        sb.append("  if(tac.gestioneIva = 2,TRUE,FALSE) as reverseCharge ");
        sb.append("from cont_righe_iva rigaiva inner join cont_aree_iva areaiva on rigaiva.areaIva_id=areaiva.id ");
        sb.append("             inner join docu_documenti doc on doc.id = areaiva.documento_id ");
        sb.append("             inner join docu_tipi_documento tipoDoc on tipoDoc.id = doc.tipo_documento_id ");
        sb.append("             inner join anag_codici_iva codIva on codIva.id = rigaiva.codiceIva_id ");
        sb.append("             inner join cont_area_contabile ac on ac.documento_id = doc.id ");
        sb.append("             inner join cont_entita_cointestazione ec on ec.areaContabile_id = ac.id ");
        sb.append("             inner join anag_entita ent on ent.id = ec.entita_id ");
        sb.append("             inner join anag_anagrafica anag on anag.id = ent.anagrafica_id ");
        sb.append("             inner join anag_sedi_anagrafica sedeAnag on anag.sede_anagrafica_id = sedeAnag.id ");
        sb.append("             inner join geog_nazioni naz on naz.id = sedeAnag.nazione_id ");
        sb.append("             inner join cont_tipi_area_contabile tac on ac.tipoAreaContabile_id = tac.id ");
        sb.append("               left join geog_nazioni nazPF on nazPF.id = anag.nazioneNascita_pf_id ");
        sb.append("               left join geog_localita locAnag on locAnag.id = sedeAnag.localita_id ");
        sb.append("               left join geog_nazioni nazLR on nazLR.id = anag.nazioneNascita_lr_id ");
        sb.append("where codIva.includiSpesometro = 1 and ");
        sb.append("   ent.escludiSpesometro = 0 and ");
        sb.append("   nazLR.codice = 'IT' and naz.codice <> 'IT' and ");
        sb.append("   naz.blacklist = 0 and ");
        // sb.append(" tipoDoc.notaCreditoEnable = :paramNotaCreditoEnable and ");
        if (servizi) {
            sb.append(" codIva.tipologiaSpesometro = 1 and ");
        } else {
            sb.append(" codIva.tipologiaSpesometro <> 1 and ");
        }
        if (attive) {
            sb.append("      tipoDoc.tipoEntita = 0 and ");
        } else {
            sb.append("      tipoDoc.tipoEntita = 1 and ");
        }
        sb.append("   year(ac.dataRegistrazione) = :paramAnno and ");
        sb.append("   ac.statoAreaContabile < 2 ");
        sb.append("group by doc.id ");

        return sb.toString();
    }

    /**
     * Setta i parametri, resultransformer e scalar alla query rendendola pronta per essere eseguita.
     *
     * @param query
     *            query
     * @param noteCredito
     *            <code>true</code> carica solo note accredito, <code>false</code> esclude le note di accredito
     * @return query risultante
     */
    private SQLQuery setupQuery(SQLQuery query, boolean noteCredito) {

        query.setParameter("paramAnno", params.getAnnoRiferimento());
        query.setParameter("paramNotaCreditoEnable", noteCredito);
        query.setResultTransformer(Transformers.aliasToBean(DocumentoSpesometro.class));
        query.addScalar("attivo");
        query.addScalar("idDocumento");
        query.addScalar("dataRegistrazione");
        query.addScalar("idEntita");
        query.addScalar("codiceEntita");
        query.addScalar("denominazioneEntita");
        query.addScalar("riepilogativo");
        query.addScalar("idAnagrafica");
        query.addScalar("partitaIvaEntita");
        query.addScalar("codiceFiscaleEntita");
        query.addScalar("imponibile");
        query.addScalar("imposta");
        query.addScalar("numero");
        query.addScalar("numeroOrder");
        query.addScalar("idTipoDocumento");
        query.addScalar("codiceTipoDocumento");
        query.addScalar("descrizioneTipoDocumento");
        query.addScalar("notaCredito");
        query.addScalar("reverseCharge");

        return query;
    }

    /**
     * Setta i parametri, resultransformer e scalar alla query rendendola pronta per essere eseguita.
     *
     * @param query
     *            query
     * @param noteCredito
     *            <code>true</code> carica solo note accredito, <code>false</code> esclude le note di accredito
     * @return query risultante
     */
    private SQLQuery setupQueryNonResidenti(SQLQuery query) {

        query.setParameter("paramAnno", params.getAnnoRiferimento());
        query.setResultTransformer(Transformers.aliasToBean(DocumentoSpesometro.class));
        query.addScalar("attivo");
        query.addScalar("idDocumento");
        query.addScalar("dataRegistrazione");
        query.addScalar("idEntita");
        query.addScalar("codiceEntita");
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

        query.addScalar("riepilogativo");
        query.addScalar("idAnagrafica");
        query.addScalar("partitaIvaEntita");
        query.addScalar("codiceFiscaleEntita");
        query.addScalar("imponibile");
        query.addScalar("imposta");
        query.addScalar("numero");
        query.addScalar("numeroOrder");
        query.addScalar("idTipoDocumento");
        query.addScalar("codiceTipoDocumento");
        query.addScalar("descrizioneTipoDocumento");
        query.addScalar("notaCredito");
        query.addScalar("reverseCharge");

        return query;
    }

    private void splitDocument(Map<Integer, List<DocumentoSpesometro>> map, List<DocumentoSpesometro> documents,
            int split) {
        int progr = 1;

        int current = 0;
        for (DocumentoSpesometro doc : documents) {

            if (current == split) {
                progr++;
                current = 0;
            }

            List<DocumentoSpesometro> docs = map.get(progr);
            if (docs == null) {
                docs = new ArrayList<DocumentoSpesometro>();
            }
            docs.add(doc);
            map.put(progr, docs);

            current++;
        }
    }
}
