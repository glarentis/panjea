package it.eurotn.panjea.vending.manager.righemovimentocassa;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.RigaMovimentoCassa;
import it.eurotn.panjea.vending.manager.righemovimentocassa.interfaces.RigheMovimentoCassaManager;

@Stateless(name = "Panjea.RigheMovimentoCassaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheMovimentoCassaManager")
public class RigheMovimentoCassaManagerBean extends CrudManagerBean<RigaMovimentoCassa>implements RigheMovimentoCassaManager {

    @Override
    protected Class<RigaMovimentoCassa> getManagedClass() {
        return RigaMovimentoCassa.class;
    }

}