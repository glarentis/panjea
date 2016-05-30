package it.eurotn.panjea.conai.test;

import static org.junit.Assert.fail;
import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;
import it.eurotn.panjea.conai.domain.ConaiListino;
import it.eurotn.panjea.conai.service.interfaces.ConaiService;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoAnagraficaService;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import junit.framework.Assert;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Before;
import org.junit.Test;

public class TestConai {

	private ConaiService conaiService;
	private MagazzinoAnagraficaService magazzinoAnagraficaService;
	private InitialContext ic;

	/**
	 * init test.
	 * 
	 * @throws Exception .
	 */
	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config", TestConai.class.getClassLoader().getResource("auth.conf")
				.getPath());

		String username = "europa#MARPET#Panjea#it";
		String credential = "europasw";
		char[] password = credential.toCharArray();
		BasicConfigurator.configure();
		FileInputStream is = new FileInputStream(TestConai.class.getClassLoader().getResource("log4j.properties")
				.getPath());
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

		ic = new InitialContext();
		conaiService = (ConaiService) ic.lookup("Panjea.ConaiService");
		magazzinoAnagraficaService = (MagazzinoAnagraficaService) ic.lookup("Panjea.MagazzinoAnagraficaService");
	}

	/**
	 * test esportazione spesometro.
	 */
	@Test
	public void testConai() {
		List<ConaiArticolo> articoliConai = conaiService.caricaArticoliConai();

		ConaiArticolo conaiAcciaio = articoliConai.get(0);
		Assert.assertEquals(conaiAcciaio.getMateriale(), ConaiMateriale.ACCIAIO);
		Articolo a = new Articolo();
		a.setId(142);
		a = magazzinoAnagraficaService.caricaArticolo(a, false);
		conaiAcciaio.setArticolo(a.getArticoloLite());

		ConaiArticolo ca = conaiService.salvaArticoloConai(conaiAcciaio);
		Assert.assertNotNull(ca.getId());

		ConaiArticolo conaiAlluminio = articoliConai.get(1);
		Assert.assertEquals(conaiAlluminio.getMateriale(), ConaiMateriale.ACCIAIO);
		Articolo b = new Articolo();
		b.setId(143);
		b = magazzinoAnagraficaService.caricaArticolo(b, false);
		conaiAlluminio.setArticolo(b.getArticoloLite());

		ConaiListino list1 = new ConaiListino();
		list1.setConaiArticolo(conaiAlluminio);

		Calendar cal = Calendar.getInstance();
		cal.set(2001, 0, 1, 0, 0, 0);
		Date dataInizio = cal.getTime();
		list1.setDataInizio(dataInizio);

		cal.set(2015, 11, 31, 0, 0, 0);
		Date dataFine = cal.getTime();
		list1.setDataFine(dataFine);

	}
}
