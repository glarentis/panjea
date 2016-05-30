/**
 * 
 */
package it.eurotn.panjea.magazzino.test;

import static org.junit.Assert.fail;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoAnagraficaService;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoStatisticheService;
import it.eurotn.panjea.rate.domain.Rata;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
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
public class TestDistinte {

	private static Logger logger = Logger.getLogger(TestAreaMagazzino.class);

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

	private MagazzinoDocumentoService magazzinoDocumentoService;

	private final String codiceAzienda = "dolcelit";

	private MagazzinoStatisticheService magazzinoStatisticheService;

	private MagazzinoAnagraficaService magazzinoAnagraficaService;

	private Rata creaRata(int numero, BigDecimal importo) {
		Rata rata = new Rata();
		rata.setId(numero);
		rata.setNumeroRata(numero);
		rata.setDataScadenza(Calendar.getInstance().getTime());
		rata.getImporto().setCodiceValuta("EUR");
		rata.getImporto().setImportoInValuta(importo);
		rata.getImporto().calcolaImportoValutaAzienda(2);
		return rata;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config",
				TestAreaMagazzino.class.getClassLoader().getResource("auth.conf").getPath());

		String username = "europa#" + codiceAzienda + "#it";
		String credential = "europasw";
		char[] password = credential.toCharArray();
		BasicConfigurator.configure();
		FileInputStream is = new FileInputStream(TestAreaMagazzino.class.getClassLoader()
				.getResource("log4j.properties").getPath());
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
		magazzinoDocumentoService = (MagazzinoDocumentoService) ic.lookup("Panjea.MagazzinoDocumentoService");
		magazzinoStatisticheService = (MagazzinoStatisticheService) ic.lookup("Panjea.MagazzinoStatisticheService");
		magazzinoAnagraficaService = (MagazzinoAnagraficaService) ic.lookup("Panjea.MagazzinoAnagraficaService");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreaConfigurazione() {
		ConfigurazioneDistinta conf = new ConfigurazioneDistinta();
		Articolo distinta = new Articolo();
		distinta.setId(11360);
		distinta = magazzinoAnagraficaService.caricaArticolo(distinta, false);
		conf.setDistinta(distinta);
		conf.setNome("Configurazione 3");
		magazzinoAnagraficaService.salvaConfigurazioneDistinta(conf);
	}
}
