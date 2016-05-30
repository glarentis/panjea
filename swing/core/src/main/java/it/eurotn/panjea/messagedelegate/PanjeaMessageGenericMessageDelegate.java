package it.eurotn.panjea.messagedelegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;

import it.eurotn.panjea.anagrafica.rich.statusBarItem.PanjeaUpdateMessageDelegate;
import it.eurotn.panjea.service.TextMessageExtend;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.security.JecPrincipal;

/**
 * Visualizzo un messaggio di testo ricevuto sul topic Panjea.topic in un popup.
 *
 * @author giangi
 * @version 1.0, 03/ott/2011
 *
 */
public class PanjeaMessageGenericMessageDelegate {

    private static Logger logger = Logger.getLogger(PanjeaUpdateMessageDelegate.class);

    public static final String DELEGATE_ID = "panjeaUpdateMessageDelegate";

    public static final String MESSAGE_CHANGE = "messageChange";

    /**
     * Costruttore.
     */
    public PanjeaMessageGenericMessageDelegate() {
    }

    /**
     *
     * @param objectMessage
     *            messaggio elaborato di tipo TextMEssageExtended
     */
    public void handleMessage(ObjectMessage objectMessage) {
        TextMessageExtend message;
        try {
            message = (TextMessageExtend) objectMessage.getObject();
            String messaggio = message.getTesto();
            JecPrincipal principal = PanjeaSwingUtil.getUtenteCorrente();
            List<String> ruoliAbilitatiPerMessaggio = Arrays.asList(message.getRoles());
            //La asList ritorna unna lista readOnly, la wrappo.
            List<String> ruoliUtente = new ArrayList<>(Arrays.asList(principal.getPermissions()));
            
            ruoliUtente.retainAll(ruoliAbilitatiPerMessaggio);
            if (ruoliUtente.size() > 0) {
                Message msg = new DefaultMessage(messaggio, Severity.INFO);
                MessageAlert messageAlert = new MessageAlert(msg);
                messageAlert.showAlert();
            }
        } catch (Exception e) {
            // potrei ricevere un messaggio che non è un TextMessageExtend oppure ho un errore nel
            // leggere il mesaggo.
            // Loggo e basta
            logger.error("-->errore nel leggare l'oggetto nel messaggio jms", e);
        }
    }

    /**
     *
     * @param message
     *            messaggio generico di testo ricevuto
     */
    public void handleMessage(TextMessage message) {
        try {
            String messaggio = message.getText();
            Message msg = new DefaultMessage(messaggio, Severity.INFO);
            MessageAlert messageAlert = new MessageAlert(msg);
            messageAlert.showAlert();
        } catch (JMSException e) {
            logger.error("-->errore durante la ricezione del messaggio dalla coda", e);
        }
    }
}