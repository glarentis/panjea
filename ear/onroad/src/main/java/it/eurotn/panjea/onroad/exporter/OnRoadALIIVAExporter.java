package it.eurotn.panjea.onroad.exporter;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.manager.interfaces.CodiciIvaManager;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.onroad.exporter.manager.interfaces.DataExporter;

@Stateful(mappedName = "Panjea.OnRoadALIIVAExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnRoadALIIVAExporter")
public class OnRoadALIIVAExporter extends AbstractDataExporter implements DataExporter {

    public static final String BEAN_NAME = "Panjea.OnRoadALIIVAExporter";

    @EJB
    private CodiciIvaManager codiciIvaManager;

    @Override
    public void esporta() throws FileCreationException {

        List<CodiceIva> result = codiciIvaManager.caricaCodiciIva("%");

        StreamFactory factory = StreamFactory.newInstance();
        factory.load(getFilePathForTemplate());
        BeanWriter out = factory.createWriter("codIVA", getFileForExport());

        for (CodiceIva iva : result) {
            out.write(iva);
        }
        out.flush();
        out.close();
    }
}
