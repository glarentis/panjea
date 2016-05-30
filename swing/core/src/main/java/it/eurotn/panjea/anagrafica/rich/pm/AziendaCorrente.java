package it.eurotn.panjea.anagrafica.rich.pm;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.pm.IAziendaCorrente;

/**
 * L'azienda corrente con accessibili le proprieta' utili dell'azienda. Per inizializzare il bean e' necessario chiamare
 * il metodo initialize().
 *
 * @author Leonardo
 */
public class AziendaCorrente implements Serializable, InitializingBean, IAziendaCorrente {

    public static final String BEAN_ID = "aziendaCorrente";
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(AziendaCorrente.class);
    private AziendaLite aziendaLite = null;
    private AbstractAnnoCompetenzaLocator annoCompetenzaContabileLocator = null;
    private AbstractAnnoCompetenzaLocator annoCompetenzaMagazzinoLocator = null;
    private IAnagraficaBD anagraficaBD;
    private Integer annoContabile = null;
    private Integer annoMagazzino = null;
    private Deposito depositoPrincipale = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        org.springframework.util.Assert.notNull(anagraficaBD, "anagraficaBD cannot be null!");
    }

    /**
     * @return the annoCompetenzaContabile
     */
    public AbstractAnnoCompetenzaLocator getAnnoCompetenzaContabileLocator() {
        return annoCompetenzaContabileLocator;
    }

    /**
     * @return the annoCompetenzaMagazzino
     */
    public AbstractAnnoCompetenzaLocator getAnnoCompetenzaMagazzinoLocator() {
        return annoCompetenzaMagazzinoLocator;
    }

    /**
     * Implemento un metodo diverso per ritornare il solo valore integer dell'anno di competenza contabile dato che
     * Spring richiede il get e il set di annoCompetenzaContabile.
     *
     * @return the annoCompetenzaContabile
     */
    public Integer getAnnoContabile() {
        if (annoCompetenzaContabileLocator != null && annoContabile == null) {
            annoContabile = annoCompetenzaContabileLocator.getAnnoCompetenza();
        }
        if (annoContabile == null) {
            annoContabile = new Integer(0);
        }
        return annoContabile;
    }

    /**
     * Implemento un metodo diverso per ritornare il solo valore integer dell'anno di competenza magazzino dato che
     * Spring richiede il get e il set di annoCompetenzaMagazzino.
     *
     * @return the annoCompetenzaMagazzino
     */
    public Integer getAnnoMagazzino() {
        if (annoCompetenzaMagazzinoLocator != null && annoMagazzino == null) {
            annoMagazzino = annoCompetenzaMagazzinoLocator.getAnnoCompetenza();
        }
        if (annoMagazzino == null) {
            annoMagazzino = new Integer(0);
        }
        return annoMagazzino;
    }

    /**
     * @return the aziendaLite
     */
    public AziendaLite getAziendaLite() {
        return aziendaLite;
    }

    /**
     * @return città
     */
    public String getCitta() {
        return (aziendaLite != null)
                ? aziendaLite.getCap().getDescrizione() + " - " + aziendaLite.getLocalita().getDescrizione() : null;
    }

    /**
     * @return codice
     */
    public String getCodice() {
        return aziendaLite != null ? aziendaLite.getCodice() : null;
    }

    /**
     * @return codice fiscale
     */
    public String getCodiceFiscale() {
        return aziendaLite != null ? aziendaLite.getCodiceFiscale() : null;
    }

    /**
     * @return codice valuta
     */
    public String getCodiceValuta() {
        return aziendaLite.getCodiceValuta();
    }

    /**
     * @return data fine esercizio
     */
    public Date getDataFineEsercizio() {
        return aziendaLite.getDataFineEsercizio(getAnnoCompetenzaContabileLocator().getAnnoCompetenza());
    }

    /**
     * @return data inizio esercizio
     */
    public Date getDataInizioEsercizio() {
        return aziendaLite.getDataInizioEsercizio(getAnnoCompetenzaContabileLocator().getAnnoCompetenza());
    }

    /**
     * @return denominazione
     */
    public String getDenominazione() {
        return aziendaLite != null ? aziendaLite.getDenominazione() : null;
    }

    /**
     * @return deposito principale
     */
    public Deposito getDepositoPrincipale() {
        return depositoPrincipale;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return (aziendaLite != null) ? aziendaLite.getId() : null;
    }

    /**
     * @return indirizzo
     */
    public String getIndirizzo() {
        return (aziendaLite != null) ? aziendaLite.getIndirizzo() : null;
    }

    /**
     * @return lingua
     */
    public String getLingua() {
        return aziendaLite.getLingua();
    }

    /**
     * @return nazione
     */
    public Nazione getNazione() {
        return (aziendaLite != null) ? aziendaLite.getNazione() : new Nazione();
    }

    /**
     * @return partita iva
     */
    public String getPartitaIVA() {
        return aziendaLite != null ? aziendaLite.getPartitaIVA() : null;
    }

    /**
     * Inizializza l'azienda per accedere a tutte le proprieta', deve essere chiamato questo metodo per il corretto uso
     * di questo bean.
     */
    public void initialize() {
        logger.debug("--> Enter initialize azienda corrente");
        aziendaLite = anagraficaBD.caricaAzienda();
        depositoPrincipale = anagraficaBD.caricaDepositoPrincipale();
    }

    /**
     * Esegue il reset delle proprieta' dell'azienda che la identificano.
     */
    public void reset() {
        logger.debug("--> Enter reset azienda corrente");
        aziendaLite = null;
        annoContabile = null;
        annoMagazzino = null;
    }

    /**
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    /**
     * @param annoCompetenzaContabileLocator
     *            the annoCompetenzaContabileLocator to set
     */
    public void setAnnoCompetenzaContabileLocator(AbstractAnnoCompetenzaLocator annoCompetenzaContabileLocator) {
        this.annoCompetenzaContabileLocator = annoCompetenzaContabileLocator;
    }

    /**
     * @param annoCompetenzaMagazzinoLocator
     *            the annoCompetenzaMagazzinoLocator to set
     */
    public void setAnnoCompetenzaMagazzinoLocator(AbstractAnnoCompetenzaLocator annoCompetenzaMagazzinoLocator) {
        this.annoCompetenzaMagazzinoLocator = annoCompetenzaMagazzinoLocator;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("AziendaCorrente[");
        buffer.append("codice " + getCodice());
        buffer.append(" data inizio esercizio " + getDataInizioEsercizio());
        buffer.append(" data fine esercizio " + getDataFineEsercizio());
        buffer.append(" anno competenza contabile " + getAnnoCompetenzaContabileLocator());
        buffer.append(" anno competenza magazzino " + getAnnoCompetenzaMagazzinoLocator());
        buffer.append("]");
        return buffer.toString();
    }
}
