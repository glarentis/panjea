package it.eurotn.panjea.dms;

import static org.junit.Assert.fail;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Before;
import org.junit.Test;

import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.dms.exception.DMSLoginException;
import it.eurotn.panjea.dms.service.interfaces.DMSService;
import it.eurotn.panjea.dms.service.interfaces.RubricaExporterService;
import it.eurotn.panjea.mrp.TestMrp;

public class TestDMS {

    private InitialContext ic;

    private DMSService dmsService;

    private AnagraficaService anagraficaService;

    private RubricaExporterService rubricaExporterService;

    @Before
    public void setUp() throws Exception {
        System.setProperty("java.security.auth.login.config",
                TestMrp.class.getClassLoader().getResource("auth.conf").getPath());

        String username = "europa#" + "zorzi" + "#it";
        String credential = "pnj_adm";
        char[] password = credential.toCharArray();
        UsernamePasswordHandler passwordHandler = new UsernamePasswordHandler(username, password);
        LoginContext loginContext;
        try {
            loginContext = new LoginContext("other", passwordHandler);
            loginContext.login();
        } catch (LoginException e) {
            fail(e.getMessage());
        }

        ic = new InitialContext();
        dmsService = (DMSService) ic.lookup("Panjea.LogicalDocDMSService");
        rubricaExporterService = (RubricaExporterService) ic.lookup("Panjea.LogicalDocDMSService");
    }

    @Test
    public void testPubblica() throws DMSLoginException {
        dmsService.getAllegatoTile(1);
    }
}
