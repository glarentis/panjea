package it.eurotn.panjea.magazzino.manager.documento;

import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoDAO;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.RigaMagazzinoDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaMagazzinoDAO")
public class RigaMagazzinoDAOBean extends RigaMagazzinoAbstractDAOBean implements RigaMagazzinoDAO {

}
