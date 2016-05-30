package it.eurotn.panjea.fatturepa.manager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.fatturepa.domain.TipoRegimeFiscale;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturePAAnagraficaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.FatturePAAnagraficaManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.FatturePAAnagraficaManager")
public class FatturePAAnagraficaManagerBean implements FatturePAAnagraficaManager {

    private static final Logger LOGGER = Logger.getLogger(FatturePAAnagraficaManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @SuppressWarnings("unchecked")
    @Override
    public List<TipoRegimeFiscale> caricaTipiRegimiFiscali() {
        LOGGER.debug("--> Enter caricaTipiRegimiFiscali");

        List<TipoRegimeFiscale> list = new ArrayList<TipoRegimeFiscale>();

        Query query = panjeaDAO.prepareQuery("select trf from TipoRegimeFiscale trf");
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento dei tipi regime fiscale", e);
            throw new RuntimeException("Errore durante il caricamento dei tipi regime fiscale", e);
        }
        LOGGER.debug("--> Exit caricaTipiRegimiFiscali");
        return list;
    }

}
