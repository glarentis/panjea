package it.eurotn.panjea.giroclienti.manager;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.giroclienti.domain.GiroClientiSettings;
import it.eurotn.panjea.giroclienti.exception.GiroClientiException;
import it.eurotn.panjea.giroclienti.manager.interfaces.GiroClientiSettingsManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.GiroClientiSettingsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.GiroClientiSettingsManager")
public class GiroClientiSettingsManagerBean implements GiroClientiSettingsManager {

    private static final Logger LOGGER = Logger.getLogger(GiroClientiSettingsManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext context;

    @Override
    public GiroClientiSettings caricaGiroClientiSettings() {
        LOGGER.debug("--> Enter caricaGiroClientiSettings");

        Query query = panjeaDAO.prepareNamedQuery("GiroClientiSettings.caricaAll");
        query.setParameter("codiceAzienda", getAzienda());

        GiroClientiSettings giroClientiSettings = null;

        // carico il settings, se non lo trovo ne creo uno di default
        try {
            giroClientiSettings = (GiroClientiSettings) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            LOGGER.debug("--> GiroClientiSettings.caricaAll non trovato, ne creo uno nuovo", e);
            // se non trovo i settings impostati scelgo io di default i parametri obbligatori usati nell'applicazione
            giroClientiSettings = new GiroClientiSettings();
            giroClientiSettings.setCodiceAzienda(getAzienda());
            giroClientiSettings = salvaGiroClientiSettings(giroClientiSettings);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento delle settings del giro clienti", e);
            throw new GiroClientiException("Errore durante il caricamento delle settings del giro clienti", e);
        }
        LOGGER.debug("--> Exit caricaGiroClientiSettings");
        return giroClientiSettings;
    }

    /**
     * Recupera il codice azienda dell'utente autenticato nel context.
     *
     * @return String codice azienda
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    @Override
    public GiroClientiSettings salvaGiroClientiSettings(GiroClientiSettings giroClientiSettings) {
        LOGGER.debug("--> Enter salvaGiroClientiSettings");

        GiroClientiSettings giroClientiSettingsSalvate = null;
        try {
            giroClientiSettingsSalvate = panjeaDAO.save(giroClientiSettings);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio delle settings del giro clienti.", e);
            throw new GiroClientiException("errore durante il salvataggio delle settings del giro clienti.", e);
        }

        return giroClientiSettingsSalvate;
    }

}
