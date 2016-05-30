package it.eurotn.panjea.vending.manager.modello;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.manager.modello.interfaces.ModelloManager;

@Stateless(name = "Panjea.ModelloManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ModelloManager")
public class ModelloManagerBean extends CrudManagerBean<Modello>implements ModelloManager {

    @Override
    protected Class<Modello> getManagedClass() {
        return Modello.class;
    }

}
