package it.eurotn.panjea.vending.manager.casse;

import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.Cassa;
import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.panjea.vending.domain.RigaMovimentoCassa;
import it.eurotn.panjea.vending.manager.casse.interfaces.CasseManager;
import it.eurotn.panjea.vending.manager.movimenticassa.interfaces.MovimentiCassaManager;

@Stateless(name = "Panjea.CasseManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CasseManager")
public class CasseManagerBean extends CrudManagerBean<Cassa>implements CasseManager {

    private static final Logger LOGGER = Logger.getLogger(CasseManagerBean.class);

    @EJB
    private MovimentiCassaManager movimentiCassaManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<Cassa> caricaAll() {
        LOGGER.debug("--> Enter caricaAll");

        // @formatter:off
        String queryString = "select cassa.id as id, cassa.version as version, cassa.codice as codice, cassa.descrizione as descrizione, cassa.tipologia as tipologia, "
                + "sum(case when mc.apertura = true then ((rmc.quantitaEntrata*rmc.gettone.valore)-(rmc.quantitaUscita*rmc.gettone.valore)) else 0 end) as totaleIniziale, "
                + "sum(case when mc.apertura = false then (rmc.quantitaEntrata*rmc.gettone.valore) else 0 end) as totaleEntrate, "
                + "sum(case when mc.apertura = false then (rmc.quantitaUscita*rmc.gettone.valore) else 0 end) as totaleUscite "
                + "from RigaMovimentoCassa rmc "
                + "inner join rmc.movimentoCassa mc inner join mc.cassa cassa "
                + "group by cassa";
        // @formatter:on

        Query query = panjeaDAO.prepareQuery(queryString, Cassa.class, null);

        List<Cassa> casse = null;
        try {
            casse = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> errore durante il caricamento delle casse.", e);
            throw new GenericException("errore durante il caricamento delle casse.", e);
        }

        // aggiungo tutte le casse che non hanno movimenti
        queryString = "select cassa.id as id, cassa.version as version, cassa.codice as codice, cassa.descrizione as descrizione, cassa.tipologia as tipologia, "
                + "cast(0 as big_decimal) as totaleIniziale, cast(0 as big_decimal) as totaleEntrate, cast(0 as big_decimal) as totaleUscite "
                + "from Cassa cassa left join cassa.movimenti mov " + "where mov.id is null";
        query = panjeaDAO.prepareQuery(queryString, Cassa.class, null);
        List<Cassa> casseSenzaMovimenti = null;
        try {
            casseSenzaMovimenti = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> errore durante il caricamento delle casse.", e);
            throw new GenericException("errore durante il caricamento delle casse.", e);
        }
        casse.addAll(casseSenzaMovimenti);

        return casse;
    }

    @Override
    public Cassa caricaById(Integer id) {
        LOGGER.debug("--> Enter caricaById");

        String queryString = "select distinct c from Cassa c left join fetch c.movimenti mov left join fetch mov.righe righeMov left join fetch righeMov.gettone gett where c.id = :paramId";
        Query query = panjeaDAO.prepareQuery(queryString);
        query.setParameter("paramId", id);

        Cassa cassa = null;
        try {
            cassa = (Cassa) panjeaDAO.getSingleResult(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento della cassa " + id, e);
            throw new GenericException("errore durante il caricamento della cassa " + id, e);
        }

        LOGGER.debug("--> Exit caricaById");
        return cassa;
    }

    @SuppressWarnings("unchecked")
    private List<RigaMovimentoCassa> caricaTotaliPerCassa(Integer cassaId) {

        // @formatter:off
        String queryString = "select gett as gettone, "
                + "cast(sum(rmc.quantitaEntrata) as int) as quantitaEntrata, "
                + "cast(sum(rmc.quantitaUscita) as int) as quantitaUscita "
                + "from RigaMovimentoCassa rmc inner join rmc.gettone gett "
                + "inner join rmc.movimentoCassa mc "
                + "where mc.cassa.id = :paramCassaId "
                + "group by gett";
        // @formatter:on

        Query query = panjeaDAO.prepareQuery(queryString, RigaMovimentoCassa.class, null);
        query.setParameter("paramCassaId", cassaId);

        List<RigaMovimentoCassa> righe = null;
        try {
            righe = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle righe per la cassa " + cassaId, e);
            throw new GenericException("errore durante il caricamento delle righe per la cassa " + cassaId, e);
        }

        return righe;
    }

    @Override
    public void chiudiCassa(Integer id) {
        LOGGER.debug("--> Enter chiudiCassa");

        if (!verificaMovimentiDaChiudere(id)) {
            return;
        }

        MovimentoCassa movimentoCassa = new MovimentoCassa();
        movimentoCassa.setCassa(panjeaDAO.loadLazy(Cassa.class, id));
        movimentoCassa.setApertura(true);
        movimentoCassa.setData(Calendar.getInstance().getTime());
        movimentoCassa.setRighe(new TreeSet<RigaMovimentoCassa>(new MovimentoCassa.RigheMovimentoCassaComparator()));

        // carico la somma di tutte le righe movimentazione per la cassa
        List<RigaMovimentoCassa> righe = caricaTotaliPerCassa(id);
        for (RigaMovimentoCassa riga : righe) {
            riga.setMovimentoCassa(movimentoCassa);
            movimentoCassa.getRighe().add(riga);
        }

        // cancello i movimenti esistenti
        movimentiCassaManager.cancellaByCassa(id);

        try {
            movimentoCassa = panjeaDAO.save(movimentoCassa);
            System.out.println(movimentoCassa.getId());
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio del movimento di apertura", e);
            throw new GenericException("errore durante il salvataggio del movimento di apertura", e);
        }

        LOGGER.debug("--> Exit chiudiCassa");
    }

    @Override
    protected Class<Cassa> getManagedClass() {
        return Cassa.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Cassa> ricercaCasse() {
        LOGGER.debug("--> Enter ricercaCasse");

     // @formatter:off
        String queryString = "select cassa.id as id, cassa.version as version, "
                + "cassa.codice as codice, cassa.descrizione as descrizione, "
                + "cassa.tipologia as tipologia "
                + "from Cassa cassa ";
        // @formatter:on

        Query query = panjeaDAO.prepareQuery(queryString, Cassa.class, null);

        List<Cassa> casse = null;
        try {
            casse = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> errore durante il caricamento delle casse.", e);
            throw new GenericException("errore durante il caricamento delle casse.", e);
        }

        LOGGER.debug("--> Exit ricercaCasse");
        return casse;
    }

    private boolean verificaMovimentiDaChiudere(Integer cassaId) {
        LOGGER.debug("--> Enter verificaMovimentiDaChiudere");

        String queryString = "select count(mc.id) from MovimentoCassa mc where mc.apertura = false and mc.cassa.id = :paramCassaId";
        Query query = panjeaDAO.prepareQuery(queryString);
        query.setParameter("paramCassaId", cassaId);

        Long countMov;
        try {
            countMov = (Long) panjeaDAO.getSingleResult(query);
        } catch (Exception e) {
            countMov = new Long(0);
        }

        LOGGER.debug("--> Exit verificaMovimentiDaChiudere");
        return countMov > 0;
    }
}