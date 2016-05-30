package it.eurotn.panjea.giroclienti.rich.editors.scheda.header.copiascheda;

import it.eurotn.panjea.giroclienti.domain.ModalitaCopiaGiroClienti;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;

public class SchedaCopiaPM {

    private Utente utente;

    private Giorni giorno;

    private Utente utenteDestinazione;

    private Giorni giornoDestinazione;

    private ModalitaCopiaGiroClienti modalitaCopia;

    /**
     * @return the giorno
     */
    public Giorni getGiorno() {
        return giorno;
    }

    /**
     * @return the giornoDestinazione
     */
    public Giorni getGiornoDestinazione() {
        return giornoDestinazione;
    }

    /**
     * @return the modalitaCopia
     */
    public ModalitaCopiaGiroClienti getModalitaCopia() {
        return modalitaCopia;
    }

    /**
     * @return the utente
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * @return the utenteDestinazione
     */
    public Utente getUtenteDestinazione() {
        return utenteDestinazione;
    }

    /**
     * @param giorno
     *            the giorno to set
     */
    public void setGiorno(Giorni giorno) {
        this.giorno = giorno;
    }

    /**
     * @param giornoDestinazione
     *            the giornoDestinazione to set
     */
    public void setGiornoDestinazione(Giorni giornoDestinazione) {
        this.giornoDestinazione = giornoDestinazione;
    }

    /**
     * @param modalitaCopia
     *            the modalitaCopia to set
     */
    public void setModalitaCopia(ModalitaCopiaGiroClienti modalitaCopia) {
        this.modalitaCopia = modalitaCopia;
    }

    /**
     * @param utente
     *            the utente to set
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    /**
     * @param utenteDestinazione
     *            the utenteDestinazione to set
     */
    public void setUtenteDestinazione(Utente utenteDestinazione) {
        this.utenteDestinazione = utenteDestinazione;
    }
}