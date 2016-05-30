package it.eurotn.panjea.conai.manager;

import it.eurotn.panjea.conai.domain.RigaConaiArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.manager.documento.RigaMagazzinoAbstractDAOBean;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoDAO;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.RigaConaiArticoloDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaConaiArticoloDAO")
public class RigaConaiArticoloDAO extends RigaMagazzinoAbstractDAOBean implements RigaMagazzinoDAO {

	@Override
	public RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
		RigaConaiArticolo rigaConaiArticolo = (RigaConaiArticolo) rigaDocumentoManager.creaRigaArticoloDocumento(
				new RigaConaiArticolo(), parametriCreazioneRigaArticolo);

		return rigaConaiArticolo;
	}
}
