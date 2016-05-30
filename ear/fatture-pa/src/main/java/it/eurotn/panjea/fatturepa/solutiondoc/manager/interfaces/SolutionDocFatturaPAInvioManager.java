package it.eurotn.panjea.fatturepa.solutiondoc.manager.interfaces;

import javax.ejb.Local;

@Local
public interface SolutionDocFatturaPAInvioManager {

    /**
     * Verifica e importa eventuali esiti non ancora presenti.
     */
    void checkEsitiFatturePA();

    /**
     * Invia la fattura PA al SdI.
     *
     * @param idAreaMagazzinoFatturaPA
     *            id area magazzino fattura pa
     * @return identificativo SdI
     */
    String invioSdiFatturaPA(Integer idAreaMagazzinoFatturaPA);

}
