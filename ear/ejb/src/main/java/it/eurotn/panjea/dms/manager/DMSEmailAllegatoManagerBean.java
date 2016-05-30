package it.eurotn.panjea.dms.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import com.logicaldoc.panjea.domain.WSAttributeDocument;
import com.logicaldoc.webservice.document.WSDocument;

import it.eurotn.panjea.dms.exception.DMSLoginException;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;

@Stateless(mappedName = "Panjea.DMSEmailAllegatoManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DMSEmailAllegatoManagerBean")
public class DMSEmailAllegatoManagerBean extends DMSAllegatoManagerBean {

    @Override
    public List<WSDocument> getAllegati(AllegatoDMS allegatoDms) throws DMSLoginException {
        String sid = securityManager.login();

        List<Long> idDocs = new ArrayList<>();

        List<WSDocument> allegati = super.getAllegati(allegatoDms);
        for (WSDocument doc : allegati) {
            idDocs.add(doc.getId());
        }

        // carico anche gli extended attribute ( nel caso della email può essercene sono 1, non obbigatorio, che
        // rappresenta l'entità )
        if (!idDocs.isEmpty()) {
            WSAttributeDocument[] extAttr = lookupService.creaPanjeaService().findExtendedAttribute(sid,
                    idDocs.toArray(new Long[idDocs.size()]));
            for (int i = 0; i < allegati.size(); i++) {
                WSAttributeDocument attr = extAttr[i];
                allegati.get(i).setExtendedAttributes(attr.getAttributes());
            }
        }

        securityManager.logout(sid);
        return allegati;
    }
}
