package it.eurotn.panjea.fatturepa.manager;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.manager.interfaces.AziendaFatturaPAManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.AziendaFatturaPAManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.AziendaFatturaPAManager")
public class AziendaFatturaPAManagerBean implements AziendaFatturaPAManager {

    private static final Logger LOGGER = Logger.getLogger(AziendaFatturaPAManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext context;

    @EJB
    private AziendeManager aziendeManager;

    @Override
    public AziendaFatturaPA caricaAziendaFatturaPA() {
        LOGGER.debug("--> Enter caricaAziendaFatturaPA");

        Azienda azienda = null;

        AziendaFatturaPA aziendaFatturaPA = null;

        // se non ne trovo una la creo
        try {
            azienda = aziendeManager.caricaAziendaByCodice(getAzienda());
            Query query = panjeaDAO.prepareQuery("from AziendaFatturaPA a where a.azienda = :paramAzienda");
            query.setParameter("paramAzienda", azienda);

            aziendaFatturaPA = (AziendaFatturaPA) panjeaDAO.getSingleResult(query);
            aziendaFatturaPA.getAzienda().getSedi().size();
        } catch (ObjectNotFoundException e) {
            LOGGER.debug("--> AziendaFatturaPA non trovata, ne creo uno nuova");
            aziendaFatturaPA = new AziendaFatturaPA();
            azienda.getSedi().size();
            aziendaFatturaPA.setAzienda(azienda);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento della AziendaFatturaPA", e);
            throw new RuntimeException(e);
        }

        LOGGER.debug("--> Exit caricaAziendaFatturaPA");
        return aziendaFatturaPA;
    }

    /**
     * Recupera il codice azienda dell'utente autenticato nel context.
     *
     * @return String codice azienda
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    @Override
    public AziendaFatturaPA salvaAziendaFatturaPA(AziendaFatturaPA aziendaFatturaPA) {
        LOGGER.debug("--> Enter salvaAziendaFatturaPA");

        AziendaFatturaPA aziendaFatturaPASalvata = null;
        try {
            aziendaFatturaPASalvata = panjeaDAO.save(aziendaFatturaPA);
            aziendaFatturaPASalvata.getAzienda().getSedi().size();
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio dell'azienda fattura PA", e);
            throw new RuntimeException("errore durante il salvataggio dell'azienda fattura PA", e);
        }

        LOGGER.debug("--> Exit salvaAziendaFatturaPA");
        return aziendaFatturaPASalvata;
    }

}
