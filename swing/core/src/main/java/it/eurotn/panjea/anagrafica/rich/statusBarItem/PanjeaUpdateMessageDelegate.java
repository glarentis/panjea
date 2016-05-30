package it.eurotn.panjea.anagrafica.rich.statusBarItem;

import it.eurotn.panjea.anagrafica.util.PanjeaUpdateDescriptor;
import it.eurotn.panjea.rich.jms.IMessageDelegate;

import java.awt.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

public class PanjeaUpdateMessageDelegate extends Component implements IMessageDelegate {

    private static final long serialVersionUID = -7521549324414797022L;

    private static Logger logger = Logger.getLogger(PanjeaUpdateMessageDelegate.class);

    public static final String DELEGATE_ID = "panjeaUpdateMessageDelegate";

    public static final String MESSAGE_CHANGE = "messageChange";

    @Override
    public void handleMessage(ObjectMessage message) {

        try {
            PanjeaUpdateDescriptor stateDescriptor = (PanjeaUpdateDescriptor) message.getObject();

            firePropertyChange(MESSAGE_CHANGE, null, stateDescriptor);
        } catch (JMSException e) {
            logger.error("-->errore durante la ricezione del messaggio dalla coda", e);
        }
    }
}