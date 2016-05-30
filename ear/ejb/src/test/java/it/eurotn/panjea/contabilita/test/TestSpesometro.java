package it.eurotn.panjea.contabilita.test;

import static org.junit.Assert.fail;
import it.eurotn.panjea.anagrafica.TestAnagrafica;
import it.eurotn.panjea.contabilita.service.interfaces.ComunicazionePolivalenteService;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente.TipologiaDati;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente.TipologiaInvio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class TestSpesometro {
	private ComunicazionePolivalenteService comunicazionePolivalenteService;
	private InitialContext ic;

	/**
	 * init test.
	 * 
	 * @throws Exception .
	 */
	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config",
				TestAnagrafica.class.getClassLoader().getResource("auth.conf").getPath());

		String username = "internalAdmin#dolcelit#Panjea#it";
		String credential = "internalEuropaSw";
		char[] password = credential.toCharArray();
		BasicConfigurator.configure();
		FileInputStream is = new FileInputStream(TestSpesometro.class.getClassLoader().getResource("log4j.properties")
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
		comunicazionePolivalenteService = (ComunicazionePolivalenteService) ic
				.lookup("Panjea.ComunicazionePolivalenteService");
	}

	/**
	 * test esportazione spesometro.
	 */
	@Test
	public void testSpesometroAggregato() {
		ParametriCreazioneComPolivalente parametriCreazione = new ParametriCreazioneComPolivalente();
		parametriCreazione.setAnnoRiferimento(2012);
		parametriCreazione.setTipologiaInvio(TipologiaInvio.INVIO_ORDINARIO);
		parametriCreazione.setTipologiaDati(TipologiaDati.AGGREGATI);
		byte[] spesometro = null;
		spesometro = comunicazionePolivalenteService.genera(parametriCreazione);
		System.out.println(spesometro);
		Assert.assertNotNull(spesometro);

		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(new File("/tmp/spesometroAggregato.txt"));
			fileOutputStream.write(spesometro);
		} catch (Exception e) {

		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * test esportazione spesometro.
	 */
	@Test
	public void testSpesometroAnalitico() {
		ParametriCreazioneComPolivalente parametriCreazione = new ParametriCreazioneComPolivalente();
		parametriCreazione.setAnnoRiferimento(2012);
		parametriCreazione.setTipologiaInvio(TipologiaInvio.INVIO_ORDINARIO);
		parametriCreazione.setTipologiaDati(TipologiaDati.ANALITICI);
		byte[] spesometro = null;
		spesometro = comunicazionePolivalenteService.genera(parametriCreazione);
		System.out.println(spesometro);
		Assert.assertNotNull(spesometro);

		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(new File("/tmp/spesometroAnalitico.txt"));
			fileOutputStream.write(spesometro);
		} catch (Exception e) {

		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
