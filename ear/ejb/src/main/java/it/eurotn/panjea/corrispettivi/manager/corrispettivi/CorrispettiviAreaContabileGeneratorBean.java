package it.eurotn.panjea.corrispettivi.manager.corrispettivi;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.StrutturaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiEntitaAssentiException;
import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;
import it.eurotn.panjea.corrispettivi.domain.CalendarioCorrispettivo;
import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.corrispettivi.domain.GiornoCorrispettivo;
import it.eurotn.panjea.corrispettivi.domain.RigaCorrispettivo;
import it.eurotn.panjea.corrispettivi.manager.corrispettivi.interfaces.CorrispettiviAreaContabileGenerator;
import it.eurotn.panjea.corrispettivi.manager.corrispettivi.interfaces.CorrispettiviManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.CorrispettiviAreaContabileGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CorrispettiviAreaContabileGenerator")
public class CorrispettiviAreaContabileGeneratorBean implements CorrispettiviAreaContabileGenerator {

    private static final Logger LOGGER = Logger.getLogger(CorrispettiviAreaContabileGeneratorBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private StrutturaContabileManager strutturaContabileManager;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private TipiAreaContabileManager tipiAreaContabileManager;

    @EJB
    private AreaContabileManager areaContabileManager;

    @EJB
    private AreaIvaManager areaIvaManager;

    @EJB
    private CorrispettiviManager corrispettiviManager;

    @Override
    public void creaDocumenti(CalendarioCorrispettivo calendarioCorrispettivo) {
        LOGGER.debug("--> Enter creaDocumenti");
        List<StrutturaContabile> strutturaContabileList = null;
        try {
            strutturaContabileList = strutturaContabileManager
                    .caricaStrutturaContabile(calendarioCorrispettivo.getTipoDocumento(), null, true);
        } catch (Exception e) {
            LOGGER.error("--> Errore nella ricerca della struttura contabile per il tipoDocumento "
                    + calendarioCorrispettivo.getTipoDocumento());
            throw new RuntimeException("Errore nella ricerca della struttura contabile per il tipoDocumento", e);
        }

        if (strutturaContabileList.size() == 0) {
            LOGGER.error("--> non ho struttura contabile per generare documento per il tipo documento  "
                    + calendarioCorrispettivo.getTipoDocumento());
            throw new GenericException(
                    "Struttura contabile assente per la generazione delle righe documento per il tipo documento "
                            + calendarioCorrispettivo.getTipoDocumento().getCodice() + " - "
                            + calendarioCorrispettivo.getTipoDocumento().getDescrizione());
        }

        // Carica AziendaLite per recuperare il codiceValuta dell'Azienda
        AziendaLite aziendaLite;
        try {
            aziendaLite = aziendeManager.caricaAzienda(getAzienda());
        } catch (AnagraficaServiceException e1) {
            LOGGER.error("--> errore, impossibile recuperare l'azienda corrente  ", e1);
            throw new RuntimeException("Impossibile recuperare l'azienda corrente", e1);
        }

        for (GiornoCorrispettivo giornoCorrispettivo : calendarioCorrispettivo.getGiorniCorrispettivo()) {
            Corrispettivo corrispettivo = giornoCorrispettivo.getCorrispettivo();

            if (corrispettivo.getTotale() != null && corrispettivo.getTotale().compareTo(BigDecimal.ZERO) != 0) {
                TipoAreaContabile tipoAreaContabile = null;

                try {
                    tipoAreaContabile = tipiAreaContabileManager.caricaTipoAreaContabilePerTipoDocumento(
                            calendarioCorrispettivo.getTipoDocumento().getId());
                } catch (ContabilitaException ex) {
                    LOGGER.error("--> Errore durante il caricamento del tipo area contabile per il tipo documento: "
                            + calendarioCorrispettivo.getTipoDocumento().getId(), ex);
                    throw new RuntimeException(
                            "Errore durante il caricamento del tipo area contabile per il tipo documento: "
                                    + calendarioCorrispettivo.getTipoDocumento().getId(),
                            ex);
                }

                Importo totale = new Importo();
                totale.setCodiceValuta(aziendaLite.getCodiceValuta());
                totale.setImportoInValutaAzienda(corrispettivo.getTotale());
                totale.setImportoInValuta(corrispettivo.getTotale());

                AreaContabile areaContabile = new AreaContabile(calendarioCorrispettivo.getAnno(),
                        corrispettivo.getData(), tipoAreaContabile);
                areaContabile.getDocumento().setTotale(totale);

                try {
                    areaContabile = areaContabileManager.salvaAreaContabile(areaContabile, true);
                } catch (Exception e) {
                    LOGGER.error("--> Errore durante il salvataggio dell'area contabile.", e);
                    throw new RuntimeException("Errore durante il salvataggio dell'area contabile.", e);
                }

                AreaIva areaIva = areaIvaManager.caricaAreaIva(areaContabile);
                // creo le righe iva per l'area contabile
                for (RigaCorrispettivo rigaCorrispettivo : corrispettivo.getRigheCorrispettivo()) {

                    if (rigaCorrispettivo.getImporto() != null
                            && rigaCorrispettivo.getImporto().compareTo(BigDecimal.ZERO) != 0) {
                        RigaIva rigaIva = new RigaIva();
                        rigaIva.setAreaIva(areaIva);
                        rigaIva.setCodiceIva(rigaCorrispettivo.getCodiceIva());
                        // HACK correzione per eccezione sollevata durante il salvataggio di una RigaIva (save the
                        // transient) perchè il codiceIvaCollegato valorizzata con un istanza vuota inizializzato con il
                        // rispettivo valore all'interno di CodiceIVA
                        rigaIva.setCodiceIvaCollegato(rigaCorrispettivo.getCodiceIva().getCodiceIvaCollegato());

                        // nuovo importo con riferimenti tassoDiCambio e valuta recuperati da areaContabile
                        Importo rigaImporto = new Importo(areaContabile.getDocumento().getTotale().getCodiceValuta(),
                                areaContabile.getDocumento().getTotale().getTassoDiCambio());
                        rigaImporto.setImportoInValuta(rigaCorrispettivo.getImporto());
                        rigaImporto.calcolaImportoValutaAzienda(RigaIva.SCALE_FISCALE);

                        Importo imponibile = rigaImporto.multiply(Importo.HUNDRED, RigaIva.SCALE_FISCALE).divide(
                                rigaCorrispettivo.getCodiceIva().getPercApplicazione().add(Importo.HUNDRED),
                                RigaIva.SCALE_FISCALE, BigDecimal.ROUND_HALF_UP);
                        rigaIva.setImponibile(imponibile);
                        rigaIva.setImposta(rigaImporto.subtract(imponibile, RigaIva.SCALE_FISCALE));
                        rigaIva.setOrdinamento(Calendar.getInstance().getTimeInMillis());
                        try {
                            rigaIva = areaIvaManager.salvaRigaIva(rigaIva, areaContabile.getTipoAreaContabile());
                        } catch (CodiceIvaCollegatoAssenteException e) {
                            LOGGER.error("--> errore durante il salvataggio delle righe iva", e);
                            throw new GenericException("Codice iva collegato non presente.");
                        }
                        areaIva.getRigheIva().add(rigaIva);
                    }
                }

                areaIvaManager.validaAreaIva(areaIva);

                // creo le riga contabili
                List<ControPartita> listControPartite = null;

                try {
                    listControPartite = strutturaContabileManager.caricaControPartiteConImporto(areaContabile);
                    strutturaContabileManager.creaRigheContabili(areaContabile, listControPartite);
                    areaContabileManager.validaRigheContabili(areaContabile);
                } catch (ContiEntitaAssentiException e) {
                    StringBuilder sb = new StringBuilder(200);
                    sb.append("Non sono presenti i conti per le seguenti entità: ");
                    for (ContoEntitaAssenteException contoEntitaAssenteException : e.getContiEntitaExceptions()) {
                        sb.append(StringUtils.defaultString(contoEntitaAssenteException.getEntita()));
                    }
                    throw new GenericException(sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // cancello tutti i corrispettivi del mese
        corrispettiviManager.cancellaCorrispettivi(calendarioCorrispettivo);

        LOGGER.debug("--> Exit creaDocumenti");
    }

    /**
     *
     * @return azienda loggata
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }
}
