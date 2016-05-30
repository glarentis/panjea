package it.eurotn.panjea.giroclienti.rich.editors.riepilogo;

import java.util.Date;
import java.util.List;

import it.eurotn.panjea.sicurezza.domain.Utente;

public class RiepilogoGiornalieroPM {

    private List<Utente> utenti;

    private Date data;

    private boolean daEseguire;

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the utenti
     */
    public List<Utente> getUtenti() {
        return utenti;
    }

    /**
     * @return the daEseguire
     */
    public boolean isDaEseguire() {
        return daEseguire;
    }

    /**
     * @param daEseguire
     *            the daEseguire to set
     */
    public void setDaEseguire(boolean daEseguire) {
        this.daEseguire = daEseguire;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param utenti
     *            the utenti to set
     */
    public void setUtenti(List<Utente> utenti) {
        this.utenti = utenti;
    }
}
