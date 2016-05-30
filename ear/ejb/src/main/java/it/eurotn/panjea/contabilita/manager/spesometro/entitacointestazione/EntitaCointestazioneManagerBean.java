package it.eurotn.panjea.contabilita.manager.spesometro.entitacointestazione;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.contabilita.domain.EntitaCointestazione;
import it.eurotn.panjea.contabilita.manager.spesometro.entitacointestazione.interfaces.EntitaCointestazioneManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;

@Stateless(name = "Panjea.EntitaCointestazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.EntitaCointestazioneManager")
public class EntitaCointestazioneManagerBean extends CrudManagerBean<EntitaCointestazione>
        implements EntitaCointestazioneManager {

    private static final Logger LOGGER = Logger.getLogger(EntitaCointestazioneManagerBean.class);

    @Override
    public void cancellaByAreaContabile(Integer idAreaContabile) {
        LOGGER.debug("--> Enter cancellaByAreaContabile");

        Query query = panjeaDAO.prepareNamedQuery("EntitaCointestazione.eliminaByAreaContabile");
        query.setParameter("paramIdAreaContabile", idAreaContabile);

        try {
            panjeaDAO.executeQuery(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione delle entita cointestate per l'area contabile "
                    + idAreaContabile, e);
            throw new GenericException(
                    "errore durante la cancellazione delle entita cointestate per l'area contabile " + idAreaContabile,
                    e);
        }

        LOGGER.debug("--> Exit caricaByAreaContabile");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<EntitaCointestazione> caricaByAreaContabile(Integer idAreaContabile) {
        LOGGER.debug("--> Enter caricaByAreaContabile");

        Query query = panjeaDAO.prepareNamedQuery("EntitaCointestazione.caricaByAreaContabile");
        query.setParameter("paramIdAreaContabile", idAreaContabile);

        List<EntitaCointestazione> entita = null;
        try {
            entita = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle entita cointestate per l'area contabile "
                    + idAreaContabile, e);
            throw new GenericException(
                    "errore durante il caricamento delle entita cointestate per l'area contabile " + idAreaContabile,
                    e);
        }

        LOGGER.debug("--> Exit caricaByAreaContabile");
        return entita;
    }

    @Override
    protected Class<EntitaCointestazione> getManagedClass() {
        return EntitaCointestazione.class;
    }

}