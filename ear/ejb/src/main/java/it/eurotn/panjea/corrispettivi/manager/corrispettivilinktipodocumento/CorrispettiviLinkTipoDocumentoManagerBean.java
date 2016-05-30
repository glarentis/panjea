package it.eurotn.panjea.corrispettivi.manager.corrispettivilinktipodocumento;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.corrispettivi.domain.CorrispettivoLinkTipoDocumento;
import it.eurotn.panjea.corrispettivi.manager.corrispettivilinktipodocumento.interfaces.CorrispettiviLinkTipoDocumentoManager;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;

@Stateless(name = "Panjea.CorrispettiviLinkTipoDocumentoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CorrispettiviLinkTipoDocumentoManager")
public class CorrispettiviLinkTipoDocumentoManagerBean extends CrudManagerBean<CorrispettivoLinkTipoDocumento>
        implements CorrispettiviLinkTipoDocumentoManager {

    private static final Logger LOGGER = Logger.getLogger(CorrispettiviLinkTipoDocumentoManagerBean.class);

    @Override
    public List<TipoDocumento> caricaTipiDocumentoCorrispettivi() {
        LOGGER.debug("--> Enter caricaTipiDocumentoCorrispettivi");

        List<CorrispettivoLinkTipoDocumento> caricaAll = caricaAll();

        Set<TipoDocumento> tipiDocSet = new TreeSet<>();
        for (CorrispettivoLinkTipoDocumento link : caricaAll) {
            tipiDocSet.add(link.getTipoDocumentoDestinazione());
        }

        LOGGER.debug("--> Exit caricaTipiDocumentoCorrispettivi");
        return new ArrayList<>(tipiDocSet);
    }

    @Override
    protected Class<CorrispettivoLinkTipoDocumento> getManagedClass() {
        return CorrispettivoLinkTipoDocumento.class;
    }

}