/**
 *
 */
package it.eurotn.panjea.fatturepa.test;

import static org.junit.Assert.fail;
import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.service.interfaces.ConservazioneSostitutivaService;
import it.eurotn.panjea.fatturepa.service.interfaces.FatturePAService;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

import java.io.FileInputStream;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * TestCase per il manager dei Contratti.
 *
 * @author adriano
 * @version 1.0, 17/giu/08
 */
public class TestFatturePA {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private FatturePAService fatturePAService;
	private ConservazioneSostitutivaService conservazioneSostitutivaService;

	private final String codiceAzienda = "edgiord";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config",
				TestFatturePA.class.getClassLoader().getResource("auth.conf").getPath());

		String username = "europa#" + codiceAzienda + "#it";
		String credential = "pnj_adm";
		char[] password = credential.toCharArray();
		BasicConfigurator.configure();
		FileInputStream is = new FileInputStream(TestFatturePA.class.getClassLoader().getResource("log4j.properties")
				.getPath());
		Properties LogProperties = new Properties();
		LogProperties.load(is);
		BasicConfigurator.resetConfiguration();
		PropertyConfigurator.configure(LogProperties);
		UsernamePasswordHandler passwordHandler = new UsernamePasswordHandler(username, password);
		LoginContext loginContext;
		try {
			loginContext = new LoginContext("other", passwordHandler);
			loginContext.login();
		} catch (LoginException e) {
			fail(e.getMessage());
		}

		InitialContext ic = new InitialContext();
		fatturePAService = (FatturePAService) ic.lookup("Panjea.FatturePAService");
		conservazioneSostitutivaService = (ConservazioneSostitutivaService) ic
				.lookup("Panjea.ConservazioneSostitutivaService");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCaricaFatturaTypeFromXML() {

		AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePAService.caricaAreaMagazzinoFatturaPA(50062);

		IFatturaElettronicaType fatturaElettronicaType = fatturePAService
				.caricaFatturaElettronicaType(areaMagazzinoFatturaPA.getXmlFattura().getXmlFattura());

		System.out.println(fatturaElettronicaType);
	}

	// @Test
	// public void testCheckClienteConservazioneSostitutiva() {
	// String connessione = solutionDocFatturaPAManager.checkClienteFatturaPA();
	//
	// if (Objects.equals("0", connessione)) {
	// System.out.println("Verifica dati connessione ok");
	// } else {
	// System.out.println(connessione);
	// }
	// }

	@Test
	public void testCheckEsiti() {
		fatturePAService.checkEsitiFatturePA();
	}

	@Test
	public void testConservaFatturePA() {

		conservazioneSostitutivaService.conservaXMLFatturePA();
	}

	@Test
	public void testCreaFileXML() {

		// 50247 FACC FA72 04/07/14
		// fatturePAService.creaXML(66847);

	}

	// @Test
	// public void testGetEsitiFatturaPASolutionDoc() {
	//
	// try {
	// Map<StatoFatturaPA, String> esiti = conservazioneSostitutivaService.getEsitiSdIFatturaPA("129", null);
	// for (Entry<StatoFatturaPA, String> entry : esiti.entrySet()) {
	// System.out.println("Stato: " + entry.getKey() + " id: " + entry.getValue());
	// byte[] fileEsitoFatturaPA = conservazioneSostitutivaService.getFileEsitoFatturaPA(entry.getValue());
	// System.out.println(new String(fileEsitoFatturaPA));
	// }
	// } catch (Exception e) {
	// System.out.println(e.getMessage());
	// }
	// }
	//
	// @Test
	// public void testInvioFatturaSdiSolutionDoc() {
	//
	// conservazioneSostitutivaService.invioSdiFatturaPA();
	// }

	// @Test
	// public void testVerificaServizioConservazioneSostitutiva() {
	// String connessione = solutionDocFatturaPAManager.contattoHub();
	//
	// if (Objects.equals("0", connessione)) {
	// System.out.println("Servizio ok");
	// } else {
	// System.out.println(connessione);
	// }
	// }
}
