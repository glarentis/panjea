package it.eurotn.panjea.agenti.manager.provvigionestrategy;

import java.math.BigDecimal;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.PrezzoNettoCalcoloPrezzoProvvigioneStrategy")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PrezzoNettoCalcoloPrezzoProvvigioneStrategy")
public class PrezzoNettoCalcoloPrezzoProvvigioneStrategy implements CalcoloPrezzoProvvigioneStrategy {

    @Override
    public BigDecimal calcolaPrezzoNetto(RigaArticolo rigaArticolo) {
        return rigaArticolo.getPrezzoNetto().getImportoInValutaAzienda();
    }

    @Override
    public BigDecimal calcolaPrezzoUnitario(RigaArticolo rigaArticolo) {
        return rigaArticolo.getPrezzoUnitario().getImportoInValutaAzienda();
    }

}
