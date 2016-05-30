package it.eurotn.panjea.fatturepa.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.fatturepa.domain.TipoRegimeFiscale;

/**
 * @author fattazzo
 *
 */
@Local
public interface FatturePAAnagraficaManager {

    /**
     * Carica tutti i tipi di regimi fiscali.
     *
     * @return regimi caricati
     */
    List<TipoRegimeFiscale> caricaTipiRegimiFiscali();

}
