package it.eurotn.panjea.vending.manager.anagraficheasl;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.AnagraficaAsl;
import it.eurotn.panjea.vending.manager.anagraficheasl.interfaces.AnagraficheAslManager;

@Stateless(name = "Panjea.AnagraficheAslManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AnagraficheAslManager")
public class AnagraficheAslManagerBean extends CrudManagerBean<AnagraficaAsl>implements AnagraficheAslManager {

    @Override
    protected Class<AnagraficaAsl> getManagedClass() {
        return AnagraficaAsl.class;
    }

}