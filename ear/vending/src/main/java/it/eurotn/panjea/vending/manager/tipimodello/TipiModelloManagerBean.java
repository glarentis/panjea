package it.eurotn.panjea.vending.manager.tipimodello;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.panjea.vending.manager.tipimodello.interfaces.TipiModelloManager;

@Stateless(name = "Panjea.TipiModelloManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipiModelloManager")
public class TipiModelloManagerBean extends CrudManagerBean<TipoModello> implements TipiModelloManager {

    private void aggiungiunmetodo() {
        System.out.println("DAVIDE GAY");
    }

    @Override
    protected Class<TipoModello> getManagedClass() {
        return TipoModello.class;
    }

}
