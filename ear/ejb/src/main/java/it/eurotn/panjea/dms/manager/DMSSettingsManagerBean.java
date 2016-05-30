package it.eurotn.panjea.dms.manager;

import java.net.UnknownHostException;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.dms.domain.DmsSettings;
import it.eurotn.panjea.dms.exception.DmsException;
import it.eurotn.panjea.dms.manager.interfaces.DMSSettingsManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(mappedName = "Panjea.DMSSettingsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DMSSettingsManager")
public class DMSSettingsManagerBean implements DMSSettingsManager {

    private static final String ALL_IP = "0.0.0.0";

    private static final String LOCALHOST_IP = "127.0.0.1";

    private static final Logger LOGGER = Logger.getLogger(DMSSettingsManager.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext context;

    @Override
    public DmsSettings caricaDmsSettings() {
        LOGGER.debug("--> Enter caricaDmsSettings");

        Query query = panjeaDAO.prepareNamedQuery("DmsSettings.caricaAll");
        query.setParameter("codiceAzienda", getAzienda());

        DmsSettings dmsSettings = null;

        // carico il settings, se non lo trovo ne creo uno di default
        try {
            dmsSettings = (DmsSettings) panjeaDAO.getSingleResult(query);

            if (StringUtils.isBlank(dmsSettings.getServiceUrl())) {
                dmsSettings.setServiceUrl(getServiceUrlFromServer());
                dmsSettings = salvaDmsSettings(dmsSettings);
            }
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento delle dms settings", e);
            throw new DmsException(e);
        }
        LOGGER.debug("--> Enter caricaDmsSettings");
        return dmsSettings;
    }

    private String getAzienda() {
        return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
    }

    private String getServiceUrlFromServer() {
        LOGGER.debug("--> Enter getServiceUrlFromJbossAddress");

        String bindAddr = System.getProperty("jboss.bind.address");
        // se jboss è bindato con -b 0.0.0.0 o niente uso il nome della macchina
        if (ALL_IP.equals(bindAddr) || LOCALHOST_IP.equals(bindAddr)) {
            try {
                bindAddr = java.net.InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e1) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(
                            "--> non riesco a trovare l'ip locale. vuol dire che è bindato su tutti gli indirizzi.");
                }
                bindAddr = ALL_IP;
            }
        }
        StringBuilder sbServerUrl = new StringBuilder(200);
        sbServerUrl.append("http://");
        sbServerUrl.append(bindAddr);
        sbServerUrl.append(":8080/logicaldoc");

        LOGGER.debug("--> Exit enclosing_method");
        return sbServerUrl.toString();
    }

    @Override
    public boolean isEnable() {
        return StringUtils.isEmpty(caricaDmsSettings().getServiceUrl());
    }

    @Override
    public DmsSettings salvaDmsSettings(DmsSettings dmsSettings) {
        LOGGER.debug("--> Enter salvaDmsSettings");

        DmsSettings dmsSettingsSalvati = null;
        try {
            dmsSettingsSalvati = panjeaDAO.save(dmsSettings);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio dei settings.", e);
            throw new DmsException("errore durante il salvataggio dei settings.", e);
        }

        LOGGER.debug("--> Exit salvaDmsSettings");
        return dmsSettingsSalvati;
    }

}
