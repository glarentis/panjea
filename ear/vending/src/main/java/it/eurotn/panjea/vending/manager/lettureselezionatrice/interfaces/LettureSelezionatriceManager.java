package it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.RigaLetturaSelezionatrice;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.RisultatiChiusuraLettureDTO;

@Local
public interface LettureSelezionatriceManager extends CrudManager<LetturaSelezionatrice> {

    /**
     * Chiude le letture valide associando o creando il rifornimento corrispondente.
     *
     * @param letture
     *            letture da chiudere
     * @return resoconto della chiusura delle letture
     */
    RisultatiChiusuraLettureDTO chiudiLettureSelezionatrice(List<LetturaSelezionatrice> letture);

    /**
     * @param idLettura
     *            id lettura
     * @return carica tutte le letture della selezionatrice
     */
    List<LetturaSelezionatrice> ricercaLettureSelezionatrice(Integer idLettura);

    /**
     * Carica tutte le righe di lettura che corrispondono al progressivo richiesto.
     *
     * @param progressivo
     *            progressivo
     * @return righe caricate
     */
    List<RigaLetturaSelezionatrice> ricercaRigheLetturaSelezionatrice(Integer progressivo);
}