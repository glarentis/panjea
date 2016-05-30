package it.eurotn.panjea.vending.manager.gettoni;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.Gettone;
import it.eurotn.panjea.vending.manager.gettoni.interfaces.GettoniManager;

@Stateless(name = "Panjea.GettoniManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.GettoniManager")
public class GettoniManagerBean extends CrudManagerBean<Gettone>implements GettoniManager {

    @Override
    protected Class<Gettone> getManagedClass() {
        return Gettone.class;
    }

}