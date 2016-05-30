/**
 *
 */
package it.eurotn.panjea.sicurezza.rich.pm;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.sicurezza.domain.Ruolo;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Classe presentation model per Utente contiene una List<Ruolo> ruoli e l' utente stesso, implementa IDefProperty per
 * avere così il supporto per il lock.
 *
 * @author Leonardo
 * @see it.eurotn.locking.IDefProperty
 */
public class UtentePM implements IDefProperty {

    private static Logger logger = Logger.getLogger(UtentePM.class);

    private Utente utente;

    private String confermaPassword = new String();
    private String confermaPasswordJasperServer = new String();
    private String confermaPasswordBugTracker = new String();

    private List<Ruolo> ruoli;

    private List<Ruolo> ruoliDaAggiungere = null;
    private List<Ruolo> ruoliDaRimuovere = null;

    /**
     * Costruttore di default.
     */
    public UtentePM() {
        this.utente = new Utente();
        this.ruoli = new ArrayList<Ruolo>();
    }

    /**
     * Costruttore di default per l'utente.
     *
     * @param utente
     *            l'utente di cui creare il PM
     */
    public UtentePM(final Utente utente) {
        this.utente = utente;
        this.confermaPassword = this.utente.getPassword();
        this.confermaPasswordBugTracker = this.utente.getDatiBugTracker().getPassword();
        this.confermaPasswordJasperServer = this.utente.getDatiJasperServer().getPassword();
    }

    /**
     * @return the confermaPassword
     */
    public String getConfermaPassword() {
        return confermaPassword;
    }

    /**
     * @return the confermaPasswordBugTracker
     */
    public String getConfermaPasswordBugTracker() {
        return confermaPasswordBugTracker;
    }

    /**
     * @return Returns the confermaPasswordJasperServer.
     */
    public String getConfermaPasswordJasperServer() {
        return confermaPasswordJasperServer;
    }

    @Override
    public String getDomainClassName() {
        return utente.getDomainClassName();
    }

    @Override
    public Integer getId() {
        return utente.getId();
    }

    /**
     * @return Returns the ruoli.
     */
    public List<Ruolo> getRuoli() {
        if (ruoli == null) {
            ruoli = transformRuoliSetToList(utente.getRuoli());
        }
        return ruoli;
    }

    /**
     * @return Returns the ruoliDaAggiungere.
     */
    @SuppressWarnings("unchecked")
    public List<Ruolo> getRuoliDaAggiungere() {
        logger.debug("---> Enter getRuoliDaAggiungere");
        ruoliDaAggiungere = (List<Ruolo>) PanjeaSwingUtil.getItemsToInsert(getUtente().getRuoli(), getRuoli());
        logger.debug("---> ruoli da Aggiungere " + ruoliDaAggiungere);
        logger.debug("---> Exit getRuoliDaAggiungere");
        return ruoliDaAggiungere;
    }

    /**
     * @return Returns the ruoliDaRimuovere.
     */
    @SuppressWarnings("unchecked")
    public List<Ruolo> getRuoliDaRimuovere() {
        logger.debug("---> Enter getRuoliDaRimuovere");
        ruoliDaRimuovere = (List<Ruolo>) PanjeaSwingUtil.getItemsToDelete(getUtente().getRuoli(), getRuoli());
        logger.debug("---> ruoli da Rimuovere " + ruoliDaRimuovere);
        logger.debug("---> Exit getRuoliDaRimuovere");
        return ruoliDaRimuovere;
    }

    /**
     * @return Returns the utente.
     */
    public Utente getUtente() {
        return utente;
    }

    @Override
    public Integer getVersion() {
        return utente.getVersion();
    }

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    /**
     * @param confermaPassword
     *            the confermaPassword to set
     */
    public void setConfermaPassword(String confermaPassword) {
        this.confermaPassword = confermaPassword;
    }

    /**
     * @param confermaPasswordBugTracker
     *            the confermaPasswordBugTracker to set
     */
    public void setConfermaPasswordBugTracker(String confermaPasswordBugTracker) {
        this.confermaPasswordBugTracker = confermaPasswordBugTracker;
    }

    /**
     * @param confermaPasswordJasperServer
     *            The confermaPasswordJasperServer to set.
     */
    public void setConfermaPasswordJasperServer(String confermaPasswordJasperServer) {
        this.confermaPasswordJasperServer = confermaPasswordJasperServer;
    }

    /**
     * @param ruoli
     *            The ruoli to set.
     */
    public void setRuoli(List<Ruolo> ruoli) {
        this.ruoli = ruoli;
    }

    /**
     * @param utente
     *            The utente to set.
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    /**
     * Trasforma il Set<Permesso> in List<Permesso> per avere una collection adatta al binding (@see
     * SwingBindingFactory).
     *
     * @param setruoli
     *            Set<Ruolo>
     * @return List<Ruolo>
     */
    private List<Ruolo> transformRuoliSetToList(Set<Ruolo> setruoli) {
        logger.debug("---> Enter transformRuoliSetToList");
        List<Ruolo> listruoli = new ArrayList<Ruolo>();
        if (setruoli == null) {
            return listruoli;
        }
        for (Ruolo ruolo : setruoli) {
            listruoli.add(ruolo);
        }
        logger.debug("---> Exit transformRuoliSetToList");
        return listruoli;
    }

}
