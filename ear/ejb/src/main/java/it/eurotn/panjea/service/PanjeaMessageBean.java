/**
 * 
 */
package it.eurotn.panjea.service;

import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.security.JecPrincipal;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author Leonardo
 */
@Stateless(name = "Panjea.PanjeaMessage")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PanjeaMessage")
public class PanjeaMessageBean implements PanjeaMessage {

	protected static Logger logger = Logger.getLogger(PanjeaMessageBean.class);

	@Resource(mappedName = "ConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "topic/Panjea.PanjeaTopic")
	private Topic panjeaTopic;
	@Resource
	protected SessionContext sessionContext;

	private MessageProducer sender;

	private Session session;

	private Connection connection;

	/**
	 * Chiude una sessione jms.
	 */
	private void closeSession() {
		if (session != null) {
			try {
				session.close();
			} catch (JMSException e) {
				logger.warn("errore nel chiudere la JmsSessione");
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception ignore) {
				logger.warn("errore nel chiudere la JmsSessione");
			}
		}
	}

	/**
	 * 
	 * @return principal loggato
	 */
	private JecPrincipal getPrincipal() {
		return (JecPrincipal) sessionContext.getCallerPrincipal();
	}

	/**
	 * Apre una session jms.
	 * 
	 * @throws JMSException
	 *             eccezione rilanciata da qualche problema sulla sessione jms.
	 */
	private void openSession() throws JMSException {
		sender = null;
		session = null;
		connection = null;
		connection = connectionFactory.createConnection();
		connection.start();
		logger.debug("-->Creo la sessione JMS");
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		sender = session.createProducer(panjeaTopic);
		sender.setDeliveryMode(javax.jms.DeliveryMode.NON_PERSISTENT);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void send(Serializable serializableObjectMessage, String messageSelector) {
		logger.debug("--> Enter send");
		try {
			openSession();
			ObjectMessage message = session.createObjectMessage(serializableObjectMessage);
			message.setStringProperty("messagePrincipal", getPrincipal().getUserName());
			message.setStringProperty("messageSelector", messageSelector);
			sender.send(message);
		} catch (JMSException e) {
			logger.error("--> errore nel metodo send", e);
			// non sollevo nessuna eccezione per evitare che venga effettuato il
			// rollback della transazione JTA corrente
		} finally {
			closeSession();
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void send(String textMessage, String messageSelector) {
		logger.debug("--> Enter send");
		try {
			openSession();
			TextMessage message = session.createTextMessage(textMessage);
			message.setStringProperty("messagePrincipal", getPrincipal().getUserName());
			message.setStringProperty("messageSelector", messageSelector);
			sender.send(message);
		} catch (JMSException e) {
			logger.error("--> errore nel metodo send", e);
			// non sollevo nessuna eccezione per evitare che venga effettuato il
			// rollback della transazione JTA corrente
		} finally {
			closeSession();
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void send(String message, String[] roles, int timeout, String messageSelector) {
		logger.debug("--> Enter send");
		try {
			openSession();
			ObjectMessage objectMessage = session.createObjectMessage(new TextMessageExtend(message, roles, timeout));
			objectMessage.setStringProperty("messagePrincipal", getPrincipal().getUserName());
			objectMessage.setStringProperty("messageSelector", messageSelector);
			sender.send(objectMessage);
		} catch (JMSException e) {
			logger.error("--> errore nel metodo send", e);
			// non sollevo nessuna eccezione per evitare che venga effettuato il
			// rollback della transazione JTA corrente
		} finally {
			closeSession();
		}
	}
}
