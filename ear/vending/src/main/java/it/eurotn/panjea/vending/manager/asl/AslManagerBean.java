package it.eurotn.panjea.vending.manager.asl;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.Asl;
import it.eurotn.panjea.vending.manager.asl.interfaces.AslManager;

@Stateless(name = "Panjea.AslManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AslManager")
public class AslManagerBean extends CrudManagerBean<Asl>implements AslManager {

    @Override
    protected Class<Asl> getManagedClass() {
        return Asl.class;
    }

}