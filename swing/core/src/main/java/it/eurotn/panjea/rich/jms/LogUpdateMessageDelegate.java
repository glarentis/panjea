package it.eurotn.panjea.rich.jms;

import it.eurotn.panjea.utils.PanjeaSwingUtil;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.richclient.text.HtmlPane;

/**
 * Riceve il messaggio di aggiornamento dalla coda di update. Scrive
 * 
 * @author giangi
 * 
 */
public class LogUpdateMessageDelegate implements MessageListener {

    static Logger logger = Logger.getLogger(LogUpdateMessageDelegate.class);

    public static final String EVENT_NAME = "logUpdateMessage";

    private HtmlPane htmlPane;

    public LogUpdateMessageDelegate(HtmlPane htmlPane) {
        super();
        this.htmlPane = htmlPane;
        logger.debug("--> Creato " + this.getClass().getName());
    }

    /**
     * @see it.eurotn.panjea.importazioni.rich.jms.LogMessageDelegate#receiveLogMessage(javax.jms.TextMessage)
     */
    @Override
    public void onMessage(Message originalMessage) {
        try {
            TextMessage message = (TextMessage) originalMessage;
            htmlPane.setText(htmlPane.getText() + "<br/>" + message.getText());
            logger.debug("-->" + message.getText());
        } catch (JMSException e) {
            logger.error("--> errore, ricezione messaggi log importazione ", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        }
    }
}
