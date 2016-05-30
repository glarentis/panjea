package it.eurotn.panjea.lotti.test;

import static org.junit.Assert.fail;
import it.eurotn.dao.exception.DuplicateKeyObjectException;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.manager.rimanenzefinali.RimanenzeFinaliDTO;
import it.eurotn.panjea.lotti.service.interfaces.LottiService;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Before;
import org.junit.Test;

public class TestLotti {

	private static Logger logger = Logger.getLogger(TestLotti.class);

	private LottiService lottiService;
	private MagazzinoDocumentoService magazzinoDocumentoService;

	@Test
	public void cancellaRigaArticoloConLotti() {

		RigaArticolo rigaArticolo = new RigaArticolo();
		rigaArticolo.setId(594650);
		magazzinoDocumentoService.cancellaRigaMagazzino(rigaArticolo);
	}

	@Test
	public void modificaQuantitaRigaLotto() {

		RigaArticolo rigaArticolo = new RigaArticolo();
		rigaArticolo.setId(594649);
		rigaArticolo = (RigaArticolo) magazzinoDocumentoService.caricaRigaMagazzino(rigaArticolo);

		rigaArticolo.getRigheLotto().iterator().next().setQuantita(19.0);

		try {
			rigaArticolo = (RigaArticolo) magazzinoDocumentoService.salvaRigaMagazzino(rigaArticolo);
		} catch (Exception e) {
			Assert.fail("Riga articolo non salvata.");
		}
	}

	/**
	 * Salva 2 lotti con lo stesso indice univoco ( azienda - codice - articolo).
	 */
	@Test
	public void salvaLottiConIndiceUguale() {

		salvaLotto();

		try {
			salvaLotto();
		} catch (RuntimeException e) {

			if (e.getCause().getCause() instanceof DuplicateKeyObjectException) {
				Assert.assertTrue(true);
			} else {
				Assert.fail("E' stato possibile salvare 2 lotti con indice univoco uguale.");
			}
		}
	}

	/**
	 * Salva un lotto.
	 */
	@Test
	public void salvaLotto() {

		Lotto lotto = new Lotto();
		lotto.setCodice("Lotto 1");

		ArticoloLite articolo = new ArticoloLite();
		articolo.setId(417);
		articolo.setVersion(0);
		lotto.setArticolo(articolo);

		Lotto lottoSalvato = null;
		lottoSalvato = lottiService.salvaLotto(lotto);

		Assert.assertNotNull("Il lotto salvato è nullo.", lottoSalvato);
		Assert.assertNotNull("L'id del lotto salvato è nullo", lottoSalvato.getId());
	}

	@Test
	public void salvaRigaArticoloConLotti() {

		AreaMagazzino areaMagazzino = new AreaMagazzino();
		areaMagazzino.setId(15000);
		areaMagazzino.setVersion(0);

		RigaArticolo rigaArticolo = (RigaArticolo) magazzinoDocumentoService.caricaRigheMagazzino(areaMagazzino).get(0);

		rigaArticolo.setId(null);
		rigaArticolo.setVersion(null);

		Lotto lotto = new Lotto();
		lotto.setCodice("Lotto 1");

		ArticoloLite articolo = new ArticoloLite();
		articolo.setId(418);
		articolo.setVersion(0);
		lotto.setArticolo(articolo);

		Lotto lottoSalvato = null;
		lottoSalvato = lottiService.salvaLotto(lotto);

		RigaLotto rigaLotto1 = new RigaLotto();
		rigaLotto1.setLotto(lottoSalvato);
		rigaLotto1.setQuantita(20.0);
		rigaLotto1.setRigaArticolo(rigaArticolo);

		RigaLotto rigaLotto2 = new RigaLotto();
		rigaLotto2.setLotto(lottoSalvato);
		rigaLotto2.setQuantita(20.0);
		rigaLotto2.setRigaArticolo(rigaArticolo);

		RigaLotto rigaLotto3 = new RigaLotto();
		rigaLotto3.setLotto(lottoSalvato);
		rigaLotto3.setQuantita(20.0);
		rigaLotto3.setRigaArticolo(rigaArticolo);

		RigaLotto rigaLotto4 = new RigaLotto();
		rigaLotto4.setLotto(lottoSalvato);
		rigaLotto4.setQuantita(20.0);
		rigaLotto4.setRigaArticolo(rigaArticolo);

		rigaArticolo.setRigheLotto(new HashSet<RigaLotto>());

		rigaArticolo.getRigheLotto().add(rigaLotto1);
		rigaArticolo.getRigheLotto().add(rigaLotto2);
		rigaArticolo.getRigheLotto().add(rigaLotto3);
		rigaArticolo.getRigheLotto().add(rigaLotto4);

		try {
			rigaArticolo = (RigaArticolo) magazzinoDocumentoService.salvaRigaMagazzino(rigaArticolo);
		} catch (Exception e) {
			Assert.fail("Riga articolo non salvata.");
		}
	}

	@Test
	public void salvaRigaArticoloConLottiSbagliati() {

		AreaMagazzino areaMagazzino = new AreaMagazzino();
		areaMagazzino.setId(1659);
		areaMagazzino.setVersion(3);

		RigaArticolo rigaArticolo = (RigaArticolo) magazzinoDocumentoService.caricaRigheMagazzino(areaMagazzino).get(0);

		rigaArticolo.setId(null);
		rigaArticolo.setVersion(null);
		rigaArticolo.setQta(9999.0);

		Lotto lotto = new Lotto();
		lotto.setCodice("Lotto prova art 999");

		ArticoloLite articolo = new ArticoloLite();
		articolo.setId(9);
		articolo.setVersion(0);
		lotto.setArticolo(articolo);

		Lotto lottoSalvato = null;
		lottoSalvato = lottiService.salvaLotto(lotto);

		RigaLotto rigaLotto1 = new RigaLotto();
		rigaLotto1.setLotto(lottoSalvato);
		rigaLotto1.setQuantita(20.0);
		rigaLotto1.setRigaArticolo(rigaArticolo);

		rigaArticolo.getRigheLotto().add(rigaLotto1);

		try {
			rigaArticolo = (RigaArticolo) magazzinoDocumentoService.salvaRigaMagazzino(rigaArticolo);
			Assert.fail();
		} catch (RuntimeException e) {
			if (e.getCause().getCause() instanceof RimanenzaLottiNonValidaException) {
				Assert.assertTrue(true);
			} else {
				Assert.fail();
			}
		} catch (Exception e) {
			Assert.fail("Riga articolo non salvata. " + e.getMessage());
		}
	}

	/**
	 * @throws Exception
	 *             Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config", TestLotti.class.getClassLoader().getResource("auth.conf")
				.getPath());

		String username = "europa#DOLCELIT#Panjea#it";
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
		lottiService = (LottiService) ic.lookup("Panjea.LottiService");
		magazzinoDocumentoService = (MagazzinoDocumentoService) ic.lookup("Panjea.MagazzinoDocumentoService");
	}

	@Test
	public void testCancellaLottiOrfani() {

		lottiService.cancellaLottiNonUtilizzati();
	}

	@Test
	public void testProva() {

		RigaArticolo rigaArticolo = new RigaArticolo();
		rigaArticolo.setId(13539);
		rigaArticolo.setVersion(0);

		rigaArticolo = (RigaArticolo) magazzinoDocumentoService.caricaRigaMagazzino(rigaArticolo);

		Lotto lotto = new Lotto();
		lotto.setId(1);
		lotto.setVersion(0);
		lotto = lottiService.caricaLotto(lotto);

		RigaLotto rigaLotto = new RigaLotto();
		rigaLotto.setRigaArticolo(rigaArticolo);
		rigaLotto.setLotto(lotto);
		rigaLotto.setQuantita(rigaArticolo.getQta());

		// lottiService.salvaRigaLotto(rigaLotto);

	}

	@Test
	public void testRimanenzeFinali() {

		Map<Object, Object> parametri = new HashMap<Object, Object>();

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		parametri.put("data", dateFormat.format(Calendar.getInstance().getTime()));
		parametri.put("idDeposito", new Integer(1));
		// parametri.put("idFornitore", new Integer(5292));

		List<RimanenzeFinaliDTO> rimanenzeFinali = lottiService.caricaRimanenzeFinali(parametri);

		for (RimanenzeFinaliDTO rimanenzeFinaliDTO : rimanenzeFinali) {
			System.out.println("Fornitore: " + rimanenzeFinaliDTO.getFornitore().getId() + " - "
					+ rimanenzeFinaliDTO.getFornitore().getAnagrafica().getDenominazione());
			System.out.println("Articolo: " + rimanenzeFinaliDTO.getArticolo().getCodice() + " - "
					+ rimanenzeFinaliDTO.getArticolo().getDescrizione());
			System.out.println("Giacenza: " + rimanenzeFinaliDTO.getGiacenza());
			System.out.println("Qta vendita mese: " + rimanenzeFinaliDTO.getQtaScaricoVenditaMese());
			System.out.println("Qta vendita anno: " + rimanenzeFinaliDTO.getQtaScaricoVenditaAnno());
			if (rimanenzeFinaliDTO.getLotto() != null) {
				System.out.println("Lotto: " + rimanenzeFinaliDTO.getLotto().getCodice());
				System.out.println("Rimanenza: " + rimanenzeFinaliDTO.getLotto().getRimanenza());
			}
			System.out.println("-------------------------------------------------------------------------------------");
		}
	}
}
