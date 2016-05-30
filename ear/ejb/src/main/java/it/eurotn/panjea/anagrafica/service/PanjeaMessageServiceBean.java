package it.eurotn.panjea.anagrafica.service;

import it.eurotn.panjea.anagrafica.service.interfaces.PanjeaMessageService;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

import java.io.Serializable;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.PanjeaMessageService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.PanjeaMessageService")
public class PanjeaMessageServiceBean implements PanjeaMessageService {

	private static Logger logger = Logger.getLogger(PanjeaMessageServiceBean.class);

	@EJB
	private PanjeaMessage panjeaMessage;

	@Override
	public void sendPanjeaQueueMessage(Serializable descriptor, String selector) {
		logger.debug("--> Enter sendPanjeaQueueMessage");

		panjeaMessage.send(descriptor, selector);

		logger.debug("--> Exit sendPanjeaQueueMessage");
	}

}
