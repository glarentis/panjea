package it.eurotn.panjea.magazzino.manager;

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
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * Manager per la gestione di <code>MagazzinoSettings</code>.
 *
 * @author Leonardo
 */
@Stateless(name = "Panjea.MagazzinoSettingsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MagazzinoSettingsManager")
public class MagazzinoSettingsManagerBean implements MagazzinoSettingsManager {

    private static Logger logger = Logger.getLogger(MagazzinoSettingsManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;
    @Resource
    private SessionContext context;

    @Override
    public MagazzinoSettings caricaMagazzinoSettings() {
        logger.debug("--> Enter caricaContabilitaSettings");

        Query query = panjeaDAO.prepareNamedQuery("MagazzinoSettings.caricaAll");
        query.setParameter("codiceAzienda", getAzienda());

        MagazzinoSettings magazzinoSettings = null;

        // carico il settings, se non lo trovo ne creo uno di default
        try {
            magazzinoSettings = (MagazzinoSettings) panjeaDAO.getSingleResult(query);
            magazzinoSettings.getSogliaAddebitoDichiarazione().size();
        } catch (ObjectNotFoundException e) {
            logger.debug("--> ContabilitaSettings non trovato, ne creo uno nuovo");
            // se non trovo i settings impostati scelgo io di default i parametri obbligatori
            // usati nell'applicazione
            magazzinoSettings = new MagazzinoSettings();
            magazzinoSettings.setCodiceAzienda(getAzienda());
            magazzinoSettings = salvaMagazzinoSettings(magazzinoSettings);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento del contabilita settings", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaContabilitaSettings");
        return magazzinoSettings;
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
    public MagazzinoSettings salvaMagazzinoSettings(MagazzinoSettings magazzinoSettingsToSave) {
        logger.debug("--> Enter salvaContabilitaSettings");

        MagazzinoSettings magazzinoSettingsSalvato;
        try {
            magazzinoSettingsSalvato = panjeaDAO.save(magazzinoSettingsToSave);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio di ContabilitaSettings", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit salvaContabilitaSettings");
        return magazzinoSettingsSalvato;
    }

}
