package it.eurotn.panjea.magazzino.manager.omaggio;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.omaggio.RigaOmaggioArticolo;
import it.eurotn.panjea.magazzino.manager.documento.RigaMagazzinoAbstractDAOBean;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoDAO;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;

/**
 * @author leonardo
 */
@Stateless(mappedName = "Panjea.RigaOmaggioArticoloDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaOmaggioArticoloDAO")
public class RigaOmaggioArticoloDAO extends RigaMagazzinoAbstractDAOBean implements RigaMagazzinoDAO {

    @Override
    public RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {

        return (RigaOmaggioArticolo) rigaDocumentoManager.creaRigaArticoloDocumento(new RigaOmaggioArticolo(),
                parametriCreazioneRigaArticolo);
    }

}
