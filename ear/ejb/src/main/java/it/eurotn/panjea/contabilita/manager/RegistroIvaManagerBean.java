/**
 *
 */
package it.eurotn.panjea.contabilita.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva.IndicatoreVolumeAffari;
import it.eurotn.panjea.anagrafica.domain.CodiceIva.TipoCaratteristica;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings.ETipoPeriodicita;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.contabilita.manager.corrispettivo.interfaces.RegistroIvaTipologiaCorrispettivoManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaAnagraficaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.contabilita.manager.interfaces.RegistroIvaManager;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTOAreaComparator;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTOComparator;
import it.eurotn.panjea.contabilita.util.giornaleiva.DataPeriodicitaGiornaleIvaPrecedenteCalculator;
import it.eurotn.panjea.contabilita.util.giornaleiva.DataPeriodicitaGiornaleIvaPrecedenteCalculatorFactory;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Manager che gestisce le operazioni del registro e liquidazione iva roles: amministraRegistroIva,
 * visualizzaRegistroIva.
 *
 * @author Leonardo
 * @version 1.0, 22/nov/07
 */
@Stateless(name = "Panjea.RegistroIvaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RegistroIvaManager")
public class RegistroIvaManagerBean implements RegistroIvaManager {

    private static Logger logger = Logger.getLogger(RegistroIvaManagerBean.class);

    @Resource
    protected SessionContext context;

    @EJB
    protected PanjeaDAO panjeaDAO;

    @EJB
    protected ContabilitaAnagraficaManager contabilitaAnagraficaManager;

    @EJB
    protected AnagraficaService anagraficaService;

    @EJB
    protected RegistroIvaTipologiaCorrispettivoManager registroIvaTipologiaCorrispettivoManager;

    @EJB
    protected PanjeaMessage panjeaMessage;

    @EJB
    protected ContabilitaSettingsManager contabilitaSettingsManager;

    /**
     * Carica il giornale associato al registro iva, se non disponibile si preoccupa di crearne un o nuovo con i
     * parametri anno e mese passati.
     *
     * @param registroIva
     *            il registro iva di cui trovare il giornale associato
     * @param anno
     *            l'anno in cui cercare il giornale
     * @param mese
     *            il mese in cui cercare il giornale
     * @return GiornaleIva
     */
    private GiornaleIva caricaGiornaleByRegistro(RegistroIva registroIva, Integer anno, Integer mese) {
        logger.debug("--> Enter caricaGiornaleByRegistro");
        if (registroIva == null) {
            return null;
        }
        try {
            Query query = panjeaDAO.prepareNamedQuery("GiornaleIva.caricaGiornaleByRegistroIva");

            query.setParameter("paramIdRegistroIva", registroIva.getId());
            query.setParameter("paramAnno", anno);
            query.setParameter("paramMese", mese);
            query.setParameter("paramCodiceAzienda", getAzienda());

            GiornaleIva giornaleIvaTrovato = null;
            try {
                giornaleIvaTrovato = (GiornaleIva) panjeaDAO.getSingleResult(query);
            } catch (ObjectNotFoundException e) {
                logger.debug(
                        "--> Non ho giornale iva associato al registro iva " + registroIva.getId() + ", ritorno null");
                return null;
            }
            logger.debug("--> Exit caricaGiornaleByRegistro");
            return giornaleIvaTrovato;

        } catch (Exception e) {
            logger.error("--> Errore nel caricare il Giornale associato al registroIva " + registroIva.getId(), e);
            throw new RuntimeException(
                    "--> Errore nel caricare il Giornale associato al registroIva " + registroIva.getId(), e);
        }
    }

    @Override
    public GiornaleIva caricaGiornaleIvaPrecedente(GiornaleIva giornaleCorrente) {
        logger.debug("--> Enter caricaGiornalePrecedente");
        int anno = giornaleCorrente.getAnnoCompetenza().intValue();

        // lo chiamo meseDaUno per indicare che non è il mese del calendar che parte da 0, ma gennaio=1
        int meseDaUno = giornaleCorrente.getMese().intValue();

        int mesePrecDaUno = meseDaUno - 1;
        int annoPrec = anno;
        GiornaleIva giornalePrecedente = null;

        if (giornaleCorrente.getRegistroIva().getTipoRegistro() == TipoRegistro.RIEPILOGATIVO) {
            ETipoPeriodicita tipoPeriodicita = contabilitaSettingsManager.caricaContabilitaSettings()
                    .getTipoPeriodicita();

            DataPeriodicitaGiornaleIvaPrecedenteCalculator calculator = DataPeriodicitaGiornaleIvaPrecedenteCalculatorFactory
                    .create(tipoPeriodicita);
            Calendar calendar = calculator.calculate(anno, meseDaUno, meseDaUno == -1);
            mesePrecDaUno = calendar.get(Calendar.MONTH) + 1;
            annoPrec = calendar.get(Calendar.YEAR);
        }

        giornalePrecedente = caricaGiornaleByRegistro(giornaleCorrente.getRegistroIva(), annoPrec, mesePrecDaUno);
        logger.debug("--> Exit caricaGiornalePrecedente");
        return giornalePrecedente;
    }

    @Override
    public List<GiornaleIva> caricaGiornaliIva(Integer anno, Integer mese) {
        logger.debug("--> Enter caricaGiornali");

        AziendaLite aziendaLite = null;
        try {
            aziendaLite = anagraficaService.caricaAzienda();
        } catch (AnagraficaServiceException e) {
            logger.error("--> Errore durante il caricamento dell'azienda corrente.", e);
            throw new RuntimeException("Errore durante il caricamento dell'azienda corrente.", e);
        }

        // ContabilitaSettings contabilitaSettings = contabilitaSettingsManager.caricaContabilitaSettings();

        // la data deve essere composta dalla data inizio esercizio per giorno e
        // mese, mentre l'anno deve essere recuperato dall'anno competenza
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(aziendaLite.getDataInizioAttivita());
        // calendar.set(Calendar.YEAR, contabilitaSettings.getAnnoCompetenza());

        int annoInizioEsercizio = calendar.get(Calendar.YEAR);
        int meseInizioEsercizio = calendar.get(Calendar.MONTH);
        meseInizioEsercizio++;

        try {
            // carico i registri ordinati per tipo registro
            List<RegistroIva> registriIva = contabilitaAnagraficaManager.caricaRegistriIva("tipoRegistro", null);

            List<GiornaleIva> giornaliIvaPerRegistro = new ArrayList<GiornaleIva>();

            for (RegistroIva registroIva : registriIva) {
                // cerco il giornale associato al registro Iva
                GiornaleIva giornaleIvaRegistro = caricaGiornaleByRegistro(registroIva, anno, mese);
                int numeroGiornaliEsistenti = contaGiornali(registroIva);

                // se non esiste la stampa registro ne creo uno da associare
                if (giornaleIvaRegistro == null) {
                    giornaleIvaRegistro = creaGiornaleIva(registroIva, anno, mese);
                }

                if (giornaleIvaRegistro != null) {
                    // carico il giornale precedente che deve essere stampabile,se non ho nel mese
                    // precedente nessun giornale e neanche in nessun altro mese per
                    // lo stesso registro iva oppure se il giornale del mese
                    // precedente dello stesso registro iva e' valido e quindi stampato
                    GiornaleIva giornaleIvaPrecedente = caricaGiornaleIvaPrecedente(giornaleIvaRegistro);
                    // non sono stati mai stampati registri e quindi risulta stampabile solo il
                    // mese e anno uguale al mese e anno della data inizio esercizio azienda
                    if ((giornaleIvaPrecedente == null && numeroGiornaliEsistenti == 0 && mese == meseInizioEsercizio
                            && anno == annoInizioEsercizio) ||
                            // e' stato stampato un giornale quindi deve essere
                            // stampabile se il giornale precedente risulta stampato
                    (giornaleIvaPrecedente != null && giornaleIvaPrecedente.getStato() == GiornaleIva.STAMPATO) ||
                            // se non c'e' giornale precedente ma il giornale e'
                            // gia' stato creato (stampato)
                    (giornaleIvaPrecedente == null && giornaleIvaRegistro.getId() != null
                            && giornaleIvaRegistro.getMese() == 1)) {
                        giornaleIvaRegistro.setStampabile(true);
                    }

                    // aggiungo il giornale (trovato/creato) alla lista di risultati
                    giornaliIvaPerRegistro.add(giornaleIvaRegistro);
                }
            }

            logger.debug("--> Exit caricaGiornali");
            return giornaliIvaPerRegistro;
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dei giornali per anno: " + anno + " e mese: " + mese, e);
            throw new RuntimeException(
                    "--> Errore durante il caricamento dei giornali per anno: " + anno + " e mese: " + mese, e);
        }

    }

    @Override
    public List<GiornaleIva> caricaGiornaliIvaRiepilogativi(int anno) {
        // Carico il registro riepilogativo
        // Il registro riepilogativo e' creato in automatico sempre con numero=1
        RegistroIva registroIva = null;
        try {
            registroIva = contabilitaAnagraficaManager.caricaRegistroIva(1, TipoRegistro.RIEPILOGATIVO);
        } catch (ObjectNotFoundException e) {
            registroIva = new RegistroIva();
            registroIva.setNumero(new Integer(1));
            registroIva.setNome("Registro Riepilogativo");
            registroIva.setCodiceAzienda(getAzienda());
            registroIva.setDescrizione("registro iva riepilogativo generato automaticamente");
            registroIva.setTipoRegistro(TipoRegistro.RIEPILOGATIVO);
            registroIva = contabilitaAnagraficaManager.salvaRegistroIva(registroIva);
        }

        ContabilitaSettings contabilitaSettings = contabilitaSettingsManager.caricaContabilitaSettings();

        // Cerco i registri riepilogativi per 12 mesi, se la periodicita'
        // fosse di 4 trimestri al massimo non trovo i mesi>4
        List<GiornaleIva> giornaliIva = new ArrayList<GiornaleIva>();
        GiornaleIva giornaleIva;
        for (int i = 1; i <= 12; i++) {
            giornaleIva = caricaGiornaleByRegistro(registroIva, anno, i);
            // Se il giornale iva e' null,quindi non esiste, lo creo.
            if (giornaleIva == null) {
                giornaleIva = getGiornaleIva(registroIva, anno, i);

                if (contabilitaSettings.getTipoPeriodicita() == ETipoPeriodicita.TRIMESTRALE) {
                    giornaleIva.setPercTrimestraleValore(contabilitaSettings.getPercTrimestrale());
                }
            }
            giornaliIva.add(giornaleIva);
        }

        // aggiungo il riepilogativo annuale della liquidazione, come mese imposto -1 per identificarlo come annuale
        giornaleIva = caricaGiornaleByRegistro(registroIva, anno, -1);
        if (giornaleIva == null) {
            giornaleIva = getGiornaleIva(registroIva, anno, -1);
        }
        giornaliIva.add(giornaleIva);

        return giornaliIva;
    }

    /**
     * Carica i giornali successivi cronologicamente al giornale attuale scelto.
     *
     * @param giornaleAttuale
     *            il giornale da cui partire nella ricerca dei successivi.
     * @return List<GiornaleIva>
     */
    @SuppressWarnings("unchecked")
    private List<GiornaleIva> caricaGiornaliSuccessivi(GiornaleIva giornaleAttuale) {
        logger.debug("--> Enter caricaGiornaliSuccessivi");

        Query query = panjeaDAO.prepareNamedQuery("GiornaleIva.caricaGiornaliSuccessivi");
        query.setParameter("paramAnno", giornaleAttuale.getAnno());
        query.setParameter("paramMese", giornaleAttuale.getMese());
        query.setParameter("paramIdRegistroIva", giornaleAttuale.getRegistroIva().getId());
        query.setParameter("paramCodiceAzienda", getAzienda());

        List<GiornaleIva> listGiornali = null;
        try {
            listGiornali = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento dei giornali successivi.", e);
            throw new RuntimeException("--> Errore durante il caricamento dei giornali successivi.", e);
        }

        logger.debug("--> Exit caricaGiornaliSuccessivi");
        return listGiornali;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TotaliCodiceIvaDTO> caricaRigheIvaVolumeAffariPrivati(Date dataInizio, Date dataFine) {
        StringBuffer sb = new StringBuffer();
        sb.append("select new it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO(");
        sb.append("rigaIva.id, ");
        sb.append("rigaIva.ordinamento, ");
        sb.append("rigaIva.imponibile.importoInValutaAzienda, ");
        sb.append("rigaIva.imposta.importoInValutaAzienda, ");
        sb.append("codiceIva.id, ");
        sb.append("codiceIva.codice, ");
        sb.append("codiceIva.descrizioneRegistro, ");
        sb.append("codiceIva.percApplicazione, ");
        sb.append("codiceIva.percIndetraibilita, ");
        sb.append("areaContabile.id, ");
        sb.append("documento.dataDocumento, ");
        sb.append("documento.codice.codice, ");
        sb.append("areaContabile.codice.codice, ");
        sb.append("documento.totale, ");
        sb.append("entita.id, ");
        sb.append("entita.codice, ");
        sb.append("anagrafica.denominazione, ");
        sb.append("areaContabile.numeroPaginaGiornale, ");
        sb.append("tipoDocumento.notaCreditoEnable ");
        sb.append(") ");
        sb.append("from RigaIva rigaIva ");
        sb.append("inner join rigaIva.codiceIva codiceIva ");
        sb.append("inner join rigaIva.areaIva areaIva ");
        sb.append("inner join areaIva.areaContabile areaContabile ");
        sb.append("inner join areaContabile.documento documento ");
        sb.append("inner join areaContabile.tipoAreaContabile tipoAreaContabile ");
        sb.append("inner join areaIva.registroIva registroIva ");
        sb.append("inner join documento.entita entita ");
        sb.append("inner join documento.tipoDocumento tipoDocumento ");
        sb.append("inner join entita.anagrafica anagrafica ");
        sb.append("where codiceIva.indicatoreVolumeAffari=1 ");
        sb.append("and documento.codiceAzienda = :paramCodiceAzienda ");
        sb.append("and (registroIva.tipoRegistro=1 or registroIva.tipoRegistro=2) ");
        sb.append("and (areaContabile.dataRegistrazione >= :paramDaDataRegistrazione) ");
        sb.append("and (areaContabile.dataRegistrazione <= :paramADataRegistrazione) ");
        sb.append("and tipoAreaContabile.stampaRegistroIva=true ");
        sb.append("and codiceIva.liquidazionePeriodica=true ");
        sb.append(
                "and anagrafica.partiteIVA is null and documento.entita.anagrafica.sedeAnagrafica.datiGeografici.nazione.codice='IT' ");
        sb.append("and codiceIva.percApplicazione in (4,10,21,22) ");

        org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(sb.toString());

        query.setParameter("paramDaDataRegistrazione", dataInizio);
        query.setParameter("paramADataRegistrazione", dataFine);
        query.setParameter("paramCodiceAzienda", getAzienda());

        List<TotaliCodiceIvaDTO> righe = query.list();
        return righe;
    }

    @Override
    public TotaliCodiceIvaDTO caricaTotaliCodiceIvaByVolumeAffari(Date dataInizio, Date dataFine,
            IndicatoreVolumeAffari indicatoreVolumeAffari, Boolean conPartitaIva, boolean filtraAliquoteIva) {
        StringBuffer sb = new StringBuffer();
        // totali per indicatoreVolumeAffari=:paramIndicatoreVolumeAffari e per entita con partita iva oppure senza
        sb.append("select ");
        sb.append("sum(rigaIva.imponibile.importoInValutaAzienda) as imponibile, ");
        sb.append("sum(rigaIva.imposta.importoInValutaAzienda) as imposta ");
        sb.append("from RigaIva rigaIva ");
        sb.append("inner join rigaIva.codiceIva codiceIva ");
        sb.append("inner join rigaIva.areaIva areaIva ");
        sb.append("inner join areaIva.areaContabile areaContabile ");
        sb.append("inner join areaContabile.documento documento ");
        sb.append("inner join areaContabile.tipoAreaContabile tipoAreaContabile ");
        sb.append("inner join areaIva.registroIva registroIva ");
        sb.append("where codiceIva.indicatoreVolumeAffari=:paramIndicatoreVolumeAffari ");
        sb.append("and documento.codiceAzienda = :paramCodiceAzienda ");
        sb.append("and (registroIva.tipoRegistro=1 or registroIva.tipoRegistro=2) ");
        sb.append("and (areaContabile.dataRegistrazione >= :paramDaDataRegistrazione) ");
        sb.append("and (areaContabile.dataRegistrazione <= :paramADataRegistrazione) ");
        sb.append("and tipoAreaContabile.stampaRegistroIva=true ");
        sb.append("and codiceIva.liquidazionePeriodica=true ");

        if (filtraAliquoteIva) {
            sb.append("and codiceIva.percApplicazione in (4,10,21,22) ");
        }

        if (conPartitaIva != null) {
            if (conPartitaIva.booleanValue()) {
                sb.append("and documento.entita.anagrafica.partiteIVA is not null ");
            } else {
                sb.append(
                        "and documento.entita.anagrafica.partiteIVA is null  and documento.entita.anagrafica.sedeAnagrafica.datiGeografici.nazione.codice='IT'");
            }
        }

        org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(TotaliCodiceIvaDTO.class));

        query.setParameter("paramDaDataRegistrazione", dataInizio);
        query.setParameter("paramADataRegistrazione", dataFine);
        query.setParameter("paramIndicatoreVolumeAffari", indicatoreVolumeAffari);
        query.setParameter("paramCodiceAzienda", getAzienda());

        TotaliCodiceIvaDTO totaliCodiceIvaDTO = (TotaliCodiceIvaDTO) query.uniqueResult();
        if (totaliCodiceIvaDTO.getImponibile() == null) {
            totaliCodiceIvaDTO.setImponibile(BigDecimal.ZERO);
        }
        if (totaliCodiceIvaDTO.getImposta() == null) {
            totaliCodiceIvaDTO.setImposta(BigDecimal.ZERO);
        }
        return totaliCodiceIvaDTO;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TotaliCodiceIvaDTO> caricaTotaliCodiciIvaByRegistro(Date dataInizioPeriodo, Date dataFinePeriodo,
            RegistroIva registroIva, TipoCaratteristica caratteristica, GestioneIva gestioneIva) {
        logger.debug("--> Enter caricaTotaliCodiciIvaByRegistro");

        List<TotaliCodiceIvaDTO> mergedList = new ArrayList<TotaliCodiceIvaDTO>();
        try {
            StringBuffer buffer = new StringBuffer();
            StringBuffer bufferAlternativo = new StringBuffer();
            // la prima parte della select e' in comune con entrambe le query
            buffer.append("select sum(r.imponibile.importoInValutaAzienda) as imponibile,");
            buffer.append("sum(r.imposta.importoInValutaAzienda) as imposta,");
            buffer.append("r.codiceIva.id as idCodiceIva,");
            buffer.append("r.codiceIva.codice as codiceIva,");
            buffer.append("r.codiceIva.descrizioneRegistro as descrizioneRegistro,");
            buffer.append("r.codiceIva.percApplicazione as percApplicazione,");
            buffer.append("r.codiceIva.percIndetraibilita as percIndetraibilita,");
            buffer.append("coalesce(r.codiceIva.splitPayment,false) as splitPayment,");
            buffer.append(
                    "(CASE r.codiceIva.ivaSospesa WHEN true THEN false else true END) as consideraPerLiquidazione ");
            buffer.append(
                    "from RigaIva r where r.areaIva.areaContabile.documento.codiceAzienda = :paramCodiceAzienda ");
            buffer.append("and r.areaIva.registroIva.id = :paramIdRegistroIva ");
            buffer.append("and r.areaIva.areaContabile.statoAreaContabile in (0,1) ");
            buffer.append("and (r.areaIva.areaContabile.dataRegistrazione >= :paramDaDataRegistrazione) ");
            buffer.append("and (r.areaIva.areaContabile.dataRegistrazione <= :paramADataRegistrazione) ");
            buffer.append(
                    "and (r.areaIva.areaContabile.tipoAreaContabile.gestioneIva = :paramGestioneIva or 1 = :paramAllGestioneIva) ");
            buffer.append("and r.areaIva.areaContabile.tipoAreaContabile.stampaRegistroIva=true ");
            buffer.append(
                    "and (r.codiceIva.tipoCaratteristica = :paramCaratteristica or 1 = :paramAllCaratteristiche) ");
            buffer.append("and r.codiceIva.liquidazionePeriodica=true ");
            buffer.append("group by r.codiceIva.codice");

            bufferAlternativo.append("select sum(r.imponibile.importoInValutaAzienda) as imponibile,");
            bufferAlternativo.append("sum(r.imposta.importoInValutaAzienda) as imposta,");
            bufferAlternativo.append("r.codiceIvaCollegato.id as idCodiceIva,");
            bufferAlternativo.append("r.codiceIvaCollegato.codice as codiceIva,");
            bufferAlternativo.append("r.codiceIvaCollegato.descrizioneRegistro as descrizioneRegistro,");
            bufferAlternativo.append("r.codiceIvaCollegato.percApplicazione as percApplicazione,");
            bufferAlternativo.append("r.codiceIvaCollegato.percIndetraibilita as percIndetraibilita,");
            bufferAlternativo.append("coalesce(r.codiceIva.splitPayment,false) as splitPayment,");
            bufferAlternativo.append(
                    "(CASE r.codiceIvaCollegato.ivaSospesa WHEN true THEN false else true END) as consideraPerLiquidazione ");
            bufferAlternativo.append(
                    "from RigaIva r where r.areaIva.areaContabile.documento.codiceAzienda = :paramCodiceAzienda ");
            bufferAlternativo.append("and r.areaIva.registroIvaCollegato.id = :paramIdRegistroIva ");
            bufferAlternativo.append("and r.areaIva.areaContabile.statoAreaContabile in (0,1) ");
            bufferAlternativo.append("and (r.areaIva.areaContabile.dataRegistrazione >= :paramDaDataRegistrazione) ");
            bufferAlternativo.append("and (r.areaIva.areaContabile.dataRegistrazione <= :paramADataRegistrazione) ");
            bufferAlternativo.append(
                    "and (r.areaIva.areaContabile.tipoAreaContabile.gestioneIva = :paramGestioneIva or 1 = :paramAllGestioneIva) ");
            bufferAlternativo.append("and r.areaIva.areaContabile.tipoAreaContabile.stampaRegistroIva=true ");
            bufferAlternativo.append(
                    "and (r.codiceIva.tipoCaratteristica = :paramCaratteristica or 1 = :paramAllCaratteristiche) ");

            bufferAlternativo.append("and r.codiceIva.liquidazionePeriodica = true ");
            bufferAlternativo.append("group by r.codiceIva.codice");

            org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate())
                    .createQuery(buffer.toString());
            query.setResultTransformer(Transformers.aliasToBean(TotaliCodiceIvaDTO.class));

            org.hibernate.Query queryAlternativa = ((Session) panjeaDAO.getEntityManager().getDelegate())
                    .createQuery(bufferAlternativo.toString());
            queryAlternativa.setResultTransformer(Transformers.aliasToBean(TotaliCodiceIvaDTO.class));

            query.setParameter("paramIdRegistroIva", registroIva.getId());
            query.setParameter("paramDaDataRegistrazione", dataInizioPeriodo);
            query.setParameter("paramADataRegistrazione", dataFinePeriodo);
            query.setParameter("paramCodiceAzienda", getAzienda());

            queryAlternativa.setParameter("paramIdRegistroIva", registroIva.getId());
            queryAlternativa.setParameter("paramDaDataRegistrazione", dataInizioPeriodo);
            queryAlternativa.setParameter("paramADataRegistrazione", dataFinePeriodo);
            queryAlternativa.setParameter("paramCodiceAzienda", getAzienda());

            // HACK per evitare di comporre la query vedi RigaIva
            // and (r.areaIva.areaContabile.tipoAreaContabile.gestioneIva = :paramGestioneIva or 1 =
            // :paramAllGestioneIva)
            // se imposto una gestione iva prendo quella e vale la prima parte della condizione;
            // se la gestione iva passata e' null faccio fallire il primo test
            // mettendo gestione iva null, ma passare la or cioe' che 1=1
            // NOTA : la stessa cosa vale per caratteristica.
            if (gestioneIva != null) {
                query.setParameter("paramGestioneIva", gestioneIva);
                query.setParameter("paramAllGestioneIva", 0);

                queryAlternativa.setParameter("paramGestioneIva", gestioneIva);
                queryAlternativa.setParameter("paramAllGestioneIva", 0);
            } else {
                query.setParameter("paramGestioneIva", null);
                query.setParameter("paramAllGestioneIva", 1);

                queryAlternativa.setParameter("paramGestioneIva", null);
                queryAlternativa.setParameter("paramAllGestioneIva", 1);
            }

            if (caratteristica != null) {
                query.setParameter("paramCaratteristica", caratteristica);
                query.setParameter("paramAllCaratteristiche", 0);

                queryAlternativa.setParameter("paramCaratteristica", caratteristica);
                queryAlternativa.setParameter("paramAllCaratteristiche", 0);
            } else {
                query.setParameter("paramCaratteristica", TipoCaratteristica.MERCE);
                query.setParameter("paramAllCaratteristiche", 1);

                queryAlternativa.setParameter("paramCaratteristica", TipoCaratteristica.MERCE);
                queryAlternativa.setParameter("paramAllCaratteristiche", 1);
            }

            List<TotaliCodiceIvaDTO> listTotaliCodiceIvaDTO = query.list();
            // lista per intra e art17
            List<TotaliCodiceIvaDTO> listAlternativa = queryAlternativa.list();
            // aggiungo alla lista di origine i risultati trovati

            mergedList = mergeRiepilogoTotaliCodiceIva(listTotaliCodiceIvaDTO, listAlternativa);

            // ordino per codice di codice iva
            Collections.sort(mergedList, new TotaliCodiceIvaDTOComparator());

            logger.debug("--> TotaliCodiciIvaDTO trovati # " + mergedList.size());

            // se il registro e' di tipo acquisto devo calcolare gli imponibili
            // e le imposte detraibili e indetraibili. Il registro iva di TotaliCodiceIvaDTO non è valorizzato quindi lo
            // passo dall'esterno per verificare se il registro e' di tipo acquisto (regola di dominio che integro in
            // TotaliCodiceIvaDTO). Qui rifaccio il controllo per evitare di ciclare inutilmente nel caso in cui il
            // registro iva non sia acquisto.
            if (registroIva.getTipoRegistro().compareTo(TipoRegistro.ACQUISTO) == 0) {
                for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : mergedList) {
                    totaliCodiceIvaDTO.calcolaValoriDetraibili(registroIva);
                }
            }

        } catch (Exception e) {
            logger.error("--> Errore nel caricare i totaliCodiceIvaDTO del registro iva " + registroIva.getId(), e);
            throw new RuntimeException(
                    "--> Errore nel caricare i totaliCodiceIvaDTO del registro iva " + registroIva.getId(), e);
        }

        logger.debug("--> Exit caricaTotaliCodiciIvaByRegistro");
        return mergedList;
    }

    /**
     * Conta i giornali del registro iva passato.
     *
     * @param registroIva
     *            il registro iva di cui caricare i giornali collegati
     * @return il numero di giornali per quel registro iva
     */
    private int contaGiornali(RegistroIva registroIva) {
        logger.debug("--> Enter contaGiornali");

        Query query = panjeaDAO.prepareNamedQuery("GiornaleIva.contaGiornaliByRegistroIva");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("paramIdRegistroIva", registroIva.getId());

        Long totGiornali = new Long(0);
        try {
            totGiornali = (Long) panjeaDAO.getSingleResult(query);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il caricamento dei giornali.", e);
        }

        logger.debug("--> Exit contaGiornali");
        return totGiornali.intValue();
    }

    /**
     * Metodo che crea un nuovo giornale iva associato al registro iva.
     *
     * @param registroIva
     *            registro da associare al giornale iva
     * @param anno
     *            l'anno del giornale
     * @param mese
     *            il mese del giornale
     * @return GiornaleIva
     */
    private GiornaleIva creaGiornaleIva(RegistroIva registroIva, Integer anno, Integer mese) {

        GiornaleIva nuovoGiornaleIva = new GiornaleIva();

        nuovoGiornaleIva.setAnno(anno);
        nuovoGiornaleIva.setAnnoCompetenza(anno);
        nuovoGiornaleIva.setCodiceAzienda(getAzienda());
        nuovoGiornaleIva.setMese(mese);
        nuovoGiornaleIva.setImponibile(null);
        nuovoGiornaleIva.setImposta(null);
        nuovoGiornaleIva.setTotaleDocumento(null);
        nuovoGiornaleIva.setStampabile((mese == 1) ? true : false);
        nuovoGiornaleIva.setRegistroIva(registroIva);

        return nuovoGiornaleIva;
    }

    /**
     * Recupera il codice azienda dell'utente autenticato nel context.
     *
     * @return String codice azienda
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    /**
     * Prepara un giornaleIva con registro, anno e mese definiti.
     *
     * @param registroIva
     *            il registro iva da associare
     * @param anno
     *            l'anno
     * @param mese
     *            il mese
     * @return GiornaleIva
     */
    private GiornaleIva getGiornaleIva(RegistroIva registroIva, int anno, int mese) {
        GiornaleIva giornaleIva = new GiornaleIva();
        giornaleIva.setAnno(anno);
        giornaleIva.setAnnoCompetenza(anno);
        giornaleIva.setCodiceAzienda(getAzienda());
        giornaleIva.setMese(mese);
        giornaleIva.setRegistroIva(registroIva);
        giornaleIva.setPercTrimestraleImporto(BigDecimal.ZERO);
        giornaleIva.setPercTrimestraleValore(BigDecimal.ZERO);
        return giornaleIva;
    }

    @Override
    public void invalidaGiornaleIva(AreaContabile areaContabile) {
        invalidaGiornaleIva(areaContabile, null);
    }

    @Override
    public void invalidaGiornaleIva(AreaContabile areaContabile, Date dataRegistrazionePrecedente) {
        logger.debug("--> Enter invalidaGiornaleIva");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(areaContabile.getDataRegistrazione());

        // se c'e' una dataRegistrazionePrecedente ed essa e' precedente alla
        // dataregistrazione dell'areaContabile
        // allora prendo come base per invalidare i registri iva la
        // dataRegistrazionePrecedente
        if (dataRegistrazionePrecedente != null) {
            if (dataRegistrazionePrecedente.before(areaContabile.getDataRegistrazione())) {
                calendar.setTime(dataRegistrazionePrecedente);
            }
        }

        logger.debug("--> Data per l'invalidazione dei registri iva " + calendar.toString());
        // controllo se l'area iva � inattiva, che quindi non sar� presente
        // nessun registro iva da invalidare
        if (!areaContabile.getTipoAreaContabile().getTipoDocumento().isRigheIvaEnable()) {
            logger.debug(
                    "--> Nessun registro iva associato da invalidare, area iva non presente per questo tipo documento.");
            return;
        }

        RegistroIva registroIva = areaContabile.getTipoAreaContabile().getRegistroIva();

        // aggiungo 1 al mese perch� calendar.get(Calendar.MONTH) mi restituisce
        // il numero del mese -1
        GiornaleIva giornaleIva = caricaGiornaleByRegistro(registroIva, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1);

        // se non trovo nessun giornale non faccio niente
        if (giornaleIva == null) {
            return;
        }
        logger.debug("--> Caricato il registro iva " + giornaleIva.getId());
        // se e' gia' stato invalidato non faccio nulla
        if (giornaleIva.getStato() != GiornaleIva.NON_VALIDO) {
            giornaleIva.setStato(GiornaleIva.NON_VALIDO);
            giornaleIva = salvaGiornaleIva(giornaleIva);

            List<GiornaleIva> listGiornaliSuccessivi = caricaGiornaliSuccessivi(giornaleIva);
            logger.debug("--> trovati " + listGiornaliSuccessivi + " registri iva successivi da invalidare");
            for (GiornaleIva giornaleSuccessivo : listGiornaliSuccessivi) {
                giornaleSuccessivo.setStato(GiornaleIva.NON_VALIDO);
                salvaGiornaleIva(giornaleSuccessivo);
            }

            // spedisco messaggio di invalidazione del registro iva
            panjeaMessage.send(giornaleIva, "registroIvaInvalidato");
        }
        logger.debug("--> Exit invalidaGiornaleIva");
    }

    /**
     * Esegue il merge tra due liste di totali codice iva dto nel caso in cui queste sono raggruppate per codice del
     * codice iva, in modo da avere un solo elemento con quel codice.
     *
     * @param riepilogoNormale
     *            elementi della lista 1 da aggiungere
     * @param riepilogoAlternativo
     *            elementi della lista 2 da aggiungere
     * @return List<TotaliCodiceIvaDTO> che e' la lista ordinata delle righe delle due liste di origine
     */
    private List<TotaliCodiceIvaDTO> mergeRiepilogoTotaliCodiceIva(List<TotaliCodiceIvaDTO> riepilogoNormale,
            List<TotaliCodiceIvaDTO> riepilogoAlternativo) {
        logger.debug("--> Enter mergeRiepilogoTotaliCodiceIva #" + riepilogoNormale.size() + " #"
                + riepilogoAlternativo.size());

        // mappa per unire le due liste in modo da non avere codici iva doppi
        Map<String, TotaliCodiceIvaDTO> mergedMap = new HashMap<String, TotaliCodiceIvaDTO>();
        // riempio la map con le righe riepilogo normali
        for (TotaliCodiceIvaDTO rigaNormale : riepilogoNormale) {
            mergedMap.put(rigaNormale.getCodiceIva(), rigaNormale);
        }
        // scorro le righe alternative e se trovo gia' la riga di quel codice
        // iva aggiungo imponibile e imposta
        // altrimenti inserisco nella mappa il nuovo elemento
        for (TotaliCodiceIvaDTO rigaAlternativa : riepilogoAlternativo) {
            TotaliCodiceIvaDTO rigaEsistente = mergedMap.get(rigaAlternativa.getCodiceIva());
            // se esiste gia' il codice iva devo aggiungere totale imponibile e
            // imposta
            if (rigaEsistente != null) {
                rigaEsistente.setImponibile(rigaEsistente.getImponibile().add(rigaAlternativa.getImponibile()));
                rigaEsistente.setImposta(rigaEsistente.getImposta().add(rigaAlternativa.getImposta()));
            } else {
                // altrimenti aggiungo la riga
                mergedMap.put(rigaAlternativa.getCodiceIva(), rigaAlternativa);
            }
        }
        // trasformo la mappa in una lista per restituirla
        List<TotaliCodiceIvaDTO> mergedList = new ArrayList<TotaliCodiceIvaDTO>();
        mergedList.addAll(mergedMap.values());
        // riordino la collecion per presentare i risultati ordinati per codice
        // di codice iva
        Collections.sort(mergedList, new TotaliCodiceIvaDTOComparator());
        logger.debug("--> Exit mergeRiepilogoTotaliCodiceIva");
        return mergedList;
    }

    /**
     * Crea la query per il caricamento delle righe iva in condizioni alternative di gestione iva (INTRA o ART.17)
     * considerando il registro iva collegato su tipoAreaContabile e per le righe il codice iva collegato e
     * imponibileCollegato.
     *
     * @param parametriRicerca
     *            parametri per eseguire la ricerca
     * @return Query
     */
    private Query prepareHqlQueryForGestioneAlternativa(ParametriRicercaMovimentiContabili parametriRicerca) {
        logger.debug("--> Enter prepareHqlQueryForGestioneAlternativa");
        Map<String, Object> valueParametri = new TreeMap<String, Object>();

        StringBuffer queryHQL = new StringBuffer(" select new it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO( ");
        queryHQL.append(
                " r.id,r.ordinamento,r.imponibile.importoInValutaAzienda, r.impostaCollegata.importoInValutaAzienda, r.codiceIvaCollegato.id, r.codiceIvaCollegato.codice, r.codiceIvaCollegato.descrizioneRegistro, r.codiceIvaCollegato.percApplicazione, r.codiceIvaCollegato.percIndetraibilita, ");
        queryHQL.append(" ac.id, doc.dataDocumento,doc.codice.codice, ac.codiceCollegato.codice,doc.totale, ");
        queryHQL.append(" ent.id,ent.codice,anag.denominazione, ");
        queryHQL.append(" ac.numeroPaginaGiornale ) ");
        queryHQL.append(" from RigaIva r inner join r.areaIva as ai inner join  ai.areaContabile as ac ");
        queryHQL.append(
                " inner join ac.documento as doc left join doc.entita as ent left join ent.anagrafica as anag ");

        StringBuffer whereHQL = new StringBuffer(" where doc.codiceAzienda = :paramCodiceAzienda  ");

        valueParametri.put("paramCodiceAzienda", getAzienda());
        // filtro registro iva
        if (parametriRicerca.getRegistroIva() != null) {
            whereHQL.append(" and ai.registroIvaCollegato.id = :paramIdRegistroIva ");
            valueParametri.put("paramIdRegistroIva", parametriRicerca.getRegistroIva().getId());
        }
        // filtro anno
        if (!parametriRicerca.getAnnoCompetenza().equals("")) {
            whereHQL.append(" and ac.annoMovimento = :paramAnnoMovimento ");
            valueParametri.put("paramAnnoMovimento", new Integer(parametriRicerca.getAnnoCompetenza()));
        }
        // filtro data registrazione
        if (parametriRicerca.getDataRegistrazione().getDataIniziale() != null) {
            whereHQL.append(" and (ac.dataRegistrazione >= :paramDaDataRegistrazione) ");
            valueParametri.put("paramDaDataRegistrazione",
                    PanjeaEJBUtil.getDateTimeToZero(parametriRicerca.getDataRegistrazione().getDataIniziale()));
        }
        if (parametriRicerca.getDataRegistrazione().getDataFinale() != null) {
            whereHQL.append(" and (ac.dataRegistrazione <= :paramADataRegistrazione) ");
            valueParametri.put("paramADataRegistrazione",
                    PanjeaEJBUtil.getDateTimeToZero(parametriRicerca.getDataRegistrazione().getDataFinale()));
        }
        // filtro Stato documento
        if ((parametriRicerca.getStatiAreaContabile() != null)
                && (parametriRicerca.getStatiAreaContabile().size() != 0)) {
            whereHQL.append(" and ac.statoAreaContabile in (:paramStatoDocumento) ");
            valueParametri.put("paramStatoDocumento", parametriRicerca.getStatiAreaContabile());
        }
        // filtro stampato (valore parametri = false)
        if (parametriRicerca.getEscludiMovimentiStampati().booleanValue()) {
            whereHQL.append(" and ac.numeroPaginaGiornale = 0 ");
        }
        // filtra tipiArea abilitati alla stampa registro iva
        if (parametriRicerca.isFiltraAbilitatiStampaRegistroIva()) {
            whereHQL.append(" and ac.tipoAreaContabile.stampaRegistroIva = true ");
        }
        // ordinamento righe iva
        whereHQL.append(" order by ac.codice.codiceOrder, r.ordinamento");

        Query query = panjeaDAO.prepareQuery(queryHQL.toString() + whereHQL.toString());
        Set<String> set = valueParametri.keySet();
        for (String key : set) {
            Object value = valueParametri.get(key);
            if (value instanceof Date) {
                Date valueDate = (Date) value;
                query.setParameter(key, valueDate, TemporalType.DATE);
            } else {
                query.setParameter(key, valueParametri.get(key));
            }
        }
        logger.debug("--> Exit prepareHqlQueryForGestioneAlternativa");
        return query;
    }

    /**
     * Crea la query per il caricamento delle righe iva in condizioni normali di gestione iva.
     *
     * @param parametriRicerca
     *            parametri per eseguire la ricerca
     * @return Query
     */
    private Query prepareHqlQueryForGestioneNormale(ParametriRicercaMovimentiContabili parametriRicerca) {
        logger.debug("--> Enter prepareHqlQueryForGestioneNormale");
        Map<String, Object> valueParametri = new TreeMap<String, Object>();

        StringBuffer queryHQL = new StringBuffer(" select new it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO( ");
        queryHQL.append(
                " r.id,r.ordinamento,r.imponibile.importoInValutaAzienda, r.imposta.importoInValutaAzienda, r.codiceIva.id, r.codiceIva.codice, r.codiceIva.descrizioneRegistro, r.codiceIva.percApplicazione, r.codiceIva.percIndetraibilita, ");
        queryHQL.append(" ac.id, doc.dataDocumento, doc.codice.codice, ac.codice.codice,doc.totale , ");
        queryHQL.append(" ent.id,ent.codice,anag.denominazione, ");
        queryHQL.append(" ac.numeroPaginaGiornale, tipoDoc.notaCreditoEnable ) ");
        queryHQL.append(" from RigaIva r inner join r.areaIva as ai inner join ai.areaContabile as ac ");
        queryHQL.append(
                " inner join ac.documento as doc inner join doc.tipoDocumento tipoDoc left join doc.entita as ent left join ent.anagrafica as anag ");

        StringBuffer whereHQL = new StringBuffer(" where doc.codiceAzienda = :paramCodiceAzienda  ");

        valueParametri.put("paramCodiceAzienda", getAzienda());
        // filtro registro iva
        if (parametriRicerca.getRegistroIva() != null) {
            whereHQL.append(" and ai.registroIva.id = :paramIdRegistroIva ");
            valueParametri.put("paramIdRegistroIva", parametriRicerca.getRegistroIva().getId());
        }
        // filtro anno
        if (!parametriRicerca.getAnnoCompetenza().equals("")) {
            whereHQL.append(" and ac.annoMovimento = :paramAnnoMovimento ");
            valueParametri.put("paramAnnoMovimento", new Integer(parametriRicerca.getAnnoCompetenza()));
        }
        // filtro data registrazione
        if (parametriRicerca.getDataRegistrazione().getDataIniziale() != null) {
            whereHQL.append(" and (ac.dataRegistrazione >= :paramDaDataRegistrazione) ");
            valueParametri.put("paramDaDataRegistrazione",
                    PanjeaEJBUtil.getDateTimeToZero(parametriRicerca.getDataRegistrazione().getDataIniziale()));
        }
        if (parametriRicerca.getDataRegistrazione().getDataFinale() != null) {
            whereHQL.append(" and (ac.dataRegistrazione <= :paramADataRegistrazione) ");
            valueParametri.put("paramADataRegistrazione",
                    PanjeaEJBUtil.getDateTimeToZero(parametriRicerca.getDataRegistrazione().getDataFinale()));
        }
        // filtro Stato documento
        if ((parametriRicerca.getStatiAreaContabile() != null)
                && (parametriRicerca.getStatiAreaContabile().size() != 0)) {
            whereHQL.append(" and ac.statoAreaContabile in (:paramStatoDocumento) ");
            valueParametri.put("paramStatoDocumento", parametriRicerca.getStatiAreaContabile());
        }
        // filtro stampato (valore parametri = false)
        if (parametriRicerca.getEscludiMovimentiStampati().booleanValue()) {
            whereHQL.append(" and ac.numeroPaginaGiornale = 0 ");
        }
        // filtra tipiArea abilitati alla stampa registro iva
        if (parametriRicerca.isFiltraAbilitatiStampaRegistroIva()) {
            whereHQL.append(" and ac.tipoAreaContabile.stampaRegistroIva = true ");
        }
        // ordinamento righe iva
        whereHQL.append(" order by ac.codice.codiceOrder, r.ordinamento");

        Query query = panjeaDAO.prepareQuery(queryHQL.toString() + whereHQL.toString());
        Set<String> set = valueParametri.keySet();
        for (String key : set) {
            Object value = valueParametri.get(key);
            if (value instanceof Date) {
                Date valueDate = (Date) value;
                query.setParameter(key, valueDate, TemporalType.DATE);
            } else {
                query.setParameter(key, valueParametri.get(key));
            }
        }
        logger.debug("--> Exit prepareHqlQueryForGestioneNormale");
        return query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TotaliCodiceIvaDTO> ricercaRigheIva(ParametriRicercaMovimentiContabili parametriRicerca) {
        logger.debug("--> Enter ricercaRigheIva");

        List<TotaliCodiceIvaDTO> righeIvaGestioneNormale = new ArrayList<TotaliCodiceIvaDTO>();
        List<TotaliCodiceIvaDTO> righeIvaGestioneAlternativa = new ArrayList<TotaliCodiceIvaDTO>();
        try {
            righeIvaGestioneNormale = panjeaDAO.getResultList(prepareHqlQueryForGestioneNormale(parametriRicerca));
            righeIvaGestioneAlternativa = panjeaDAO
                    .getResultList(prepareHqlQueryForGestioneAlternativa(parametriRicerca));
            righeIvaGestioneNormale.addAll(righeIvaGestioneAlternativa);

            // ordina la lista risultante
            Collections.sort(righeIvaGestioneNormale, new TotaliCodiceIvaDTOAreaComparator());
        } catch (DAOException e) {
            logger.error("--> errore, impossibile eseguire l'interrogazione di ricercaRigheIva ", e);
            throw new RuntimeException("Impossibile eseguire l'interrogazione di ricercaRigheIva ", e);
        }
        logger.debug("--> Exit ricercaRigheIva #" + righeIvaGestioneNormale != null ? righeIvaGestioneNormale.size()
                : "null");
        return righeIvaGestioneNormale;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TotaliCodiceIvaDTO> ricercaRigheRiepilogoCodiciIva(
            ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili) {
        try {
            logger.debug("--> Enter ricercaRigheRiepilogoCodiciIva");
            StringBuffer sf1 = new StringBuffer();
            sf1.append("select sum(r.imponibile.importoInValutaAzienda) as imponibile,");
            sf1.append("sum(r.imposta.importoInValutaAzienda) as imposta,");
            sf1.append("r.codiceIva.id as idCodiceIva,");
            sf1.append("r.codiceIva.codice as codiceIva,");
            sf1.append("r.codiceIva.descrizioneRegistro as descrizioneRegistro,");
            sf1.append("r.codiceIva.percApplicazione as percApplicazione,");
            sf1.append("r.codiceIva.percIndetraibilita as percIndetraibilita,");
            sf1.append("(CASE r.codiceIva.ivaSospesa WHEN true THEN false else true END) as consideraPerLiquidazione ");
            sf1.append(" from RigaIva r where r.areaIva.areaContabile.documento.codiceAzienda = :paramCodiceAzienda ");
            sf1.append(" and r.areaIva.registroIva.id = :paramIdRegistroIva ");
            sf1.append(" and r.areaIva.areaContabile.statoAreaContabile in (0,1) ");
            sf1.append(" and (r.areaIva.areaContabile.dataRegistrazione >= :paramDaDataRegistrazione) ");
            sf1.append(" and (r.areaIva.areaContabile.dataRegistrazione <= :paramADataRegistrazione) ");
            if (parametriRicercaMovimentiContabili.isFiltraAbilitatiStampaRegistroIva()) {
                sf1.append(" and (r.areaIva.areaContabile.tipoAreaContabile.stampaRegistroIva=true) ");
            }
            sf1.append(" group by r.codiceIva.codice ");
            org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate())
                    .createQuery(sf1.toString());
            query.setResultTransformer(Transformers.aliasToBean(TotaliCodiceIvaDTO.class));

            StringBuffer sf2 = new StringBuffer();
            sf2.append("select sum(r.imponibile.importoInValutaAzienda) as imponibile,");
            sf2.append("sum(r.imposta.importoInValutaAzienda) as imposta,");
            sf2.append("r.codiceIva.id as idCodiceIva,");
            sf2.append("r.codiceIva.codice as codiceIva,");
            sf2.append("r.codiceIva.descrizioneRegistro as descrizioneRegistro,");
            sf2.append("r.codiceIva.percApplicazione as percApplicazione,");
            sf2.append("r.codiceIva.percIndetraibilita as percIndetraibilita,");
            sf2.append("(CASE r.codiceIva.ivaSospesa WHEN true THEN false else true END) as consideraPerLiquidazione ");
            sf2.append(" from RigaIva r where r.areaIva.areaContabile.documento.codiceAzienda = :paramCodiceAzienda ");
            sf2.append(" and r.areaIva.registroIvaCollegato.id = :paramIdRegistroIva ");
            sf2.append(" and r.areaIva.areaContabile.statoAreaContabile in (0,1) ");
            sf2.append(" and (r.areaIva.areaContabile.dataRegistrazione >= :paramDaDataRegistrazione) ");
            sf2.append(" and (r.areaIva.areaContabile.dataRegistrazione <= :paramADataRegistrazione) ");
            if (parametriRicercaMovimentiContabili.isFiltraAbilitatiStampaRegistroIva()) {
                sf1.append(" and (r.areaIva.areaContabile.tipoAreaContabile.stampaRegistroIva=true) ");
            }
            sf2.append(" group by r.codiceIva.codice ");
            org.hibernate.Query queryAlternativa = ((Session) panjeaDAO.getEntityManager().getDelegate())
                    .createQuery(sf2.toString());
            queryAlternativa.setResultTransformer(Transformers.aliasToBean(TotaliCodiceIvaDTO.class));

            query.setParameter("paramIdRegistroIva", parametriRicercaMovimentiContabili.getRegistroIva().getId());
            query.setParameter("paramDaDataRegistrazione",
                    parametriRicercaMovimentiContabili.getDataRegistrazione().getDataIniziale());
            query.setParameter("paramADataRegistrazione",
                    parametriRicercaMovimentiContabili.getDataRegistrazione().getDataFinale());
            query.setParameter("paramCodiceAzienda", getAzienda());

            queryAlternativa.setParameter("paramIdRegistroIva",
                    parametriRicercaMovimentiContabili.getRegistroIva().getId());
            queryAlternativa.setParameter("paramDaDataRegistrazione",
                    parametriRicercaMovimentiContabili.getDataRegistrazione().getDataIniziale());
            queryAlternativa.setParameter("paramADataRegistrazione",
                    parametriRicercaMovimentiContabili.getDataRegistrazione().getDataFinale());
            queryAlternativa.setParameter("paramCodiceAzienda", getAzienda());

            List<TotaliCodiceIvaDTO> righeRiepilogoCodiciIva = query.list();
            List<TotaliCodiceIvaDTO> righeRiepilogoCodiciIvaAlternative = queryAlternativa.list();

            List<TotaliCodiceIvaDTO> mergedList = mergeRiepilogoTotaliCodiceIva(righeRiepilogoCodiciIva,
                    righeRiepilogoCodiciIvaAlternative);
            logger.debug("--> Exit ricercaRigheRiepilogoCodiciIva trovate # " + mergedList.size()
                    + " righe del riepilogo codici iva");
            return mergedList;
        } catch (Exception e) {
            logger.error("--> Errore nel caricare le righe riepilogo codici iva del registro iva "
                    + parametriRicercaMovimentiContabili.getRegistroIva().getId(), e);
            throw new RuntimeException("--> Errore nel caricare le righe riepilogo codici iva del registro iva "
                    + parametriRicercaMovimentiContabili.getRegistroIva().getId(), e);
        }
    }

    @Override
    public GiornaleIva salvaGiornaleIva(GiornaleIva giornaleIva) {
        logger.debug("--> Enter salvaGiornale");

        // controllo se esiste già un giornaleIva con anno/mese/registroIvaId
        GiornaleIva giornaleEsistente = caricaGiornaleByRegistro(giornaleIva.getRegistroIva(), giornaleIva.getAnno(),
                giornaleIva.getMese());

        // se esiste e voglio salvare un nuovo giornale associo id e version al nuovo giornale in modo da aggiornare il
        // record esistente e non salvarne uno nuovo che causerebbe errore (NonUniqueResultException)
        if (giornaleEsistente != null && giornaleEsistente.getId() != null && giornaleIva.getId() == null) {
            giornaleIva.setId(giornaleEsistente.getId());
            giornaleIva.setVersion(giornaleEsistente.getVersion());
        }

        GiornaleIva giornaleIvaSalvato = null;
        try {
            giornaleIvaSalvato = panjeaDAO.save(giornaleIva);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio del giornale", e);
            throw new RuntimeException("--> Errore durante il salvataggio del giornale", e);
        }

        logger.debug("--> Exit salvaGiornale");
        return giornaleIvaSalvato;
    }
}
