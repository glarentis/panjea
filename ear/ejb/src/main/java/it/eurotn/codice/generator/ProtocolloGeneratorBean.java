package it.eurotn.codice.generator;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.codice.generator.interfaces.IProtocolloValore;
import it.eurotn.codice.generator.interfaces.ProtocolloGenerator;
import it.eurotn.dao.exception.GenerazioneProtocolloException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.ProtocolloGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ProtocolloGenerator")
public class ProtocolloGeneratorBean implements ProtocolloGenerator {

    private static Logger logger = Logger.getLogger(ProtocolloGeneratorBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    /**
     * Restituisce il principal corrente.
     *
     * @return JecPrincipal
     */
    private JecPrincipal getCurrentPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

    /**
     * Recupera {@link IProtocolloValore} attraverso <code>CodiceProtocollo</code>. Se <code>CodiceProtocollo</code> non
     * � valorizzato tenta di recuperarlo attraverso l'attributo <code>registroPropertyPath</code>, se anche questo
     * fallisce si intende che codiceProtocollo viene definito dall'utente e viene restituito null. Recuperato il
     * <code>CodiceProtocollo</code> recupera l'istanza attraverso la namedQuery "ProtocolloValore.caricaByCodice"
     *
     *
     * @return classe contenente il valore del protocollo
     */
    private IProtocolloValore getProtocollo(String codiceProtocollo) {
        logger.debug("--> Enter getProtocollo");

        logger.debug("--> ricerca valore protocollo di " + codiceProtocollo);
        final Query query = panjeaDAO.getEntityManager().createNamedQuery("ProtocolloValore.caricaByCodice");
        query.setParameter("paramCodiceAzienda", getCurrentPrincipal().getCodiceAzienda());
        query.setParameter("paramCodice", codiceProtocollo);
        IProtocolloValore iprotocolloValore = null;
        try {
            iprotocolloValore = (IProtocolloValore) query.getSingleResult();
        } catch (final PersistenceException e) {
            logger.error("--> errore getSingleResult in getProtocollo", e);
        }
        if (iprotocolloValore == null) {
            throw new GenericException("Protocollo inesistente ");
        }
        logger.debug("--> Exit getProtocollo, protocollo trovato ");
        return iprotocolloValore;
    }

    @Override
    public String nextCodice(String codiceProtocollo, Integer maxValue) {
        logger.debug("--> Enter nextCodice");

        IProtocolloValore protocolloValore = null;
        try {
            protocolloValore = getProtocollo(codiceProtocollo);
        } catch (final GenerazioneProtocolloException e) {
            logger.error("--> " + e.getMessage());
            throw new GenericException(e);
        }

        Integer valore = protocolloValore.getValore();
        logger.debug("--> recuperato valore " + valore);
        valore++;
        protocolloValore.setValore(valore);

        // se è uguale al valore massimo faccio ripartire il numeratore
        if (maxValue.equals(valore)) {
            protocolloValore.setValore(0);
        }

        /*
         * esegue il salvataggio di protocolloValore aggiornandone il valore del progressivo
         */
        panjeaDAO.getEntityManager().merge(protocolloValore);

        logger.debug("--> Exit nextCodice valore " + protocolloValore.getValore());
        return valore.toString();
    }

}
