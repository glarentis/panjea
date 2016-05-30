package it.eurotn.panjea.sicurezza.test;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Before;
import org.junit.Test;

import it.eurotn.panjea.sicurezza.service.interfaces.SicurezzaService;

public class TestSicurezza {

    private SicurezzaService sicurezzaService;

    @Test
    public void login() {
        try {
            sicurezzaService.login();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception {
        System.setProperty("java.security.auth.login.config",
                TestSicurezza.class.getClassLoader().getResource("auth.conf").getPath());

        String username = "europa#ZORZI#Panjea#it";
        String credential = "pnj_adm";
        char[] password = credential.toCharArray();
        UsernamePasswordHandler passwordHandler = new UsernamePasswordHandler(username, password);
        LoginContext loginContext;
        try {
            loginContext = new LoginContext("PanjeaLoginModule", passwordHandler);
            loginContext.login();
        } catch (LoginException e) {
            fail(e.getMessage());
        }
        InitialContext ic = new InitialContext();
        sicurezzaService = (SicurezzaService) ic.lookup("Panjea.SicurezzaService");
    }
}
