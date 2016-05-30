package it.eurotn.panjea.ordini.manager.documento.righeinserimento.loaders.attributi;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.magazzino.manager.rigadocumento.interfaces.RigaDocumentoManager;
import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.manager.documento.righeinserimento.interfaces.AttributiInserimentoLoader;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;

@Stateless(mappedName = "Panjea.AttributiArticoloInserimentoLoaderBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AttributiArticoloInserimentoLoaderBean")
public class AttributiArticoloInserimentoLoaderBean implements AttributiInserimentoLoader {

    @EJB
    private RigaDocumentoManager rigaDocumentoManager;

    @Override
    public RigaOrdineInserimento fillAttributi(RigaOrdineInserimento rigaOrdineInserimento) {
        RigaArticolo rigaArticolo = (RigaArticolo) rigaDocumentoManager.creaAttributiRiga(new RigaArticolo(),
                rigaOrdineInserimento.getArticolo().creaProxyArticolo());
        Map<String, AttributoRigaArticolo> attributi = new HashMap<String, AttributoRigaArticolo>();
        for (AttributoRiga attributoRiga : rigaArticolo.getAttributi()) {
            attributi.put(attributoRiga.getTipoAttributo().getNome(), attributoRiga);
        }
        rigaOrdineInserimento.setAttributi(attributi);

        return rigaOrdineInserimento;
    }

}
