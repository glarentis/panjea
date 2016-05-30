package it.eurotn.panjea.service.interfaces;

import java.io.Serializable;

import javax.ejb.Local;

/**
 * Servizio per la gestione di un messaggio e il suo invio al topic PanjeaTopic.
 * 
 * @author Leonardo
 */
@Local
public interface PanjeaMessage {

	String MESSAGE_GENERIC_SELECTOR = "message_generic_selector";

	/**
	 * Metodo per inoltrare il messaggio.
	 * 
	 * @param serializableObjectMessage
	 *            oggetto serializzabile da allegare al message
	 * @param messageSelector
	 *            selettore del messaggio.
	 */
	void send(Serializable serializableObjectMessage, String messageSelector);

	/**
	 * Metodo per inoltrare il messaggio.
	 * 
	 * @param testMessage
	 *            oggetto serializzabile da allegare al message
	 * @param messageSelector
	 *            selettore del messaggio.
	 */
	void send(String testMessage, String messageSelector);

	/**
	 * Metodo per inoltrare un messaggio di testo con varie opzioni.
	 * 
	 * @param message
	 *            messaggio da visualizzare
	 * @param roles
	 *            ruoli per i quali visualizzare il messaggio.
	 * @param timeout
	 *            timeout di visualizzazione dell'alert. Se =0 compare una finestra che si chiude con il pulsante Chiudi
	 * @param messageSelector
	 *            messageSelector da impostare su {@link PanjeaMessage#MESSAGE_GENERIC_SELECTOR}
	 */
	void send(String message, String[] roles, int timeout, String messageSelector);

}
