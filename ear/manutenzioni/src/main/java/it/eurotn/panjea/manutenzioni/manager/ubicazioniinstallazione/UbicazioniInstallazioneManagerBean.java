package it.eurotn.panjea.manutenzioni.manager.ubicazioniinstallazione;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.manutenzioni.domain.UbicazioneInstallazione;
import it.eurotn.panjea.manutenzioni.manager.ubicazioniinstallazione.interfaces.UbicazioniInstallazioneManager;

@Stateless(name = "Panjea.UbicazioniInstallazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.UbicazioniInstallazioneManager")
public class UbicazioniInstallazioneManagerBean extends CrudManagerBean<UbicazioneInstallazione>implements UbicazioniInstallazioneManager {

    @Override
    protected Class<UbicazioneInstallazione> getManagedClass() {
        return UbicazioneInstallazione.class;
    }

}