package it.eurotn.panjea.anagrafica.manager;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.CodiceEsternoEntitaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.CodiceEsternoEntitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.CodiceEsternoEntitaManager")
public class CodiceEsternoEntitaManagerBean implements CodiceEsternoEntitaManager {
    private static Logger logger = Logger.getLogger(CodiceEsternoEntitaManagerBean.class);
    @EJB
    private PanjeaDAO panjeaDAO;

    @SuppressWarnings("unchecked")
    @Override
    public List<EntitaLite> caricaEntitaConCodiceEsternoDaConfermare() {
        logger.debug("--> Enter caricaEntitaConCodiceEsternoDaConfermare");
        Query query = panjeaDAO
                .prepareQuery("select e from EntitaLite e where e.codiceEsterno like '#%'");
        List<EntitaLite> result = null;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("-->errore nel caricareil numero di entita con cod esterno da confermare",
                    e);
            throw new RuntimeException(
                    "-->errore nel caricareil numero di entita con cod esterno da confermare", e);
        }
        logger.debug("--> Exit caricaEntitaConCodiceEsternoDaConfermare");
        return result;
    }

    @Override
    public long caricaNumeroEntitaConCodiceEsternoDaConfermare() {
        logger.debug("--> Enter caricaNumeroEntitaConCodiceEsternoDaConfermare");
        Query query = panjeaDAO
                .prepareQuery("select count(*) from Entita e where e.codiceEsterno like '#%'");
        long nentita = -1;
        try {
            nentita = (long) panjeaDAO.getSingleResult(query);
        } catch (DAOException e) {
            logger.error("-->errore nel caricareil numero di entita con cod esterno da confermare",
                    e);
            throw new RuntimeException(
                    "-->errore nel caricareil numero di entita con cod esterno da confermare", e);
        }
        logger.debug(
                "--> Exit caricaNumeroEntitaConCodiceEsternoDaConfermare con value " + nentita);
        return nentita;

    }

    @Override
    public boolean checkEntitaConCodiceEsternoPresente(String codiceEsterno) {
        logger.debug("--> Enter isClienteConCodiceEsternoPresente");
        Query query = panjeaDAO.prepareQuery(
                "select count(*) from Cliente c where c.codiceEsterno=:codiceEsterno or c.codiceEsterno=:codiceEsternoDaVerificare");
        query.setParameter("codiceEsterno", codiceEsterno);
        query.setParameter("codiceEsternoDaVerificare", "#" + codiceEsterno);
        long count = 0;
        try {
            count = (long) panjeaDAO.getSingleResult(query);
        } catch (DAOException e) {
            logger.error("-->errore nel verificare se è presente un cliente con codice esterno", e);
            throw new RuntimeException(
                    "-->errore nel verificare se è presente un cliente con codice esterno", e);
        }
        logger.debug("--> Exit isClienteConCodiceEsternoPresente");
        return count > 0;
    }

}
