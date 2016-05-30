package it.eurotn.panjea.cosaro.sync.exporter;

import it.eurotn.panjea.cosaro.sync.exporter.interfaces.DataDocumentoExporter;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateful;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.CosaroDOCUMENTOExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CosaroDOCUMENTOExporter")
public class CosaroDOCUMENTOExporter extends AbstractDataDocumentoCosaroExporter implements DataDocumentoExporter {

	public static final String BEAN_NAME = "Panjea.CosaroDOCUMENTOExporter";

	@Override
	protected CosaroExporterAction getAction() {
		return CosaroExporterAction.WRITE;
	}

}
