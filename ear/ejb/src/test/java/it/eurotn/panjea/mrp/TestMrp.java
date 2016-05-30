package it.eurotn.panjea.mrp;

import static org.junit.Assert.fail;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.exception.GenerazioneCodiceException;
import it.eurotn.panjea.magazzino.exception.GenerazioneDescrizioneException;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoAnagraficaService;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoStatisticheService;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione.EModalitaValorizzazione;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriValorizzazioneDistinte;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.domain.RisultatoMRPArticoloBucket;
import it.eurotn.panjea.mrp.service.interfaces.MrpService;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.StringUtils;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Before;
import org.junit.Test;

public class TestMrp {
	private final String codiceAzienda = "zorzi";
	private MrpService mrpService;
	private MagazzinoStatisticheService magazzinoStatisticheService;
	private MagazzinoAnagraficaService magazzinoAnagraficaService;

	private void generaDistinte() {

	}

	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config", TestMrp.class.getClassLoader().getResource("auth.conf")
				.getPath());

		String username = "europa#" + codiceAzienda + "#it";
		String credential = "europasw";
		char[] password = credential.toCharArray();
		UsernamePasswordHandler passwordHandler = new UsernamePasswordHandler(username, password);
		LoginContext loginContext;
		try {
			loginContext = new LoginContext("other", passwordHandler);
			loginContext.login();
		} catch (LoginException e) {
			fail(e.getMessage());
		}

		InitialContext ic = new InitialContext();
		mrpService = (MrpService) ic.lookup("Panjea.MrpService");
		magazzinoStatisticheService = (MagazzinoStatisticheService) ic.lookup("Panjea.MagazzinoStatisticheService");
		magazzinoAnagraficaService = (MagazzinoAnagraficaService) ic.lookup("Panjea.MagazzinoAnagraficaService");

	}

	private void stampaBOM(Bom bom, int livello) {
		System.out.println(StringUtils.repeat("-", livello) + livello + bom.getIdArticolo() + " : " + bom.getCosto()
				+ ":" + bom);
		if (bom.getFigli() == null) {
			return;
		}
		for (Bom dto : bom.getFigli()) {
			stampaBOM(dto, livello++);
		}
	}

	@Test
	public void testEsplodiBOM() {
		Map<ArticoloConfigurazioneKey, Bom> result = mrpService.esplodiBoms();
		// for (Bom bom : result.values()) {
		// System.out.println("DEBUG:TestMrp->testEsplodiBOM: " + bom.getIdDistinta() + " " + bom.getPadri().size()
		// + " " + bom.getFigli().size());
		// }
	}

	public void testGeneraDistinte() {

		Articolo[] articoli = new Articolo[100];

		for (int i = 0; i < 100; i++) {
			Articolo articolo = new Articolo();
			articolo.setCodice("DISTINTA " + i);
			articolo.setDescrizione("DESCRIZIONE ");
			try {
				articolo = magazzinoAnagraficaService.salvaArticolo(articolo);
				articoli[i] = articolo;
			} catch (GenerazioneCodiceException | GenerazioneDescrizioneException e) {
				e.printStackTrace();
			}
		}
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			int index = r.nextInt(100);
			if (index != i) {
				Articolo comp = articoli[index];
				Articolo distinta = articoli[i];
				Componente c = new Componente();
				c.setArticolo(comp.getArticoloLite());
				c.setDistinta(distinta.getArticoloLite());
				magazzinoAnagraficaService.salvaComponente(c);
			}
		}
	}

	@Test
	public void testGeneraMrp() {
		Calendar cal = Calendar.getInstance();
		cal.set(2014, 1, 1);
		try {
			mrpService.calcolaMrp(30, cal.getTime(), null);
		} catch (ConnectException e) {
			fail("Errore nella connessione con il server Mrp");
		}
	}

	@Test
	public void testGeneraOrdini() {
		mrpService.generaOrdini(null);
	}

	@Test
	public void testSalvaRisultato() {
		List<RisultatoMRPArticoloBucket> risultati = new ArrayList<RisultatoMRPArticoloBucket>();
		RisultatoMRPArticoloBucket r = new RisultatoMRPArticoloBucket();
		r.setIdArticolo(1);
		risultati.add(r);
		mrpService.salvaRisultatoMRP(risultati);
	}

	@Test
	public void testValorizzaDistinte() {
		ParametriValorizzazioneDistinte parametriValorizzazioneDistinte = new ParametriValorizzazioneDistinte();
		parametriValorizzazioneDistinte.setModalitaValorizzazione(EModalitaValorizzazione.COSTO_STANDARD);
		Map<ArticoloConfigurazioneKey, Bom> result = magazzinoStatisticheService
				.valorizzaDistinte(parametriValorizzazioneDistinte);
		System.out.println("DEBUG:TestMrp->testValorizzaDistinte:mìnum distinte valorizzate " + result.size());
		for (Bom dto : result.values()) {
			stampaBOM(dto, 1);
		}
	}

}
