package it.eurotn.panjea.ordini.manager.documento.righeinserimento.loaders;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.ordini.manager.documento.righeinserimento.interfaces.RigheInserimentoLoader;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;

@Stateless(mappedName = "Panjea.RigheInserimentoAssortimentoLoaderBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheInserimentoAssortimentoLoaderBean")
public class RigheInserimentoAssortimentoLoaderBean implements RigheInserimentoLoader {

    @Override
    public List<RigaOrdineInserimento> caricaRigheOrdineInserimento(ParametriRigheOrdineInserimento parametri) {

        return new ArrayList<RigaOrdineInserimento>();
    }

}
