package it.eurotn.panjea.vending.manager.vendingsettings;

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
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.VendingSettings;
import it.eurotn.panjea.vending.manager.vendingsettings.interfaces.VendingSettingsManager;

@Stateless(name = "Panjea.VendingSettingsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.VendingSettingsManager")
public class VendingSettingsManagerBean implements VendingSettingsManager {

    private static final Logger LOGGER = Logger.getLogger(VendingSettingsManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public VendingSettings caricaVendingSettings() {
        LOGGER.debug("--> Enter caricaVendingSettings");

        Query query = panjeaDAO.prepareNamedQuery("VendingSettings.caricaAll");

        VendingSettings vendingSettings = null;
        try {
            vendingSettings = (VendingSettings) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) { // NOSONAR
            // se non trovo le preferenze le creo di default e le salvo
            vendingSettings = new VendingSettings();
            vendingSettings = salvaVendingSettings(vendingSettings);
        } catch (DAOException e) {
            LOGGER.error("--> errore durante il caricamento delle settings del vending.", e);
            throw new GenericException("errore durante il caricamento delle settings del vending.", e);
        }

        LOGGER.debug("--> Exit caricaVendingSettings");
        return vendingSettings;
    }

    @Override
    public VendingSettings salvaVendingSettings(VendingSettings vendingSettings) {
        LOGGER.debug("--> Enter salvaVendingSettings");

        VendingSettings vendingSettingsSave = null;
        try {
            vendingSettingsSave = panjeaDAO.save(vendingSettings);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio delle settings del vending", e);
            throw new GenericException("errore durante il salvataggio delle settings del vending", e);
        }

        LOGGER.debug("--> Exit salvaVendingSettings");
        return vendingSettingsSave;
    }
}