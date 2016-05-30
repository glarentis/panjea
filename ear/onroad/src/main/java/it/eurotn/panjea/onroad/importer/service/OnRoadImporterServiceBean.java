package it.eurotn.panjea.onroad.importer.service;

import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.onroad.domain.wrapper.ClientiOnRoad;
import it.eurotn.panjea.onroad.domain.wrapper.DocumentiOnRoad;
import it.eurotn.panjea.onroad.importer.manager.interfaces.ImportaClienti;
import it.eurotn.panjea.onroad.importer.manager.interfaces.ImportaDocumenti;
import it.eurotn.panjea.onroad.importer.service.interfaces.OnRoadImporterService;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.io.File;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.OnRoadImporterService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.OnRoadImporterService")
public class OnRoadImporterServiceBean implements OnRoadImporterService {

	private static Logger logger = Logger.getLogger(OnRoadImporterServiceBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;
	@EJB
	protected ImportaClienti importaClienti;
	@EJB
	protected ImportaDocumenti importaDocumenti;

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ClientiOnRoad importaClienti() throws ImportException {
		logger.debug("--> Enter importa");
		String dir = importaClienti.getAtonImportDir();
		if (dir.isEmpty()) {
			return null;
		}
		String prefix = importaClienti.getAtonPrefixImport();
		String fileName = prefix + "NCLIENT" + ".tot";
		File fileClienti = new File(dir + fileName);
		if (!fileClienti.exists()) {
			return null;
		}

		ClientiOnRoad clientiOnRoad = importaClienti.importa(dir + fileName);
		fileClienti.delete();

		logger.debug("--> Exit importa");
		return clientiOnRoad;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public DocumentiOnRoad importaDocumenti() throws ImportException {
		logger.debug("--> Enter importa");
		String dir = importaDocumenti.getAtonImportDir();
		if (dir.isEmpty()) {
			return null;
		}
		String prefix = importaDocumenti.getAtonPrefixImport();
		String fileName = prefix + "TESTAT.tot";
		File fileTestata = new File(dir + fileName);
		if (!fileTestata.exists()) {
			return null;
		}

		System.out.println("pathFile " + dir + fileName);
		DocumentiOnRoad documentiOnRoad = importaDocumenti.importa(dir + fileName);

		fileName = fileName.replace("TESTAT", "RIGHE");
		File fileRighe = new File(dir + fileName);
		fileName = fileName.replace("RIGHE", "ALIQUO");
		File fileRigheIva = new File(dir + fileName);

		fileTestata.delete();
		fileRighe.delete();
		fileRigheIva.delete();

		logger.debug("--> Exit importa");
		return documentiOnRoad;
	}

}