package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.evasione;

import it.eurotn.panjea.contabilita.util.AbstractStateDescriptor;
import it.eurotn.panjea.rich.jms.IMessageDelegate;

import java.awt.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

public class EvasioneOrdiniMessageDelegate extends Component implements IMessageDelegate {

	private static final long serialVersionUID = -7521549324414797022L;

	private static Logger logger = Logger.getLogger(EvasioneOrdiniMessageDelegate.class);

	public static final String DELEGATE_ID = "evasioneOrdiniMessageDelegate";

	public static final String OBJECT_CHANGE = "objectChange";

	@Override
	public void handleMessage(ObjectMessage message) {

		try {
			AbstractStateDescriptor stateDescriptor = (AbstractStateDescriptor) message.getObject();

			firePropertyChange(OBJECT_CHANGE, null, stateDescriptor);
		} catch (JMSException e) {
			logger.error("-->errore durante la ricezione del messaggio dalla coda", e);
		}
	}
}
