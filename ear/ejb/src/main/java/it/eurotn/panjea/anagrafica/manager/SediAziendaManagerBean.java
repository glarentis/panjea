package it.eurotn.panjea.anagrafica.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficheManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediAziendaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.SedeAnagraficaOrphanException;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 *
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.SediAziendaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SediAziendaManager")
public class SediAziendaManagerBean implements SediAziendaManager {

    private static final Logger LOGGER = Logger.getLogger(SediAziendaManagerBean.class);

    /**
     * @uml.property name="panjeaDAO"
     * @uml.associationEnd
     */
    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    @IgnoreDependency
    private AziendeManager aziendeManager;

    /**
     * @uml.property name="anagraficaManager"
     * @uml.associationEnd
     */
    @EJB
    private AnagraficheManager anagraficaManager;

    @Override
    public void cancellaSedeAzienda(SedeAzienda sedeAzienda)
            throws AnagraficaServiceException, SedeAnagraficaOrphanException {
        LOGGER.debug("--> Enter cancellaSedeAzienda");
        cancellaSedeAzienda(sedeAzienda, false);
        LOGGER.debug("--> Exit cancellaSedeAzienda");

    }

    @Override
    public void cancellaSedeAzienda(SedeAzienda sedeAzienda, boolean deleteOrphan)
            throws AnagraficaServiceException, SedeAnagraficaOrphanException {
        if (sedeAzienda.isNew()) {
            LOGGER.error("--> Impossibile cancellare una sedeAzienda con id nullo.");
            throw new AnagraficaServiceException("Impossibile cancellare una sedeAzienda con id nullo.");
        }
        /*
         * controllo se sede azienda da cancellare � sede principale. In caso positivo rilancia un
         * eccezione
         */
        if (sedeAzienda.getTipoSede().isSedePrincipale()) {
            LOGGER.warn("--> impossibile cancellare la sede azienda principale ");
            throw new AnagraficaServiceException(" Impossibile eliminare la sede anagrafica principale ");
        }
        // Verifico se la sede anagrafica
        // della sede azienda cancellata � orfana o no. Nel caso in cui risulti
        // esserlo e non � richiesta la
        // cancellazione delle sedi orfane rilancio un eccezione
        boolean isOrphan = anagraficaManager.isSedeOrphan(sedeAzienda.getSede());
        if (isOrphan && (!deleteOrphan)) {
            LOGGER.warn("--> La sede risulta essere orfana. Forzare la cancellazione per poterla cancellare.");
            throw new SedeAnagraficaOrphanException(
                    "La sede risulta essere orfana. Forzare la cancellazione per poterla cancellare.",
                    sedeAzienda.getSede());
        }

        try {
            panjeaDAO.delete(sedeAzienda);
            LOGGER.debug("--> Cancellata la sedeAzienda con id " + sedeAzienda.getId());
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante la cancellazione della sedeAzienda " + sedeAzienda.getId(), e);
            throw new GenericException(e);
        }

        /*
         * Elimino SedeAnagrafica se dal precedente controllo risulta essere orfana
         */
        if (isOrphan) {
            LOGGER.debug("--> cancellazione sede anagrafica orfana " + sedeAzienda.getSede().getId());
            anagraficaManager.cancellaSedeAnagrafica(sedeAzienda.getSede());
        }

    }

    @Override
    public SedeAzienda caricaSedeAzienda(Integer idSedeAzienda) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaSedeAzienda");

        SedeAzienda sedeAzienda;
        try {
            sedeAzienda = panjeaDAO.load(SedeAzienda.class, idSedeAzienda);
        } catch (ObjectNotFoundException e) {
            LOGGER.error("--> Errore durante il caricamento della sede azienda " + idSedeAzienda, e);
            throw new AnagraficaServiceException(e);
        }

        LOGGER.debug("--> Exit caricaSedeAzienda");
        return sedeAzienda;
    }

    @Override
    public SedeAzienda caricaSedePrincipaleAzienda(Azienda azienda) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaSedePrincipaleAzienda");

        if (azienda.isNew()) {
            LOGGER.error("--> Impossibile caricare la sede. Id azienda nullo.");
            throw new AnagraficaServiceException("Impossibile caricare la sede. Id azienda nullo.");
        }
        try {
            Session hibernateSession = (Session) panjeaDAO.getEntityManager().getDelegate();
            Criteria criteria = hibernateSession.createCriteria(SedeAzienda.class)
                    .createAlias(SedeAzienda.PROP_AZIENDA, "azienda")
                    .createAlias(SedeAzienda.PROP_TIPO_SEDE, "tipoSede")
                    .add(Restrictions.eq("azienda.id", azienda.getId()))
                    .add(Restrictions.eq("tipoSede." + TipoSedeEntita.PROP_SEDE_PRINCIPALE, true));
            SedeAzienda sedeAzienda = (SedeAzienda) criteria.uniqueResult();
            LOGGER.debug("--> Exit caricaSedePrincipaleAzienda");
            return sedeAzienda;
        } catch (HibernateException e) {
            LOGGER.error("--> Errore durante il caricamento della sede per l'azienda " + azienda.getId(), e);
            throw new AnagraficaServiceException(e);
        }
    }

    @Override
    public List<SedeAnagrafica> caricaSediAnagraficaAzienda(Azienda azienda) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaSediAnagraficaAzienda");
        List<SedeAzienda> sediAzienda = caricaSediAzienda(azienda);
        List<SedeAnagrafica> sediAnagrafiche = new ArrayList<SedeAnagrafica>();
        for (SedeAzienda sedeAzienda : sediAzienda) {
            sediAnagrafiche.add(sedeAzienda.getSede());
        }

        LOGGER.debug("--> Exit caricaSediAnagrafica");
        return sediAnagrafiche;
    }

    @Override
    public List<SedeAzienda> caricaSediAzienda() throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaSediAzienda");
        javax.persistence.Query query = panjeaDAO.prepareNamedQuery("SedeAzienda.caricaAll");

        List<SedeAzienda> sediAzienda;
        try {
            sediAzienda = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> errore, impossibile recuperare le Sedi di azienda  ", e);
            throw new AnagraficaServiceException(e);
        }
        LOGGER.debug("--> Exit caricaSediAzienda");
        return sediAzienda;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SedeAzienda> caricaSediAzienda(Azienda azienda) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaSediAzienda");
        javax.persistence.Query query = panjeaDAO.prepareNamedQuery("SedeAzienda.caricaByAzienda");
        query.setParameter("paramIdAzienda", azienda.getId());

        List<SedeAzienda> sediAzienda;
        try {
            sediAzienda = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> errore, impossibile recuperare le Sedi di azienda  ", e);
            throw new AnagraficaServiceException(e);
        }

        return sediAzienda;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SedeAzienda> caricaSediSecondarieAzienda(Azienda azienda) throws AnagraficaServiceException {
        List<SedeAzienda> sediAzienda = new ArrayList<SedeAzienda>();
        try {
            Session hibernateSession = (Session) panjeaDAO.getEntityManager().getDelegate();
            Criteria crit = hibernateSession.createCriteria(SedeAzienda.class);
            sediAzienda = crit.add(Restrictions.eq(SedeAzienda.PROP_AZIENDA + ".id", azienda.getId()))
                    .createAlias(SedeAzienda.PROP_TIPO_SEDE, "ts").add(Restrictions.eq("ts.sedePrincipale", false))
                    .list();
        } catch (HibernateException e) {
            LOGGER.error("--> Errore durante il caricamento delle sedi dell'azienda " + azienda.getId());
            throw new AnagraficaServiceException(e);
        }
        LOGGER.debug("--> Exit caricaSediSecondarieAzienda");
        return sediAzienda;
    }

    @Override
    public SedeAzienda salvaSedeAzienda(SedeAzienda sedeAzienda)
            throws AnagraficaServiceException, SedeEntitaPrincipaleAlreadyExistException {
        LOGGER.debug("--> Enter salvaSedeAzienda");
        // controlla la variazione della sede principale
        if (sedeAzienda.getTipoSede().isSedePrincipale()) {
            SedeAzienda sedeAziendaPrincipale = caricaSedePrincipaleAzienda(sedeAzienda.getAzienda());
            if ((sedeAziendaPrincipale != null) && !sedeAzienda.equals(sedeAziendaPrincipale)) {
                LOGGER.warn("--> attenzione, sede principale Azienda gia' esistente");
                throw new SedeEntitaPrincipaleAlreadyExistException("Sede Azienda Principale gia' esistente",
                        sedeAziendaPrincipale);
            }
        }
        SedeAzienda sedeAziendaSave = null;
        try {
            sedeAziendaSave = panjeaDAO.save(sedeAzienda);
        } catch (DAOException e) {
            throw new GenericException(e);
        }
        return sedeAziendaSave;
    }
}
