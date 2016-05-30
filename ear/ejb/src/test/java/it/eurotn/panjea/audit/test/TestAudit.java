package it.eurotn.panjea.audit.test;

import static org.junit.Assert.fail;
import it.eurotn.panjea.anagrafica.service.interfaces.AuditService;
import it.eurotn.panjea.magazzino.test.TestContratti;

import java.io.FileInputStream;
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

public class TestAudit {
	private AuditService auditService;
	private InitialContext ic;

	@Test
	public void cancellaAudit() {
		auditService.cancellaAuditPrecedente(Calendar.getInstance().getTime());
	}

	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config", TestAudit.class.getClassLoader().getResource("auth.conf")
				.getPath());

		String username = "europa#dolcelit#Panjea#it";
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
		auditService = (AuditService) ic.lookup("Panjea.AuditService");
	}
}
