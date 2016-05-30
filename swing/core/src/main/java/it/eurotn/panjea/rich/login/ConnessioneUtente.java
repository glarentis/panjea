package it.eurotn.panjea.rich.login;

import it.eurotn.panjea.sicurezza.license.exception.MassimoNumerUtentiCollegati;
import it.eurotn.panjea.sicurezza.service.interfaces.ConnessioneUtenteService;
import it.eurotn.util.PanjeaEJBUtil;

import java.net.ConnectException;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.MessageDialog;

public class ConnessioneUtente {
    private class TimerAlive extends TimerTask {

        @Override
        public void run() {
            try {
                connessioneUtenteService.checkAlive(PanjeaEJBUtil.getMacAddress());
            } catch (MassimoNumerUtentiCollegati e) {
                MessageDialog dialog = new MessageDialog("Num max utenti", new DefaultMessage(
                        "Impossibile effetturare l'accesso sulla riconnessione.\n Superato il numero massimo di utenti concorrenti"));
                dialog.showDialog();
            } catch (Exception e) {
                if (e != null && e.getCause() != null && e.getCause() instanceof ConnectException) {
                    System.out.println("Persa la connessione con il server" + e);
                }
                System.err.println("Errore durante la connessione " + e.getMessage());
            }
        }

    }

    public static final String BEAN_ID = "connessioneUtente";
    public static final int TIMEOUT_MIN = 240_000;
    private Timer timer = null;

    private ConnessioneUtenteService connessioneUtenteService;

    /**
     * Aggiorna lo stato della connessione al server.
     */
    public void check() {

    }

    /**
     * @return Returns the connessioneUtenteService.
     */
    public ConnessioneUtenteService getConnessioneUtenteService() {
        return connessioneUtenteService;
    }

    /**
     * Avvia il controllo degli utenti.
     * 
     * @return true se riesco a loggarmi ed inserire l'utente fra gli utenti connessi.
     */
    public boolean login() {
        // l'inserimento dell'utente non lo faccio thread safe
        // perchè dura poco e poi l'interfaccia deve attendere la verifica
        boolean result = true;
        try {
            connessioneUtenteService.aggiungiUtente(PanjeaEJBUtil.getMacAddress());
            timer = new Timer();
            timer.schedule(new TimerAlive(), TIMEOUT_MIN, TIMEOUT_MIN);
        } catch (MassimoNumerUtentiCollegati e) {
            MessageDialog dialog = new MessageDialog("Num max utenti", new DefaultMessage(
                    "Impossibile effetturare l'accesso.\n Superato il numero massimo di utenti concorrenti"));
            dialog.showDialog();
            result = false;
        }
        return result;
    }

    /**
     * Ferma il controllo degli utenti.
     */
    public void logout() {
        timer.cancel();
        timer.purge();
        connessioneUtenteService.rimuoviUtente(PanjeaEJBUtil.getMacAddress());
    }

    /**
     * @param connessioneUtenteService
     *            The connessioneUtenteService to set.
     */
    public void setConnessioneUtenteService(ConnessioneUtenteService connessioneUtenteService) {
        this.connessioneUtenteService = connessioneUtenteService;
    }
}
