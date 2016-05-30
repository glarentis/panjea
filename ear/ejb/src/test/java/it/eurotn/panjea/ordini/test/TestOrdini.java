package it.eurotn.panjea.ordini.test;

import static org.junit.Assert.fail;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.lotti.exception.LottiException;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.ordini.domain.OrdineImportato.EProvenienza;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.exception.CodicePagamentoAssenteException;
import it.eurotn.panjea.ordini.exception.CodicePagamentoEvasioneAssenteException;
import it.eurotn.panjea.ordini.exception.EntitaSenzaTipoDocumentoEvasioneException;
import it.eurotn.panjea.ordini.exception.TipoAreaPartitaDestinazioneRichiestaException;
import it.eurotn.panjea.ordini.service.interfaces.OrdiniDocumentoService;
import it.eurotn.panjea.ordini.util.ParametriRicercaProduzione;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Before;
import org.junit.Test;

public class TestOrdini {

	private OrdiniDocumentoService ordiniService;
	private MagazzinoDocumentoService magazzinoDocumentoService;

	@Test
	public void caricaOrdiniImportati() {
		ParametriRicercaOrdiniImportati parametri = new ParametriRicercaOrdiniImportati();
		parametri.setProvenienza(EProvenienza.TUTTI);
		List<RigaOrdineImportata> ordini = ordiniService.caricaRigheOrdineImportate(parametri);
		System.out.println("righe importate " + ordini.size());
	}

	@Test
	public void generaCaricoProduzione() throws EntitaSenzaTipoDocumentoEvasioneException, ContabilizzazioneException,
			ContiBaseException, TipoAreaPartitaDestinazioneRichiestaException, CodicePagamentoEvasioneAssenteException,
			CodicePagamentoAssenteException, LottiException {
		ParametriRicercaProduzione parametri = new ParametriRicercaProduzione();
		parametri.setEffettuaRicerca(true);
		List<? extends RigaDistintaCarico> result = ordiniService.caricaRigheEvasioneProduzione(parametri);
		List<RigaDistintaCarico> righeDaEvadere = new ArrayList<RigaDistintaCarico>();
		TipoAreaMagazzino tam = new TipoAreaMagazzino();
		tam.setId(36);
		for (RigaDistintaCarico rigaDistintaCaricoProduzione : result) {
			rigaDistintaCaricoProduzione.setQtaEvasa(rigaDistintaCaricoProduzione.getQtaOrdinata());
			rigaDistintaCaricoProduzione.getDatiEvasioneDocumento().setContoTerzi(false);
			rigaDistintaCaricoProduzione.getDatiEvasioneDocumento().setTipoAreaEvasione(tam);
			rigaDistintaCaricoProduzione.getRigaArticolo().getAreaOrdine().getDepositoOrigine().setVersion(0);
			righeDaEvadere.add(rigaDistintaCaricoProduzione);
		}
		try {
			ordiniService.evadiOrdini(righeDaEvadere, Calendar.getInstance().getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config", TestOrdini.class.getClassLoader()
				.getResource("auth.conf").getPath());

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
		ordiniService = (OrdiniDocumentoService) ic.lookup("Panjea.OrdiniDocumentoService");
		magazzinoDocumentoService = (MagazzinoDocumentoService) ic.lookup("Panjea.MagazzinoDocumentoService");
	}

}
