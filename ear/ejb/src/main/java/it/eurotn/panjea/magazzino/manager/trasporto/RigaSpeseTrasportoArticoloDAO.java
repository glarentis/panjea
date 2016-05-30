package it.eurotn.panjea.magazzino.manager.trasporto;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.trasporto.RigaSpeseTrasportoArticolo;
import it.eurotn.panjea.magazzino.manager.documento.RigaMagazzinoAbstractDAOBean;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoDAO;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author leonardo
 */
@Stateless(mappedName = "Panjea.RigaSpeseTrasportoArticoloDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaSpeseTrasportoArticoloDAO")
public class RigaSpeseTrasportoArticoloDAO extends RigaMagazzinoAbstractDAOBean implements RigaMagazzinoDAO {

	@Override
	public RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
		RigaSpeseTrasportoArticolo rigaSpeseTrasportoArticolo = (RigaSpeseTrasportoArticolo) rigaDocumentoManager
				.creaRigaArticoloDocumento(new RigaSpeseTrasportoArticolo(), parametriCreazioneRigaArticolo);

		return rigaSpeseTrasportoArticolo;
	}

}
