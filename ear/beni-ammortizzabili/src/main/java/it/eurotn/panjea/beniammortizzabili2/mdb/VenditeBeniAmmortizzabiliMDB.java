package it.eurotn.panjea.beniammortizzabili2.mdb;

import it.eurotn.panjea.beniammortizzabili.util.MessageBeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.SimulazioneAmmortamentoManager;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.jboss.annotation.security.SecurityDomain;
import org.jboss.security.auth.callback.UsernamePasswordHandler;

/**
 * MessageDrivenBean incaricato di eseguire il calcolo di tutte le simulazioni non consolidate collegate al
 * BeneAmmortizzabile contenuto nel Message.
 * 
 * @author adriano
 */
@MessageDriven(name = "Panjea.VenditeBeniMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/VenditaBeneAmmortizzabile") })
@SecurityDomain("PanjeaLoginModule")
public class VenditeBeniAmmortizzabiliMDB implements MessageListener {

	private static Logger logger = Logger.getLogger(VenditeBeniAmmortizzabiliMDB.class);

	@EJB
	protected SimulazioneAmmortamentoManager simulazioneAmmortamentoManager;

	/**
	 * @see SimulazioneAmmortamentoManager#calcolaSimulazioniBene(BeneAmmortizzabile, Integer)
	 * 
	 * @param beneAmmortizzabile
	 *            bene ammortizzabile
	 * @param anno
	 *            anno
	 */
	private void calcolaSimulazioniBene(BeneAmmortizzabile beneAmmortizzabile, Integer anno) {
		logger.debug("--> Enter calcolaSimulazioniBene");
		simulazioneAmmortamentoManager.calcolaSimulazioniBene(beneAmmortizzabile, anno);
		logger.debug("--> Exit calcolaSimulazioniBene");
	}

	/**
	 * Comment for onMessage.
	 * 
	 * @param message
	 *            messaggio
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void onMessage(Message message) {
		logger.debug("--> Enter onMessage");
		/* recupero l'object message e rieffettua il login */
		MessageBeneAmmortizzabile messageBeneAmmortizzabile;
		try {

			messageBeneAmmortizzabile = (MessageBeneAmmortizzabile) ((ObjectMessage) message).getObject();
		} catch (JMSException e) {
			logger.error("--> errore, impossibile recuperare l'object MessageBeneAmmortizzabile ", e);
			throw new RuntimeException(e);
		}

		String username = "internalAdmin" + "#" + messageBeneAmmortizzabile.getJecPrincipal().getCodiceAzienda()
				+ "#IT";
		String credential = "internalEuropaSw";
		UsernamePasswordHandler passwordHandler = new UsernamePasswordHandler(username, credential);
		LoginContext loginContext;
		try {
			loginContext = new LoginContext("PanjeaLoginModule", passwordHandler);
			loginContext.login();
		} catch (LoginException e) {
			logger.error("--> errore, impossibile effettuare il login ");
			throw new RuntimeException(e);
		}
		/* ricalcolo simulazione bene */
		calcolaSimulazioniBene(messageBeneAmmortizzabile.getBeneAmmortizzabile(), messageBeneAmmortizzabile.getAnno());

	}

}
