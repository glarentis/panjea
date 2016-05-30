package it.eurotn.panjea.fatturepa.manager;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturaPASettingsManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.sicurezza.manager.interfaces.PanjeaCryptoManager;
import it.eurotn.security.JecPrincipal;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.FatturaPASettingsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.FatturaPASettingsManager")
public class FatturaPASettingsManagerBean implements FatturaPASettingsManager {

    private static final Logger LOGGER = Logger.getLogger(FatturaPASettingsManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaCryptoManager panjeaCryptoManager;

    @Override
    public FatturaPASettings caricaFatturaPASettings() {
        LOGGER.debug("--> Enter caricaFatturaPASettings");

        Query query = panjeaDAO.prepareNamedQuery("FatturaPASettings.caricaAll");
        query.setParameter("codiceAzienda", getAzienda());

        FatturaPASettings fatturaPASettings = null;

        // carico il settings, se non lo trovo ne creo uno di default
        try {
            fatturaPASettings = (FatturaPASettings) panjeaDAO.getSingleResult(query);
            if (fatturaPASettings != null) {
                ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(fatturaPASettings);
                fatturaPASettings.getDatiConservazioneSostitutiva().setPassword(
                        panjeaCryptoManager.decrypt(fatturaPASettings.getDatiConservazioneSostitutiva().getPassword()));
                fatturaPASettings.getDatiMailInvioSdI().setPasswordMail(
                        panjeaCryptoManager.decrypt(fatturaPASettings.getDatiMailInvioSdI().getPasswordMail()));
                fatturaPASettings.getDatiMailRicezioneSdI().setPasswordMail(
                        panjeaCryptoManager.decrypt(fatturaPASettings.getDatiMailRicezioneSdI().getPasswordMail()));
            }
        } catch (ObjectNotFoundException e) {
            LOGGER.debug("--> FatturaPASettings.caricaAll non trovato, ne creo uno nuovo");
            // se non trovo i settings impostati scelgo io di default i parametri obbligatori usati nell'applicazione
            fatturaPASettings = new FatturaPASettings();
            fatturaPASettings.setCodiceAzienda(getAzienda());
            fatturaPASettings = salvaFatturaPASettings(fatturaPASettings);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento delle fattura pa settings", e);
            throw new RuntimeException(e);
        }
        LOGGER.debug("--> Exit caricaFatturaPASettings");
        return fatturaPASettings;
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
    public FatturaPASettings salvaFatturaPASettings(FatturaPASettings fatturaPaSettings) {
        LOGGER.debug("--> Enter salvaFatturaPASettings");

        FatturaPASettings fatturaPASettingsSalvati = null;
        try {
            fatturaPaSettings.getDatiConservazioneSostitutiva().setPassword(
                    panjeaCryptoManager.encrypt(fatturaPaSettings.getDatiConservazioneSostitutiva().getPassword()));
            fatturaPaSettings.getDatiMailInvioSdI().setPasswordMail(
                    panjeaCryptoManager.encrypt(fatturaPaSettings.getDatiMailInvioSdI().getPasswordMail()));
            fatturaPaSettings.getDatiMailRicezioneSdI().setPasswordMail(
                    panjeaCryptoManager.encrypt(fatturaPaSettings.getDatiMailRicezioneSdI().getPasswordMail()));
            fatturaPASettingsSalvati = panjeaDAO.save(fatturaPaSettings);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio dei settings.", e);
            throw new RuntimeException("errore durante il salvataggio dei settings.", e);
        }

        LOGGER.debug("--> Exit salvaFatturaPASettings");
        return fatturaPASettingsSalvati;
    }

}
