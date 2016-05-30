package it.eurotn.panjea.documenti.graph.test;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Before;
import org.junit.Test;

import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.documenti.graph.service.interfaces.DocumentiGraphService;
import it.eurotn.panjea.documenti.graph.util.DocumentoGraph;

public class TestDocumentiGraph {

    private DocumentiGraphService documentiGraphService;
    private InitialContext ic;

    /**
     * init test.
     *
     * @throws Exception
     *             .
     */
    @Before
    public void setUp() throws Exception {
        // System.setProperty("java.security.auth.login.config",
        // TestDocumentiGraph.class.getClassLoader().getResource("auth.conf").getPath());

        String username = "europa#zorzi#Panjea#it";
        String credential = "pnj_adm";
        char[] password = credential.toCharArray();
        BasicConfigurator.configure();
        FileInputStream is = new FileInputStream(
                TestDocumentiGraph.class.getClassLoader().getResource("log4j.properties").getPath());
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
        documentiGraphService = (DocumentiGraphService) ic.lookup("Panjea.DocumentiGraphService");
    }

    /**
     * test di creazione di un nodo documento.
     */
    @Test
    public void testCreaNodoDocumento() {

        DocumentoGraph documento = new DocumentoGraph();
        documento.setIdDocumento(567);

        DocumentoNode node = documentiGraphService.createNode(documento, false);

        System.err.println(node);
    }

}
