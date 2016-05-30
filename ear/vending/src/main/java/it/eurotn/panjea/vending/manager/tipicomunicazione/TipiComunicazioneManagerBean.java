package it.eurotn.panjea.vending.manager.tipicomunicazione;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.TipoComunicazione;
import it.eurotn.panjea.vending.manager.tipicomunicazione.interfaces.TipiComunicazioneManager;

@Stateless(name = "Panjea.TipiComunicazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipiComunicazioneManager")
public class TipiComunicazioneManagerBean extends CrudManagerBean<TipoComunicazione>
        implements TipiComunicazioneManager {

    @Override
    protected Class<TipoComunicazione> getManagedClass() {
        return TipoComunicazione.class;
    }

}
