package it.eurotn.panjea.dms.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoPanjea;
import it.eurotn.panjea.dms.manager.interfaces.DMSAttributiManager;
import it.eurotn.panjea.dms.manager.interfaces.DMSLookupClientWebService;
import it.eurotn.panjea.dms.manager.interfaces.DMSSecurityManager;
import it.eurotn.panjea.dms.mdb.AttributoAggiornamentoDTO;
import it.eurotn.panjea.dms.mdb.AttributoAggiornamentoDTO.TipoAttributo;

@Stateless(mappedName = "Panjea.DMSAttributiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DMSAttributiManager")
public class DMSAttributiManagerBean implements DMSAttributiManager {

    private static final Logger LOGGER = Logger.getLogger(DMSAttributiManagerBean.class);

    @EJB
    private DMSSecurityManager securityManager;

    @EJB
    private DMSLookupClientWebService lookupService;

    @Resource(mappedName = "ConnectionFactory")
    private ConnectionFactory jmsConnectionFactory;

    @Resource(mappedName = "queue/allegatiDMS")
    private Queue queue;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void aggiornaAttributi(TipoAttributo tipoAttributo, List<AttributoAllegatoPanjea> attributi) {
        LOGGER.debug("--> Enter aggiornaAttributiArticolo");

        String codiceAzienda = "";
        StringBuilder attributiValues = new StringBuilder(5000);
        for (AttributoAllegatoPanjea attributo : attributi) {
            attributiValues.append(attributo.getValue());
            attributiValues.append("~");
            codiceAzienda = attributo.getCodiceAzienda();
        }
        AttributoAggiornamentoDTO dto = new AttributoAggiornamentoDTO(tipoAttributo, attributiValues.toString(),
                codiceAzienda);

        sendMessage(dto);

        LOGGER.debug("--> Exit aggiornaAttributiArticolo");

    }

    @Override
    public void aggiornaAttributo(TipoAttributo tipoAttributo, AttributoAllegatoPanjea attributo) {
        LOGGER.debug("--> Enter aggiornaAttributoArticolo");
        List<AttributoAllegatoPanjea> atts = new ArrayList<>();
        atts.add(attributo);
        aggiornaAttributi(tipoAttributo, atts);
        LOGGER.debug("--> Exit aggiornaAttributoArticolo");
    }

    /**
     * Invia sulla coda degli allegati dms il dto specificato.
     *
     * @param dto
     *            dto da inviare
     */
    private void sendMessage(AttributoAggiornamentoDTO dto) {
        LOGGER.debug("--> Enter sendMessage");

        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = jmsConnectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(queue);
            ObjectMessage message = session.createObjectMessage();
            message.setObject(dto);
            message.setJMSRedelivered(false);
            LOGGER.debug("--> Spedizione messaggio");
            producer.send(message);
        } catch (JMSException e) {
            LOGGER.error("--> errore, impossibile inviare il messaggio ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("errore durante la chiusura della connessione", e);
            }
        }

        LOGGER.debug("--> Exit sendMessage");
    }
}
