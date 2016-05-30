package it.eurotn.panjea.ordini.manager.documento;

import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineDAO;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.RigaOrdineDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaOrdineDAO")
public class RigaOrdineDAOBean extends RigaOrdineAbstractDAOBean implements RigaOrdineDAO {

}
