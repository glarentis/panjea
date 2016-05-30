package it.eurotn.panjea.dms.manager.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.dms.exception.DMSLoginException;

@Local
public interface DMSSecurityManager {

    /**
     * login con i dati utente di Panjea.
     *
     * @return sid del login
     * @throws DMSLoginException
     *             sollevata se viene generato un problema durante il login
     */
    String login() throws DMSLoginException;

    /**
     * @param sid
     *            handle auth
     */
    void logout(String sid);
}
