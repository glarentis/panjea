package it.eurotn.panjea.anagrafica.service.interfaces;

import java.io.Serializable;

import javax.ejb.Remote;

@Remote
public interface PanjeaMessageService {

	/**
	 * Invia un messaggio sulla coda di panjea.
	 * 
	 * @param descriptor
	 *            descriptor
	 * @param selector
	 *            selector
	 */
	void sendPanjeaQueueMessage(Serializable descriptor, String selector);
}
