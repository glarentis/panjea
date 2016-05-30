package it.eurotn.panjea.manutenzioni.manager.manutenzionisettings;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manutenzioni.domain.ManutenzioneSettings;
import it.eurotn.panjea.manutenzioni.manager.manutenzionisettings.interfaces.ManutenzioniSettingsManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.ManutenzioniSettingsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ManutenzioniSettingsManager")
public class ManutenzioniSettingsManagerBean implements ManutenzioniSettingsManager {

    private static final Logger LOGGER = Logger.getLogger(ManutenzioniSettingsManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public ManutenzioneSettings caricaManutenzioniSettings() {
        LOGGER.debug("--> Enter caricaManutenzioniSettings");

        Query query = panjeaDAO.prepareQuery("select ms from ManutenzioneSettings ms");

        ManutenzioneSettings manutenzioneSettings = null;
        try {
            manutenzioneSettings = (ManutenzioneSettings) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            // se non trovo le preferenze le creo di default e le salvo
            manutenzioneSettings = new ManutenzioneSettings();
            manutenzioneSettings = salvaManutenzioneSettings(manutenzioneSettings);
        } catch (DAOException e) {
            LOGGER.error("--> errore durante il caricamento delle settings delle manutenzioni.", e);
            throw new GenericException("errore durante il caricamento delle settings delle manutenzioni.", e);
        }

        LOGGER.debug("--> Exit caricaManutenzioniSettings");
        return manutenzioneSettings;
    }

    @Override
    public ManutenzioneSettings salvaManutenzioneSettings(ManutenzioneSettings manutenzioneSettings) {
        LOGGER.debug("--> Enter salvaManutenzioneSettings");

        ManutenzioneSettings manutenzioneSettingsSave = null;
        try {
            manutenzioneSettingsSave = panjeaDAO.save(manutenzioneSettings);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio delle settings delle manutenzioni", e);
            throw new GenericException("errore durante il salvataggio delle settings delle manutenzioni", e);
        }

        LOGGER.debug("--> Exit salvaManutenzioneSettings");
        return manutenzioneSettingsSave;
    }

}