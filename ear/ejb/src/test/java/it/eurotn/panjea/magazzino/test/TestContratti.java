/**
 *
 */
package it.eurotn.panjea.magazzino.test;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaArticolo;
import it.eurotn.panjea.magazzino.exception.DistintaCircolareException;
import it.eurotn.panjea.magazzino.service.interfaces.ContrattoService;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoAnagraficaService;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoStatisticheService;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.ContrattoProspettoDTO;
import it.eurotn.panjea.magazzino.util.ContrattoStampaDTO;
import it.eurotn.panjea.magazzino.util.IndiceGiacenzaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaContratti;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCalcoloIndiciRotazioneGiacenza;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaStampaContratti;
import it.eurotn.panjea.parametriricerca.domain.AnnoCorrenteStrategy;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.PeriodoStrategyDateCalculator;
import junit.framework.Assert;

/**
 * TestCase per il manager dei Contratti
 *
 * @author adriano
 * @version 1.0, 17/giu/08
 */
public class TestContratti {

    static Logger logger = Logger.getLogger(TestContratti.class);

    /**
     * @uml.property name="magazzinoAnagraficaService"
     * @uml.associationEnd
     */
    private MagazzinoAnagraficaService magazzinoAnagraficaService;

    private MagazzinoDocumentoService magazzinoDocumentoService;

    private MagazzinoStatisticheService magazzinoStatisticheService;

    /**
     * @uml.property name="contrattoService"
     * @uml.associationEnd
     */
    private ContrattoService contrattoService;

    private final String codiceAzienda = "zorzi";

    /**
     * @uml.property name="contratto"
     * @uml.associationEnd
     */
    private Contratto contratto;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        System.setProperty("java.security.auth.login.config",
                TestContratti.class.getClassLoader().getResource("auth.conf").getPath());

        String username = "europa#" + codiceAzienda + "#it";
        String credential = "pnj_adm";
        char[] password = credential.toCharArray();
        BasicConfigurator.configure();
        FileInputStream is = new FileInputStream(
                TestContratti.class.getClassLoader().getResource("log4j.properties").getPath());
        Properties LogProperties = new Properties();
        LogProperties.load(is);
        BasicConfigurator.resetConfiguration();
        PropertyConfigurator.configure(LogProperties);
        UsernamePasswordHandler passwordHandler = new UsernamePasswordHandler(username, password);
        LoginContext loginContext;
        try {
            loginContext = new LoginContext("other", passwordHandler);
            loginContext.login();
        } catch (LoginException e) {
            fail(e.getMessage());
        }

        InitialContext ic = new InitialContext();
        magazzinoAnagraficaService = (MagazzinoAnagraficaService) ic.lookup("Panjea.MagazzinoAnagraficaService");
        contrattoService = (ContrattoService) ic.lookup("Panjea.ContrattoService");
        magazzinoDocumentoService = (MagazzinoDocumentoService) ic.lookup("Panjea.MagazzinoDocumentoService");
        magazzinoStatisticheService = (MagazzinoStatisticheService) ic.lookup("Panjea.MagazzinoStatisticheService");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAggiungiCategorieAContratto() {
        logger.debug("--> Enter aggiungiCategorieAContratto");
        List<Contratto> contratti = contrattoService.caricaContratti(new ParametriRicercaContratti());
        Assert.assertTrue(contratti.size() != 0);
        Contratto contrattoTest = contratti.get(0);
        List<CategoriaLite> list = magazzinoAnagraficaService.caricaCategorie();

        RigaContratto rigaContrattoCategoria;
        CategoriaCommercialeArticolo categoria;
        for (CategoriaLite categoriaDTO : list) {
            rigaContrattoCategoria = new RigaContratto();
            // Assegna Categoria
            categoria = new CategoriaCommercialeArticolo();
            categoria.setId(categoriaDTO.getId());
            categoria.setVersion(categoriaDTO.getVersion());
            rigaContrattoCategoria.setCategoriaCommercialeArticolo(categoria);
            rigaContrattoCategoria.setContratto(contrattoTest);
            // rigaContrattoCategoria.setVariazionePercentuale(new Sconto());
            contrattoService.salvaRigaContratto(rigaContrattoCategoria);
        }
    }

    @Test
    public void testAggiungiComponente() {
        // Articolo articolo = new Articolo();
        // articolo.setId(1);
        // articolo = magazzinoAnagraficaService.caricaArticolo(articolo, false);
        // Componente componente = new Componente();
        // componente.setDistinta(articolo);
        // componente.setQta(20.0);
        // List<ArticoloLite> componenti = magazzinoAnagraficaService.caricaArticoliByCategoria(1,
        // false);
        // componente.setArticolo(componenti.get(0));
        // magazzinoAnagraficaService.salvaComponente(componente);
        try {
            System.out.println(magazzinoAnagraficaService.caricaComponenti(1));
        } catch (DistintaCircolareException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testCancellaContratto() {
        logger.debug("--> Enter cancellaContratto");
        if (contratto == null) {
            testCreaContratto();
        }
        contrattoService.cancellaContratto(contratto);
        // cerca di caricare il contratto
        logger.debug("--> verifica che il contratto non esista ");
        try {
            contratto = contrattoService.caricaContratto(contratto, false);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        logger.debug("--> Exit cancellaContratto");
    }

    @Test
    public void testCaricaArticolo() {
        logger.debug("--> Enter testCaricaArticolo");
        Articolo articolo = new Articolo();
        articolo.setId(100);
        Articolo cloneArticolo = magazzinoAnagraficaService.cloneArticolo(100, "A", "B", true, true, null, true, true);
        System.out.println(cloneArticolo.getCodice());
        for (AttributoArticolo attributo : cloneArticolo.getAttributiArticolo()) {
            System.out.println("Attributo " + attributo.getTipoAttributo().getCodice() + " - " + attributo.getValore());
        }
        for (Entry<String, DescrizioneLinguaArticolo> item : articolo.getDescrizioniLingua().entrySet()) {
            System.out.println(item.getValue());
        }
        logger.debug("--> Exit testCaricaArticolo");
    }

    @Test
    public void testCaricaRigheContratto() {
        List<ContrattoProspettoDTO> result = contrattoService.caricaContrattoProspetto(15,
                Calendar.getInstance().getTime());
        for (ContrattoProspettoDTO rigaContrattoCalcolo : result) {
            System.out.println(rigaContrattoCalcolo.toString());
        }
    }

    /**
     * metodo di test che provvede a creare un nuovo contratto di variazione, a modificarlo e
     * cancellarlo. verifica inoltre l'associazione con categoria listino, cliente, categoria
     * articolo e articolo
     */
    @Test
    public void testContratto() {
        logger.debug("--> Enter testContrattoVariazione");
        testCreaContratto();
        // modifica contratto
        testModificaContratto();
        // aggiungi Categoria Articolo
        // aggiungiCategoriaArticoloAContrattoVariazione();
        // aggiungi Articolo
        // aggiungiArticoloAContrattoVariazione();

        //
    }

    @Test
    public void testCreaContratto() {
        logger.debug("--> Enter creaContrattoVariazione");
        contratto = new Contratto();
        contratto.setCodice("cntrTest3");
        contratto.setDescrizione("contratto test");
        Calendar calendar = Calendar.getInstance();
        // usa la data corrente come data di inizio contratto
        contratto.setDataInizio(calendar.getTime());
        // fissa la data fine all'ultimo giorno dell'anno
        calendar.set(calendar.get(Calendar.YEAR), 11, 31);
        Date date = calendar.getTime();
        contratto.setDataFine(date);
        contratto.setCodiceValuta("EUR");
        contratto.setNumeroDecimali(2);
        contratto = contrattoService.salvaContratto(contratto, false);
        Assert.assertEquals(contratto.getVersion(), new Integer(0));
        Assert.assertEquals(contratto.getCodiceAzienda(), codiceAzienda);
        logger.debug("--> Exit creaContrattoVariazione");

    }

    @Test
    public void testEliminaRigaContratto() {
        logger.debug("--> Enter eliminaCategoriaDaContratto");
        List<Contratto> contratti = contrattoService.caricaContratti(new ParametriRicercaContratti());
        Assert.assertTrue(contratti.size() != 0);
        Contratto contrattoTest = contratti.get(0);
        contrattoTest = contrattoService.caricaContratto(contrattoTest, true);
        for (RigaContratto rigaContratto : contrattoTest.getRigheContratto()) {
            contrattoService.cancellaRigaContratto(rigaContratto);
            contrattoTest.getRigheContratto().remove(rigaContratto);
            contrattoService.salvaContratto(contrattoTest, true);
            break;
        }
    }

    @Test
    public void testIndiciRotazione() {
        ParametriRicercaArticolo parametri = new ParametriRicercaArticolo();
        parametri.setCodice("nuda");
        List<ArticoloRicerca> articoli = magazzinoAnagraficaService.ricercaArticoli(parametri);

        ParametriCalcoloIndiciRotazioneGiacenza parametriindici = new ParametriCalcoloIndiciRotazioneGiacenza();
        parametriindici.setArticoli(articoli);
        List<Deposito> depositi = new ArrayList<>();
        Deposito d = new Deposito();
        d.setId(1);
        d.setVersion(0);
        depositi.add(d);
        parametriindici.setDepositi(depositi);
        PeriodoStrategyDateCalculator periodoStrategy = new AnnoCorrenteStrategy();
        parametriindici.setPeriodo(periodoStrategy.calcola(new Periodo()));
        List<IndiceGiacenzaArticolo> result = magazzinoStatisticheService.calcolaIndiciRotazione(parametriindici);
        for (IndiceGiacenzaArticolo indiceGiacenzaArticolo : result) {
            System.out.println(indiceGiacenzaArticolo.getArticolo() + ":" + indiceGiacenzaArticolo.getRotazione());
        }
    }

    @Test
    public void testModificaContratto() {
        logger.debug("--> Enter modificaContrattoVariazione");
        if (contratto == null) {
            testCreaContratto();
        }
        // modifica del contratto
        Contratto contrattoModificato = contrattoService.salvaContratto(contratto, false);
        Assert.assertEquals(contratto, contrattoModificato);
        // verifica la variazione di versione
        Assert.assertFalse(contratto.getVersion().equals(contrattoModificato.getVersion()));
        logger.debug("--> Exit modificaContrattoVariazione");
    }

    @Test
    public void testMultyTenancy() {
    }

    @Test
    public void testSalvaDistinta() {
        Articolo articolo = new Articolo();
        articolo.setId(6837);
        magazzinoAnagraficaService.caricaArticolo(articolo, false);
    }

    @Test
    public void testStampaContratti() {
        ParametriRicercaStampaContratti parametri = new ParametriRicercaStampaContratti();

        List<ContrattoStampaDTO> stampaContratti = contrattoService.caricaStampaContratti(parametri);

        for (ContrattoStampaDTO contrattoStampaDTO : stampaContratti) {
            System.out.println(contrattoStampaDTO.toString());
        }
    }
}
