package it.eurotn.panjea.corrispettivi.manager.calendariocorrispettivi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.corrispettivi.domain.CalendarioCorrispettivo;
import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.corrispettivi.domain.GiornoCorrispettivo;
import it.eurotn.panjea.corrispettivi.domain.TotaliCodiceIvaDTO;
import it.eurotn.panjea.corrispettivi.manager.calendariocorrispettivi.interfaces.CalendarioCorrispettiviManager;
import it.eurotn.panjea.corrispettivi.manager.corrispettivi.interfaces.CorrispettiviManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.CalendarioCorrispettiviManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CalendarioCorrispettiviManager")
public class CalendarioCorrispettiviManagerBean implements CalendarioCorrispettiviManager {

    private static final Logger LOGGER = Logger.getLogger(CalendarioCorrispettiviManagerBean.class);

    @Resource
    private SessionContext sessionContext;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private CorrispettiviManager corrispettiviManager;

    @EJB
    private AreaContabileManager areaContabileManager;

    @Override
    public void aggiornaCodiciIvaCalendarioCorrispettivi(int anno, int mese, TipoDocumento tipoDocumento) {
        LOGGER.debug("--> Enter aggiornaCodiciIvaCalendarioCorrispettivi");

        List<Corrispettivo> corrispettiviDaAggiornare = new ArrayList<Corrispettivo>();

        // cancello tutti i corrispettivi che non hanno righe corrispettivo
        // inserite
        CalendarioCorrispettivo calendarioCorrispettivo = caricaCalendarioCorrispettivo(anno, mese, tipoDocumento);

        for (GiornoCorrispettivo giornoCorrispettivo : calendarioCorrispettivo.getGiorniCorrispettivo()) {

            if (giornoCorrispettivo.getCorrispettivo().getTotaleRighe().compareTo(BigDecimal.ZERO) == 0) {
                try {
                    panjeaDAO.delete(giornoCorrispettivo.getCorrispettivo());
                } catch (Exception e) {
                    LOGGER.error("-->Errore durante la cancellazione del corrispettivo.", e);
                    throw new RuntimeException("Errore durante la cancellazione del corrispettivo.", e);
                }
            } else {
                corrispettiviDaAggiornare.add(giornoCorrispettivo.getCorrispettivo());
            }
        }

        // aggiorno le righe corrispettivo rimanenti con i nuovi codici iva
        for (Corrispettivo corrispettivo : corrispettiviDaAggiornare) {
            corrispettiviManager.aggiornaCodiceIva(corrispettivo, tipoDocumento);
        }
    }

    @Override
    public CalendarioCorrispettivo caricaCalendarioCorrispettivo(int anno, int mese, TipoDocumento tipoDocumento) {
        LOGGER.debug("--> Enter caricaCalendarioCorrispettivo");

        CalendarioCorrispettivo calendarioCorrispettivo = new CalendarioCorrispettivo();
        calendarioCorrispettivo.setAnno(anno);
        calendarioCorrispettivo.setMese(mese);
        calendarioCorrispettivo.setTipoDocumento(tipoDocumento);
        calendarioCorrispettivo.setGiorniCorrispettivo(caricaGiorniCorrispettivo(anno, mese, tipoDocumento));

        LOGGER.debug("--> Exit caricaCalendarioCorrispettivo");
        return calendarioCorrispettivo;
    }

    /**
     * Carica documenti.
     *
     * @param anno
     *            anno
     * @param mese
     *            mese
     * @param tipoDocumento
     *            tipoDocumento
     * @return Map<Integer, List<Documento>>
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, Collection<Documento>> caricaDocumenti(int anno, int mese, TipoDocumento tipoDocumento) {
        LOGGER.debug("--> Enter caricaDocumenti");

        StringBuilder sb = new StringBuilder(1000);
        sb.append("select doc.id as id, ");
        sb.append("doc.version as version, ");
        sb.append("doc.dataDocumento as dataDocumento, ");
        sb.append("doc.totale as totale, ");
        sb.append("doc.codice as codice, ");
        sb.append("tipoDoc.id as tipoDocumento$id, ");
        sb.append("tipoDoc.version as tipoDocumento$version, ");
        sb.append("tipoDoc.codice as tipoDocumento$codice, ");
        sb.append("tipoDoc.descrizione as tipoDocumento$descrizione ");
        sb.append("from Documento doc ");
        sb.append("inner join doc.tipoDocumento tipoDoc ");
        sb.append("where doc.dataDocumento >= :paramDataIniziale ");
        sb.append("and doc.dataDocumento <= :paramDataFinale ");
        if (tipoDocumento != null && !tipoDocumento.isNew()) {
            sb.append("and tipoDoc = :paramTipoDoc ");
        }

        Query query = panjeaDAO.prepareQuery(sb.toString(), Documento.class, null);

        if (tipoDocumento != null && !tipoDocumento.isNew()) {
            query.setParameter("paramTipoDoc", tipoDocumento);
        }

        Calendar calendarParam = Calendar.getInstance();
        calendarParam.set(Calendar.YEAR, anno);
        calendarParam.set(Calendar.MONTH, mese);
        calendarParam.set(Calendar.DAY_OF_MONTH, 1);
        query.setParameter("paramDataIniziale", calendarParam.getTime());

        int lastDay = calendarParam.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendarParam.set(Calendar.DAY_OF_MONTH, lastDay);
        query.setParameter("paramDataFinale", calendarParam.getTime());

        List<Documento> documenti = new ArrayList<>();
        try {
            documenti = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dei documenti", e);
            throw new GenericException("errore durante il caricamento dei documenti", e);
        }

        MultiValuedMap<Integer, Documento> multiValueMap = MultiMapUtils.newListValuedHashMap();

        for (Documento documento : documenti) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(documento.getDataDocumento());
            multiValueMap.put(calendar.get(Calendar.DAY_OF_MONTH), documento);
        }

        LOGGER.debug("--> Exit caricaDocumenti");
        return multiValueMap.asMap();
    }

    /**
     * Carica tutti i giorni corrispettivo per il mese dell'anno passato come parametro.
     *
     * @param anno
     *            Anno di riferimento
     * @param mese
     *            Mese di riferimento
     * @param tipoDocumento
     *            tipoDocumento
     * @return <code>List</code> di giorni caricati
     * @throws CorrispettivoPresenteException
     *             CorrispettivoPresenteException
     */
    private List<GiornoCorrispettivo> caricaGiorniCorrispettivo(int anno, int mese, TipoDocumento tipoDocumento) {
        LOGGER.debug("--> Enter caricaGiorniCorrispettivo");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, anno);
        cal.set(Calendar.MONTH, mese);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // trovo quanti giorni ha il mese
        int giorniMese = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        LOGGER.debug("--> Il mese " + mese + " ha " + giorniMese + " giorni.");

        // carico tutti i documenti del mese
        Map<Integer, Collection<Documento>> mapDocumenti = caricaDocumenti(anno, mese, tipoDocumento);

        List<GiornoCorrispettivo> listGiornoCorrispettivo = new ArrayList<GiornoCorrispettivo>();

        // creo i giorni corrispettivo per il mese
        for (int i = 1; i <= giorniMese; i++) {

            Integer giornoMese = new Integer(i);
            cal.set(Calendar.DAY_OF_MONTH, giornoMese);
            int giornoDellaSettimana = cal.get(Calendar.DAY_OF_WEEK);

            GiornoCorrispettivo giornoCorrispettivo = new GiornoCorrispettivo();
            giornoCorrispettivo.setNumero(giornoMese);
            // se il giorno della settimana è 7 (sabato) o 1 (domenica)
            // setto il giorno come festivo
            if (giornoDellaSettimana == 7 || giornoDellaSettimana == 1) {
                giornoCorrispettivo.setFeriale(false);
            } else {
                giornoCorrispettivo.setFeriale(true);
            }

            // aggiungo le aree contabili al giorno corrispettivo
            giornoCorrispettivo.setDocumenti(new ArrayList<Documento>());
            if (mapDocumenti.containsKey(giornoMese)) {
                giornoCorrispettivo.getDocumenti().addAll(mapDocumenti.get(giornoMese));
            }

            // aggiungo il corrispettivo
            giornoCorrispettivo
                    .setCorrispettivo(corrispettiviManager.caricaCorrispettivo(cal.getTime(), tipoDocumento, true));

            listGiornoCorrispettivo.add(giornoCorrispettivo);

        }

        LOGGER.debug("--> Exit caricaGiorniCorrispettivo");
        return listGiornoCorrispettivo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TotaliCodiceIvaDTO> caricaTotaliCalendarioCorrispettivi(
            CalendarioCorrispettivo calendarioCorrispettivo) {
        LOGGER.debug("--> Enter caricaTotaliCalendarioCorrispettivi");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendarioCorrispettivo.getAnno());
        calendar.set(Calendar.MONTH, calendarioCorrispettivo.getMese());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date dataIni = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date dataFin = calendar.getTime();

        // carico i totali dei corrispettivi del mese
        Query query = panjeaDAO.prepareNamedQuery("Corrispettivo.caricaCorrispettiviGroupByCodiceIva");
        query.setParameter("paramDataIni", dataIni);
        query.setParameter("paramDataFin", dataFin);
        query.setParameter("paramCodiceAzienda", getCodiceAzienda());
        query.setParameter("paramTipoDocumentoId", calendarioCorrispettivo.getTipoDocumento().getId());

        List<Object[]> result = new ArrayList<Object[]>();
        try {
            result = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento dei totali dei codici iva per il calendairo corrispettivi.",
                    e);
            throw new GenericException(
                    "Errore durante il caricamento dei totali dei codici iva per il calendairo corrispettivi.", e);
        }

        List<TotaliCodiceIvaDTO> listTotaliCodiceIvaDTO = new ArrayList<TotaliCodiceIvaDTO>();

        for (Object[] objectResult : result) {
            BigDecimal totale = (BigDecimal) objectResult[0];
            CodiceIva codiceIva = (CodiceIva) objectResult[1];

            if (totale != null) {
                TotaliCodiceIvaDTO totaliCodiceIvaDTO = new TotaliCodiceIvaDTO(codiceIva.getCodice(),
                        codiceIva.getDescrizioneInterna(), totale);
                listTotaliCodiceIvaDTO.add(totaliCodiceIvaDTO);
            }
        }

        // carico i totali dei documenti del mese
        query = panjeaDAO.prepareNamedQuery("RigaIva.caricaTotaliForCalendarioCorrispettivo");
        query.setParameter("paramDataIni", dataIni);
        query.setParameter("paramDataFin", dataFin);
        query.setParameter("paramCodiceAzienda", getCodiceAzienda());
        query.setParameter("paramTipoDocumentoId", calendarioCorrispettivo.getTipoDocumento().getId());

        List<TotaliCodiceIvaDTO> listTotaliRigheIva = new ArrayList<TotaliCodiceIvaDTO>();
        try {
            listTotaliRigheIva = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento dei totali codici iva delle righe iva.", e);
            throw new RuntimeException("Errore durante il caricamento dei totali codici iva delle righe iva.", e);
        }

        for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : listTotaliRigheIva) {
            if (totaliCodiceIvaDTO.getTotale() != null) {
                if (listTotaliCodiceIvaDTO.contains(totaliCodiceIvaDTO)) {
                    int index = listTotaliCodiceIvaDTO.indexOf(totaliCodiceIvaDTO);

                    TotaliCodiceIvaDTO totaliCodiceIvaDTOOld = listTotaliCodiceIvaDTO.get(index);
                    totaliCodiceIvaDTOOld
                            .setTotale(totaliCodiceIvaDTOOld.getTotale().add(totaliCodiceIvaDTO.getTotale()));

                } else {
                    listTotaliCodiceIvaDTO.add(totaliCodiceIvaDTO);
                }
            }
        }

        LOGGER.debug("--> Exit caricaTotaliCalendarioCorrispettivi");
        return listTotaliCodiceIvaDTO;
    }

    /**
     * @return codiceAzienda
     */
    protected String getCodiceAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }
}
