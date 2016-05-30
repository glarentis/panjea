package it.eurotn.panjea.contabilita.test;

import static org.junit.Assert.fail;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.contabilita.service.interfaces.ContabilitaService;
import it.eurotn.panjea.contabilita.util.RisultatoControlloProtocolli;
import it.eurotn.panjea.contabilita.util.RisultatoControlloRateSaldoContabile;
import it.eurotn.panjea.magazzino.test.TestContratti;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Before;
import org.junit.Test;

public class TestRigheContabili {
	private ContabilitaService contabilitaService;

	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config",
				TestRigheContabili.class.getClassLoader().getResource("auth.conf").getPath());

		String username = "europa#marpet#Panjea#it";
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
		contabilitaService = (ContabilitaService) ic.lookup("Panjea.ContabilitaService");
	}

	public void testRigaRateoRisconto() {
		RigaRateoRisconto rr = new RigaRateoRisconto();
		Calendar c = Calendar.getInstance();
		c.set(2014, 5, 1);
		rr.setInizio(c.getTime());
		c.set(2018, 5, 31);
		rr.setFine(c.getTime());
		rr.setImporto(new BigDecimal(1000));
		System.out.println(rr);
	}

	@Test
	public void testVerificaProtocolli() {
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("anno", new Integer(1900));
		List<RisultatoControlloProtocolli> result = contabilitaService.verificaDataProtocolli(params);
		for (RisultatoControlloProtocolli risultatoControlloProtocolliMancanti : result) {
			System.out.println(risultatoControlloProtocolliMancanti);
		}
	}

	@Test
	public void testVerificaSaldi() {
		List<RisultatoControlloRateSaldoContabile> result = contabilitaService.verificaSaldi(SottotipoConto.CLIENTE);
		for (RisultatoControlloRateSaldoContabile risultatoControlloRateSaldoContabile : result) {
			System.out.println(risultatoControlloRateSaldoContabile);
		}
		System.out.println("Saldi:" + result.size());
	};
}
