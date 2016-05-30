package it.eurotn.panjea.dms.manager.transfer;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.dms.manager.interfaces.DMSSettingsManager;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.sicurezza.service.exception.SicurezzaServiceException;
import it.eurotn.panjea.sicurezza.service.interfaces.SicurezzaService;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.LogicalDocEjbTransferFile")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LogicalDocEjbTransferFile")
public class LogicalDocEjbTransferFileBean extends LogicalDocTransferFile implements LogicalDocEjbTransferFile {

    @Resource
    private SessionContext context;

    @EJB
    private DMSSettingsManager dmsSettingManager;

    @EJB
    private SicurezzaService sicurezzaService;

    @Override
    public String getLogicalDocUrl() {
        return dmsSettingManager.caricaDmsSettings().getServiceUrl();
    }

    private String getUserName() {
        return ((JecPrincipal) context.getCallerPrincipal()).getUserName();
    }

    @Override
    public Utente getUtente() {
        try {
            return sicurezzaService.caricaUtente(getUserName());
        } catch (SicurezzaServiceException e) {
            return null;
        }
    }

}
