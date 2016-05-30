package it.eurotn.panjea.anagrafica.manager.interfaces;

import javax.ejb.Local;

@Local
public interface ImportazioneMailManager {

    /**
     * Restituisce il numero delle mail che possono ancora essere importate.
     *
     * @return numero mail
     */
    Integer caricaNumeroMailDaImportare();

    /**
     * Importa tutte le mail della vecchia gestione nel dms.
     */
    void importaMail();
}
