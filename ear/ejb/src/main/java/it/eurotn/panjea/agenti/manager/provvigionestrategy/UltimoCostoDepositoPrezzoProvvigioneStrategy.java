package it.eurotn.panjea.agenti.manager.provvigionestrategy;

import java.math.BigDecimal;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.UltimoCostoDepositoPrezzoProvvigioneStrategy")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.UltimoCostoDepositoPrezzoProvvigioneStrategy")
public class UltimoCostoDepositoPrezzoProvvigioneStrategy implements CalcoloPrezzoProvvigioneStrategy {

    private static final Logger LOGGER = Logger.getLogger(UltimoCostoDepositoPrezzoProvvigioneStrategy.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public BigDecimal calcolaPrezzoNetto(RigaArticolo rigaArticolo) {

        Sconto sconto = new Sconto();
        sconto.setSconto1(rigaArticolo.getVariazione1());
        sconto.setSconto2(rigaArticolo.getVariazione2());
        sconto.setSconto3(rigaArticolo.getVariazione3());
        sconto.setSconto4(rigaArticolo.getVariazione4());

        BigDecimal prezzoUnitarioBaseProvvigionale = calcolaPrezzoUnitario(rigaArticolo);

        return sconto.applica(prezzoUnitarioBaseProvvigionale, rigaArticolo.getNumeroDecimaliPrezzo());
    }

    @Override
    public BigDecimal calcolaPrezzoUnitario(RigaArticolo rigaArticolo) {

        BigDecimal prezzoUnitarioBaseProvvigionale = null;

        Query query = panjeaDAO.prepareNamedQuery("Articolo.caricaUltimoCostoDeposito");
        query.setParameter("dataRegistrazione", rigaArticolo.getAreaMagazzino().getDataRegistrazione());
        query.setParameter("articolo_id", rigaArticolo.getArticolo().getId());
        query.setParameter("deposito_id", rigaArticolo.getAreaMagazzino().getDepositoOrigine().getId());
        query.setMaxResults(1);
        Object[] datiUltimoCosto = null;
        try {
            datiUltimoCosto = (Object[]) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            // non ho nessun movimento, setto il prezzo della base provvigionale
            // a 0
            LOGGER.warn("Nessun movimento trovato per determinare il costo ultimo");
            prezzoUnitarioBaseProvvigionale = BigDecimal.ZERO;
        } catch (Exception e) {
            LOGGER.error("-->errore nel caricare l'ultimo costo", e);
            throw new RuntimeException(e);
        }

        if (datiUltimoCosto != null) {
            prezzoUnitarioBaseProvvigionale = (BigDecimal) datiUltimoCosto[0];
        }

        return prezzoUnitarioBaseProvvigionale;
    }

}
