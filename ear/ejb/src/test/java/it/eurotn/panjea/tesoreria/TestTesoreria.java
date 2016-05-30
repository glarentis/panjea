package it.eurotn.panjea.tesoreria;

import static org.junit.Assert.fail;
import it.eurotn.panjea.anagrafica.TestAnagrafica;
import it.eurotn.panjea.magazzino.test.TestContratti;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.service.interfaces.RateService;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Before;
import org.junit.Test;

public class TestTesoreria {
	private RateService rateService;

	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config",
				TestAnagrafica.class.getClassLoader().getResource("auth.conf").getPath());

		String username = "europa#MARPET#Panjea#it";
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

		InitialContext ic = new InitialContext();
		rateService = (RateService) ic.lookup("Panjea.RateService");
	}

	@Test
	public void TestSimulaRate() {
		CodicePagamento codicePagamento = new CodicePagamento();
		codicePagamento.setId(26);
		BigDecimal imponibile = new BigDecimal(1000);
		BigDecimal iva = new BigDecimal(200);
		List<Rata> result = rateService.generaRate(codicePagamento, Calendar.getInstance().getTime(), imponibile, iva,
				null);
		for (Rata rata : result) {
			System.out.println(rata);
		}
	}
}
