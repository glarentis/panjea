package it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces;

import java.math.BigDecimal;

import javax.ejb.Local;

import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;

@Local
public interface LetturaAreaRifornimentoGenerator {

    /**
     * Crea l'area rifornimento con i dati provenienti dalla lettura della selezionatrice.
     *
     * @param installazione
     *            installzione
     * @param distributore
     *            distributore
     * @param caricatore
     *            caricatore
     * @param incasso
     *            incasso
     * @param reso
     *            reso
     * @param numeroSacchetto
     *            numero sacchetto
     * @return area rifornimento
     */
    AreaRifornimento creaAreaRifornimento(Installazione installazione, Distributore distributore, Operatore caricatore,
            BigDecimal incasso, BigDecimal reso, Integer numeroSacchetto);
}
