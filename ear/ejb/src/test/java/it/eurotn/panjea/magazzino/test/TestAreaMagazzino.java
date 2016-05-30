/**
 *
 */
package it.eurotn.panjea.magazzino.test;

import static org.junit.Assert.fail;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoStatisticheService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTOStampa;
import it.eurotn.panjea.magazzino.util.DisponibilitaArticolo;
import it.eurotn.panjea.magazzino.util.StatisticaArticolo;
import it.eurotn.panjea.magazzino.util.StatisticheArticolo;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.service.interfaces.LayoutStampeService;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import junit.framework.Assert;

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
public class TestAreaMagazzino {

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

	private static Logger logger = Logger.getLogger(TestAreaMagazzino.class);

	private MagazzinoDocumentoService magazzinoDocumentoService;

	private final String codiceAzienda = "dolcelit";

	private MagazzinoStatisticheService magazzinoStatisticheService;

	private LayoutStampeService magazzinoLayoutStampeService;

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
		magazzinoLayoutStampeService = (LayoutStampeService) ic.lookup("Panjea.LayoutStampeService");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLayoutStampe() {
		List<LayoutStampa> result = magazzinoLayoutStampeService.caricaLayoutStampe();
		result = magazzinoLayoutStampeService.caricaLayoutStampe();
	}

	@Test
	public void testRatePerStampa() {

		AreaMagazzinoFullDTO areaMagazzinoFullDTO = new AreaMagazzinoFullDTO();
		areaMagazzinoFullDTO.getAreaMagazzino().setTipoAreaMagazzino(new TipoAreaMagazzino());
		areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().setEntita(new FornitoreLite());
		areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().setSedeEntita(new SedeEntita());
		areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getSedeEntita().setTipoSede(new TipoSedeEntita());
		AreaMagazzinoFullDTOStampa areaMagazzinoFullDTOStampa = new AreaMagazzinoFullDTOStampa(areaMagazzinoFullDTO,
				new SedeAzienda(), new Deposito(), new Deposito(), new TrasportoCura(), new SedeEntita(),
				new SedeAzienda());

		areaMagazzinoFullDTO.getAreaRate().getRate().clear();

		// rate inferiori al numero rate richieste
		Rata rata1 = creaRata(1, BigDecimal.TEN);
		Rata rata2 = creaRata(2, new BigDecimal(23));
		Set<Rata> rateOrdered = new TreeSet<Rata>(new Rata.Ratacomparator());
		rateOrdered.addAll(Arrays.asList(new Rata[] { rata1, rata2 }));
		areaMagazzinoFullDTO.getAreaRate().setRate(rateOrdered);

		Set<Rata> rateStampa = areaMagazzinoFullDTOStampa.getRatePerStampa(5);
		Assert.assertNotNull(rateStampa);
		Assert.assertEquals(rateStampa.size(), 2);

		rateStampa = null;
		rateStampa = areaMagazzinoFullDTOStampa.getRatePerStampa(3);
		Assert.assertNotNull(rateStampa);
		Assert.assertEquals(rateStampa.size(), 2);

		// rate uguali al numero rate richieste
		rateStampa = null;
		rateStampa = areaMagazzinoFullDTOStampa.getRatePerStampa(2);
		Assert.assertNotNull(rateStampa);
		Assert.assertEquals(rateStampa.size(), 2);

		Rata rata3 = creaRata(3, new BigDecimal(50));
		rateOrdered = new TreeSet<Rata>(new Rata.Ratacomparator());
		rateOrdered.addAll(Arrays.asList(new Rata[] { rata1, rata2, rata3 }));
		areaMagazzinoFullDTO.getAreaRate().setRate(rateOrdered);

		rateStampa = null;
		rateStampa = areaMagazzinoFullDTOStampa.getRatePerStampa(3);
		Assert.assertNotNull(rateStampa);
		Assert.assertEquals(rateStampa.size(), 3);
		Iterator<Rata> iteratorRate = rateStampa.iterator();
		rata1 = iteratorRate.next();
		Assert.assertEquals(rata1.getImporto().getImportoInValuta(), BigDecimal.TEN);
		Assert.assertNotNull(rata1.getDataScadenza());
		rata2 = iteratorRate.next();
		Assert.assertEquals(rata2.getImporto().getImportoInValuta(), new BigDecimal(23));
		Assert.assertNotNull(rata2.getDataScadenza());
		rata3 = iteratorRate.next();
		Assert.assertEquals(rata3.getImporto().getImportoInValuta(), new BigDecimal(50));
		Assert.assertNotNull(rata3.getDataScadenza());

		// rate superiori al numero rate richieste
		Rata rata4 = creaRata(4, new BigDecimal(20));
		rateOrdered = new TreeSet<Rata>(new Rata.Ratacomparator());
		rateOrdered.addAll(Arrays.asList(new Rata[] { rata1, rata2, rata3, rata4 }));
		areaMagazzinoFullDTO.getAreaRate().setRate(rateOrdered);

		rateStampa = null;
		rateStampa = areaMagazzinoFullDTOStampa.getRatePerStampa(3);
		Assert.assertNotNull(rateStampa);
		Assert.assertEquals(rateStampa.size(), 3);
		iteratorRate = rateStampa.iterator();
		rata1 = iteratorRate.next();
		Assert.assertEquals(rata1.getImporto().getImportoInValuta(), BigDecimal.TEN);
		Assert.assertNotNull(rata1.getDataScadenza());
		rata2 = iteratorRate.next();
		Assert.assertEquals(rata2.getImporto().getImportoInValuta(), new BigDecimal(23));
		Assert.assertNotNull(rata2.getDataScadenza());
		rata3 = iteratorRate.next();
		Assert.assertEquals(rata3.getImporto().getImportoInValuta(), new BigDecimal(70));
		Assert.assertNull(rata3.getDataScadenza());

		rateStampa = null;
		rateStampa = areaMagazzinoFullDTOStampa.getRatePerStampa(2);
		Assert.assertNotNull(rateStampa);
		Assert.assertEquals(rateStampa.size(), 2);
		iteratorRate = rateStampa.iterator();
		rata1 = iteratorRate.next();
		Assert.assertEquals(rata1.getImporto().getImportoInValuta(), BigDecimal.TEN);
		Assert.assertNotNull(rata1.getDataScadenza());
		rata2 = iteratorRate.next();
		Assert.assertEquals(rata2.getImporto().getImportoInValuta(), new BigDecimal(93));
		Assert.assertNull(rata2.getDataScadenza());

	}

	@Test
	public void testStatistiche() {
		Articolo articolo = new Articolo();
		articolo.setId(9824);
		StatisticheArticolo st = magazzinoStatisticheService.caricaStatisticheArticolo(articolo, 2013);
		StatisticaArticolo lista = st.getStatistichePerTipologiaDeposito().get("Generico").iterator().next();
		for (DisponibilitaArticolo d : lista.getDisponibilita()) {
			System.out.println(d.getFabbisognoTotale() + " " + d.getDataConsegna());
		}
	}
}
