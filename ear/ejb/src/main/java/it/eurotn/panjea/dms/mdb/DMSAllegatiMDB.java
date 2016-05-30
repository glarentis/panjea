package it.eurotn.panjea.dms.mdb;

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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.security.SecurityDomain;
import org.jboss.security.auth.callback.UsernamePasswordHandler;

import it.eurotn.panjea.dms.exception.DMSLoginException;
import it.eurotn.panjea.dms.manager.interfaces.DMSLookupClientWebService;
import it.eurotn.panjea.dms.manager.interfaces.DMSSecurityManager;

@MessageDriven(name = "Panjea.DMSAllegatiMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/allegatiDMS") })
@SecurityDomain("PanjeaLoginModule")
public class DMSAllegatiMDB implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(DMSAllegatiMDB.class);

    @EJB
    private DMSLookupClientWebService lookupService;

    @EJB
    private DMSSecurityManager securityManager;

    /**
     * Esegue il login.
     *
     * @param nomeAzienda
     *            azienda
     */
    private void login(String nomeAzienda) {
        String username = new StringBuilder("internalAdmin#").append(nomeAzienda).append("#IT").toString();
        String credential = "internalEuropaSw";
        UsernamePasswordHandler passwordHandler = new UsernamePasswordHandler(username, credential);
        LoginContext loginContext;
        try {
            loginContext = new LoginContext("PanjeaLoginModule", passwordHandler);
            loginContext.login();
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void onMessage(Message message) {
        LOGGER.debug("--> Enter onMessage");

        AttributoAggiornamentoDTO dto = null;

        /* recupero l'object message e rieffettua il login */
        Object messageObject = null;
        try {
            messageObject = ((ObjectMessage) message).getObject();

            if (messageObject == null || !(messageObject instanceof AttributoAggiornamentoDTO)) {
                return;
            }

            dto = (AttributoAggiornamentoDTO) messageObject;

            login(dto.getAzienda());
            String sid = securityManager.login();
            lookupService.creaPanjeaService().storeExtAttribute(sid, dto.getTipoAttributo().getNomeAttributoDMS(),
                    StringUtils.chop(dto.getValue()));

        } catch (DMSLoginException e1) {
            LOGGER.error("--> Impossibile eseguire il login nel dsm", e1);
        } catch (Exception e) {
            LOGGER.error("--> errore nell'aggiornare gli attributi tipodocumento nel dms ", e);
            throw new RuntimeException("errore nell'aggiornare gli attributi tipodocumento nel dms", e);
        } finally {
            try {
                message.acknowledge();
            } catch (JMSException e) {
                LOGGER.error("--> errore durante l'acknowledge del messaggio");
            }
        }

        LOGGER.debug("--> Exit onMessage");
    }

}
