package it.eurotn.panjea.vending.manager.lettureselezionatrice;

import java.math.BigDecimal;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.manager.arearifornimento.interfaces.AreaRifornimentoAggiornaManager;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces.LetturaAreaRifornimentoGenerator;

@Stateless(name = "Panjea.LetturaAreaRifornimentoGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LetturaAreaRifornimentoGenerator")
public class LetturaAreaRifornimentoGeneratorBean implements LetturaAreaRifornimentoGenerator {

    @EJB
    private AreaRifornimentoAggiornaManager areaRifornimentoAggiornaManager;

    @Override
    public AreaRifornimento creaAreaRifornimento(Installazione installazione, Distributore distributore,
            Operatore caricatore, BigDecimal incasso, BigDecimal reso, Integer numeroSacchetto) {

        AreaRifornimento areaRifornimento = new AreaRifornimento();
        areaRifornimento.setAreaMagazzino(new AreaMagazzino());
        areaRifornimento = areaRifornimentoAggiornaManager.aggiornaDatiInstallazione(areaRifornimento, installazione);

        // sovrascrivo i dati dell'installazione con quelli arrivati dalla lettura
        areaRifornimento.setDistributore(distributore);
        caricatore = caricatore != null && !caricatore.isNew() ? caricatore : null;
        areaRifornimento.setOperatore(caricatore);
        areaRifornimento.setIncasso(incasso);
        areaRifornimento.setReso(reso);
        areaRifornimento.setNumeroSacchetto(numeroSacchetto);

        return areaRifornimento;
    }

}
