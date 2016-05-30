package it.eurotn.panjea.vending.manager.movimenticassa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.panjea.vending.domain.RigaMovimentoCassa;
import it.eurotn.panjea.vending.manager.movimenticassa.interfaces.MovimentiCassaManager;

@Stateless(name = "Panjea.MovimentiCassaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MovimentiCassaManager")
public class MovimentiCassaManagerBean extends CrudManagerBean<MovimentoCassa>implements MovimentiCassaManager {

    private static final Logger LOGGER = Logger.getLogger(MovimentiCassaManagerBean.class);

    @Override
    public void cancellaByCassa(Integer cassaId) {
        LOGGER.debug("--> Enter cancellaByCassa");

        Query query = panjeaDAO.prepareNamedQuery("MovimentoCassa.cancellaByCassaId");
        query.setParameter("paramCassaId", cassaId);
        try {
            panjeaDAO.executeQuery(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione dei movimenti per la cassa " + cassaId, e);
            throw new GenericException("errore durante la cancellazione dei movimenti per la cassa " + cassaId, e);
        }

        LOGGER.debug("--> Exit caricaAll");
    }

    @Override
    public List<MovimentoCassa> caricaAll() {
        return caricaAll(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MovimentoCassa> caricaAll(boolean includiChiusure) {
        LOGGER.debug("--> Enter caricaAll");

        List<MovimentoCassa> movimenti = null;

        // @formatter:off
        String queryString = "select mc.id as id, mc.version as version, "
                + "mc.cassa as cassa, mc.data as data, "
                + "sum(rmc.quantitaEntrata*rmc.gettone.valore) as totaleEntrate, "
                + "sum(rmc.quantitaUscita*rmc.gettone.valore) as totaleUscite "
                + "from RigaMovimentoCassa rmc inner join rmc.movimentoCassa mc ";
        if (!includiChiusure) {
            queryString += "where mc.apertura = false ";
        }
        queryString += "group by mc";
        // @formatter:on

        Query query = panjeaDAO.prepareQuery(queryString, MovimentoCassa.class, null);

        try {
            movimenti = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> errore durante il caricamento dei movimenti cassa.", e);
            throw new GenericException("errore durante il caricamento dei movimenti cassa.", e);
        }

        LOGGER.debug("--> Exit caricaAll");
        return movimenti;
    }

    @Override
    public MovimentoCassa caricaById(Integer id) {
        LOGGER.debug("--> Enter caricaById");

        MovimentoCassa movimentoCassa = super.caricaById(id);

        BigDecimal entrateRiga = BigDecimal.ZERO;
        BigDecimal usciteRiga = BigDecimal.ZERO;
        for (RigaMovimentoCassa riga : movimentoCassa.getRighe()) {
            Hibernate.initialize(riga);
            entrateRiga = entrateRiga.add(riga.getGettone().getValore()
                    .multiply(new BigDecimal(riga.getQuantitaEntrata())).setScale(2, RoundingMode.HALF_UP));

            usciteRiga = usciteRiga.add(riga.getGettone().getValore().multiply(new BigDecimal(riga.getQuantitaUscita()))
                    .setScale(2, RoundingMode.HALF_UP));
        }
        movimentoCassa.setTotaleEntrate(entrateRiga);
        movimentoCassa.setTotaleUscite(usciteRiga);

        LOGGER.debug("--> Exit caricaById");
        return movimentoCassa;
    }

    @Override
    protected Class<MovimentoCassa> getManagedClass() {
        return MovimentoCassa.class;
    }

    @Override
    public MovimentoCassa salva(MovimentoCassa movimento) {

        // se il movimento contiene una cassa di destinazione lo considero come trasferimento e ne vado a creare uno per
        // cassa
        if (movimento.getCassaDestinazione() != null && !movimento.getCassaDestinazione().isNew()) {
            MovimentoCassa movimentoDestinazione = new MovimentoCassa();
            movimentoDestinazione.setData(movimento.getData());
            movimentoDestinazione.setCassa(movimento.getCassaDestinazione());
            movimentoDestinazione.setRighe(new TreeSet<>(new MovimentoCassa.RigheMovimentoCassaComparator()));
            for (RigaMovimentoCassa riga : movimento.getRighe()) {
                RigaMovimentoCassa rigaDest = new RigaMovimentoCassa();
                rigaDest.setMovimentoCassa(movimentoDestinazione);
                rigaDest.setGettone(riga.getGettone());
                // Il trasferimento prevede solo un entrata sulla cassa di destinazione
                rigaDest.setQuantitaEntrata(riga.getQuantitaUscita());
                rigaDest.setQuantitaUscita(0);
                movimentoDestinazione.getRighe().add(rigaDest);
            }
            super.salva(movimentoDestinazione);
        }

        MovimentoCassa movimentoCassa = super.salva(movimento);
        return caricaById(movimentoCassa.getId());
    }

}