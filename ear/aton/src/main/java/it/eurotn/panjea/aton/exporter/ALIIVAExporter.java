package it.eurotn.panjea.aton.exporter;

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
import it.eurotn.panjea.aton.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.exporter.exception.FileCreationException;

@Stateful(mappedName = "Panjea.AtonALIIVAExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AtonALIIVAExporter")
public class ALIIVAExporter extends AbstractDataExporter implements DataExporter {

    public static final String BEAN_NAME = "Panjea.AtonALIIVAExporter";

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
