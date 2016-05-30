package it.eurotn.panjea.magazzino.manager.omaggio;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.omaggio.Omaggio;
import it.eurotn.panjea.magazzino.domain.omaggio.RigaOmaggioArticolo;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.manager.omaggio.interfaces.OmaggioManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author leonardo
 */
@Stateless(name = "Panjea.OmaggioManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OmaggioManager")
public class OmaggioManagerBean implements OmaggioManager {

    private static final Logger LOGGER = Logger.getLogger(OmaggioManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    @Override
    public List<Omaggio> caricaOmaggi() {
        List<Omaggio> omaggi = new ArrayList<Omaggio>();
        TipoOmaggio[] tipi = TipoOmaggio.values();
        for (TipoOmaggio tipoOmaggio : tipi) {
            if (!tipoOmaggio.equals(TipoOmaggio.NESSUNO)) {
                Omaggio omaggio = caricaOmaggioByTipo(tipoOmaggio);
                omaggi.add(omaggio);
            }
        }
        return omaggi;
    }

    @Override
    public Omaggio caricaOmaggioByTipo(TipoOmaggio tipoOmaggio) {
        Query query = panjeaDAO.prepareNamedQuery("Omaggio.caricaByTipoOmaggio");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("paramTipoOmaggio", tipoOmaggio);

        Omaggio omaggio = null;
        try {
            omaggio = (Omaggio) panjeaDAO.getSingleResult(query);
            // init di codice iva lazy
            if (omaggio.getCodiceIva() != null) {
                omaggio.getCodiceIva().getCodice();
            }
        } catch (ObjectNotFoundException e) {
            LOGGER.warn("--> Nessun omaggio trovato per il tipo omaggio " + tipoOmaggio.name() + ", ne creo uno nuovo");
            omaggio = new Omaggio();
            omaggio.setCodiceAzienda(getAzienda());
            omaggio.setTipoOmaggio(tipoOmaggio);
            omaggio = salvaOmaggio(omaggio);
        } catch (Exception e) {
            LOGGER.error("--> Errore nella ricerca di omaggi per il tipo omaggio " + tipoOmaggio.name());
            throw new RuntimeException("--> Errore nella ricerca di omaggi per il tipo omaggio " + tipoOmaggio.name(),
                    e);
        }
        return omaggio;
    }

    @Override
    public RigaOmaggioArticolo caricaRigaOmaggio(AreaMagazzino areaMagazzino) {
        Query queryRigheOmaggioPresenti = panjeaDAO.prepareNamedQuery("RigaOmaggioArticolo.caricaByAreaMagazzino");
        queryRigheOmaggioPresenti.setParameter("areaMagazzino", areaMagazzino);
        RigaOmaggioArticolo righeOmaggio = null;
        try {
            righeOmaggio = (RigaOmaggioArticolo) panjeaDAO.getSingleResult(queryRigheOmaggioPresenti);
        } catch (ObjectNotFoundException e) {
            LOGGER.warn("--> Nessuna riga omaggio articolo trovata");
        } catch (DAOException e) {
            LOGGER.error("--> Errore nel recuperare la riga omaggio per l'areaMagazzino " + areaMagazzino, e);
            throw new RuntimeException(e);
        }
        return righeOmaggio;
    }

    /**
     * @return codice azienda loggata
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    @Override
    public Omaggio salvaOmaggio(Omaggio omaggio) {
        LOGGER.debug("--> Enter salvaOmaggio");
        Omaggio omaggioSalvato = null;
        try {
            if (omaggio.getCodiceAzienda() == null) {
                omaggio.setCodiceAzienda(getAzienda());
            }
            omaggioSalvato = panjeaDAO.save(omaggio);
            // init codice iva lazy
            if (omaggioSalvato.getCodiceIva() != null) {
                omaggioSalvato.getCodiceIva().getCodice();
            }
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio dell'omaggio ", e);
            throw new RuntimeException("Errore durante il salvataggio dell'omaggio ", e);
        }
        LOGGER.debug("--> Exit salvaOmaggio");
        return omaggioSalvato;
    }

}
