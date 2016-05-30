package it.eurotn.panjea.agenti.manager;

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
import it.eurotn.panjea.agenti.domain.AgentiSettings;
import it.eurotn.panjea.agenti.domain.BaseProvvigionaleSettings;
import it.eurotn.panjea.agenti.domain.BaseProvvigionaleStrategy;
import it.eurotn.panjea.agenti.manager.interfaces.AgentiSettingsManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.AgentiSettingsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AgentiSettingsManager")
public class AgentiSettingsManagerBean implements AgentiSettingsManager {

    private static final Logger LOGGER = Logger.getLogger(AgentiSettingsManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext context;

    @Override
    public AgentiSettings caricaAgentiSettings() {
        LOGGER.debug("--> Enter caricaAgentiSettings");

        Query query = panjeaDAO.prepareNamedQuery("AgentiSettings.caricaAll");
        query.setParameter("codiceAzienda", getAzienda());

        AgentiSettings agentiSettings = null;

        // carico il settings, se non lo trovo ne creo uno di default
        try {
            agentiSettings = (AgentiSettings) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            LOGGER.debug("--> AgentiSettings non trovato, ne creo uno nuovo");
            // se non trovo i settings impostati scelgo io di default i
            // parametri obbligatori
            // usati nell'applicazione
            agentiSettings = new AgentiSettings();
            agentiSettings.setCodiceAzienda(getAzienda());
            BaseProvvigionaleSettings baseProvvigionaleSettings = new BaseProvvigionaleSettings();
            baseProvvigionaleSettings.setBaseProvvigionaleStrategy(BaseProvvigionaleStrategy.PREZZO_NETTO);
            agentiSettings.setBaseProvvigionaleSettings(baseProvvigionaleSettings);
            agentiSettings = salvaAgentiSettings(agentiSettings);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento del agenti settings", e);
            throw new RuntimeException(e);
        }
        LOGGER.debug("--> Exit caricaAgentiSettings");
        return agentiSettings;
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
    public AgentiSettings salvaAgentiSettings(AgentiSettings agentiSettings) {
        LOGGER.debug("--> Enter salvaAgentiSettings");

        AgentiSettings agentiSettingsSalvati = null;
        try {
            agentiSettingsSalvati = panjeaDAO.save(agentiSettings);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio dei settings.", e);
            throw new RuntimeException("errore durante il salvataggio dei settings.", e);
        }

        LOGGER.debug("--> Exit salvaAgentiSettings");
        return agentiSettingsSalvati;
    }

}
