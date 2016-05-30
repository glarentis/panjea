package it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.manutenzioni.exception.TaiSenzaTamException;

@Local
public interface AreeInstallazioniMagazzinoManager {

    /**
     * Cancella un'eventuale araMagazzino collegata all'area installazione
     *
     * @param idAreaMagazzino
     *            areaMagazzino da cancellare
     */
    void cancellaAreaMagazzino(Integer idAreaMagazzino);

    /**
     *
     * @param idAreaInstallazione
     *            id areaInstallazione per la quale creare l'area magazzino
     * @return idAreaMagazzino creata
     * @throws TaiSenzaTamException
     *             rilanciata se il tipoAreaInstallazioneNon ha un tipoAreaMagazzino
     */
    int creaAreaMagazzino(int idAreaInstallazione) throws TaiSenzaTamException;
}
