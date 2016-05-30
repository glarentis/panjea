package it.eurotn.panjea.fatturepa.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.domain.TipoRegimeFiscale;
import it.eurotn.panjea.fatturepa.manager.interfaces.AziendaFatturaPAManager;
import it.eurotn.panjea.fatturepa.manager.interfaces.EmailNotificheSdICheckerManager;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturaPASettingsManager;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturePAAnagraficaManager;
import it.eurotn.panjea.fatturepa.service.interfaces.FatturePAAnagraficaService;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.FatturePAAnagraficaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.FatturePAAnagraficaService")
public class FatturePAAnagraficaServiceBean implements FatturePAAnagraficaService {

    @EJB
    private FatturePAAnagraficaManager fatturePAAnagraficaManager;

    @EJB
    private AziendaFatturaPAManager aziendaFatturaPAManager;

    @EJB
    private FatturaPASettingsManager fatturaPASettingsManager;

    @EJB
    private EmailNotificheSdICheckerManager emailNotificheSdICheckerManager;

    @Override
    public AziendaFatturaPA caricaAziendaFatturaPA() {
        return aziendaFatturaPAManager.caricaAziendaFatturaPA();
    }

    @Override
    public FatturaPASettings caricaFatturaPASettings() {
        return fatturaPASettingsManager.caricaFatturaPASettings();
    }

    @Override
    public List<TipoRegimeFiscale> caricaTipiRegimiFiscali() {
        return fatturePAAnagraficaManager.caricaTipiRegimiFiscali();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public String checkMailForTest() {
        return emailNotificheSdICheckerManager.checkMailForTest();
    }

    @Override
    public AziendaFatturaPA salvaAziendaFatturaPA(AziendaFatturaPA aziendaFatturaPA) {
        return aziendaFatturaPAManager.salvaAziendaFatturaPA(aziendaFatturaPA);
    }

    @Override
    public FatturaPASettings salvaFatturaPASettings(FatturaPASettings fatturaPaSettings) {
        return fatturaPASettingsManager.salvaFatturaPASettings(fatturaPaSettings);
    }

}
