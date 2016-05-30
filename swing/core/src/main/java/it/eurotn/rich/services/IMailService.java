package it.eurotn.rich.services;

import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Utente;

public interface IMailService {

    /**
     * Carica l'utente corrente per mail service.
     *
     * @return utente corrente
     */
    Utente caricaUtenteCorrente();

    /**
     * spedice una email.
     *
     * @param parametri
     *            parametri per la spedizione della mail. .
     * @param aggiungiFirma
     *            se TRUE aggiunge la firma impostata dall'utente sui dati utente.
     * @param throwException
     *            <code>true</code> per non far gestire le eccezioni dal metodo ma rilanciarle
     * @return boolean. true se sono riuscito a spedire la mail.
     */
    boolean send(ParametriMail parametri, boolean aggiungiFirma, boolean throwException);

    /**
     * @param utente
     *            l'utente che testa la connessione
     * @param datiMail
     *            dati mail per la connessione
     * @return true se la connessione riesce, false altrimenti
     */
    boolean testConnection(Utente utente, DatiMail datiMail);

}
