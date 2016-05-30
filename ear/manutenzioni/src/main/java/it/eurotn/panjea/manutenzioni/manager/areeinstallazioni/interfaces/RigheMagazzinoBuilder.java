package it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;

@Local
public interface RigheMagazzinoBuilder {

    /**
     * Crea le righe di magazzino dalle righe dell'area installazione
     * 
     * @param ai
     *            area installazione
     * @param areaMagazzino
     *            area magazzino salvata
     */
    void creaRigheMagazzino(AreaInstallazione ai, AreaMagazzino areaMagazzino);

}
