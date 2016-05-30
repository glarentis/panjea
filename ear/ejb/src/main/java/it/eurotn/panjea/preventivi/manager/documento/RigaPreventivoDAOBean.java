package it.eurotn.panjea.preventivi.manager.documento;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.RigaPreventivoDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaPreventivoDAO")
public class RigaPreventivoDAOBean extends RigaPreventivoAbstractDAOBean {

}
