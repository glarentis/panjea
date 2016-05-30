/**
 *
 */
package it.eurotn.panjea.magazzino.test;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.service.interfaces.ListinoService;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.util.ConfrontoListinoDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaConfrontoListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.TipoConfronto;
import it.eurotn.panjea.magazzino.util.parametriricerca.TipoConfronto.EConfronto;

/**
 * TestCase per il manager dei Contratti.
 *
 * @author adriano
 * @version 1.0, 17/giu/08
 */
public class TestListini {

    private ListinoService listinoService;

    private MagazzinoDocumentoService magazzinoDocumentoService;

    private final String codiceAzienda = "zorzi";

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

    @Test
    public void calcoloPrezzi() {
        // List<ListinoPrezziDTO> result = magazzinoDocumentoService.caricaListinoPrezzi(3994, 0,
        // 50, 304);
        // for (ListinoPrezziDTO listinoPrezziDTO : result) {
        // System.out.println(listinoPrezziDTO.getArticolo().getCodice() + " " +
        // listinoPrezziDTO.getImporto());
        // }
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        System.setProperty("java.security.auth.login.config",
                TestListini.class.getClassLoader().getResource("auth.conf").getPath());

        String username = "europa#" + codiceAzienda + "#it";
        String credential = "pnj_adm";
        char[] password = credential.toCharArray();
        BasicConfigurator.configure();
        FileInputStream is = new FileInputStream(
                TestListini.class.getClassLoader().getResource("log4j.properties").getPath());
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
        listinoService = (ListinoService) ic.lookup("Panjea.ListinoService");
        magazzinoDocumentoService = (MagazzinoDocumentoService) ic.lookup("Panjea.MagazzinoDocumentoService");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConfrontoListino() {

        ParametriRicercaConfrontoListino parametri = new ParametriRicercaConfrontoListino();

        // confronto base
        TipoConfronto confrontoBase = new TipoConfronto();
        confrontoBase.setConfronto(EConfronto.LISTINO);
        // confrontoBase.setTipoCosto(ETipoCosto.COSTO_MEDIO);
        Listino listino = new Listino();
        listino.setId(8);
        confrontoBase.setListino(listino);
        parametri.setConfrontoBase(confrontoBase);

        // confronti
        TipoConfronto confronto1 = new TipoConfronto();
        confronto1.setConfronto(EConfronto.ULTIMO_COSTO_AZIENDALE);
        TipoConfronto confronto2 = new TipoConfronto();
        confronto2.setConfronto(EConfronto.LISTINO);
        listino = new Listino();
        listino.setId(10);
        confronto2.setListino(listino);
        parametri.setConfronti(new ArrayList<TipoConfronto>());
        parametri.getConfronti().add(confronto1);
        parametri.getConfronti().add(confronto2);

        ConfrontoListinoDTO confronto = listinoService.caricaConfrontoListino(parametri);
    }
}
