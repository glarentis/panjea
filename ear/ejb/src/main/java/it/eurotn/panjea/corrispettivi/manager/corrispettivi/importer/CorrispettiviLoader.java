package it.eurotn.panjea.corrispettivi.manager.corrispettivi.importer;

import java.util.Date;
import java.util.List;

public interface CorrispettiviLoader {

    /**
     * Carica i totali dei corrispettivi da importare per il giorno richiesto.
     *
     * @param data
     *            data
     * @return totali corrispettivi
     */
    List<CorrispettivoImportDTO> caricaTotaliCorrispettivi(Date data);
}
