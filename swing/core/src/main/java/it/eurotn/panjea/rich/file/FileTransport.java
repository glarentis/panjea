/**
 * 
 */
package it.eurotn.panjea.rich.file;

import it.eurotn.panjea.magazzino.domain.rendicontazione.DatiSpedizione;

import java.util.List;
import java.util.Map;

/**
 * @author fattazzo
 * 
 */
public interface FileTransport {

    /**
     * Invia i flussi dati secondi i dati spedizione indicati.
     * 
     * @param files
     *            flusso dati
     * @param datiSpedizione
     *            dati di spedizione
     * @param parametri
     *            parametri aggiuntivi
     * @return <code>true</code> se l'invio è avvenuto correttamente, <code>false</code> altrimenti
     */
    boolean send(List<byte[]> files, DatiSpedizione datiSpedizione, Map<String, Object> parametri);

}
