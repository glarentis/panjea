package it.eurotn.panjea.manutenzioni.manager.righeinstallazione.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;

@Local
public interface RigheInstallazioneManager extends CrudManager<RigaInstallazione> {

    /**
     *
     * @param idAreaInstallazione
     *            id area installazione
     * @return list di righeInstallazione. Le righe installazioni sono legate 1:1 alle installazioni
     *         . Se non ci sono nel documento viene ritornata la riga nuova con l'installazione
     *         legata.
     */
    List<RigaInstallazione> caricaRigheInstallazioneByAreaInstallazione(Integer idAreaInstallazione);

    /**
     *
     * @param idArticolo
     *            id articolo
     * @return lista di movimenti di installazione per quell'articolo
     */
    List<RigaInstallazione> caricaRigheInstallazioneByArticolo(Integer idArticolo);

    /**
     *
     * @param idInstallazione
     *            id installazione
     * @return lista di movimenti per quell'installazione
     */
    List<RigaInstallazione> caricaRigheInstallazioneByInstallazione(Integer idInstallazione);

    /**
     *
     * @param rigaInstallazione
     *            riga da salvare
     * @return riga da salvare con le proprietà Lazy inizializzate
     */
    RigaInstallazione salvaInizializza(RigaInstallazione rigaInstallazione);
}