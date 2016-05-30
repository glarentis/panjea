package it.eurotn.panjea.anagrafica;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Before;
import org.junit.Test;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.anagrafica.service.interfaces.PanjeaMessageService;
import it.eurotn.panjea.anagrafica.util.PanjeaUpdateDescriptor;
import it.eurotn.panjea.anagrafica.util.PanjeaUpdateDescriptor.UpdateOperation;
import it.eurotn.panjea.anagrafica.util.RubricaDTO;
import it.eurotn.panjea.magazzino.test.TestContratti;
import it.eurotn.querybuilder.service.interfaces.QueryBuilderService;

public class TestAnagrafica {
    private AnagraficaService anagraficaService;
    private PanjeaMessageService messageService;
    private QueueConnectionFactory tcf;
    private InitialContext ic;

    private QueryBuilderService queryBuilderService;

    @Test
    public void caricaMetadataQuerable() {
        List<String> querable = queryBuilderService.caricaAllEntityQuerable();
        for (String class1 : querable) {
            System.out.println(class1);
        }

    }

    @Test
    public void caricaRubrica() {
        System.out.println("carico tutte le anagrafiche");
        List<RubricaDTO> anagrafiche = anagraficaService.caricaAnagraficheFull();
        System.out.println(anagrafiche.size());
        for (RubricaDTO rubricaDTO : anagrafiche) {
            System.out.println(rubricaDTO.getDenominazione() + ":" + rubricaDTO.getIndirizzo());
            for (RubricaDTO rubricaDTOFiglia : rubricaDTO.getRubricaDTO()) {
                System.out.println(
                        "*************" + rubricaDTOFiglia.getDenominazione() + ":" + rubricaDTOFiglia.getIndirizzo());
            }
        }
    }

    @Test
    public void eseguiHql() {
        // criteriaExecutor.execute(getClass(), null)
        // List<Projection> p = new ArrayList<>();
        // p.add(new Projection(new String[] { "codice", "descrizione" }));
        // PanjeaCriteria criteria = new PanjeaCriteria(p);
        // List<Listino> result = criteriaExecutor.execute(Listino.class, criteria);
        // for (Listino listino : result) {
        // for (SedeMagazzino sedeMagazzino : listino.getSedi()) {
        // System.out.println(listino.getCodice() + ":" + sedeMagazzino.getSedeEntita().getId() +
        // ":"
        // + sedeMagazzino.getSedeEntita().getCodice() +
        // sedeMagazzino.getSedeEntita().isAbilitato());
        // }
        // }
    }

    @Test
    public void sendMessage() {
        PanjeaUpdateDescriptor updateDescriptor = new PanjeaUpdateDescriptor("giangi", 10, UpdateOperation.START);
        messageService.sendPanjeaQueueMessage(updateDescriptor, "");
    }

    @Test
    public void sendModificheEntita() {
        Entita entita = new Cliente();
        entita.setId(15);
        try {
            entita = anagraficaService.caricaEntita(entita, true);
            entita.setNote("test modifica");
            QueueConnection connSend = tcf.createQueueConnection();
            connSend.setExceptionListener(new ExceptionListener() {

                @Override
                public void onException(JMSException jmsexception) {
                    System.err.println("Errore connessione " + jmsexception);
                }
            });

            Queue topicSend = (Queue) ic.lookup("/queue/panjea-sync");
            QueueSession sessionSend = connSend.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueSender send = sessionSend.createSender(topicSend);
            entita.setNote("test");
            ObjectMessage tm = sessionSend.createObjectMessage(entita);
            tm.setStringProperty("azienda", "marsilli");
            tm.setStringProperty("operazione", "update");
            send.send(tm);
            connSend.start();
            send.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception {
        System.setProperty("java.security.auth.login.config",
                TestAnagrafica.class.getClassLoader().getResource("auth.conf").getPath());

        String username = "europa#ZORZI#Panjea#it";
        String credential = "pnj_adm";
        char[] password = credential.toCharArray();
        BasicConfigurator.configure();
        FileInputStream is = new FileInputStream(
                TestContratti.class.getClassLoader().getResource("log4j.properties").getPath());
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
        anagraficaService = (AnagraficaService) ic.lookup("Panjea.AnagraficaService");
        messageService = (PanjeaMessageService) ic.lookup("Panjea.PanjeaMessageService");
        queryBuilderService = (QueryBuilderService) ic.lookup("Panjea.QueryBuilderService");
        tcf = (QueueConnectionFactory) ic.lookup("/ConnectionFactory");
    }
}
