/**
 * 
 */
package it.eurotn.panjea.magazzino.test;

import static org.junit.Assert.fail;
import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * TestCase per il manager dei Contratti.
 * 
 * @author adriano
 * @version 1.0, 17/giu/08
 */
public class TestAreaMagazzinoImportazioni {

	private static Logger logger = Logger.getLogger(TestAreaMagazzinoImportazioni.class);

	/**
	 * @throws java.lang.Exception .
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	/**
	 * @throws java.lang.Exception .
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private MagazzinoDocumentoService magazzinoDocumentoService;

	private final String codiceAzienda = "dolcelit";

	/**
	 * @throws java.lang.Exception .
	 */
	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config", TestAreaMagazzinoImportazioni.class.getClassLoader()
				.getResource("auth.conf").getPath());

		String username = "europa#" + codiceAzienda + "#it";
		String credential = "europasw";
		char[] password = credential.toCharArray();
		BasicConfigurator.configure();
		FileInputStream is = new FileInputStream(TestAreaMagazzinoImportazioni.class.getClassLoader()
				.getResource("log4j.properties").getPath());
		Properties logProperties = new Properties();
		logProperties.load(is);
		BasicConfigurator.resetConfiguration();
		PropertyConfigurator.configure(logProperties);
		UsernamePasswordHandler passwordHandler = new UsernamePasswordHandler(username, password);
		LoginContext loginContext;
		try {
			loginContext = new LoginContext("other", passwordHandler);
			loginContext.login();
		} catch (LoginException e) {
			fail(e.getMessage());
		}

		InitialContext ic = new InitialContext();
		magazzinoDocumentoService = (MagazzinoDocumentoService) ic.lookup("Panjea.MagazzinoDocumentoService");
	}

	/**
	 * @throws java.lang.Exception .
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test per il caricamento dei documenti di magazzino.
	 */
	@Test
	public void testCaricaDocumenti() {

		byte[] readFileToByteArray = null;
		try {
			readFileToByteArray = FileUtils.readFileToByteArray(new File("/tmp/import/dolcelit/DOLCEL.dat"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// HierarchicalDocumentImporter importer = new HierarchicalDocumentImporter();
		// importer.setCodice("Vipiteno");
		// importer.setByteArray(readFileToByteArray);
		// importer.setTipoAreaMagazzino(magazzinoDocumentoService.caricaTipoAreaMagazzino(19));
		// importer.setXmlTemplatePath("/media/dati/sviluppo/java/tools/jboss/server/default/conf/template/import/magazzino/VIPITENO_AM_IMPORT.xml");

		Collection<DocumentoImport> documenti = magazzinoDocumentoService.caricaDocumenti("Vipiteno",
				readFileToByteArray);
		Assert.assertNotNull("Nessun documento caricato", documenti);
		Assert.assertTrue("Nessun documento caricato", !documenti.isEmpty());
		if (logger.isDebugEnabled()) {
			logger.debug("--> Documenti caricati: " + documenti.size());
		}
	}

	/**
	 * Test per l'importazione di documenti di magazzino.
	 */
	@Test
	public void testImportaDocumenti() {

		byte[] readFileToByteArray = null;
		try {
			readFileToByteArray = FileUtils.readFileToByteArray(new File("/tmp/import/dolcelit/DOLCEL.dat"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// HierarchicalDocumentImporter importer = new HierarchicalDocumentImporter();
		// importer.setCodice("Vipiteno");
		// importer.setByteArray(readFileToByteArray);
		// importer.setTipoAreaMagazzino(magazzinoDocumentoService.caricaTipoAreaMagazzino(19));
		// importer.setXmlTemplatePath("/media/dati/sviluppo/java/tools/jboss/server/default/conf/template/import/magazzino/VIPITENO_AM_IMPORT.xml");

		Collection<DocumentoImport> documenti = magazzinoDocumentoService.caricaDocumenti("Vipiteno",
				readFileToByteArray);
		Assert.assertNotNull("Nessun documento caricato", documenti);
		Assert.assertTrue("Nessun documento caricato", !documenti.isEmpty());
		if (logger.isDebugEnabled()) {
			logger.debug("--> Documenti caricati: " + documenti.size());
		}

		List<AreaMagazzinoRicerca> areeCreate = magazzinoDocumentoService.importaDocumenti(documenti, "Vipiteno");
		Assert.assertNotNull("Nessuna area creata", areeCreate);
		Assert.assertTrue("Nessuna area creata", !areeCreate.isEmpty());
	}
}
