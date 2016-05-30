package it.eurotn.panjea.rich.editors.email;

import java.awt.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import it.eurotn.panjea.contabilita.util.AbstractStateDescriptor;
import it.eurotn.panjea.rich.jms.IMessageDelegate;

public class ImportazioneMailMessageDelegate extends Component implements IMessageDelegate {

    private static final Logger LOGGER = Logger.getLogger(ImportazioneMailMessageDelegate.class);

    private static final long serialVersionUID = -7521549324414797022L;

    public static final String DELEGATE_ID = "importazioneMailMessageDelegate";

    public static final String MESSAGE_CHANGE = "messageChange";

    @Override
    public void handleMessage(ObjectMessage message) {

        try {
            AbstractStateDescriptor stateDescriptor = (AbstractStateDescriptor) message.getObject();

            firePropertyChange(MESSAGE_CHANGE, null, stateDescriptor);
        } catch (JMSException e) {
            LOGGER.error("-->errore durante la ricezione del messaggio dalla coda", e);
        }
    }
}
