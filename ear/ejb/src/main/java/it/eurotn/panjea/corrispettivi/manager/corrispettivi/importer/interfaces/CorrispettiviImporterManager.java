package it.eurotn.panjea.corrispettivi.manager.corrispettivi.importer.interfaces;

import java.util.Date;

import javax.ejb.Local;

@Local
public interface CorrispettiviImporterManager {

    /**
     * Importa i corrispettivi per la data indicata.
     *
     * @param data
     *            data di importazione
     */
    void importa(Date data);
}
