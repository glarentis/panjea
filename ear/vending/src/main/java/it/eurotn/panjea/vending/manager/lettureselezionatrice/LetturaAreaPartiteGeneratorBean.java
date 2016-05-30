package it.eurotn.panjea.vending.manager.lettureselezionatrice;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.pagamenti.manager.interfaces.SediPagamentoManager;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces.LetturaAreaPartiteGenerator;

@Stateless(name = "Panjea.LetturaAreaPartiteGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LetturaAreaPartiteGenerator")
public class LetturaAreaPartiteGeneratorBean implements LetturaAreaPartiteGenerator {

    @EJB
    private TipiAreaPartitaManager tipiAreaPartitaManager;

    @EJB
    private SediPagamentoManager sediPagamentoManager;

    @Override
    public AreaPartite creaAreaPartite(AreaMagazzino areaMagazzino) {
        AreaPartite areaPartite = null;
        TipoAreaPartita tipoAreaPartita = tipiAreaPartitaManager
                .caricaTipoAreaPartitaPerTipoDocumento(areaMagazzino.getTipoAreaDocumento().getTipoDocumento());
        if (!tipoAreaPartita.isNew()) {
            areaPartite = new AreaRate();
            SedePagamento sedePagamento = sediPagamentoManager
                    .caricaSedePagamentoBySedeEntita(areaMagazzino.getDocumento().getSedeEntita(), false);
            areaPartite.setCodicePagamento(sedePagamento.getCodicePagamento());
        }

        return areaPartite;
    }

}
