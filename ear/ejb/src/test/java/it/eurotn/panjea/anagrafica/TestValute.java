package it.eurotn.panjea.anagrafica;

import static org.junit.Assert.fail;
import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.magazzino.test.TestContratti;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Before;
import org.junit.Test;

public class TestValute {
	private InitialContext ic;
	private AnagraficaService anagraficaService;

	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config",
				TestAnagrafica.class.getClassLoader().getResource("auth.conf").getPath());

		String username = "europa#AESSE#Panjea#it";
		String credential = "europasw";
		char[] password = credential.toCharArray();
		BasicConfigurator.configure();
		FileInputStream is = new FileInputStream(TestContratti.class.getClassLoader().getResource("log4j.properties")
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
		anagraficaService = (AnagraficaService) ic.lookup("Panjea.AnagraficaService");
	}

	@Test
	public void TestCreaValuta() {
		ValutaAzienda valuta = new ValutaAzienda();
		valuta.setCodiceValuta("USD");
		valuta.setNumeroDecimali(2);
		valuta.setSimbolo("$");
		CambioValuta cambioValuta = new CambioValuta();
		cambioValuta.setValuta(valuta);
		Calendar now = Calendar.getInstance();
		cambioValuta.setData(now.getTime());
		cambioValuta.setTasso(BigDecimal.ONE);
		cambioValuta = anagraficaService.salvaCambioValuta(cambioValuta);

	}

}
