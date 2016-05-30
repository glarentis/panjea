package it.eurotn.panjea.fatturepa.service;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturaPAConservazioneSostitutiva;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturaPASettingsManager;
import it.eurotn.panjea.fatturepa.service.interfaces.ConservazioneSostitutivaService;

@Stateless(name = "Panjea.ConservazioneSostitutivaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.ConservazioneSostitutivaService")
public class ConservazioneSostitutivaServiceBean implements ConservazioneSostitutivaService {

    private static final Logger LOGGER = Logger.getLogger(ConservazioneSostitutivaServiceBean.class);

    @EJB
    private FatturaPAConservazioneSostitutiva fatturaPAConservazioneSostitutiva;

    @EJB
    private FatturaPASettingsManager fatturaPASettingsManager;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void conservaXMLFatturePA() {

        FatturaPASettings fatturaPASettings;
        try {
            fatturaPASettings = fatturaPASettingsManager.caricaFatturaPASettings();
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle settings delle fattura PA", e);
            fatturaPASettings = null;
        }

        if (fatturaPASettings != null && fatturaPASettings.isAttivaConservazioneSostitutiva()) {
            fatturaPAConservazioneSostitutiva.conservaXMLFatturePA();
        }
    }

}
