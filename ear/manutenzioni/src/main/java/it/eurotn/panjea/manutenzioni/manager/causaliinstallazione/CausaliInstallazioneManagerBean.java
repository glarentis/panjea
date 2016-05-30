package it.eurotn.panjea.manutenzioni.manager.causaliinstallazione;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione;
import it.eurotn.panjea.manutenzioni.manager.causaliinstallazione.interfaces.CausaliInstallazioneManager;

@Stateless(name = "Panjea.CausaliInstallazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CausaliInstallazioneManager")
public class CausaliInstallazioneManagerBean extends CrudManagerBean<CausaleInstallazione>
        implements CausaliInstallazioneManager {

    @Override
    protected Class<CausaleInstallazione> getManagedClass() {
        return CausaleInstallazione.class;
    }

}