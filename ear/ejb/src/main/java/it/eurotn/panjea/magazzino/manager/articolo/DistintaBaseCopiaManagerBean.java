package it.eurotn.panjea.magazzino.manager.articolo;

import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.exception.DistintaCircolareException;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.DistintaBaseCopiaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.DistintaBaseManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.DistintaBaseCopiaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DistintaBaseCopiaManager")
public class DistintaBaseCopiaManagerBean implements DistintaBaseCopiaManager {

    private static final Logger LOGGER = Logger.getLogger(DistintaBaseCopiaManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private DistintaBaseManager distintaBaseManager;

    private void copiaComponenti(Set<Componente> componentiOrigine, Articolo distintaDestinazione,
            ConfigurazioneDistinta configurazioneDestinazione) {
        for (Componente componenteOrigine : componentiOrigine) {
            try {
                Componente componenteDestinazione = (Componente) componenteOrigine.clone();
                componenteDestinazione.setDistinta(distintaDestinazione.getArticoloLite());
                componenteDestinazione.setConfigurazioneDistinta(configurazioneDestinazione);
                componenteDestinazione = panjeaDAO.save(componenteDestinazione);

                Query queryFasi = panjeaDAO.prepareNamedQuery("FaseLavorazioneArticolo.loadByConfigurazioneComponente");
                queryFasi.setParameter("componente", componenteOrigine);
                @SuppressWarnings("unchecked")
                List<FaseLavorazioneArticolo> fasiComponente = panjeaDAO.getResultList(queryFasi);
                copiaFasi(fasiComponente, distintaDestinazione, configurazioneDestinazione, componenteDestinazione);
            } catch (DAOException e) {
                LOGGER.error("-->errore nel salvere il componente copiato. Codice Articolo componente "
                        + componenteOrigine.getArticolo().getCodice(), e);
                throw new RuntimeException("-->errore nel salvere il componente copiato. Codice Articolo componente "
                        + componenteOrigine.getArticolo().getCodice(), e);
            }
        }

    }

    private void copiaConfigurazione(ConfigurazioneDistinta configurazioneOriginale, Articolo distintaDestinazione) {
        if (!distintaDestinazione.isDistinta()) {
            throw new IllegalArgumentException("L'articolo di destinazione deve essere una distinta");
        }
        ConfigurazioneDistinta configurazioneCopia = new ConfigurazioneDistinta();
        configurazioneCopia.setNome(configurazioneOriginale.getNome());
        configurazioneCopia.setDistinta(distintaDestinazione);
        configurazioneCopia = distintaBaseManager.salvaConfigurazioneDistinta(configurazioneCopia);

        // copio fasi legate alla distinta
        Query query = panjeaDAO.prepareNamedQuery("FaseLavorazioneArticolo.loadByConfigurazione");
        query.setParameter("configurazione", configurazioneOriginale);
        @SuppressWarnings("unchecked")
        List<FaseLavorazioneArticolo> fasiOrigine = query.getResultList();
        copiaFasi(fasiOrigine, distintaDestinazione, configurazioneCopia, null);

        Set<Componente> componenti;
        try {
            componenti = distintaBaseManager.caricaComponenti(configurazioneOriginale);
            copiaComponenti(componenti, distintaDestinazione, configurazioneCopia);
        } catch (DistintaCircolareException e) {
            LOGGER.error("-->errore distinta circolare. improbabile", e);
            throw new RuntimeException(e);
        }

    }

    private void copiaConfigurazioneBase(Articolo distintaOrigine, Articolo distintaDestinazione) {
        if (!distintaDestinazione.isDistinta()) {
            throw new IllegalArgumentException("L'articolo di destinazione deve essere una distinta");
        }
        // copio le fasi legate alla distinta
        Query query = panjeaDAO.prepareNamedQuery("FaseLavorazioneArticolo.loadByConfigurazioneBase");
        query.setParameter("articolo", distintaOrigine);
        @SuppressWarnings("unchecked")
        List<FaseLavorazioneArticolo> fasiOrigine = query.getResultList();
        copiaFasi(fasiOrigine, distintaDestinazione, null, null);

        Set<Componente> componentiOrigine;
        try {
            componentiOrigine = distintaBaseManager.caricaComponenti(distintaOrigine);
            copiaComponenti(componentiOrigine, distintaDestinazione, null);
        } catch (DistintaCircolareException e1) {
            LOGGER.error("-->errore distinta circolare. improbabile", e1);
            throw new RuntimeException(e1);
        }
    }

    @Override
    public void copiaDistinta(Articolo distintaOrigine, Articolo distintaDestinazione, boolean copiaConfigurazioni) {
        copiaConfigurazioneBase(distintaOrigine, distintaDestinazione);
        if (copiaConfigurazioni) {
            List<ConfigurazioneDistinta> configurazioni = distintaBaseManager
                    .caricaConfigurazioniDistinta(distintaOrigine.getArticoloLite());
            for (ConfigurazioneDistinta configurazioneDistinta : configurazioni) {
                if (!configurazioneDistinta.isConfigurazioneBase()) {
                    copiaConfigurazione(configurazioneDistinta, distintaDestinazione);
                }
            }
        }
    }

    private void copiaFasi(List<FaseLavorazioneArticolo> fasiOrigine, Articolo distintaDestinazione,
            ConfigurazioneDistinta configurazioneDistintaDestinazione, Componente componente) {
        for (FaseLavorazioneArticolo faseLavorazioneArticolo : fasiOrigine) {
            FaseLavorazioneArticolo nuovaFase = (FaseLavorazioneArticolo) faseLavorazioneArticolo.clone();
            nuovaFase.setArticolo(distintaDestinazione.getArticoloLite());
            nuovaFase.setConfigurazioneDistinta(configurazioneDistintaDestinazione);
            nuovaFase.setComponente(componente);
            try {
                panjeaDAO.save(nuovaFase);
            } catch (DAOException e) {
                LOGGER.error("-->errore nel salvare la fase sulla disinta copiata", e);
                throw new RuntimeException("-->errore nel salvare la fase sulla disinta copiata", e);
            }
        }
    }

}
