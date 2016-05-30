package it.eurotn.panjea.anagrafica.manager.rapportibancarisedeentita;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.manager.rapportibancarisedeentita.interfaces.RapportiBancariSedeEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(mappedName = "Panjea.RapportiBancariSedeEntitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RapportiBancariSedeEntitaManager")
public class RapportiBancariSedeEntitaManagerBean implements RapportiBancariSedeEntitaManager {

    private static final Logger LOGGER = Logger.getLogger(RapportiBancariSedeEntitaManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    @IgnoreDependency
    private SediEntitaManager sediEntitaManager;

    @Override
    public void cancellaRapportoBancarioSedeEntita(RapportoBancarioSedeEntita rapportoBancario)
            throws AnagraficaServiceException {
        LOGGER.debug("--> Enter cancellaRapportoBancario");
        if (rapportoBancario.isNew()) {
            LOGGER.error("--> Impossibile cancellare il rapporto bancario, id nullo.");
            throw new AnagraficaServiceException("Impossibile cancellare il rapporto bancario, id nullo.");
        }
        try {
            panjeaDAO.delete(rapportoBancario);
        } catch (DAOException e) {
            throw new GenericException(e);
        }
        LOGGER.debug("--> Exit cancellaRapportoBancario");
    }

    @Override
    public List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(Integer idSedeEntita)
            throws AnagraficaServiceException {
        return caricaRapportiBancariSedeEntita(idSedeEntita, true);
    }

    @Override
    public List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(Integer idSedeEntita, boolean ignoraEredita)
            throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaRapportiBancariEntita");

        return caricaRapportiBancariSedeEntita("numero", null, idSedeEntita, ignoraEredita);
    }

    /**
     *
     * @param fieldSearch
     *            campo da filtrare
     * @param valueSearch
     *            valore da filtrare
     * @param idSedeEntita
     *            id sede entità
     * @param ignoraEredita
     *            ignora l'eredità della sede
     * @return lista sedi
     */
    @SuppressWarnings("unchecked")
    private List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(String fieldSearch, String valueSearch,
            Integer idSedeEntita, boolean ignoraEredita) {
        List<RapportoBancarioSedeEntita> rapporti = null;
        try {
            if (!ignoraEredita) {
                // carico la sede e verifico il flag eredita rapporti bancari.
                SedeEntita sedeEntita = sediEntitaManager.caricaSedeEntita(idSedeEntita);
                if (!sedeEntita.getTipoSede().isSedePrincipale() && sedeEntita.isEreditaRapportiBancari()) {
                    idSedeEntita = sediEntitaManager.caricaSedePrincipaleEntita(sedeEntita.getEntita()).getId();
                }
            }

            StringBuilder sb = new StringBuilder(
                    "select rb from RapportoBancarioSedeEntita rb where rb.sedeEntita.id = :paramIdSedeEntita and rb.abilitato = true ");
            if (valueSearch != null) {
                sb.append(" and rb.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
            }
            Query query = panjeaDAO.prepareQuery(sb.toString());
            query.setParameter("paramIdSedeEntita", idSedeEntita);
            rapporti = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore, impossibile caricare Rapporti Bancari Azienda  ", e);
            throw new GenericException(e);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Exit caricaRapportiBancariEntita " + rapporti);
        }
        return rapporti;
    }

    @Override
    public List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntitaPricipale(String fieldSearch,
            String valueSearch, Integer idEntita) {
        LOGGER.debug("--> Enter caricaRapportiBancariSedeEntitaPricipale");
        SedeEntita sedeEntita = null;
        List<RapportoBancarioSedeEntita> rapporti = null;
        try {
            sedeEntita = sediEntitaManager.caricaSedePrincipaleEntita(idEntita);
        } catch (AnagraficaServiceException e) {
            LOGGER.error("--> errore nel caricare la sede entità principale", e);
            throw new GenericException(e);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Sede entita pricnipale recuperata " + sedeEntita);
        }
        rapporti = caricaRapportiBancariSedeEntita(fieldSearch, valueSearch, sedeEntita.getId(), true);
        LOGGER.debug("--> Exit caricaRapportiBancariSedeEntitaPricipale");
        return rapporti;
    }

    @Override
    public RapportoBancarioSedeEntita caricaRapportoBancarioPerTipoPagamentoDefault(TipoPagamento tipoPagamento,
            SedeEntita sedeEntita, Integer idEntita) {
        LOGGER.debug("--> Enter caricaRapportoBancarioPerTipoPagamentoDefault");
        if (sedeEntita == null) {
            // se e' null carico sede principale
            try {
                sedeEntita = sediEntitaManager.caricaSedePrincipaleEntita(idEntita);
            } catch (AnagraficaServiceException e) {
                throw new GenericException(e);
            }
        }
        // Controllo la sede
        List<RapportoBancarioSedeEntita> rapps;
        try {
            rapps = caricaRapportiBancariSedeEntita(sedeEntita.getId(), false);
        } catch (AnagraficaServiceException e) {
            throw new GenericException(e);
        }
        for (RapportoBancarioSedeEntita rapportoBancarioSedeEntita : rapps) {
            if (rapportoBancarioSedeEntita.getDefaultPagamenti().contains(tipoPagamento)) {
                LOGGER.debug("--> Exit caricaRapportoBancarioPerTipoPagamentoDefault con rapporto bancario sella sede");
                return rapportoBancarioSedeEntita;
            }
        }
        LOGGER.debug("--> Exit caricaRapportoBancarioPerTipoPagamentoDefault con il primo della sede principale");
        if (rapps.size() > 0) {
            return rapps.get(0);
        }
        return null;
    }

    @Override
    public RapportoBancarioSedeEntita caricaRapportoBancarioSedeEntita(Integer idRapportoBancario)
            throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaRapportoBancario");

        RapportoBancarioSedeEntita rapportoBancarioSedeEntita = null;
        try {
            rapportoBancarioSedeEntita = panjeaDAO.load(RapportoBancarioSedeEntita.class, idRapportoBancario);
        } catch (ObjectNotFoundException e) {
            LOGGER.error("--> Errore durante il caricamento del rapporto bancario", e);
            throw new AnagraficaServiceException();
        }

        LOGGER.debug("--> Exit caricaRapportoBancario");
        return rapportoBancarioSedeEntita;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RapportoBancarioSedeEntita> caricaRiepilogoDatiBancari() {
        LOGGER.debug("--> Enter caricaRiepilogoDatiBancari");

        // @formatter:off
        StringBuilder sb = new StringBuilder(200);
        sb.append("select rbse.id as id, rbse.version as version, ");
        sb.append("          se.id as sedeEntita$id, se.version as sedeEntita$version, ");
        sb.append("          sa.descrizione as sedeEntita$sede$descrizione, sa.indirizzo as sedeEntita$sede$indirizzo, loc.descrizione as sedeEntita$sede$datiGeografici$localita$descrizione, ");
        sb.append("          ent.id as sedeEntita$entita$id, ent.version as sedeEntita$entita$version, ent.codice as sedeEntita$entita$codice, anag.denominazione as sedeEntita$entita$anagrafica$denominazione, ");
        sb.append("          rbse.banca as banca, ");
        sb.append("          rbse.filiale as filiale ");
        sb.append("from RapportoBancarioSedeEntita rbse inner join rbse.sedeEntita se inner join se.sede sa left join sa.datiGeografici.localita as loc inner join se.entita ent inner join ent.anagrafica anag ");
        // @formatter:on

        Query query = panjeaDAO.prepareQuery(sb.toString(), RapportoBancarioSedeEntita.class, null);

        List<RapportoBancarioSedeEntita> riepilogo;
        try {
            riepilogo = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento del riepilogo dei dati bancari", e);
            throw new GenericException("errore durante il caricamento del riepilogo dei dati bancari", e);
        }

        LOGGER.debug("--> Exit salvaRapportoBancarioSedeEntita");
        return riepilogo;
    }

    @Override
    public RapportoBancarioSedeEntita salvaRapportoBancarioSedeEntita(
            RapportoBancarioSedeEntita rapportoBancarioSedeEntita) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter salvaRapportoBancario");
        try {
            if (rapportoBancarioSedeEntita.getSedeEntita().isNew()) {
                LOGGER.error("--> Impossibile salvare il rapporto , SedeEntita nullo.");
                throw new AnagraficaServiceException("--> Impossibile salvare il rapporto , SedeEntita nullo.");
            }
            rapportoBancarioSedeEntita = panjeaDAO.save(rapportoBancarioSedeEntita);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Exit salvaRapportoBancarioSedeEntita con risutalto " + rapportoBancarioSedeEntita);
            }
            return rapportoBancarioSedeEntita;
        } catch (DAOException e) {
            throw new GenericException(e);
        }
    }

    @Override
    public void sostituisciDatiBancari(List<RapportoBancarioSedeEntita> rapporti, Banca banca, Filiale filiale) {
        LOGGER.debug("--> Enter sostituisciDatiBancari");

        Set<Integer> idRapporti = new TreeSet<>();
        for (RapportoBancarioSedeEntita rapportoBancarioSedeEntita : rapporti) {
            idRapporti.add(rapportoBancarioSedeEntita.getId());
        }

        Query query = panjeaDAO.prepareQuery(
                "update RapportoBancarioSedeEntita rbse set rbse.banca = :paramBanca, rbse.filiale = :paramFiliale where rbse.id in (:paramIdRapporti)");
        query.setParameter("paramBanca", banca);
        query.setParameter("paramFiliale", filiale);
        query.setParameter("paramIdRapporti", idRapporti);
        try {
            panjeaDAO.executeQuery(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la sostituzione dei rapporti bancari.", e);
            throw new GenericException("errore durante la sostituzione dei rapporti bancari.", e);
        }

        LOGGER.debug("--> Exit sostituisciDatiBancari");
    }
}
