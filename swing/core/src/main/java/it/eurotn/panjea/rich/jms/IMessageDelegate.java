/**
 * 
 */
package it.eurotn.panjea.rich.jms;

import javax.jms.ObjectMessage;

import org.springframework.jms.listener.adapter.MessageListenerAdapter;

/**
 * Interfaccia da implementare per indentificare una classe di oggetti che gestiscono il messaggio {@link ObjectMessage}
 * ricevuto dal server.<br>
 * L'istanza di classe {@link MessageListenerAdapter} ha un delegate del quale ricerca il metodo handleMessage (metodo
 * di default che e' possibile sovrascrivere con la proprieta' defaultListenerMethod via xml)
 * 
 * @author Leonardo
 */
public interface IMessageDelegate {

    /**
     * metodo destinato alla gestione dell' objectMessage ricevuto.
     * 
     * @param message
     *            messaggio ricevuto
     */
    void handleMessage(ObjectMessage message);

}
