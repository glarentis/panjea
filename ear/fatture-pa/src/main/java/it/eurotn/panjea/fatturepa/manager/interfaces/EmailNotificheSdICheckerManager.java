package it.eurotn.panjea.fatturepa.manager.interfaces;

import javax.ejb.Local;

@Local
public interface EmailNotificheSdICheckerManager {

    /**
     * Esegue il check della mail e l'importazione delle notifiche SdI presenti.
     *
     * @param codiceAzienda
     *            codice azienda
     */
    void checkMail(String codiceAzienda);

    /**
     * Metodo utilizzato solamente per eseguire un test sul client per l'azienda loggata.
     *
     * @return log delle operazioni
     */
    String checkMailForTest();
}
