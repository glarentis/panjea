package it.eurotn.panjea.preventivi.test;

import static org.junit.Assert.fail;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClassePreventivo;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.documenti.service.interfaces.DocumentiService;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoAnagraficaService;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;
import it.eurotn.panjea.preventivi.domain.RigaNota;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.RigaTestata;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.preventivi.service.interfaces.PreventiviDocumentoService;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.preventivi.util.AreaPreventivoRicerca;
import it.eurotn.panjea.preventivi.util.RigaArticoloDTO;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaAreaPreventivo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPreventiviDocumentoService {

	private PreventiviDocumentoService preventiviService;
	private DocumentiService documentiService;
	private AnagraficaService anagraficaService;
	private MagazzinoAnagraficaService magazzinoService;

	private static final String AZIENDA = "dolcelit";
	private static final int MIN_NUM_DOC = 1000000;

	private TipoAreaPreventivo test1;

	/**
	 * 
	 * @param id
	 *            id area preventivo da cancellare
	 */
	private void cancellaTipoAreaPreventivo(Integer id) {
		TipoAreaPreventivo tipoAreaPreventivo = new TipoAreaPreventivo();
		tipoAreaPreventivo.setId(id);
		preventiviService.cancellaTipoAreaPreventivo(tipoAreaPreventivo);
	}

	/**
	 * 
	 * @param id
	 *            da cancellare
	 * @throws AnagraficaServiceException
	 *             eccezione
	 */
	private void cancellaTipoDocumento(Integer id) throws AnagraficaServiceException {
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setId(id);
		documentiService.cancellaTipoDocumento(tipoDocumento);
	}

	/**
	 * 
	 * @return area preventivo nuova
	 * @throws AnagraficaServiceException
	 *             eccezione
	 * @throws DocumentoDuplicateException
	 *             eccezione
	 */
	private AreaPreventivo creaAreaPreventivo() throws AnagraficaServiceException, DocumentoDuplicateException {
		Documento documento = creaDocumentoPreventivo();
		AreaPreventivo areaPreventivo = new AreaPreventivo();
		areaPreventivo.setDocumento(documento);
		areaPreventivo.setAnnoMovimento(2014);
		areaPreventivo.setDataRegistrazione(documento.getDataDocumento());
		areaPreventivo.setTipoAreaPreventivo(test1);
		areaPreventivo.getTotaliArea().getSpeseTrasporto().setCodiceValuta("EUR");
		areaPreventivo.getTotaliArea().getAltreSpese().setCodiceValuta("EUR");
		areaPreventivo.getTotaliArea().getTotaleMerce().setCodiceValuta("EUR");

		AreaPreventivoFullDTO fulldto = preventiviService.salvaAreaPreventivo(areaPreventivo, null);
		return fulldto.getAreaPreventivo();
	}

	/**
	 * 
	 * @return documento nuovo
	 * @throws AnagraficaServiceException
	 *             eccezione
	 * @throws DocumentoDuplicateExceptio
	 *             eccezione
	 */
	private Documento creaDocumentoPreventivo() throws AnagraficaServiceException, DocumentoDuplicateException {
		Documento documento = new Documento();
		Cliente cliente = getCliente();
		Integer codice = new Random().nextInt(100000) + MIN_NUM_DOC;
		documento.getCodice().setCodice(codice.toString());
		documento.setCodiceAzienda(AZIENDA);
		documento.setEntita(getCliente().getEntitaLite());
		documento.setSedeEntita(cliente.getSedi().iterator().next());
		documento.setTipoDocumento(test1.getTipoDocumento());
		documento.setDataDocumento(getToday());
		documento.getTotale().setCodiceValuta("EUR");
		return documentiService.salvaDocumento(documento);
	}

	/**
	 * 
	 * @param codice
	 *            codice
	 * @return tipo area nuovo
	 * @throws AnagraficaServiceException
	 *             eccezione
	 * @throws ModificaTipoAreaConDocumentoException
	 *             eccezione
	 */
	private TipoAreaPreventivo creaTipoAreaPreventivo(String codice) throws AnagraficaServiceException,
			ModificaTipoAreaConDocumentoException {
		TipoDocumento tipoDocumento = creaTipoDocumento(codice);
		return creaTipoAreaPreventivo(tipoDocumento);
	}

	/**
	 * 
	 * @param tipoDocumento
	 *            tipo documento
	 * @return tipo area nuovo per il tipo documento
	 */
	private TipoAreaPreventivo creaTipoAreaPreventivo(TipoDocumento tipoDocumento) {
		TipoAreaPreventivo tipoAreaPreventivo = new TipoAreaPreventivo();
		tipoAreaPreventivo.setTipoDocumento(tipoDocumento);
		tipoAreaPreventivo.setDescrizionePerStampa(tipoDocumento.getCodice());
		return preventiviService.salvaTipoAreaPreventivo(tipoAreaPreventivo);
	}

	/**
	 * 
	 * @param codice
	 *            codice del tipo documento nuovo
	 * @return tipo documento nuovo con il codice
	 * @throws AnagraficaServiceException
	 *             eccezione
	 * @throws ModificaTipoAreaConDocumentoException
	 *             eccezione
	 */
	private TipoDocumento creaTipoDocumento(String codice) throws AnagraficaServiceException,
			ModificaTipoAreaConDocumentoException {
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setAbilitato(true);
		tipoDocumento.setCodice(codice);
		tipoDocumento.setDescrizione(codice);
		tipoDocumento.setTipoEntita(TipoEntita.CLIENTE);
		tipoDocumento.setClasseTipoDocumentoInstance(new ClassePreventivo());
		tipoDocumento.setClasseTipoDocumento(ClassePreventivo.class.getName());
		return documentiService.salvaTipoDocumento(tipoDocumento);
	}

	/**
	 * 
	 * @return articolo (già presente nel db)
	 */
	private ArticoloLite getArticolo() {
		return magazzinoService.caricaArticoloLite(8367);
	}

	/**
	 * 
	 * @return articolo (già presente nel db)
	 */
	private Articolo getArticoloCompleto() {
		Articolo articolo = new Articolo();
		articolo.setId(8367);
		articolo = magazzinoService.caricaArticolo(articolo, true);
		return articolo;
	}

	/**
	 * 
	 * @return cliente (giù presente nel db)
	 * @throws AnagraficaServiceException
	 *             eccezione
	 */
	private Cliente getCliente() throws AnagraficaServiceException {
		ClienteLite entita = new ClienteLite();
		entita.setId(5011);
		return (Cliente) anagraficaService.caricaEntita(entita, false);
	}

	/**
	 * 
	 * @param areaPreventivo
	 *            area preventivo a cui la riga nuova va collegata
	 * @return nuova riga articolo collegata all'area preventivo
	 */
	RigaArticolo getRigaArticolo(AreaPreventivo areaPreventivo) {
		ArticoloLite articolo = getArticolo();
		RigaArticolo rigaArticolo = new RigaArticolo();
		rigaArticolo.setAreaPreventivo(areaPreventivo);
		rigaArticolo.setArticolo(articolo);
		rigaArticolo.setQta(100.0);
		return rigaArticolo;
	}

	/**
	 * 
	 * @param areaPreventivo
	 *            area preventivo a cui la riga nuova va collegata
	 * @return nuova riga note collegata all'area preventivo
	 */
	RigaNota getRigaNota(AreaPreventivo areaPreventivo) {
		RigaNota rigaNota = new RigaNota();
		rigaNota.setAreaPreventivo(areaPreventivo);
		rigaNota.setNota("Riga nota");
		return rigaNota;
	}

	/**
	 * 
	 * @param areaPreventivo
	 *            area preventivo a cui la riga nuova va collegata
	 * @return nuova riga testata collegata all'area preventivo
	 */
	RigaTestata getRigaTestata(AreaPreventivo areaPreventivo) {
		RigaTestata rigaTestata = new RigaTestata();
		rigaTestata.setAreaPreventivo(areaPreventivo);
		rigaTestata.setDescrizione("Riga testata");
		return rigaTestata;
	}

	/**
	 * 
	 * @return oggi
	 */
	private Date getToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 
	 * @throws Exception
	 *             exception
	 */
	@Before
	public void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config", TestPreventiviDocumentoService.class.getClassLoader()
				.getResource("auth.conf").getPath());

		String username = "europa#" + AZIENDA + "#Panjea#it";
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
		preventiviService = (PreventiviDocumentoService) ic.lookup("Panjea.PreventiviDocumentoService");
		documentiService = (DocumentiService) ic.lookup("Panjea.DocumentiService");
		anagraficaService = (AnagraficaService) ic.lookup("Panjea.AnagraficaService");
		magazzinoService = (MagazzinoAnagraficaService) ic.lookup("Panjea.MagazzinoAnagraficaService");

		try {
			test1 = creaTipoAreaPreventivo("TEST1");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @throws Exception
	 *             exception
	 */
	@After
	public void tearDown() throws Exception {
		ParametriRicercaAreaPreventivo parametri = new ParametriRicercaAreaPreventivo();
		parametri.getNumeroDocumentoIniziale().setCodice(new Integer(MIN_NUM_DOC).toString());
		List<AreaPreventivoRicerca> documentiTrovati = preventiviService.ricercaAreePreventivo(parametri);
		for (AreaPreventivoRicerca doc : documentiTrovati) {
			AreaPreventivo areaPreventivo = new AreaPreventivo();
			areaPreventivo.setId(doc.getIdAreaDocumento());
			areaPreventivo.setDocumento(doc.getDocumento());
			try {
				preventiviService.cancellaAreaPreventivo(areaPreventivo);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		List<TipoAreaPreventivo> tipiAReePreventivo = preventiviService.caricaTipiAreaPreventivo(null, null, true);
		for (TipoAreaPreventivo tipo : tipiAReePreventivo) {
			if (tipo.getTipoDocumento().getCodice().startsWith("TEST")) {
				cancellaTipoAreaPreventivo(tipo.getId());
				cancellaTipoDocumento(tipo.getTipoDocumento().getId());
			}
		}

		List<TipoDocumento> tipiDocumentoPreventivo = preventiviService.caricaTipiDocumentiPreventivo();
		for (TipoDocumento tipo : tipiDocumentoPreventivo) {
			if (tipo.getCodice().startsWith("TEST")) {
				cancellaTipoDocumento(tipo.getId());
			}
		}
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testCancellaAreaPreventivoConRighe() throws AnagraficaServiceException, DocumentoDuplicateException {

		AreaPreventivo areaPreventivo = creaAreaPreventivo();
		RigaArticolo rigaArticolo = getRigaArticolo(areaPreventivo);
		rigaArticolo = (RigaArticolo) preventiviService.salvaRigaPreventivo(rigaArticolo, false);

		RigaTestata rigaTestata = getRigaTestata(areaPreventivo);
		rigaTestata = (RigaTestata) preventiviService.salvaRigaPreventivo(rigaTestata, false);

		RigaNota rigaNota = getRigaNota(areaPreventivo);
		rigaNota = (RigaNota) preventiviService.salvaRigaPreventivo(rigaNota, false);

		preventiviService.cancellaAreaPreventivo(areaPreventivo);
		List<RigaPreventivo> righe = preventiviService.caricaRighePreventivo(areaPreventivo);
		Assert.assertNotNull(righe);
		Assert.assertTrue(righe.isEmpty());
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testCancellaAreePreventivo() throws AnagraficaServiceException, DocumentoDuplicateException {
		AreaPreventivo areaPreventivo1 = creaAreaPreventivo();
		AreaPreventivo areaPreventivo2 = creaAreaPreventivo();
		AreaPreventivo areaPreventivo3 = creaAreaPreventivo();
		List<AreaPreventivo> lista = new ArrayList<AreaPreventivo>();
		lista.add(areaPreventivo1);
		lista.add(areaPreventivo2);
		lista.add(areaPreventivo3);
		preventiviService.cancellaAreePreventivo(lista);

		for (AreaPreventivo area : lista) {
			try {
				preventiviService.caricaAreaPreventivo(area);
				Assert.fail("Dovrebbe dare ObjectNotFoundException");
			} catch (RuntimeException ex) {
				if (!(ex.getCause().getCause() instanceof ObjectNotFoundException)) {
					throw ex;
				}
			}
		}
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testCancellaRigaPreventivo() throws AnagraficaServiceException, DocumentoDuplicateException {
		AreaPreventivo areaPreventivo = creaAreaPreventivo();
		RigaArticolo rigaArticolo = getRigaArticolo(areaPreventivo);
		rigaArticolo = (RigaArticolo) preventiviService.salvaRigaPreventivo(rigaArticolo, false);

		RigaTestata rigaTestata = getRigaTestata(areaPreventivo);
		rigaTestata = (RigaTestata) preventiviService.salvaRigaPreventivo(rigaTestata, false);

		RigaNota rigaNota = getRigaNota(areaPreventivo);
		rigaNota = (RigaNota) preventiviService.salvaRigaPreventivo(rigaNota, false);

		AreaPreventivo areaPreventivoRestituitoDaCancellazione = preventiviService.cancellaRigaPreventivo(rigaTestata);
		Assert.assertEquals(areaPreventivo, areaPreventivoRestituitoDaCancellazione);
		List<RigaPreventivo> righe = preventiviService.caricaRighePreventivo(areaPreventivo);
		Assert.assertNotNull(righe);
		Assert.assertEquals(2, righe.size());

		areaPreventivoRestituitoDaCancellazione = preventiviService.cancellaRigaPreventivo(rigaNota);
		Assert.assertEquals(areaPreventivo, areaPreventivoRestituitoDaCancellazione);
		righe = preventiviService.caricaRighePreventivo(areaPreventivo);
		Assert.assertNotNull(righe);
		Assert.assertEquals(1, righe.size());

		areaPreventivoRestituitoDaCancellazione = preventiviService.cancellaRigaPreventivo(rigaArticolo);
		Assert.assertEquals(areaPreventivo, areaPreventivoRestituitoDaCancellazione);
		righe = preventiviService.caricaRighePreventivo(areaPreventivo);
		Assert.assertNotNull(righe);
		Assert.assertEquals(0, righe.size());
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws ModificaTipoAreaConDocumentoException
	 *             ex
	 */
	@Test
	public void testCancellaTipoAreaPreventivo() throws AnagraficaServiceException,
			ModificaTipoAreaConDocumentoException {
		List<TipoDocumento> tipi = preventiviService.caricaTipiDocumentiPreventivo();
		TipoAreaPreventivo tipoAreaPreventivo = creaTipoAreaPreventivo("TEST002");
		cancellaTipoAreaPreventivo(tipoAreaPreventivo.getId());
		List<TipoDocumento> tipi2 = preventiviService.caricaTipiDocumentiPreventivo();
		Assert.assertEquals(tipi.size(), tipi2.size());
		Assert.assertEquals(tipi, tipi2);

		cancellaTipoDocumento(tipoAreaPreventivo.getTipoDocumento().getId());
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testCaricaRigaPreventivo() throws AnagraficaServiceException, DocumentoDuplicateException {
		AreaPreventivo areaPreventivo = creaAreaPreventivo();
		RigaArticolo rigaArticolo = getRigaArticolo(areaPreventivo);
		rigaArticolo = (RigaArticolo) preventiviService.salvaRigaPreventivo(rigaArticolo, false);

		RigaArticolo rigaArticoloCaricata = (RigaArticolo) preventiviService.caricaRigaPreventivo(rigaArticolo);
		Assert.assertNotNull(rigaArticoloCaricata);
		Assert.assertEquals(rigaArticolo, rigaArticoloCaricata);
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testCaricaRighePreventivo() throws AnagraficaServiceException, DocumentoDuplicateException {
		AreaPreventivo areaPreventivo = creaAreaPreventivo();

		RigaArticolo rigaArticolo = getRigaArticolo(areaPreventivo);
		rigaArticolo = (RigaArticolo) preventiviService.salvaRigaPreventivo(rigaArticolo, false);

		RigaTestata rigaTestata = getRigaTestata(areaPreventivo);
		rigaTestata = (RigaTestata) preventiviService.salvaRigaPreventivo(rigaTestata, false);

		RigaNota rigaNota = getRigaNota(areaPreventivo);
		rigaNota = (RigaNota) preventiviService.salvaRigaPreventivo(rigaNota, false);

		List<RigaPreventivo> righe = preventiviService.caricaRighePreventivo(areaPreventivo);
		Assert.assertNotNull(righe);
		Assert.assertEquals(3, righe.size());
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testCaricaRighePreventivoDTO() throws AnagraficaServiceException, DocumentoDuplicateException {
		AreaPreventivo areaPreventivo = creaAreaPreventivo();

		RigaTestata rigaTestata = getRigaTestata(areaPreventivo);
		rigaTestata.setOrdinamento(1.0);
		rigaTestata = (RigaTestata) preventiviService.salvaRigaPreventivo(rigaTestata, false);

		RigaArticolo rigaArticolo = getRigaArticolo(areaPreventivo);
		rigaArticolo.setOrdinamento(2.0);
		rigaArticolo = (RigaArticolo) preventiviService.salvaRigaPreventivo(rigaArticolo, false);

		RigaNota rigaNota = getRigaNota(areaPreventivo);
		rigaNota.setOrdinamento(3.0);
		rigaNota = (RigaNota) preventiviService.salvaRigaPreventivo(rigaNota, false);

		List<RigaPreventivoDTO> righeDTO = preventiviService.caricaRighePreventivoDTO(areaPreventivo);
		Assert.assertNotNull(righeDTO);
		Assert.assertEquals(3, righeDTO.size());
		Assert.assertEquals(rigaTestata.getId(), righeDTO.get(0).getId());
		Assert.assertEquals(rigaArticolo.getId(), righeDTO.get(1).getId());
		Assert.assertEquals(rigaNota.getId(), righeDTO.get(2).getId());
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws ModificaTipoAreaConDocumentoException
	 *             ex
	 */
	@Test
	public void testCaricaTipiAreaPreventivo() throws AnagraficaServiceException, ModificaTipoAreaConDocumentoException {
		List<TipoAreaPreventivo> tipi = preventiviService.caricaTipiAreaPreventivo(null, null, true);
		Assert.assertNotNull(tipi);
		Assert.assertEquals(1, tipi.size());

		TipoAreaPreventivo nuovo = creaTipoAreaPreventivo("TEST004");
		tipi = preventiviService.caricaTipiAreaPreventivo(null, null, true);
		Assert.assertEquals(tipi.size(), 2);

		cancellaTipoAreaPreventivo(nuovo.getId());
		cancellaTipoDocumento(nuovo.getTipoDocumento().getId());
		tipi = preventiviService.caricaTipiAreaPreventivo(null, null, true);
		Assert.assertEquals(tipi.size(), 1);
	}

	/**
	 * 
	 */
	@Test
	public void testCaricaTipiDocumentiPreventivo() {
		List<TipoDocumento> tipi = preventiviService.caricaTipiDocumentiPreventivo();
		Assert.assertNotNull(tipi);
		Assert.assertFalse(tipi.isEmpty());
		TipoDocumento tipo1 = tipi.get(0);
		Assert.assertEquals(test1.getTipoDocumento(), tipo1);
	}

	/**
	 * 
	 */
	@Test
	public void testCaricaTipoAreaPreventivo() {
		TipoAreaPreventivo tipoAreaPreventivo = preventiviService.caricaTipoAreaPreventivo(test1.getId());
		Assert.assertNotNull(tipoAreaPreventivo);
		Assert.assertEquals(tipoAreaPreventivo.getTipoDocumento().getCodice(), "TEST1");
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testCercaDocumentoPreventivo() throws AnagraficaServiceException, DocumentoDuplicateException {
		AreaPreventivoRicerca areaPreventivoTrovata = null;
		AreaPreventivo areaPreventivo = creaAreaPreventivo();
		Documento documento = areaPreventivo.getDocumento();
		ParametriRicercaAreaPreventivo parametri = new ParametriRicercaAreaPreventivo();
		parametri.setNumeroDocumentoIniziale(documento.getCodice());
		parametri.setNumeroDocumentoFinale(documento.getCodice());
		List<AreaPreventivoRicerca> documentiTrovati = preventiviService.ricercaAreePreventivo(parametri);
		Assert.assertNotNull(documentiTrovati);
		Assert.assertEquals(documentiTrovati.size(), 1);
		areaPreventivoTrovata = documentiTrovati.iterator().next();
		Assert.assertEquals(documento, areaPreventivoTrovata.getDocumento());
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testCreaAreaPreventivoConRigaArticolo() throws AnagraficaServiceException, DocumentoDuplicateException {
		AreaPreventivo areaPreventivo = creaAreaPreventivo();
		ArticoloLite articolo = getArticolo();

		RigaArticolo rigaArticolo = new RigaArticolo();
		rigaArticolo.setAreaPreventivo(areaPreventivo);
		rigaArticolo.setArticolo(articolo);
		rigaArticolo.setQta(100.0);

		rigaArticolo = (RigaArticolo) preventiviService.salvaRigaPreventivo(rigaArticolo, false);
		Assert.assertNotNull(rigaArticolo);
		Assert.assertNotNull(rigaArticolo.getId());
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testCreaAreaPreventivoConRigaNota() throws AnagraficaServiceException, DocumentoDuplicateException {
		AreaPreventivo areaPreventivo = creaAreaPreventivo();

		RigaNota rigaNota = new RigaNota();
		rigaNota.setAreaPreventivo(areaPreventivo);
		rigaNota.setNota("Riga nota");

		rigaNota = (RigaNota) preventiviService.salvaRigaPreventivo(rigaNota, false);
		Assert.assertNotNull(rigaNota);
		Assert.assertNotNull(rigaNota.getId());
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testCreaAreaPreventivoConRigaTestata() throws AnagraficaServiceException, DocumentoDuplicateException {
		AreaPreventivo areaPreventivo = creaAreaPreventivo();
		RigaTestata rigaTestata = new RigaTestata();
		rigaTestata.setAreaPreventivo(areaPreventivo);
		rigaTestata.setDescrizione("Riga testata");

		rigaTestata = (RigaTestata) preventiviService.salvaRigaPreventivo(rigaTestata, false);
		Assert.assertNotNull(rigaTestata);
		Assert.assertNotNull(rigaTestata.getId());
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testCreaDocumentoPreventivo() throws AnagraficaServiceException, DocumentoDuplicateException {
		Documento documento = creaDocumentoPreventivo();
		Assert.assertNotNull(documento);
		Assert.assertNotNull(documento.getId());
		documentiService.cancellaDocumento(documento);
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testInserisciRaggruppamentoArticoli() throws AnagraficaServiceException, DocumentoDuplicateException {
		RaggruppamentoArticoli raggruppamentoArticoli = new RaggruppamentoArticoli();
		raggruppamentoArticoli.setId(1);
		raggruppamentoArticoli = magazzinoService.caricaRaggruppamentoArticoli(raggruppamentoArticoli);
		Assert.assertNotNull(raggruppamentoArticoli);
		AreaPreventivo areaPreventivo = creaAreaPreventivo();
		preventiviService.inserisciRaggruppamentoArticoli(areaPreventivo.getId(), ProvenienzaPrezzo.LISTINO,
				raggruppamentoArticoli.getId(), getToday(), areaPreventivo.getDocumento().getSedeEntita().getId(),
				null, null, new Importo("EUR", new BigDecimal(1)), null, null, null, false, "EUR", "IT", null, null,
				BigDecimal.ZERO);

		List<RigaPreventivoDTO> righeDTO = preventiviService.caricaRighePreventivoDTO(areaPreventivo);
		Assert.assertNotNull(righeDTO);
		Assert.assertEquals(3, righeDTO.size());

		RigaArticoloDTO riga1 = (RigaArticoloDTO) righeDTO.get(0);
		RigaArticoloDTO riga2 = (RigaArticoloDTO) righeDTO.get(1);
		RigaArticoloDTO riga3 = (RigaArticoloDTO) righeDTO.get(2);

		Assert.assertEquals(new Double(100.0), riga1.getQta());
		Assert.assertEquals(new Double(100.0), riga2.getQta());
		Assert.assertEquals(new Double(100.0), riga3.getQta());

		Set<String> codiciArticolo = new HashSet<String>();
		codiciArticolo.add(riga1.getArticolo().getCodice());
		codiciArticolo.add(riga2.getArticolo().getCodice());
		codiciArticolo.add(riga3.getArticolo().getCodice());
		Assert.assertTrue(codiciArticolo.contains("0001"));
		Assert.assertTrue(codiciArticolo.contains("0002"));
		Assert.assertTrue(codiciArticolo.contains("0003"));
	}

	/**
	 * 
	 * @throws Exception
	 *             ex
	 */
	@Test
	public void testSalvaTipoAreaPreventivo() throws Exception {
		TipoDocumento tipoDocumento = creaTipoDocumento("TEST001");
		TipoAreaPreventivo tipoAreaPreventivo = creaTipoAreaPreventivo(tipoDocumento);
		Assert.assertNotNull(tipoDocumento.getId());
		Assert.assertNotNull(tipoAreaPreventivo.getId());
		Assert.assertEquals(tipoDocumento, tipoAreaPreventivo.getTipoDocumento());
		List<TipoDocumento> tipi = preventiviService.caricaTipiDocumentiPreventivo();
		Assert.assertEquals(2, tipi.size());
		Assert.assertEquals("TEST001", tipi.get(1).getCodice());
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testSetPrezzoUnitarioRiga() throws AnagraficaServiceException, DocumentoDuplicateException {
		AreaPreventivo areaPreventivo = creaAreaPreventivo();
		RigaArticolo rigaArticolo = getRigaArticolo(areaPreventivo);
		rigaArticolo = (RigaArticolo) preventiviService.salvaRigaPreventivo(rigaArticolo, false);
		rigaArticolo.setQta(100.0);

		Importo prezzo = new Importo("EUR", new BigDecimal(1));
		prezzo.setImportoInValuta(new BigDecimal(3));
		rigaArticolo.setPrezzoUnitario(prezzo);

		BigDecimal totaleRigaAtteso = new BigDecimal(300);
		totaleRigaAtteso = totaleRigaAtteso.setScale(2);
		Assert.assertEquals(totaleRigaAtteso, rigaArticolo.getPrezzoTotale().getImportoInValuta());
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testTotalizzaDocumento() throws AnagraficaServiceException, DocumentoDuplicateException {
		AreaPreventivo areaPreventivo = creaAreaPreventivo();
		RigaArticolo rigaArticolo1 = getRigaArticolo(areaPreventivo);
		rigaArticolo1.setQta(100.0);
		rigaArticolo1.setCodiceIva(getArticoloCompleto().getCodiceIva());
		Importo prezzo = new Importo("EUR", new BigDecimal(1));
		prezzo.setImportoInValuta(new BigDecimal(3));
		rigaArticolo1.setPrezzoUnitario(prezzo);
		rigaArticolo1 = (RigaArticolo) preventiviService.salvaRigaPreventivo(rigaArticolo1, false);

		AreaPreventivo areaPreventivoTotalizzata = preventiviService.totalizzaDocumento(areaPreventivo, null);
		Assert.assertEquals(areaPreventivo, areaPreventivoTotalizzata);

		BigDecimal totaleMerceAtteso = new BigDecimal(300);
		Assert.assertTrue(totaleMerceAtteso.compareTo(areaPreventivoTotalizzata.getTotaliArea().getTotaleMerce()
				.getImportoInValuta()) == 0);

		BigDecimal totaleDocumentoAtteso = new BigDecimal(330);
		Assert.assertTrue(totaleDocumentoAtteso.compareTo(areaPreventivoTotalizzata.getDocumento().getTotale()
				.getImportoInValuta()) == 0);
	}

	/**
	 * 
	 * @throws AnagraficaServiceException
	 *             ex
	 * @throws DocumentoDuplicateException
	 *             ex
	 */
	@Test
	public void testValidaRighe() throws AnagraficaServiceException, DocumentoDuplicateException {
		AreaPreventivo areaPreventivo = creaAreaPreventivo();
		RigaArticolo rigaArticolo = getRigaArticolo(areaPreventivo);
		preventiviService.salvaRigaPreventivo(rigaArticolo, false);

		areaPreventivo = preventiviService.caricaAreaPreventivo(areaPreventivo);
		AreaPreventivoFullDTO areaValidata = preventiviService.validaRighePreventivo(areaPreventivo, null, true);
		Assert.assertNotNull(areaValidata);
		Assert.assertEquals(StatoAreaPreventivo.IN_ATTESA, areaValidata.getAreaPreventivo().getStatoAreaPreventivo());
	}
}
