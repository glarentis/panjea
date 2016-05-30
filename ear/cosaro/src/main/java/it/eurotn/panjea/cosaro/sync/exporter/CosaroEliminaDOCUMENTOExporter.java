package it.eurotn.panjea.cosaro.sync.exporter;

import it.eurotn.panjea.cosaro.sync.exporter.interfaces.DataDocumentoExporter;
import it.eurotn.panjea.exporter.exception.FileCreationException;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateful;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.CosaroEliminaDOCUMENTOExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CosaroEliminaDOCUMENTOExporter")
public class CosaroEliminaDOCUMENTOExporter extends AbstractDataDocumentoCosaroExporter implements
		DataDocumentoExporter {

	public static final String BEAN_NAME = "Panjea.CosaroEliminaDOCUMENTOExporter";

	@Override
	protected CosaroExporterAction getAction() {
		return CosaroExporterAction.DELETE;
	}

	@Override
	protected String getFileExtension(String codiceTipoDocumento) throws FileCreationException {
		String fileExtension = super.getFileExtension(codiceTipoDocumento);
		if (fileExtension != null) {
			fileExtension = fileExtension + ".DEL";
		}
		return fileExtension;
	}

}
