package it.eurotn.panjea.magazzino.rich.editors.fatturazione.consultazione;

import it.eurotn.panjea.contabilita.util.AbstractStateDescriptor;
import it.eurotn.panjea.rich.jms.IMessageDelegate;

import java.awt.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

public class ConfermaMovimentiInFatturazioneMessageDelegate extends Component implements IMessageDelegate {

	private static final long serialVersionUID = 8958125060739239317L;

	private static Logger logger = Logger.getLogger(ConfermaMovimentiInFatturazioneMessageDelegate.class);

	public static final String DELEGATE_ID = "confermaMovimentiInFatturazioneMessageDelegate";

	public static final String MESSAGE_CHANGE = "confermaMovimentiInFatturazioneMessageChange";

	@Override
	public void handleMessage(ObjectMessage message) {

		try {
			AbstractStateDescriptor stateDescriptor = (AbstractStateDescriptor) message.getObject();
			firePropertyChange(MESSAGE_CHANGE, null, stateDescriptor);
		} catch (JMSException e) {
			logger.error("-->errore durante la ricezione del messaggio dalla coda", e);
		}

	}

}
