package it.eurotn.panjea.contabilita.manager.rateirisconti;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaContabileRateiRisconti;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRiscontoAnno;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.manager.rateirisconti.interfaces.AreaChiusuraRiscontiManager;
import it.eurotn.panjea.contabilita.service.exception.ChiusureRiscontiAnniSuccessiviPresentiException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaCancellaManager;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.AreaChiusuraRiscontiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaChiusuraRiscontiManager")
public class AreaChiusuraRiscontiManagerBean implements AreaChiusuraRiscontiManager {

    private static Logger logger = Logger.getLogger(AreaChiusuraRiscontiManagerBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private TipiAreaContabileManager tipiAreaContabileManager;

    @EJB
    private PianoContiManager pianoContiManager;

    @EJB
    private AreaContabileManager areaContabileManager;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private AreaContabileCancellaManager areaContabileCancellaManager;

    @EJB
    private AreaIvaCancellaManager areaIvaCancellaManager;

    /**
     * Carica tutti i movimenti di chiusura presenti nell'anno specificato e successivi.
     *
     * @param anno
     *            anno di partenza
     * @return movimenti
     */
    @SuppressWarnings("unchecked")
    private List<AreaContabile> caricaMovimentiChiusurePresenti(TipoEntita tipoEntita, int anno)
            throws ContiBaseException {

        List<AreaContabile> aree = null;

        try {
            SottoConto sottoConto = null;
            switch (tipoEntita) {
            case CLIENTE:
                sottoConto = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.RISCONTO_PASSIVO);
                break;
            default:
                sottoConto = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.RISCONTO_ATTIVO);
                break;
            }

            StringBuilder sb = new StringBuilder(200);
            sb.append("select distinct ac ");
            sb.append("from RigaContabile rc inner join rc.areaContabile ac ");
            sb.append("where ac.annoMovimento >= :anno ");
            sb.append("           and rc.conto = :sottoconto ");
            sb.append("order by ac.annoMovimento");

            Query query = panjeaDAO.prepareQuery(sb.toString());
            query.setParameter("anno", anno);
            query.setParameter("sottoconto", sottoConto);
            aree = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento dei movimenti di chiusura", e);
            throw new RuntimeException("errore durante il caricamento dei movimenti di chiusura", e);
        }

        return aree;
    }

    @SuppressWarnings("unchecked")
    private List<RigaRiscontoAnno> caricaRigheRiscontiAnno(TipoEntita tipoEntita, int anno) {

        StringBuilder sb = new StringBuilder(200);
        sb.append("select rra ");
        sb.append("from RigaRiscontoAnno rra inner join rra.rigaRateoRisconto rrr ");
        sb.append("where rrr.rigaContabile.areaContabile.documento.tipoDocumento.tipoEntita = :tipoEntita ");
        sb.append("           and rra.anno = :anno");
        sb.append(" and rrr.rigaContabile.areaContabile.statoAreaContabile in (:statiArea) ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("tipoEntita", tipoEntita);
        query.setParameter("anno", anno);
        query.setParameter("statiArea", Arrays.asList(StatoAreaContabile.CONFERMATO, StatoAreaContabile.VERIFICATO));

        List<RigaRiscontoAnno> righe = null;
        try {
            righe = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento delle righe risconto anno.", e);
            throw new RuntimeException("errore durante il caricamento delle righe risconto anno.", e);
        }

        // devo togliere tutti i risconti anno che sono stati generati dal documento di partenza
        Iterator<RigaRiscontoAnno> it = righe.iterator();
        while (it.hasNext()) {
            RigaRiscontoAnno rigaRiscontoAnno2 = it.next();
            if (rigaRiscontoAnno2.getRigaContabile() != null && rigaRiscontoAnno2.getRigaContabile().getAreaContabile()
                    .equals(rigaRiscontoAnno2.getRigaRateoRisconto().getRigaContabile().getAreaContabile())) {
                it.remove();
            }
        }

        return righe;
    }

    /**
     * Carica il tipo area contabile predefinito per la chiusura dei risconti.
     *
     * @return {@link TipoAreaContabile}
     * @throws TipoDocumentoBaseException
     *             .
     */
    private TipoAreaContabile caricaTipoAreaContabileChiusuraRisconti() throws TipoDocumentoBaseException {

        TipoAreaContabile tipoAreaContabile = null;
        try {
            tipoAreaContabile = tipiAreaContabileManager
                    .caricaTipoAreaContabilePerTipoOperazione(TipoOperazioneTipoDocumento.CHIUSURA_RISCONTI);
        } catch (ContabilitaException e) {
            throw new RuntimeException("Errore durante il caricamento del tipo documento di chiusura dei risconti.");
        }

        return tipoAreaContabile;
    }

    private AreaContabile creaAreaContabileChiusura(int anno) throws TipoDocumentoBaseException {

        TipoAreaContabile tipoAreaContabile = caricaTipoAreaContabileChiusuraRisconti();

        AreaContabile areaContabile = new AreaContabile();
        areaContabile.setAnnoIva(anno);
        areaContabile.setAnnoMovimento(anno);
        areaContabile.setCambio(BigDecimal.ONE);
        areaContabile.getDocumento().setCodiceAzienda(getAzienda());
        areaContabile.getDocumento().setTipoDocumento(tipoAreaContabile.getTipoDocumento());
        areaContabile.setStatoAreaContabile(StatoAreaContabile.PROVVISORIO);
        areaContabile.setTipoAreaContabile(tipoAreaContabile);

        return areaContabile;
    }

    @Override
    public ParametriRicercaMovimentiContabili creaMovimentiChiusureRisconti(int anno, Date dataMovimenti)
            throws ContiBaseException, TipoDocumentoBaseException {

        List<AreaContabile> movimentiCreati = new ArrayList<AreaContabile>();

        // genero un movimento per i risconti attivi e uno per i passivi se ne esistono.
        // ATTIVI
        List<RigaRiscontoAnno> riscontiAttivi = caricaRigheRiscontiAnno(TipoEntita.CLIENTE, anno);
        if (!riscontiAttivi.isEmpty()) {
            movimentiCreati.add(creaMovimentoChiusura(anno, dataMovimenti, riscontiAttivi, TipoEntita.CLIENTE));
        }

        // PASSIVI
        List<RigaRiscontoAnno> riscontiPassivi = caricaRigheRiscontiAnno(TipoEntita.FORNITORE, anno);
        if (!riscontiPassivi.isEmpty()) {
            movimentiCreati.add(creaMovimentoChiusura(anno, dataMovimenti, riscontiPassivi, TipoEntita.FORNITORE));
        }

        ParametriRicercaMovimentiContabili parametri = null;
        if (!movimentiCreati.isEmpty()) {
            parametri = new ParametriRicercaMovimentiContabili();
            parametri.setAnnoCompetenza(String.valueOf(anno));
            Periodo periodo = new Periodo();
            periodo.setTipoPeriodo(TipoPeriodo.DATE);
            periodo.setDataIniziale(dataMovimenti);
            periodo.setDataFinale(dataMovimenti);
            parametri.setDataDocumento(periodo);
            parametri.setDataRegistrazione(periodo);
            parametri.getTipiDocumento().add(movimentiCreati.get(0).getTipoAreaContabile().getTipoDocumento());
            parametri.setEffettuaRicerca(true);
        }

        return parametri;
    }

    private AreaContabile creaMovimentoChiusura(int anno, Date dataMovimento, List<RigaRiscontoAnno> risconti,
            TipoEntita tipoEntita) throws ContiBaseException, TipoDocumentoBaseException {

        // Carica AziendaLite per recuperare il codiceValuta dell'Azienda
        AziendaLite aziendaLite;
        try {
            aziendaLite = aziendeManager.caricaAzienda(getAzienda());
        } catch (AnagraficaServiceException e1) {
            logger.error("--> errore, impossibile recuperare l'azienda corrente  ", e1);
            throw new RuntimeException("Impossibile recuperare l'azienda corrente", e1);
        }

        // cerco se esiste già un movimento per l'anno
        List<AreaContabile> movimentiPresenti = caricaMovimentiChiusurePresenti(tipoEntita, anno);

        // se non esiste un movimento nell'anno specificato lo creo, se esiste e non ce ne sono di successivi uso quello
        // ricreando le sue righe
        boolean acAnnoPresente = !movimentiPresenti.isEmpty()
                && Objects.equals(anno, movimentiPresenti.get(0).getAnnoMovimento());

        BigDecimal totale = BigDecimal.ZERO;
        for (RigaRiscontoAnno rigaRiscontoAnno : risconti) {
            totale = totale.add(rigaRiscontoAnno.getImporto());
        }

        AreaContabile areaContabile = null;
        if (!acAnnoPresente) {
            areaContabile = creaAreaContabileChiusura(anno);
        } else {
            if (movimentiPresenti.size() > 1) {
                // esistono movimenti successivi quindi mi fermo
                movimentiPresenti.remove(0);
                throw new ChiusureRiscontiAnniSuccessiviPresentiException(anno, movimentiPresenti);
            } else {
                areaContabile = movimentiPresenti.get(0);
                areaIvaCancellaManager.cancellaAreaIva(areaContabile.getDocumento());

                // link ai risconti
                panjeaDAO.prepareNativeQuery(
                        "update cont_righe_rateo_risconto_anno ratanno inner join cont_righe_contabili rc on ratanno.rigaContabile_id = rc.id set ratanno.rigaContabile_id = null where rc.areaContabile_id = "
                                + areaContabile.getId())
                        .executeUpdate();

                areaContabile = areaContabileCancellaManager.cancellaRigheContabili(areaContabile);
            }
        }

        Importo totaleDoc = new Importo(aziendaLite.getCodiceValuta(), BigDecimal.ONE);
        totaleDoc.setImportoInValuta(totale);
        totaleDoc.calcolaImportoValutaAzienda(2);
        areaContabile.setDataRegistrazione(dataMovimento);
        areaContabile.getDocumento().setDataDocumento(dataMovimento);
        areaContabile.getDocumento().setTotale(totaleDoc);
        areaContabile.setStatoAreaContabile(StatoAreaContabile.PROVVISORIO);
        areaContabile.setValidRigheContabili(false);
        try {
            areaContabile = areaContabileManager.salvaAreaContabile(areaContabile, true);
        } catch (Exception e) {
            logger.error("--> errore durante il salvataggio dell'area contabile", e);
            throw new RuntimeException("errore durante il salvataggio dell'area contabile", e);
        }

        try {
            // creo la riga del totale risconti
            SottoConto sottoContoRisconti = null;
            if (tipoEntita == TipoEntita.CLIENTE) {
                sottoContoRisconti = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.RISCONTO_PASSIVO);
            } else {
                sottoContoRisconti = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.RISCONTO_ATTIVO);
            }
            RigaContabileRateiRisconti rigaContabileRisconti = (RigaContabileRateiRisconti) RigaContabile
                    .creaRigaContabile(new RigaContabileRateiRisconti(), areaContabile, sottoContoRisconti,
                            tipoEntita == TipoEntita.CLIENTE, totale, null, false);
            rigaContabileRisconti = (RigaContabileRateiRisconti) areaContabileManager
                    .salvaRigaContabile(rigaContabileRisconti);

            Map<SottoConto, BigDecimal> mapRigheDaSalvare = new HashMap<SottoConto, BigDecimal>();
            // creo le righe dei conti raggruppate per sottoconto
            for (RigaRiscontoAnno rigaRiscontoAnno : risconti) {
                SottoConto sottoConto = rigaRiscontoAnno.getRigaRateoRisconto().getRigaContabile().getConto();
                BigDecimal importo = ObjectUtils.defaultIfNull(
                        mapRigheDaSalvare.get(rigaRiscontoAnno.getRigaRateoRisconto().getRigaContabile().getConto()),
                        BigDecimal.ZERO);
                mapRigheDaSalvare.put(sottoConto, importo.add(rigaRiscontoAnno.getImporto()));

                // salvo il link alla riga contabile risconti creata
                // if (rigaRiscontoAnno.getRigaContabile() == null || rigaRiscontoAnno.getRigaContabile().isNew()) {
                rigaRiscontoAnno.setRigaContabile(rigaContabileRisconti);
                panjeaDAO.save(rigaRiscontoAnno);
                // }
            }
            for (Entry<SottoConto, BigDecimal> entry : mapRigheDaSalvare.entrySet()) {
                RigaContabile riga = RigaContabile.creaRigaContabile(areaContabile, entry.getKey(),
                        tipoEntita != TipoEntita.CLIENTE, entry.getValue(), null, false);
                areaContabileManager.salvaRigaContabile(riga);
            }

            areaContabile = areaContabileManager.validaRigheContabili(areaContabile, false);
        } catch (Exception e) {
            logger.error("--> errore durante la creazione della riga contabile del risconto.", e);
            throw new RuntimeException("errore durante la creazione della riga contabile del risconto.", e);
        }

        return areaContabile;
    }

    /**
     * @return codice azienda corrente
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

}
